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
import java.util.List;

/**
 * A POJO representation of a user object, for AppID user creation.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
  private List<UserEmail> emails;
  private String displayName;
  private String password;
  private String id;

  /**
   * Get the id.
   * 
   * @return id
   */
  public String getId() {
    return id;
  }

  /**
   * Set the id.
   * 
   * @param id
   *          id
   */
  public void setId(final String id) {
    this.id = id;
  }

  /**
   * Get the emails.
   * 
   * @return emails
   */
  public List<UserEmail> getEmails() {
    return emails;
  }

  /**
   * Set the emails.
   * 
   * @param emails
   *          emails
   */
  public void setEmails(final List<UserEmail> emails) {
    this.emails = emails;
  }

  /**
   * Get the display name.
   * 
   * @return display name
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * Set the display name.
   * 
   * @param displayName
   *          display name
   */
  public void setDisplayName(final String displayName) {
    this.displayName = displayName;
  }

  /**
   * Get the password.
   * 
   * @return password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Set the password.
   * 
   * @param password
   *          password
   */
  public void setPassword(final String password) {
    this.password = password;
  }

}
