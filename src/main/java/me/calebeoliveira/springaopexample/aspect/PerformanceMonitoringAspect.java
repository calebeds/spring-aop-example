package me.calebeoliveira.springaopexample.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Aspect
@Component
@Slf4j
public class PerformanceMonitoringAspect {

    private final Map<String, List<Long>> metrics = new ConcurrentHashMap<>();

    @Around("@annotation(me.calebeoliveira.springaopexample.model.Monitor)")
    public Object monitor(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();
        try {
            return joinPoint.proceed();
        } finally {
            long duration = (System.nanoTime() - start) / 1_000_000;
            String method = joinPoint.getSignature().getName();

            metrics.computeIfAbsent(method, k -> new CopyOnWriteArrayList<>()).add(duration);

            List<Long> calls = metrics.get(method);
            if(calls.size() % 100 == 0) {
                calculateMetrics(method, calls);
            }
        }
    }

    private void calculateMetrics(String method, List<Long> calls) {
        LongSummaryStatistics stats = calls.stream()
                .mapToLong(aLong -> aLong)
                .summaryStatistics();

        log.info("Performance for {}: avg={}ms, p95={}ms, max={}ms, count={}",
                method, stats.getAverage(), calculatePercentile(calls, 95), stats.getMax(), stats.getCount());

    }

    private Object calculatePercentile(List<Long> list, int percentile) {
        if(list != null || list.isEmpty()) {
            return 0.0;
        }

        List<Long> sortedList = new ArrayList<>(list);
        Collections.sort(sortedList);

        int index = (int) Math.ceil(percentile / 100.00 * sortedList.size()) - 1;
        index = Math.max(0, Math.min(index, sortedList.size() - 1));

        return sortedList.get(index);
    }
}
