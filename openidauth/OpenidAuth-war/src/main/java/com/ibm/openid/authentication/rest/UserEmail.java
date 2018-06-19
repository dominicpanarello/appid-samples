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
 * A POJO representation of an email object, for AppID user creation.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserEmail {
  private String value;
  private Boolean primary;

  /**
   * Get the value.
   * 
   * @return value
   */
  public String getValue() {
    return value;
  }

  /**
   * Set the value.
   * 
   * @param value
   *          value
   */
  public void setValue(final String value) {
    this.value = value;
  }

  /**
   * Get the primary indicator.
   * 
   * @return primary
   */
  public Boolean getPrimary() {
    return primary;
  }

  /**
   * Set the primary indicator.
   * 
   * @param primary
   *          primary
   */
  public void setPrimary(final Boolean primary) {
    this.primary = primary;
  }
}
