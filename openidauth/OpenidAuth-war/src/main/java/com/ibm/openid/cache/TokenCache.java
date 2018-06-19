package com.ibm.openid.cache;

import com.ibm.openid.authentication.rest.JwtToken;
import java.net.URL;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class TokenCache {
  private static final String CACHE_NAME = "tokenCache";
  private static final Cache tokenCache;
  static {
    final URL myUrl = TokenCache.class.getResource("ehcache.xml");
    final CacheManager cacheManager = CacheManager.newInstance(myUrl);
    tokenCache = cacheManager.getCache(CACHE_NAME);
  }

  public JwtToken get(final String uuid) {
    // perform a replace; can only be retrieved once
    final Element element = tokenCache.get(uuid);
    if (element != null) {
      final JwtToken token = (JwtToken) element.getObjectValue();
      tokenCache.remove(uuid);
      return token;
    }

    return null;
  }

  public void put(final String uuid, final JwtToken token) {
    tokenCache.put(new Element(uuid, token));
  }
}
