package de.dxfrontiers.demo.spring.aop.aspects.analytics;

import de.dxfrontiers.demo.spring.aop.adapter.analytics.AnalyticsInterface;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class AnalyticAspect {

    private final AnalyticsInterface analytics;

    public AnalyticAspect(AnalyticsInterface analytics) {
        this.analytics = analytics;
    }

    /**
     * Around advice that is executed around the defined pointcut.
     * @param analyticEvent the annotation object
     */
    @AfterReturning(value = "@annotation(analyticEvent)")
    public void afterReturn(AnalyticEvent analyticEvent) {
        this.analytics.triggerEvent(analyticEvent.value());
    }
}
