package com.myown.app.sample.api;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

// To convert to a Spring Boot Filter
// Uncomment @Component and Comment our @WebFilter annotation
@Component
@Slf4j
@WebFilter(filterName = "mdcFilter", urlPatterns = {"/*"})
public class MDCFilter implements Filter {

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
