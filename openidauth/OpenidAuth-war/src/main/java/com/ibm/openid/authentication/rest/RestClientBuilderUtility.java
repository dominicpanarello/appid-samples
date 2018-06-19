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

import java.util.function.Consumer;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import org.glassfish.jersey.client.ClientConfig;

/**
 * A utility class to generate a Jersey rest client.
 */
public final class RestClientBuilderUtility {

  /**
   * Default constructor.
   */
  private RestClientBuilderUtility() {
    // No operation
  }

  /**
   * Creates a client with Application Server aware SSL settings.
   * 
   * @return The JAX RS client.
   */
  public static Client newDefaultClient() {
    return newClient(newDefaultClientConfig());
  }

  /**
   * Create a client config with the required default. In particular, this sets
   * up the SSL settings required in Liberty.
   * 
   * @return The client configuration.
   */
  public static ClientConfig newDefaultClientConfig() {
    return ensureSSLConfig(new ClientConfig());
  }

  /**
   * Creates a client with Application Server aware SSL settings. This allows
   * the API user to customize the {@link CLientBuilder} settings used for the
   * client.
   * 
   * Usage:
   * 
   * <pre>
   * final Client client = RestClientBuilderUtility.newDefaultClient(builder -> 
   *     // your customizations here.
   * );
   * </pre>
   * 
   * @param clientConfig
   *          The client config.
   * @return The configured client.
   */
  public static Client newClient(final ClientConfig clientConfig) {
    return newClient(NoopConfigurationCustomizer.kInstance, clientConfig);
  }

  /**
   * Creates a client with Application Server aware SSL settings. This allows
   * the API user to customize the {@link CLientBuilder} settings used for the
   * client.
   * 
   * You should NEVER use this unless you need to modify SSL settings for some
   * reason.
   * 
   * TODO: left private until I find use case. If you have a valid use case,
   * make this method public.
   * 
   * Usage:
   * 
   * <pre>
   * final ClientConfig clientConfig = RestClientBuilderUtility.newClientConfig();
   * clientConfig.propert(.., ..);
   * final Client client = RestClientBuilderUtility.newDefaultClient(builder -> {
   *     // your customizations here.
   * }, clientConfig);
   * </pre>
   * 
   * @param builderConfigurer
   *          The builder configurer.
   * @param clientConfig
   *          The client config.
   * @return The configured client.
   */
  private static Client newClient(
      final Consumer<ClientBuilder> builderConfigurer,
      final ClientConfig clientConfig) {
    final ClientBuilder clientBulder = newClientBuilder();

    // TODO - can not think of a time we would ever not want this. For now, I
    // will not allow
    // developers to shoot themselves in the foot.

    // clientBulder.withConfig(ensureSSLConfig(clientConfig));
    builderConfigurer.accept(clientBulder);
    return clientBulder.build();
  }

  /**
   * Ensures that the client configuration contains the Liberty SSL
   * configuration.
   * 
   * @param clientConfig
   *          The client configuration.
   * @return The corrected client configuration.
   */
  private static ClientConfig ensureSSLConfig(final ClientConfig clientConfig) {

    return clientConfig;
  }

  /**
   * Corrects the JAXRS client to use the appropriate SSL configuration settings
   * in Liberty.
   * 
   * @return The client builder.
   */
  private static ClientBuilder newClientBuilder() {
    return ClientBuilder.newBuilder()
        .hostnameVerifier(TrustAllHostNameVerifier.kInstance);
  }

  /**
   * Implement host name verification that skips host name verification. This is
   * required in production due to the lack of DNS services.
   */
  private static final class TrustAllHostNameVerifier
      implements HostnameVerifier {

    /**
     * Instance.
     */
    public static final TrustAllHostNameVerifier kInstance = new TrustAllHostNameVerifier();

    /**
     * Constructor.
     */
    private TrustAllHostNameVerifier() {
      // no body
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean verify(final String hostname, final SSLSession session) {
      return true;
    }

  }

  /**
   * NoOP configuration customizaer.
   */
  private static final class NoopConfigurationCustomizer
      implements Consumer<ClientBuilder> {

    /**
     * Instance.
     */
    public static final NoopConfigurationCustomizer kInstance = new NoopConfigurationCustomizer();

    /**
     * Constructor.
     */
    private NoopConfigurationCustomizer() {
      // no body
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(final ClientBuilder t) {
      // Do nothing.
    }

  }
}
