package no.fint.customcode.service;

import lombok.extern.slf4j.Slf4j;
import no.fint.ElevActions;
import no.fint.adapter.event.EventResponseService;
import no.fint.adapter.event.EventStatusService;
import no.fint.event.model.Event;
import no.fint.event.model.ResponseStatus;
import no.fint.event.model.Status;
import no.fint.event.model.health.Health;
import no.fint.event.model.health.HealthStatus;
import no.fint.model.felles.Person;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.pwfa.Dog;
import no.fint.model.pwfa.Owner;
import no.fint.model.relation.FintResource;
import no.fint.model.relation.Relation;
import no.fint.model.utdanning.elev.Elev;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The EventHandlerService receives the <code>event</code> from SSE endpoint (provider) in the {@link #handleEvent(Event)} method.
 */
@Slf4j
@Service
public class EventHandlerService {

    @Autowired
    private EventResponseService eventResponseService;

    @Autowired
    private EventStatusService eventStatusService;

    private List<Person> personList;
    private List<Elev> elevList;

    @PostConstruct
    void init() {
        personList = new ArrayList<>();
        elevList = new ArrayList<>();
        populatePerson(3);
        populateElev(3);
    }

    private void populateElev(int i) {
        for (int j = 0; j < i; j++) {
            Elev elev = new Elev();
            elev.setSystemId(createId());
            elevList.add(elev);
        }
    }

    private void populatePerson(int i){
        for (int j = 0; j < i ; j++) {
            Person person = new Person();
            person.setFodselsnummer(createId());
            personList.add(person);
        }
    }

    private Identifikator createId(){
        Identifikator identifikator = new Identifikator();
        identifikator.setIdentifikatorverdi(UUID.randomUUID().toString());
        return identifikator;
    }

    public void handleEvent(Event event) {
        if (event.isHealthCheck()) {
            postHealthCheckResponse(event);
        } else {
            if (eventStatusService.verifyEvent(event).getStatus() == Status.ADAPTER_ACCEPTED) {
                Event<FintResource> responseEvent = new Event<>(event);
                try {

                    createEventResponse(event, responseEvent);

                } catch (Exception e) {
                    log.error("Error handling event {}", event, e);
                    responseEvent.setResponseStatus(ResponseStatus.ERROR);
                    responseEvent.setMessage(e.getMessage());
                } finally {
                    responseEvent.setStatus(Status.ADAPTER_RESPONSE);
                    eventResponseService.postResponse(responseEvent);
                }
            }
        }
    }


    private void createEventResponse(Event event, Event<FintResource> responseEvent) {
        switch (ElevActions.valueOf(event.getAction())) {
            case GET_ALL_ELEV:
                onGetElev(responseEvent);
                break;
            case GET_ALL_PERSON:
                onGetAllPerson(responseEvent);
                break;
        }
    }

    private void onGetElev(Event<FintResource> responseEvent) {
        Optional<Elev> elev = elevList.stream().filter(e -> e.getSystemId().getIdentifikatorverdi().equals(responseEvent.getQuery())).findFirst();

        if (elev.isPresent()) {
            responseEvent.addData(FintResource.with(elev.get()).addRelations(
                    new Relation.Builder().with(Elev.Relasjonsnavn.PERSON).forType(Owner.class).value(elev.get().getSystemId() + "0").build())
            );

        }
    }

    private void onGetAllPerson(Event<FintResource> responseEvent) {

        Relation relationElev = new Relation.Builder().with(Person.Relasjonsnavn.ELEV).forType(Elev.class).value("1").build();
        Relation relationElev2 = new Relation.Builder().with(Person.Relasjonsnavn.ELEV).forType(Elev.class).value("2").build();

        responseEvent.addData(FintResource.with(personList.get(0)).addRelations(relationElev));
        responseEvent.addData(FintResource.with(personList.get(1)).addRelations(relationElev2));

    }

    public void postHealthCheckResponse(Event event) {
        Event<Health> healthCheckEvent = new Event<>(event);
        healthCheckEvent.setStatus(Status.TEMP_UPSTREAM_QUEUE);

        if (healthCheck()) {
            healthCheckEvent.addData(new Health("adapter", HealthStatus.APPLICATION_HEALTHY.name()));
        } else {
            healthCheckEvent.addData(new Health("adapter", HealthStatus.APPLICATION_UNHEALTHY));
            healthCheckEvent.setMessage("The adapter is unable to communicate with the application.");
        }

        eventResponseService.postResponse(healthCheckEvent);
    }

    private boolean healthCheck() {
        /*
         * Check application connectivity etc.
         */
        return true;
    }
}
