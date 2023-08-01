package de.dxfrontiers.demo.spring.aop.aspects.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


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
     * Advice that is executed before the annotated method call (see pointcut).
     * @param joinPoint object that gives the advice the runtime information of the join point
     */
    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType()).trace("Calling {} with {}", joinPoint.getSignature().getName(), joinPoint.getArgs());
    }

    /**
     * Advice that is executed before the annotated method call (see pointcut).
     * @param joinPoint object that gives the advice the runtime information of the join point
     * @param retVal object that was returned by the target method
     */
    @AfterReturning(value = "pointcut()", returning = "retVal")
    public void afterReturn(JoinPoint joinPoint, Object retVal) {
        LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType()).trace("{} called with {} returned {}", joinPoint.getSignature().getName(), joinPoint.getArgs(), retVal);
    }

    /**
     * Advice that is executed before the annotated method call (see pointcut).
     * @param jp object that gives the advice the runtime information of the join point
     * @param ex exception that was by the target method
     */
    @AfterThrowing(value = "pointcut()", throwing = "ex")
    public void afterThrowing(JoinPoint jp, Throwable ex) {
        LoggerFactory.getLogger(jp.getSignature().getDeclaringType()).trace(
                "Method {} called with {} threw {}",
                jp.getSignature().getName(),
                jp.getArgs(),
                ex.toString()
        );
    }
}
