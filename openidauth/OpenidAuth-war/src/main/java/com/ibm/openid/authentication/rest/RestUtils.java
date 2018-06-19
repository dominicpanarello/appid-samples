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

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

public class RestUtils {
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
  public Response post(final URI path, final String requestBody,
      final MediaType mediaType, final MultivaluedMap<String, Object> headers,
      final MediaType acceptType) {

    final Client client = getClientWithoutCredentials();

    final WebTarget resource = client.target(path);

    final MediaType accept = (acceptType != null ? acceptType : mediaType);
    final Invocation.Builder invocationBuilder = resource.request(accept);

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
   * Generate json string from an object.
   *
   * @param parameter
   *          Object to be transferred to json string
   * @return json string
   * @throws JsonProcessingException
   * @throws InformationalException
   *           Generic Exception Signature
   */
  @SuppressWarnings("squid:S1166")
  public static String generateJSONString(final Object parameter)
      throws JsonProcessingException {
    final ObjectMapper mapper = new ObjectMapper();
    // Exclude Null value
    mapper.setSerializationInclusion(Include.NON_NULL);
    // Exclude Empty value
    mapper.setSerializationInclusion(Include.NON_EMPTY);
    return mapper.writeValueAsString(parameter);
  }

  /**
   * Get a REST Client with no BA credentials.
   *
   * @return the REST Client.
   */
  private Client getClientWithoutCredentials() {
    return RestClientBuilderUtility.newDefaultClient();
  }

}
