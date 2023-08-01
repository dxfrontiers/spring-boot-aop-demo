package de.dxfrontiers.demo.spring.aop.aspects.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;


@Aspect
@Component
public class LoggerAspect {

    /**
     * Pointcut for annotated methods.
     */
    @Pointcut("@annotation(de.dxfrontiers.demo.spring.aop.aspects.logging.Log)")
    private void methodPointcut() {}

    /**
     * Pointcut for annotated types (interfaces, classes, ...).
     */
    @Pointcut("within(@de.dxfrontiers.demo.spring.aop.aspects.logging.Log *)")
    private void typePointcut() {}

    /**
     * Pointcut that combines the above pointcut definitions.
     */
    @Pointcut("methodPointcut() || typePointcut()")
    private void pointcut() {}

    /**
     * Around advice that is executed around the defined pointcut.
     * @param pjp the ProceedingJoinPoint object. .proceed() has to be called by us (if we want).
     * @return returns the return value. We can modify it at runtime.
     * @throws Throwable we can throw an own exception, but it should match the underling method signature.
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Logger logger = LoggerFactory.getLogger(pjp.getSignature().getDeclaringType());
        logger.trace("Calling {} with {}", pjp.getSignature().getName(), pjp.getArgs());
        StopWatch sw = new StopWatch(pjp.toLongString());
        sw.start();
        try {
            Object retVal = pjp.proceed();
            sw.stop();
            logger.trace(
                    "Method {} called with {} returned {} executed in {} ms",
                    pjp.getSignature().getName(),
                    pjp.getArgs(),
                    retVal,
                    sw.getTotalTimeMillis()
            );
            return retVal;
        } catch (Throwable ex) {
            sw.stop();
            logger.trace(
                    "Method {} called with {} threw {} after {} ms",
                    pjp.getSignature().getName(),
                    pjp.getArgs(),
                    ex,
                    sw.getTotalTimeMillis()
            );
            throw ex;
        }
    }
}
