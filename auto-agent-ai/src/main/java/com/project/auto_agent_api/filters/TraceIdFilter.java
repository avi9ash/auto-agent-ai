package com.project.auto_agent_api.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class TraceIdFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        long startTime = System.currentTimeMillis();
        HttpServletRequest request = (HttpServletRequest) req;
        String traceId = UUID.randomUUID().toString();
        request.setAttribute("traceId", traceId);
        System.out.println("TraceID: " + traceId + " for " + request.getRequestURI());
        chain.doFilter(req, res);
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Request took: " + duration + "ms");
        chain.doFilter(req, res);

    }

}
