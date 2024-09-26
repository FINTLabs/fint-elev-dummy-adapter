package no.fint.customcode.service.person;

import lombok.extern.slf4j.Slf4j;
import no.fint.DummyAdapterActions;
import no.fint.adapter.event.EventResponseService;
import no.fint.adapter.event.EventStatusService;
import no.fint.customcode.service.Handler;
import no.fint.event.model.Event;
import no.fint.model.resource.FintLinks;
import no.fint.model.resource.felles.PersonResource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class PersonHandlerService implements Handler {

    private EventResponseService eventResponseService;

    private EventStatusService eventStatusService;

    private List<PersonResource> personer;


    public PersonHandlerService(EventResponseService eventResponseService, EventStatusService eventStatusService) {
        this.eventResponseService = eventResponseService;
        this.eventStatusService = eventStatusService;
        personer = new ArrayList<>();
    }

    @Override
    public Set<String> actions() {
        return Collections.singleton(DummyAdapterActions.GET_ALL_PERSON.name());
    }

    @Override
    public boolean health() {
        return true;
    }

    @Override
    public void accept(Event<FintLinks> response) {
        personer.forEach(response::addData);
    }
}
