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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.openid.authentication.exception.RequestException;
import com.ibm.openid.authentication.rest.JwtToken;
import com.ibm.openid.authentication.rest.RestUtils;
import com.ibm.openid.authentication.rest.UriUtils;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple servlet that performs facilitates password reset requests with an
 * OAuth2 IdP such as AppID.
 */
public class PasswordServlet extends HttpServlet {
  private static final Logger log = LoggerFactory
      .getLogger(PasswordServlet.class);
  private static final String PROPERTIES_FILE_NAME = "com.ibm.openid.authentication.auth";

  private static final String PROP_APPID_API_KEY = "appid.api.key";
  private static final String PROP_ENVIRONMENT = "environment";
  private static final String PROP_REDIRECT_URI_WHITELIST = "security.redirect.uri.whitelist";

  private static final String APPID_IDENTITY_GRANT_TYPE = "urn:ibm:params:oauth:grant-type:apikey";

  private static final String WAS_STATE_COOKIE_PREFIX = "WASOidcState";

  protected static final ObjectMapper mapper = new ObjectMapper();

  private static final String LOGIN_JSP = "/login.jsp";

  @Override
  protected void doGet(final HttpServletRequest req,
      final HttpServletResponse resp) throws ServletException, IOException {

    this.forwardToLogin(req, resp, getTenantId(req),
        req.getParameter("client_id"), getRedirectUri(req),
        req.getParameter("state"), null);
  }

  @Override
  protected void doPost(final HttpServletRequest req,
      final HttpServletResponse resp) throws ServletException, IOException {
    doPasswordReset(req, resp);
  }

  /**
   * Perform a password reset request.
   * 
   * @param req
   *          request
   * @param resp
   *          response
   * @throws ServletException
   * @throws IOException
   */
  private void doPasswordReset(final HttpServletRequest req,
      final HttpServletResponse resp) throws ServletException, IOException {
    final String email = req.getParameter("email");
    final String clientId = req.getParameter("clientId");
    final String tenantId = getTenantId(req);
    final String state = req.getParameter("state");
    final String redirectUri = getRedirectUri(req);

    requestPasswordReset(tenantId, email);

    forwardToLogin(req, resp, tenantId, clientId, redirectUri, state,
        "A password reset e-mail has been sent to you.");
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
   * Retrieve the tenantId from the request path.
   * 
   * @param req
   *          request
   * @return tenantId
   */
  private String getTenantId(final HttpServletRequest req) {
    return req.getAttribute("tenantId").toString();
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
   * Request a password reset for the given email.
   * 
   * @param email
   *          email
   * @throws IOException
   * 
   * @throws InformationalException
   * @throws AppException
   */
  private void requestPasswordReset(final String tenantId, final String email)
      throws IOException {
    final MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
    headers.putSingle("Authorization",
        "Bearer " + getIamToken().getAccessToken());
    final Response resetResponse = new RestUtils().post(
        UriUtils.getPasswordResetURI(tenantId),
        "email=" + URLEncoder.encode(email),
        MediaType.APPLICATION_FORM_URLENCODED_TYPE, headers, null);
    log.info("Attempted email '" + email + "' password reset, response: "
        + resetResponse.getStatus() + " "
        + resetResponse.readEntity(String.class));

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
   * Retrieve resource bundle properties.
   * 
   * @return properties
   */
  private static ResourceBundle getProperties() {
    return ResourceBundle.getBundle(PROPERTIES_FILE_NAME);
  }
}
