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

/**
 * A POJO representation of a user profile object, for AppID user creation.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Profile {
  private String idp;
  private String profileId;

  /**
   * Get the profileId.
   * 
   * @return profileId
   */
  public String getProfileId() {
    return profileId;
  }

  /**
   * Set the profileId.
   * 
   * @param profileId
   *          profileId
   */
  public void setProfileId(final String profileId) {
    this.profileId = profileId;
  }

  /**
   * Get the idp.
   * 
   * @return idp
   */
  public String getIdp() {
    return idp;
  }

  /**
   * Set the idp.
   * 
   * @param idp
   *          idp
   */
  public void setIdp(final String idp) {
    this.idp = idp;
  }

}
