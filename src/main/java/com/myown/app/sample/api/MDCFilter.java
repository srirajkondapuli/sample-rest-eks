package com.myown.app.sample.api;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

// To convert to a Spring Boot Filter
// Uncomment @Component and Comment our @WebFilter annotation
@Component
// @Slf4j
@jakarta.servlet.annotation.WebFilter(filterName = "mdcFilter", urlPatterns = {"/*"})
public class MDCFilter implements jakarta.servlet.Filter {

    @Override
    public void destroy() {}

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response,
            final FilterChain chain) throws IOException, ServletException {


        Map m = request.getParameterMap();
        Set s = m.entrySet();
        Iterator it = s.iterator();

        while (it.hasNext()) {

            Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) it.next();

            String key = entry.getKey();
            String[] value = entry.getValue();

            System.out.println("Key: " + key);
            System.out.println("Value: " + value);


        }

        Iterator attributeIt = request.getAttributeNames().asIterator();
        while(attributeIt.hasNext()){

            String attribute = (String)attributeIt.next();
            System.out.println(request.getAttribute(attribute));
        }

        MDC.put("sessionId", request.getParameter("traceId"));

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {}


}
