/*
 * Copyright 2018 IBM Corp.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibm.openid.authentication;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.openid.authentication.exception.RequestException;
import com.ibm.openid.authentication.exception.ValidationException;
import com.ibm.openid.authentication.rest.Attributes;
import com.ibm.openid.authentication.rest.JwtToken;
import com.ibm.openid.authentication.rest.RestClientBuilderUtility;
import com.ibm.openid.authentication.rest.RestUtils;
import com.ibm.openid.authentication.rest.UriUtils;
import com.ibm.openid.authentication.rest.Users;
import com.ibm.openid.cache.TokenCache;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.ResourceBundle;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple servlet that performs form authentication with an OAuth2 IdP such as
 * AppID.
 */
public class AuthServlet extends HttpServlet {
  private static final Logger log = LoggerFactory.getLogger(AuthServlet.class);

  private static final String OIDC_CLIENT_CONTEXT_PATH = "/oidcclient";

  private static final String PROPERTIES_FILE_NAME = "com.ibm.openid.authentication.auth";
  private static final String PROP_USER_STATUS_EXPECTED = "user.status.expected";
  private static final String PROP_USER_STATUS_ATTRIBUTE = "user.status.attribute";
  private static final String PROP_ENVIRONMENT = "environment";
  private static final String PROP_REDIRECT_URI_WHITELIST = "security.redirect.uri.whitelist";

  private static final String APPID_GRANT_TYPE = "password";
  private static final String APPID_SCOPES = "openid%20profile%20email";

  private static final String PROP_APPID_API_KEY = "appid.api.key";
  private static final String APPID_IDENTITY_GRANT_TYPE = "urn:ibm:params:oauth:grant-type:apikey";

  private static final String WAS_STATE_COOKIE_PREFIX = "WASOidcState";
  private static final String WAS_REQ_URL_COOKIE_PREFIX = "WASReqURLOidc";

  private static final String AUTH_RESULT_MSG_ID = "OIAT10000";
  private static final String AUTH_RESULT_SUCCESS = "success";
  private static final String AUTH_RESULT_FAIL = "fail";
  private static final String AUTH_RESULT_INVALID = "invalid";
  private static final String AUTH_RESULT_SEPARATOR = "|";

  protected static final ObjectMapper mapper = new ObjectMapper();

  private static final String LOGIN_JSP = "/login.jsp";

  @Override
  protected void doGet(final HttpServletRequest req,
      final HttpServletResponse resp) throws ServletException, IOException {

    this.forwardToLogin(req, resp, req.getAttribute("tenantId").toString(),
        req.getParameter("client_id"), getRedirectUri(req),
        req.getParameter("state"), null);
  }

  @Override
  protected void doPost(final HttpServletRequest req,
      final HttpServletResponse resp) throws ServletException, IOException {

    doCodeFlowLogin(req, resp);
  }

  /**
   * Uses the oauth2 authorization code flow for login. This involves invoking
   * the authorization endpoint of the IdP, then redirecting back to the given
   * OIDC redirect application.
   * 
   * @param req
   *          request
   * @param resp
   *          response
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   * @throws ServletException
   */
  private void doCodeFlowLogin(final HttpServletRequest req,
      final HttpServletResponse resp) throws JsonParseException,
      JsonMappingException, IOException, ServletException {
    final String email = req.getParameter("email");
    final String password = req.getParameter("password");
    final String clientId = req.getParameter("clientId");
    final String tenantId = req.getAttribute("tenantId").toString();
    final String state = req.getParameter("state");
    final String stateCookie = req.getParameter("stateCookie");
    final String redirectUri = getRedirectUri(req);
    final String protectedUrl = getWasReqUrl(req, stateCookie);
    String authResult = null;
    String authMsg = null;

    try {
      if (isLibertyOidcClientUri(redirectUri)
          && !isStateCookiePresent(req, stateCookie)) {
        // the oidcclient will reject the request - redirect the user back to
        // login with a fresh state instead
        log.debug("AuthServlet state cookie not present, redirecting back to "
            + protectedUrl);
        authResult = AUTH_RESULT_INVALID;
        authMsg = "missing state cookie";
        if (protectedUrl != null) {
          resp.sendRedirect(protectedUrl);
        } else {
          // no other choice but to fail hard
          resp.sendError(400, "missing header");
        }

        return;
      }

      final String oidcAppUri = codeFlowAuthenticate(tenantId, clientId, email,
          password, state, redirectUri);

      resp.sendRedirect(oidcAppUri);
      authResult = AUTH_RESULT_SUCCESS;
      return;

    } catch (final ValidationException e) { // NOSONAR
      // login was not successful
      forwardToLogin(req, resp, tenantId, clientId, redirectUri, state,
          e.getDescription());
      authResult = AUTH_RESULT_FAIL;
      authMsg = e.getMessage();
    } finally {
      logAuthResult(tenantId, clientId, email, protectedUrl, authResult,
          authMsg);
    }
  }

  /**
   * Return true if and only if this uri is for Liberty's oidcclient
   * application.
   * 
   * @param url
   *          url
   * @return true if the url request
   */
  private boolean isLibertyOidcClientUri(final String url) {
    if (url != null) {
      return url.toLowerCase().contains(OIDC_CLIENT_CONTEXT_PATH);
    }

    return false;
  }

  /**
   * Check for the presence of the WAS OIDC state cookie.
   * 
   * @param req
   *          request
   * @param cookieName
   *          cookie name
   * @return true if the cookie is present
   */
  private boolean isStateCookiePresent(final HttpServletRequest req,
      final String cookieName) {
    return getStateCookie(req, cookieName) != null;
  }

  /**
   * Check for the presence of the WAS OIDC state cookie, by prefix.
   * 
   * @param req
   *          request
   * @return cookie if the cookie is present
   */
  private Cookie getStateCookie(final HttpServletRequest req) {
    if (req.getCookies() == null) {
      return null;
    }

    for (final Cookie cookie : req.getCookies()) {
      if (cookie.getName().startsWith(WAS_STATE_COOKIE_PREFIX)) {
        return cookie;
      }
    }
    return null;
  }

  /**
   * Check for the presence of the WAS OIDC state cookie, by exact name.
   * 
   * @param req
   *          request
   * @param cookieName
   *          cookie name
   * @return cookie if the cookie is present
   */
  private Cookie getStateCookie(final HttpServletRequest req,
      final String cookieName) {
    if (cookieName == null || req.getCookies() == null) {
      return null;
    }
    for (final Cookie cookie : req.getCookies()) {
      if (cookie.getName().equals(cookieName)) {
        return cookie;
      }
    }
    return null;
  }

  /**
   * Retrieve the request URL from the WAS ReqUrl cookie.
   * 
   * @param req
   *          request
   * @param stateCookie
   *          state cookie name
   * @return url
   * @throws RequestException
   */
  private String getWasReqUrl(final HttpServletRequest req,
      final String stateCookie) throws RequestException {
    if (stateCookie == null || req.getCookies() == null) {
      // we can't determine the was req url
      return null;
    }
    final String nonce = stateCookie.length() >= WAS_STATE_COOKIE_PREFIX
        .length() ? stateCookie.substring(WAS_STATE_COOKIE_PREFIX.length())
            : null;

    for (final Cookie cookie : req.getCookies()) {
      if (cookie.getName().equals(WAS_REQ_URL_COOKIE_PREFIX + nonce)) {
        final String reqUrl = cookie.getValue();
        validateWasReqUrl(reqUrl);
        return reqUrl;
      }
    }
    return null;
  }

  /**
   * Forward this request and common parameters to the login page
   * 
   * @param req
   *          request
   * @param resp
   *          response
   * @param tenantId
   *          tenant ID
   * @param clientId
   *          client ID
   * @param redirectUri
   *          redirect URI
   * @param state
   *          application state
   * @param loginMessage
   *          message for the login page
   * @throws ServletException
   * @throws IOException
   */
  private void forwardToLogin(final HttpServletRequest req,
      final HttpServletResponse resp, final String tenantId,
      final String clientId, final String redirectUri, final String state,
      final String loginMessage) throws ServletException, IOException {

    req.setAttribute("loginMessage", loginMessage);
    req.setAttribute("tenantId", tenantId);
    req.setAttribute("clientId", clientId);
    req.setAttribute("redirectUri", redirectUri);
    req.setAttribute("state", state);
    req.setAttribute("environment",
        getProperties().getString(PROP_ENVIRONMENT));
    final Cookie stateCookie = getStateCookie(req);
    if (stateCookie != null) {
      req.setAttribute("stateCookie", stateCookie.getName());
    }
    req.getServletContext().getRequestDispatcher(LOGIN_JSP).forward(req, resp);
  }

  /**
   * Authenticate with a given user and password via the oauth2 authorization
   * code flow, returning the oidcApp redirect URI. The returned uri is expected
   * to contain the code and state parameters.
   * 
   * @param tenantId
   *          tenant ID
   * @param clientId
   *          client ID
   * @param email
   *          email
   * @param password
   *          password
   * @param state
   *          state
   * @param redirectUri
   *          redirect URI
   * @return oidcApp redirect URI
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   * @throws ValidationException
   * @throws RequestException
   */
  private String codeFlowAuthenticate(final String tenantId,
      final String clientId, final String email, final String password,
      final String state, final String redirectUri) throws JsonParseException,
      JsonMappingException, IOException, ValidationException, RequestException {

    // obtain a JWT
    final JwtToken token = this.getIdToken(tenantId, clientId, email, password);

    // enrich with tenant and clientId
    token.setTenantId(tenantId);
    token.setClientId(clientId);

    // generate a UUID and persist with JWT
    final String uuid = UUID.randomUUID().toString().replace("-", "");
    new TokenCache().put(uuid, token);

    log.debug("AuthServlet generated a token for uuid " + uuid);

    return redirectUri + "?code=" + uuid + "&state=" + state;
  }

  /**
   * Retrieve a new Json Web Token.
   * 
   * @param tenantId
   *          tenant ID
   * @param clientId
   *          client ID
   * @param email
   *          email
   * @param password
   *          password
   * @return JWT
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   * @throws ValidationException
   * @throws RequestException
   */
  private JwtToken getIdToken(final String tenantId, final String clientId,
      final String email, final String password) throws JsonParseException,
      JsonMappingException, IOException, ValidationException, RequestException {
    final URI uri = UriUtils.getOauthTokenUri(tenantId);

    final String clientSecret = getClientSecret(clientId);

    final String authHeader = new String(
        Base64.getEncoder().encode((clientId + ":" + clientSecret).getBytes()))
            .replaceAll("\\s", "").replaceAll("\\n", "");

    final String body = "grant_type=" + APPID_GRANT_TYPE + "&username="
        + URLEncoder.encode(email, "UTF-8") + "&password=" + password
        + "&scope=" + APPID_SCOPES;

    final MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
    headers.putSingle("Authorization", "Basic " + authHeader);
    final Response tokenResponse = post(uri, body,
        MediaType.APPLICATION_FORM_URLENCODED_TYPE, headers);
    final String responseStr = tokenResponse.readEntity(String.class);
    final JwtToken token = mapper.readValue(responseStr, JwtToken.class);
    if (token == null) {
      log.info(
          "AuthServlet login token json was null, response: " + responseStr);
      throw new ValidationException("empty_token",
          "Oops, the account details you entered were not recognized, please try again.");
    }

    if (token.getAccessToken() == null) {
      log.debug(
          "AuthServlet login wasn't successful, response: " + responseStr);
      if (token.getErrorDescription().equals("Pending user verification")) {
        throw new ValidationException(token.getErrorDescription(),
            "You must verify your account prior to login, please check your e-mail.");
      } else {
        // general login failure
        throw new ValidationException(token.getErrorDescription(),
            "Oops, the account details you entered were not recognized, please try again.");
      }
    }

    final String expectedStatus = getProperties()
        .getString(PROP_USER_STATUS_EXPECTED);
    if (!expectedStatus.isEmpty()) {
      final String userStatus = getUserStatus(tenantId, clientId, email);
      if (userStatus == null || !userStatus.equalsIgnoreCase(expectedStatus)) {
        log.info("AuthServlet login failed: user status is '" + userStatus
            + "', expected '" + expectedStatus + "'");
        throw new ValidationException("invalid_user_status",
            "User account is not enabled, please contact an administrator");
      } else {
        log.debug("AuthServlet login user status check passed: user " + email
            + " status is '" + userStatus + "', expected '" + expectedStatus
            + "'");
      }
    }

    return token;

  }

  /**
   * Retrieve the status of the user with the given email.
   * 
   * @param tenantId
   *          tenant
   * @param clientId
   *          client
   * @param email
   *          email
   * @return user status
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   */
  private String getUserStatus(final String tenantId, final String clientId,
      final String email)
      throws JsonParseException, JsonMappingException, IOException {
    final MultivaluedMap<String, Object> headersWithIamToken = this
        .getHeadersWithIamToken();
    final String userId = getUserId(email, tenantId, headersWithIamToken);

    if (userId != null) {
      final Attributes profile = getProfile(userId, tenantId,
          headersWithIamToken);
      if (profile != null) {
        return profile.getAttributes()
            .get(getProperties().getString(PROP_USER_STATUS_ATTRIBUTE));
      }
    }

    return null;
  }

  /**
   * Retrieve the profile's map of attributes.
   * 
   * @param userId
   *          user ID
   * @param tenantId
   *          tenant ID
   * @param headersWithIamToken
   *          headers
   * @return attributes
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   */
  private Attributes getProfile(final String userId, final String tenantId,
      final MultivaluedMap<String, Object> headersWithIamToken)
      throws JsonParseException, JsonMappingException, IOException {
    final Response userResponse = get(
        UriUtils.getUserProfileURI(userId, tenantId), headersWithIamToken);

    final String responsePayload = userResponse.readEntity(String.class);
    final Attributes attributes = mapper.readValue(responsePayload,
        Attributes.class);
    return attributes;
  }

  /**
   * Retrieve the AppID user ID of the given login ID.
   * 
   * @param loginID
   *          login ID
   * @param tenantId
   *          tenant ID
   * @param headersWithIamToken
   *          request headers
   * @return user ID
   * @throws IOException
   * @throws JsonMappingException
   * @throws JsonParseException
   */
  private String getUserId(final String loginID, final String tenantId,
      final MultivaluedMap<String, Object> headersWithIamToken)
      throws JsonParseException, JsonMappingException, IOException {
    final Response userResponse = get(
        UriUtils.getUserQueryURI(loginID, tenantId), headersWithIamToken);

    final String responsePayload = userResponse.readEntity(String.class);
    final Users users = mapper.readValue(responsePayload, Users.class);
    if (users.getUsers().size() > 0) {
      return users.getUsers().get(0).getId();
    }

    return null;
  }

  /**
   * Retrieve headers with an IAM token.
   * 
   * @return headers
   * @throws IOException
   */
  private MultivaluedMap<String, Object> getHeadersWithIamToken()
      throws IOException {
    final String iamAccessToken = getIamToken().getAccessToken();
    final MultivaluedMap<String, Object> headersWithIamToken = new MultivaluedHashMap<String, Object>();
    headersWithIamToken.putSingle("Authorization", "Bearer " + iamAccessToken);

    return headersWithIamToken;
  }

  /**
   * Issue a post request to the given URI.
   * 
   * @param path
   *          path
   * @param requestBody
   *          body
   * @param mediaType
   *          media type
   * @param headers
   *          headers
   * @return response
   */
  private Response post(final URI path, final String requestBody,
      final MediaType mediaType, final MultivaluedMap<String, Object> headers) {

    final Client client = getClientWithoutCredentials();

    final WebTarget resource = client.target(path);

    final Invocation.Builder invocationBuilder = resource.request(mediaType);

    final Entity<String> reqBody = Entity.entity(requestBody, mediaType);

    if (headers != null) {
      invocationBuilder.headers(headers);
    }
    final Response res = invocationBuilder.post(reqBody, Response.class);

    return res;
  }

  /**
   * Issue a get request to the given uri.
   * 
   * @param path
   *          uri
   * @param headers
   *          headers
   * @return response
   */
  public Response get(final URI path,
      final MultivaluedMap<String, Object> headers) {

    final Client client = getClientWithoutCredentials();

    final WebTarget resource = client.target(path);

    final Invocation.Builder invocationBuilder = resource.request();

    if (headers != null) {
      invocationBuilder.headers(headers);
    }
    final Response res = invocationBuilder.get();

    return res;
  }

  /**
   * Get a REST Client with no BA credentials.
   *
   * @return the REST Client.
   */
  private Client getClientWithoutCredentials() {
    return RestClientBuilderUtility.newDefaultClient();
  }

  /**
   * Retrieve the redirect URI from the request.
   * 
   * @param req
   *          request
   * @return redirect URI
   * @throws RequestException
   */
  private String getRedirectUri(final HttpServletRequest req)
      throws RequestException {
    String uri = req.getParameter("redirectUri");

    if (uri == null) {
      uri = req.getParameter("redirect_uri");
    }

    validateRedirectUri(uri);

    return uri;
  }

  /**
   * Check the given redirect uri to ensure it meets validity criteria, eg.
   * security.
   * 
   * @param uri
   *          uri
   * @throws RequestException
   */
  private void validateRedirectUri(final String uri) throws RequestException {
    if (uri == null) {
      return;
    }

    final String allUris = getProperties()
        .getString(PROP_REDIRECT_URI_WHITELIST);
    for (final String validUri : allUris.split(",")) {
      if (uri.startsWith(validUri)) {
        return;
      }
    }

    throw new RequestException("Invalid redirect uri: " + uri);
  }

  /**
   * Check the given redirect uri to ensure it meets validity criteria, eg.
   * security.
   * 
   * @param uri
   *          uri
   * @throws RequestException
   */
  private void validateWasReqUrl(final String uri) throws RequestException {
    if (uri == null) {
      return;
    }

    final String allUris = getProperties()
        .getString(PROP_REDIRECT_URI_WHITELIST);
    for (final String validUri : allUris.split(",")) {
      if (uri.startsWith(validUri)) {
        return;
      }
    }

    throw new RequestException("Invalid was req url: " + uri);
  }

  /**
   * Retrieve the client secret for a given client.
   * 
   * @param clientId
   *          client ID
   * @return secret
   * @throws RequestException
   */
  private String getClientSecret(final String clientId)
      throws RequestException {
    final String key = "client.id." + clientId + ".secret";
    if (getProperties().containsKey(key)) {
      return getProperties().getString(key);
    }
    throw new RequestException("Couldn't get secret for clientId " + clientId);
  }

  /**
   * Retrieve an IAM access token.
   * 
   * @return IAM token
   * @throws InformationalException
   * @throws AppException
   */
  private JwtToken getIamToken() throws IOException {
    final URI tokenPath = UriUtils.getIamTokenUri();
    final Response tokenResponse = new RestUtils().post(tokenPath,
        "grant_type=" + URLEncoder.encode(APPID_IDENTITY_GRANT_TYPE)
            + "&apikey=" + getProperties().getString(PROP_APPID_API_KEY),
        MediaType.APPLICATION_FORM_URLENCODED_TYPE, null,
        MediaType.APPLICATION_JSON_TYPE);

    final JwtToken token = mapper
        .readValue(tokenResponse.readEntity(String.class), JwtToken.class);
    return token;

  }

  /**
   * Log the authentication result.
   * 
   * @param tenantId
   *          tenant ID
   * @param clientId
   *          client ID
   * @param email
   *          email
   * @param protectedUrl
   *          url
   * @param authResult
   *          result
   * @param authMsg
   *          message
   */
  private void logAuthResult(final String tenantId, final String clientId,
      final String email, final String protectedUrl, final String authResult,
      final String authMsg) {
    final StringBuffer sb = new StringBuffer();
    sb.append(AUTH_RESULT_MSG_ID);
    sb.append(AUTH_RESULT_SEPARATOR).append(tenantId);
    sb.append(AUTH_RESULT_SEPARATOR).append(clientId);
    sb.append(AUTH_RESULT_SEPARATOR).append(email);
    sb.append(AUTH_RESULT_SEPARATOR);
    if (protectedUrl != null) {
      sb.append(protectedUrl);
    }
    sb.append(AUTH_RESULT_SEPARATOR).append(authResult);
    sb.append(AUTH_RESULT_SEPARATOR);
    if (authMsg != null) {
      sb.append(authMsg);
    }
    log.info(sb.toString());
  }

  /**
   * Retrieve resource bundle properties.
   * 
   * @return properties
   */
  private static ResourceBundle getProperties() {
    return ResourceBundle.getBundle(PROPERTIES_FILE_NAME);
  }
}
