package me.calebeoliveira.springaopexample.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
@Slf4j
public class CachingAspect {
    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    @Around("@annotation(me.calebeoliveira.springaopexample.model.CacheResult)")
    public Object cacheResult(ProceedingJoinPoint joinPoint) throws Throwable {
        String key = joinPoint.getSignature().toShortString() + Arrays.toString(joinPoint.getArgs());

        if(cache.containsKey(key)) {
            log.info("Cache HIT for: {}", key);
            return cache.get(key);
        }

        log.info("Cache MISS for: {}", key);
        Object result = joinPoint.proceed();
        cache.put(key, result);
        return result;
    }
}
