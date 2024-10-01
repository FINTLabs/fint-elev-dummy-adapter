package no.fint.ElevDummy.service;

import lombok.extern.slf4j.Slf4j;
import no.fint.adapter.event.EventResponseService;
import no.fint.adapter.event.EventStatusService;
import no.fint.ElevDummy.SupportedActions;
import no.fint.event.model.Event;
import no.fint.event.model.ResponseStatus;
import no.fint.event.model.Status;
import no.fint.event.model.health.Health;
import no.fint.event.model.health.HealthStatus;
import no.fint.model.resource.FintLinks;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

@Slf4j
@Service
public class EventHandlerService {

    @Autowired
    private EventResponseService eventResponseService;

    @Autowired
    private EventStatusService eventStatusService;

    @Autowired
    private SupportedActions supportedActions;

    @Autowired
    private Collection<Handler> handlers;

    private Map<String, Handler> actionsHandlerMap;

    @Autowired
    @Qualifier("responseHandler")
    private Executor executor;

    public Set<String> getActions() {
        return actionsHandlerMap.keySet();
    }

    public void handleEvent(String component, Event event) {
        if (event.isHealthCheck()) {
            postHealthCheckResponse(component, event);
        } else if (eventStatusService.verifyEvent(component, event)) {
            executor.execute(() -> processEvent(component, new Event<FintLinks>(event)));
        } else {
            log.debug("Unknown event: {}", event);
        }
    }

    private void processEvent(String component, Event event) {
        try {
            actionsHandlerMap.getOrDefault(event.getAction(), e -> {
                log.warn("No handler found for {}", event.getAction());
                e.setStatus(Status.ADAPTER_REJECTED);
                e.setResponseStatus(ResponseStatus.REJECTED);
                e.setMessage("Unsupported action");
                e.setStatusCode("UNSUPPORTED_ACTION");
            }).accept(event);
        } catch (Exception e) {
            log.info("Unable to handle event {}", event, e);
            event.setResponseStatus(ResponseStatus.ERROR);
            event.setStatusCode("EVENT_HANDLER_EXCEPTION");
            event.setMessage(ExceptionUtils.getStackTrace(e));

        }
        eventResponseService.postResponse(component, event);
    }

    public void postHealthCheckResponse(String component, Event event) {
        Event<Health> healthCheckEvent = new Event<>(event);
        healthCheckEvent.setStatus(Status.TEMP_UPSTREAM_QUEUE);

        if (healthCheck()) {
            healthCheckEvent.addData(new Health("adapter", HealthStatus.APPLICATION_HEALTHY));
        } else {
            healthCheckEvent.addData(new Health("adapter", HealthStatus.APPLICATION_UNHEALTHY));
            healthCheckEvent.setMessage("The adapter is unable to communicate with the application.");
        }

        eventResponseService.postResponse(component, healthCheckEvent);
    }

    private boolean healthCheck() {
        return handlers.stream().allMatch(Handler::health);
    }

    @PostConstruct
    void init() {
        actionsHandlerMap = new HashMap<>();
        handlers.forEach(h -> h.actions().forEach(a -> {
            actionsHandlerMap.put(a, h);
            supportedActions.add(a);
        }));
        log.info("Registered {} handlers, supporting actions: {}", handlers.size(), supportedActions.getActions());
    }

}
