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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 * A POJO representation of a web token received as a Json response.
 */

@SuppressWarnings("all")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtToken implements Serializable {
  private String accessToken;
  private String refreshToken;
  private String uaaToken;
  private String uaaRefreshToken;
  private String tokenType;
  private Long expiresIn;
  private Long expiration;
  private String idToken;
  private String error;
  private String errorDescription;
  private String tenantId;
  private String clientId;

  /**
   * Empty constructor.
   */
  public JwtToken() {
    // do nothing.
  }

  /**
   * Retrieve the error.
   * 
   * @return error
   */
  @JsonProperty("error")
  public String getError() {
    return error;
  }

  /**
   * Set the error.
   * 
   * @param error
   *          error
   */
  public void setError(final String error) {
    this.error = error;
  }

  /**
   * Retrieve the error description.
   * 
   * @return error description
   */
  @JsonProperty("error_description")
  public String getErrorDescription() {
    return errorDescription;
  }

  /**
   * Set the error description.
   * 
   * @param errorDescription
   *          error description
   */
  public void setErrorDescription(final String errorDescription) {
    this.errorDescription = errorDescription;
  }

  /**
   * Retrieve the access token.
   * 
   * @return access token
   */
  @JsonProperty("access_token")
  public String getAccessToken() {
    return accessToken;
  }

  /**
   * Set the access token.
   * 
   * @param accessToken
   *          access token
   */
  public void setAccessToken(final String accessToken) {
    this.accessToken = accessToken;
  }

  /**
   * Retrieve the id token.
   * 
   * @return id token
   */
  @JsonProperty("id_token")
  public String getIdToken() {
    return idToken;
  }

  /**
   * Set the id token.
   * 
   * @param idToken
   *          id token
   */
  public void setIdToken(final String idToken) {
    this.idToken = idToken;
  }

  /**
   * Get the refresh token.
   * 
   * @return refresh token
   */
  @JsonProperty("refresh_token")
  public String getRefreshToken() {
    return refreshToken;
  }

  /**
   * Set the refresh token.
   * 
   * @param refreshToken
   *          refresh token
   */
  public void setRefreshToken(final String refreshToken) {
    this.refreshToken = refreshToken;
  }

  /**
   * Get the UAA token.
   * 
   * @return uaa token
   */
  @JsonProperty("uaa_token")
  public String getUaaToken() {
    return uaaToken;
  }

  /**
   * Set the UAA token.
   * 
   * @param uaaToken
   *          uaa token
   */
  public void setUaaToken(final String uaaToken) {
    this.uaaToken = uaaToken;
  }

  /**
   * Get the UAA refresh token.
   * 
   * @return UAA refresh token
   */
  @JsonProperty("uaa_refresh_token")
  public String getUaaRefreshToken() {
    return uaaRefreshToken;
  }

  /**
   * Set the UAA refresh token.
   * 
   * @param uaaRefreshToken
   *          uaa refresh token
   */
  public void setUaaRefreshToken(final String uaaRefreshToken) {
    this.uaaRefreshToken = uaaRefreshToken;
  }

  /**
   * Get the token type.
   * 
   * @return token type
   */
  @JsonProperty("token_type")
  public String getTokenType() {
    return tokenType;
  }

  /**
   * Set the token type.
   * 
   * @param tokenType
   *          token type
   */
  public void setTokenType(final String tokenType) {
    this.tokenType = tokenType;
  }

  /**
   * Get the expiry time.
   * 
   * @return expiry time.
   */
  @JsonProperty("expires_in")
  public Long getExpiresIn() {
    return expiresIn;
  }

  /**
   * Set the expiry time.
   * 
   * @param expiresIn
   *          expiry time
   */
  public void setExpiresIn(final Long expiresIn) {
    this.expiresIn = expiresIn;
  }

  /**
   * Get the expiration.
   * 
   * @return expiration
   */
  public Long getExpiration() {
    return expiration;
  }

  /**
   * Set the expiration.
   * 
   * @param expiration
   *          expiration
   */
  public void setExpiration(final Long expiration) {
    this.expiration = expiration;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(final String tenantId) {
    this.tenantId = tenantId;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(final String clientId) {
    this.clientId = clientId;
  }
}
