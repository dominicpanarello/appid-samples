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
package com.ibm.openid.authentication.rest;

import java.net.URI;
import java.net.URLEncoder;
import java.util.ResourceBundle;
import javax.ws.rs.core.UriBuilder;

public class UriUtils {

  private static final String PROPERTIES_FILE_NAME = "com.ibm.openid.authentication.auth";
  private static final String PROP_APPID_AUTH_SERVER = "appid.host";
  private static final String PROP_APPID_MANAGEMENT_SERVER = "appid.management.host";

  private static final String IAM_TOKEN_URI = "https://iam.bluemix.net/identity/token";

  /**
   * Retrieve the IAM token uri.
   * 
   * @return URI
   */
  public static URI getIamTokenUri() {
    return UriBuilder.fromUri(IAM_TOKEN_URI).build();
  }

  /**
   * Retrieve the Oauth token URI.
   * 
   * @param tenantId
   *          tenant ID
   * @return URI
   */
  public static URI getOauthTokenUri(final String tenantId) {
    final String host = getProperties().getString(PROP_APPID_AUTH_SERVER);
    return UriBuilder
        .fromUri("https://" + host + "/oauth/v3/" + tenantId + "/token")
        .build();
  }

  /**
   * Retrieve the authorization page URI.
   * 
   * @param tenantId
   *          tenant ID
   * @param clientId
   *          client ID
   * @param state
   *          state CSRF
   * @param encodedRedirectUri
   * @return URI
   */
  public static URI getAuthorizationPageUri(final String tenantId,
      final String clientId, final String state,
      final String encodedRedirectUri) {
    final String host = getProperties().getString(PROP_APPID_AUTH_SERVER);

    return UriBuilder.fromUri("https://" + host + "/oauth/v3/" + tenantId
        + "/authorization?response_type=code&client_id=" + clientId + "&state="
        + state + "&redirect_uri=" + encodedRedirectUri
        + "&scope=openid%20profile%20email").build();
  }

  /**
   * Retrieve the auth endpoint URI.
   * 
   * @param tenantId
   *          tenant ID
   * @return URI
   */
  public static URI getAuthUri(final String tenantId) {
    final String host = getProperties().getString(PROP_APPID_AUTH_SERVER);
    return UriBuilder.fromUri(
        "https://" + host + "/oauth/v3/" + tenantId + "/cloud_directory/auth")
        .build();
  }

  /**
   * Retrieve the AppId user password reset URI.
   * 
   * @return URI
   */
  public static URI getPasswordResetURI(final String tenantId) {
    final String host = getProperties().getString(PROP_APPID_MANAGEMENT_SERVER);

    return UriBuilder.fromUri("https://" + host + "/management/v4/" + tenantId
        + "/cloud_directory/forgot_password").build();
  }

  /**
   * Retrieve the uri for querying user profiles.
   * 
   * @param userId
   *          userId to search for
   * @param tenantId
   *          tenant ID
   * @return uri
   */
  public static URI getUserProfileURI(final String userId,
      final String tenantId) {
    final String host = getProperties().getString(PROP_APPID_MANAGEMENT_SERVER);

    return UriBuilder.fromUri("https://" + host + "/management/v4/" + tenantId
        + "/users/" + userId + "/profile").build();
  }

  /**
   * Retrieve the uri for querying users.
   * 
   * @param loginID
   *          login ID to search for
   * @return uri
   */
  public static URI getUserQueryURI(final String loginID,
      final String tenantId) {
    final String host = getProperties().getString(PROP_APPID_MANAGEMENT_SERVER);
    return UriBuilder.fromUri("https://" + host + "/management/v4/" + tenantId
        + "/users" + "?email=" + URLEncoder.encode(loginID)).build();
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
