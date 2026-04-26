package me.calebeoliveira.springaopexample.aspect;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class IdempotencyAspect {
    private final Cache<String, Boolean> processedKeys = Caffeine.newBuilder()
            .expireAfterWrite(24, TimeUnit.HOURS)
            .maximumSize(10000)
            .recordStats()
            .build();

    @Around("@annotation(me.calebeoliveira.springaopexample.model.Idempotent)")
    public Object idempotent(ProceedingJoinPoint joinPoint) throws Throwable {
        String idempotencyKey = getCurrentIdempotencyKey();

        if(idempotencyKey == null) {
            return joinPoint.proceed();
        }

        Boolean existing = processedKeys.getIfPresent(idempotencyKey);

        if(existing != null) {
            log.warn("Duplicate request detected for key: {}", idempotencyKey);
            throw new RuntimeException("Duplicate request");
        }


        processedKeys.put(idempotencyKey, true);

        try {
            return joinPoint.proceed();
        } finally {
            log.debug("Completed request with key: {}", idempotencyKey);
        }
    }

    private String getCurrentIdempotencyKey() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if(attrs instanceof ServletRequestAttributes servletRequestAttributes) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            return request.getHeader("Idempotency-Key");
        }

        return null;
    }

    @Scheduled(fixedRate = 3600000)
    public void logCacheStats() {
        log.debug("Idempotency cache stats: {}", processedKeys.stats());
    }
}
