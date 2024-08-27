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

    private List<Dog> dogs;
    private List<Owner> owners;

    private List<Person> personList;
    private List<Elev> elevList;

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

    /**
     * TODO
     * <p>
     * createEventResponse is responsible for responding to the <code>event</code>. This is what should be done:
     * </p>
     * <ol>
     * <li>Call the code to handle the action</li>
     * <li>Posting back the handled <code>event</code>. This done in the {@link EventResponseService#postResponse(Event)} method</li>
     * </ol>
     * <p>
     * This is where you implement your code for handling the <code>event</code>. It is typically done by making a onEvent method:
     * </p>
     * <pre>
     *     {@code
     *     public void onGetAllDogs(Event<FintResource> dogAllEvent) {
     *
     *         // Call a service to get all dogs from the application and add the result to the event data
     *         // dogAllEvent.addData(dogResource);
     *
     *     }
     *     }
     * </pre>
     *
     * @param event The <code>event</code> received from the provider
     */
    private void createEventResponse(Event event, Event<FintResource> responseEvent) {
        switch (ElevActions.valueOf(event.getAction())) {
            case GET_ALL_ELEV:
                onGetElev(responseEvent);
                break;
            case GET_ALL_PERSON:
                onGetOwner(responseEvent);
                break;
        }
    }

    /**
     * TODO
     * Example of handling action
     *
     * @param responseEvent Event containing the response
     */
    private void onGetOwner(Event<FintResource> responseEvent) {
        Optional<Person> person = personList.stream().filter(p -> p.getFodselsnummer().equals(responseEvent.getQuery())).findFirst();

        if (person.isPresent()) {
            responseEvent.addData(FintResource.with(person.get()).addRelations(
                    new Relation.Builder().with(Person.Relasjonsnavn.ELEV).forType(Elev.class).value(person.get().getFodselsnummer().getIdentifikatorverdi().substring(0, 1)).build())
            );
        }
    }

    /**
     * TODO
     * Example of handling action
     *
     * @param responseEvent Event containing the response
     */
    private void onGetElev(Event<FintResource> responseEvent) {
        Optional<Elev> elev = elevList.stream().filter(e -> e.getSystemId().getIdentifikatorverdi().equals(responseEvent.getQuery())).findFirst();

        if (elev.isPresent()) {
            responseEvent.addData(FintResource.with(elev.get()).addRelations(
                    new Relation.Builder().with(Elev.Relasjonsnavn.PERSON).forType(Owner.class).value(elev.get().getSystemId() + "0").build())
            );

        }
    }

    /**
     * TODO
     * Example of handling action
     *
     * @param responseEvent Event containing the response
     */
    private void onGetAllPerson(Event<FintResource> responseEvent) {


        Relation relationElev = new Relation.Builder().with(Person.Relasjonsnavn.ELEV).forType(Elev.class).value("1").build();
        Relation relationElev2 = new Relation.Builder().with(Person.Relasjonsnavn.ELEV).forType(Elev.class).value("2").build();

        responseEvent.addData(FintResource.with(personList.get(0)).addRelations(relationElev));
        responseEvent.addData(FintResource.with(personList.get(1)).addRelations(relationElev2));

    }

    /**
     * TODO
     * Example of handling action
     *
     * @param responseEvent Event containing the response
     */
    private void onGetAllDogs(Event<FintResource> responseEvent) {

        Relation relationPerson = new Relation.Builder().with(Elev.Relasjonsnavn.PERSON).forType(Owner.class).value("10").build();
        Relation relationPerson2 = new Relation.Builder().with(Elev.Relasjonsnavn.PERSON).forType(Owner.class).value("20").build();

        responseEvent.addData(FintResource.with(elevList.get(0)).addRelations(relationPerson));
        responseEvent.addData(FintResource.with(elevList.get(1)).addRelations(relationPerson2));

    }

    /**
     * Checks if the application is healthy and updates the event object.
     *
     * @param event The event object
     */
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

    /**
     * TODO
     * This is where we implement the health check code
     *
     * @return {@code true} if health is ok, else {@code false}
     */
    private boolean healthCheck() {
        /*
         * Check application connectivity etc.
         */
        return true;
    }

    /**
     * TODO
     * Data used in examples
     */
    @PostConstruct
    void init() {
        owners = new ArrayList<>();
        dogs = new ArrayList<>();
        personList = new ArrayList<>();
        elevList = new ArrayList<>();
        populatePerson(3);
        populateElev(3);
    }

    private Identifikator createId(){
        Identifikator identifikator = new Identifikator();
        identifikator.setIdentifikatorverdi(UUID.randomUUID().toString());
        return identifikator;
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
}