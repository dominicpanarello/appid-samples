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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibm.openid.authentication.exception.ValidationException;
import com.ibm.openid.authentication.rest.JwtToken;
import com.ibm.openid.authentication.rest.RestUtils;
import com.ibm.openid.cache.TokenCache;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple servlet that implements the /token endpoint of an OAuth2 IdP.
 * 
 * Currently only supports the authorization_code grant type.
 */
public class TokenServlet extends HttpServlet {
  private static final Logger log = LoggerFactory.getLogger(TokenServlet.class);
  private static final String PROPERTIES_FILE_NAME = "com.ibm.openid.authentication.auth";

  @Override
  protected void doPost(final HttpServletRequest req,
      final HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("application/json");

    log.debug(
        "TokenServlet request received for uuid " + req.getParameter("code"));

    try {
      final String authHeaderClientId = getAuthHeaderClient(req, resp);

      final JwtToken jwtToken = getToken(req, resp, authHeaderClientId);

      final String json = RestUtils.generateJSONString(jwtToken);

      final PrintWriter out = resp.getWriter();
      out.print(json); // NOSONAR
      out.flush();

      log.debug("TokenServlet response: " + json);

    } catch (final ValidationException e) { // NOSONAR
      this.sendError(resp, e.getMessage(), e.getDescription(),
          e.getStatusCode());
    }
  }

  private JwtToken getToken(final HttpServletRequest req,
      final HttpServletResponse resp, final String authHeaderClientId)
      throws ValidationException {
    final String grantType = req.getParameter("grant_type");
    if (!grantType.equals("authorization_code")) {
      throw new ValidationException("unsupported_grant_type",
          "unrecognized grant_type");
    }

    final String uuid = req.getParameter("code");
    final JwtToken jwtToken = new TokenCache().get(uuid);
    if (jwtToken == null) {
      throw new ValidationException("invalid_grant", "grant code not found");
    }

    if (!jwtToken.getTenantId().equals(req.getAttribute("tenantId"))
        || !jwtToken.getClientId().equals(authHeaderClientId)) {
      throw new ValidationException("invalid_client",
          "unknown client_id or tenantId");
    }

    return jwtToken;
  }

  /**
   * Returns the clientId in the authorization headers.
   * 
   * @param req
   *          request
   * @param resp
   *          response
   * @return clientId
   * @throws ValidationException
   *           if the clientId could not be extracted or validated
   * @throws IOException
   */
  private String getAuthHeaderClient(final HttpServletRequest req,
      final HttpServletResponse resp) throws ValidationException, IOException {
    try {
      final String authHeader = req.getHeader("Authorization");
      if (authHeader == null) {
        throw new ValidationException(
            "please provide clientId as username and secret as password",
            "missing credentials in request");
      }
      final String[] values = authHeader.split(" ");
      final String type = values[0];
      final String value = values[1];

      if (!type.equals("Basic")) {
        throw new ValidationException(
            "please provide clientId as username and secret as password",
            "missing credentials in request");
      }

      final String[] decoded = new String(Base64.getDecoder().decode(value))
          .split(":");
      final String clientId = decoded[0];
      final String secret = decoded[1];
      final String knownSecret = getClientSecret(clientId);
      if (knownSecret == null) {
        throw new ValidationException("invalid_client", "unknown client_id");
      }

      if (!knownSecret.equals(secret)) {
        throw new ValidationException("invalid_client", "Invalid Credentials",
            401);
      }

      // client is valid, secret is valid
      return clientId;

    } catch (final ArrayIndexOutOfBoundsException e) {
      throw new ValidationException(
          "please provide clientId as username and secret as password",
          "missing credentials in request", e);
    }
  }

  /**
   * Retrieve the client secret for a given client.
   * 
   * @param clientId
   *          client ID
   * @return secret
   * @throws IOException
   */
  private String getClientSecret(final String clientId) throws IOException {
    final String key = "client.id." + clientId + ".secret";
    if (getProperties().containsKey(key)) {
      return getProperties().getString(key);
    }
    throw new IOException("Couldn't get secret for clientId " + clientId);
  }

  /**
   * Send a bad request error with the given message.
   * 
   * @param error
   *          name
   * @param description
   *          description
   * @param statusCode
   *          status code
   * @throws IOException
   * @throws JsonProcessingException
   */
  private void sendError(final HttpServletResponse resp, final String error,
      final String description, final Integer statusCode)
      throws JsonProcessingException, IOException {
    final JwtToken jwtToken = new JwtToken();
    jwtToken.setError(error);
    jwtToken.setErrorDescription(description);
    final int code = statusCode != null ? statusCode : 400;
    resp.setStatus(code);

    final PrintWriter out = resp.getWriter();
    final String json = RestUtils.generateJSONString(jwtToken);
    out.print(json); // NOSONAR
    out.flush();

    log.info("TokenServlet responded with error: " + json);

    return;
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
