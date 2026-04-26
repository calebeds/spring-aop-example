package me.calebeoliveira.springaopexample.aspect;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.calebeoliveira.springaopexample.entity.AuditLog;
import me.calebeoliveira.springaopexample.repository.AuditLogRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditLogRepository auditLogRepository;

    @AfterReturning(pointcut = "@annotation(me.calebeoliveira.springaopexample.model.Auditable)", returning = "result")
    @Transactional
    public void audit(JoinPoint joinPoint, Object result) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "anonymous";

        AuditLog auditLog = AuditLog.builder()
                .username(username)
                .method(joinPoint.getSignature().getName())
                .arguments(truncate(Arrays.toString(joinPoint.getArgs()), 1000))
                .result(truncate(result != null ? result.toString() : "void", 1000))
                .timestamp(Instant.now())
                .build();

        auditLogRepository.save(auditLog);
    }

    private String truncate(String string, int maxLength) {
        if(string == null || string.length() <= maxLength) {
            return string;
        }

        return string.substring(0, maxLength) + "...";
    }
}
