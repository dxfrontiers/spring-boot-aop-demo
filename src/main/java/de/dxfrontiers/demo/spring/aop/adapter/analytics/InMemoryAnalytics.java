package de.dxfrontiers.demo.spring.aop.adapter.analytics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class InMemoryAnalytics implements AnalyticsInterface {

    private final ConcurrentMap<String, Integer> eventsCounter = new ConcurrentHashMap<>();

    @Override
    public void triggerEvent(String eventName) {
        eventsCounter.put(eventName, eventsCounter.getOrDefault(eventName, 0) + 1);
        LOG.debug("Event {} detected {} times", eventName, eventsCounter.get(eventName));
    }

    private static final Logger LOG = LoggerFactory.getLogger(InMemoryAnalytics.class);
}
