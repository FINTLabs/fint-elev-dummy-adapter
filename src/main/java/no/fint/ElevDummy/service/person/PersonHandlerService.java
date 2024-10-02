package no.fint.ElevDummy.service.person;

import lombok.extern.slf4j.Slf4j;
import no.fint.DummyAdapterActions;
import no.fint.ElevDummy.service.CacheService;
import no.fint.ElevDummy.service.Handler;
import no.fint.adapter.event.EventResponseService;
import no.fint.adapter.event.EventStatusService;
import no.fint.event.model.Event;
import no.fint.model.resource.FintLinks;
import no.fint.model.resource.felles.PersonResource;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class PersonHandlerService implements Handler {

    private EventResponseService eventResponseService;

    private EventStatusService eventStatusService;

    private CacheService cacheService;

    public PersonHandlerService(EventResponseService eventResponseService, EventStatusService eventStatusService, CacheService cacheService) {
        this.eventResponseService = eventResponseService;
        this.eventStatusService = eventStatusService;
        this.cacheService = cacheService;
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
        List<PersonResource> personer = cacheService.getPersoner();
        personer.forEach(response::addData);
    }
}
