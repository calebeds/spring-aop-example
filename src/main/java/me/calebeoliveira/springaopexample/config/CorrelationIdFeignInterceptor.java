package me.calebeoliveira.springaopexample.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class CorrelationIdFeignInterceptor implements RequestInterceptor {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    private static final String CORRELATION_ID_KEY = "correlationId";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String correlationId = MDC.get(CORRELATION_ID_KEY);

        if(correlationId != null) {
            requestTemplate.header(CORRELATION_ID_HEADER, correlationId);
        }
    }
}
