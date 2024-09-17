package no.fint.customcode.service;

import no.fint.DummyAdapterActions;
import no.fint.adapter.event.EventResponseService;
import no.fint.adapter.event.EventStatusService;
import no.fint.event.model.Event;
import no.fint.model.resource.FintLinks;
import no.fint.model.resource.felles.PersonResource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class PersonHandler implements Handler {

    private EventResponseService eventResponseService;

    private EventStatusService eventStatusService;

    private List<PersonResource> personer;

    public PersonHandler(EventResponseService eventResponseService, EventStatusService eventStatusService) {
        this.eventResponseService = eventResponseService;
        this.eventStatusService = eventStatusService;
        personer = new ArrayList<>();
        populatePerson(3);
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

    private void populatePerson(int i) {
        for (int j = 0; j < i; j++) {
            PersonResource person = new PersonResource();
            person.setFodselsnummer(IdUtil.createId());
            personer.add(person);
        }
    }

}
