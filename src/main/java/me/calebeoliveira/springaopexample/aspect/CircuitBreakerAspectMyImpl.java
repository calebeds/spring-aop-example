package me.calebeoliveira.springaopexample.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
@Slf4j
public class CircuitBreakerAspectMyImpl {
    private final Map<String, CircuitBreakerState> states = new ConcurrentHashMap<>();

    @Around("@annotation(me.calebeoliveira.springaopexample.model.CircuitBreaker)")
    public Object circuitBreaker(ProceedingJoinPoint joinPoint) throws Throwable {
        String key = joinPoint.getSignature().toShortString();
        CircuitBreakerState state = states.computeIfAbsent(key, k -> new CircuitBreakerState());

        if(state.isOpen()) {
            if(state.shouldAttemptReset()) {
                log.info("Circuit is half open for {}", key);
                state.setHalfOpen();
            } else {
                throw new RuntimeException("Circuit breaker is OPEN for " + key);
            }
        }

        try {
            Object result = joinPoint.proceed();
            state.recordSuccess();
            return result;
        } catch (Exception e) {
            state.recordFailure();
            throw e;
        }
    }

    private static class CircuitBreakerState {
        private int failureCount = 0;
        private long lastFailureTime = 0;
        private boolean open = false;
        private int failureThreshold = 5;
        private long timeoutMs = 30000;

        public boolean isOpen() {
            if(!open) {
                return false;
            }

            return System.currentTimeMillis() - lastFailureTime < timeoutMs;
        }

        public boolean shouldAttemptReset() {
            return open && System.currentTimeMillis() - lastFailureTime >= timeoutMs;
        }

        public void recordFailure() {
            failureCount++;
            lastFailureTime = System.currentTimeMillis();
            if(failureCount >= failureThreshold) {
                open = true;
            }
        }

        public void recordSuccess() {
            failureCount = 0;
            open = false;
        }

        public void setHalfOpen() {
            open = false;
            failureCount = failureThreshold;
        }
    }
}
