package com.myown.app.sample.configuration;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.opentelemetry.api.trace.Span;

@Configuration
public class OpentelemetryConfiguration {
	private static final String TRACE_ID = "trace-id";

	@Bean
	Filter traceIdInResponseFilter() {
		return (request, response, chain) -> {
			Span currentSpan = Span.current();
			if (currentSpan != null) {
				HttpServletResponse resp = (HttpServletResponse) response;
				resp.addHeader(TRACE_ID, currentSpan.getSpanContext().getTraceId());
			}
			chain.doFilter(request, response);
		};
	}

	@Bean
	public HttpTraceRepository httpTraceRepository() {
		return new InMemoryHttpTraceRepository();
	}
}
