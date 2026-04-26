package me.calebeoliveira.springaopexample.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimitingAspect {
    private final Map<String, Deque<Long>> callLogs = new ConcurrentHashMap<>();

    @Around("@annotation(me.calebeoliveira.springaopexample.model.RateLimit)")
    public Object rateLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        String key = joinPoint.getSignature().toShortString();
        long now = System.currentTimeMillis();
        long windowMs = 60000;
        int maxCalls = 10;

        Deque<Long> calls = callLogs.computeIfAbsent(key, k -> new ArrayDeque<>());

        calls.removeIf(timestamp -> timestamp < now - windowMs);

        if(calls.size() >= maxCalls) {
            throw new RuntimeException("Rate limit exceeded. Try again later.");
        }

        calls.add(now);
        return joinPoint.proceed();

    }
}
