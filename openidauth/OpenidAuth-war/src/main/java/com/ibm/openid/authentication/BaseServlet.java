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
package com.ibm.openid.authentication;

import java.io.IOException;
import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A base servlet that retrieves common parameters and redirects to the relevant
 * subservlet.
 */
public class BaseServlet extends HttpServlet {

  @Override
  public void service(final ServletRequest req, final ServletResponse res)
      throws ServletException, IOException {

    if (req instanceof HttpServletRequest
        && !req.getDispatcherType().equals(DispatcherType.FORWARD)) {
      final HttpServletRequest hsr = (HttpServletRequest) req;
      final String targetRequest = getTarget(hsr);
      if (targetRequest != null) {
        final String tenantId = getTenantId(hsr);
        if (tenantId == null) {
          ((HttpServletResponse) res).sendError(400, "TenantId is required");
        } else {
          req.setAttribute("tenantId", tenantId);
          req.getRequestDispatcher(targetRequest).forward(req, res);
        }
        return;
      }
    }

    super.service(req, res);
  }

  /**
   * Retrieve the intended target from the request URI.
   * 
   * @param req
   *          request
   * @return target
   */
  private String getTarget(final HttpServletRequest req) {
    final String pathInfo = req.getPathInfo();
    return pathInfo.substring(pathInfo.lastIndexOf("/"));
  }

  /**
   * Retrieve the tenantId from the request URI.
   * 
   * @param req
   *          request
   * @return tenantId
   */
  private String getTenantId(final HttpServletRequest req) {
    final String pathInfo = req.getPathInfo(); // /{value}/test
    final String[] pathParts = pathInfo.split("/");
    if (pathParts.length > 1) {
      return pathParts[1];
    }

    return null;
  }
}
