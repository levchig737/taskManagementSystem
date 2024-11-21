package io.chekarev.taskManagementSystem.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    /**
     * Логирование выполнения метода и возврата результата.
     * @param joinPoint joinPoint
     * @return result
     * @throws Throwable Throwable
     */
    @Around("execution(* io.chekarev.taskManagementSystem.controllers..*(..)) || execution(* io.chekarev.taskManagementSystem.services..*(..)) || execution(* io.chekarev.taskManagementSystem.repositories..*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        log.info("Method {} is about to execute with arguments: {}",
                joinPoint.getSignature(),
                joinPoint.getArgs());

        Object result;
        try {
            result = joinPoint.proceed();

            long endTime = System.currentTimeMillis();
            log.info("Method {} executed successfully, returned: {}, execution time: {} ms",
                    joinPoint.getSignature(),
                    result,
                    endTime - startTime);
        } catch (Throwable throwable) {
            log.error("Exception in method {}: {}",
                    joinPoint.getSignature(),
                    throwable.getMessage());
            throw throwable;
        }
        return result;
    }
}
