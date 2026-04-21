package me.calebeoliveira.springaopexample.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* me.calebeoliveira.springaopexample.service.*.*(..))")
    public void logBeforeMethod(JoinPoint joinPoint) {
        log.info("-> Entering {}.{}() with arguments: {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "execution(* me.calebeoliveira.springaopexample.service.*.*(..))", returning = "result")
    public void logAfterMethod(JoinPoint joinPoint, Object result) {
        log.info("<- Exiting: {}.{}() with result: {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                result);
    }

    @AfterThrowing(pointcut = "execution(* me.calebeoliveira.springaopexample.service.*.*(..))", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        log.error("x Exception in: {}.{}() - {}: {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                error.getClass().getSimpleName(),
                error.getMessage());
    }

    @Around("execution(* me.calebeoliveira.springaopexample.service.*.*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Instant start = Instant.now();

        log.debug("Timing execution of: {}.{}()",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName());

        Object result = joinPoint.proceed();

        Instant end = Instant.now();
        long duration = Duration.between(start, end).toMillis();

        log.info("{}.{}() executed in {} ms",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                duration);

        return result;
    }

}
