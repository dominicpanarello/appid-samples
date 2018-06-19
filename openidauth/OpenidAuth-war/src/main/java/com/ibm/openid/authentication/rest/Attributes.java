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
import java.util.Map;

/**
 * A POJO representation of a map of user profile attributes, for AppID user
 * creation.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Attributes {
  private Map<String, String> attributes;

  /**
   * Get the attributes.
   * 
   * @return attributes
   */
  @JsonProperty("attributes")
  public Map<String, String> getAttributes() {
    return attributes;
  }

  /**
   * Set the attributes.
   * 
   * @param attributes
   *          attributes
   */
  public void setAttributes(final Map<String, String> attributes) {
    this.attributes = attributes;
  }
}
