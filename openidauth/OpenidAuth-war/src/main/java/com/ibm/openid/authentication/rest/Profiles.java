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
import java.util.List;

/**
 * A POJO representation of a list of user object, for AppID user retrieval.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Profiles {
  private List<Profile> profiles;

  /**
   * Get the profiles.
   * 
   * @return profiles
   */
  @JsonProperty("profiles")
  public List<Profile> getProfiles() {
    return profiles;
  }

  /**
   * Set the profiles.
   * 
   * @param profiles
   *          profiles
   */
  public void setResources(final List<Profile> profiles) {
    this.profiles = profiles;
  }
}
