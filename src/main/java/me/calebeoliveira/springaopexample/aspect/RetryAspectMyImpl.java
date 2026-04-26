package me.calebeoliveira.springaopexample.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class RetryAspectMyImpl {

    @Around("@annotation(me.calebeoliveira.springaopexample.model.Retryable)")
    public Object retry(ProceedingJoinPoint joinPoint) throws Throwable {
        int maxRetries = 3;
        int delay = 1000;

        for(int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                return joinPoint.proceed();
            } catch (Exception e) {
                log.warn("Attempt {} failed for {}: {}", attempt,
                        joinPoint.getSignature().getName(), e.getMessage());

                if(attempt == maxRetries) {
                    throw e;
                }

                Thread.sleep(delay);
                delay += 2;
            }
        }

        throw new RuntimeException("Unreachable");
    }
}
