package app.homsai.engine;

import java.util.UUID;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Configuration
public class AspectConfig {
    private static final String REF_ID = "refId";

    // Adds MDC to every HTTP Controller, before execution
    @Before("execution(* app.homsai.engine.*.application.http.controllers.*.*(..))")
    public void addMDC(JoinPoint joinPoint) {
        MDC.put(REF_ID, UUID.randomUUID().toString().replace("-", "").substring(0, 12));
    }

    // Remove MDC to every HTTP Controller, after execution
    @After("execution(* app.homsai.engine.*.application.http.controllers.*.*(..))")
    public void removeMDC(JoinPoint joinPoint) {
        MDC.remove(REF_ID);
    }
}
