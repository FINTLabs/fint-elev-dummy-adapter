package no.fint.customcode.service;

import lombok.extern.slf4j.Slf4j;
import no.fint.DummyAdapterActions;
import no.fint.adapter.event.EventResponseService;
import no.fint.adapter.event.EventStatusService;
import no.fint.event.model.Event;
import no.fint.event.model.ResponseStatus;
import no.fint.model.resource.FintLinks;
import no.fint.model.resource.utdanning.elev.ElevResource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class ElevHandlerService implements Handler {

    private EventResponseService eventResponseService;

    private EventStatusService eventStatusService;

    private List<ElevResource> elever;

    public ElevHandlerService(EventStatusService eventStatusService, EventResponseService eventResponseService) {
        this.eventStatusService = eventStatusService;
        this.eventResponseService = eventResponseService;
        elever = new ArrayList<>();
        populateElev(3);
    }

    @Override
    public Set<String> actions() {
        return Collections.singleton(DummyAdapterActions.GET_ALL_ELEV.name());
    }

    @Override
    public boolean health() {
        return true;
    }

    @Override
    public void accept(Event<FintLinks> response) {
        elever.forEach(response::addData);
        response.setResponseStatus(ResponseStatus.ACCEPTED);
    }

    private void populateElev(int i) {
        for (int j = 0; j < i; j++) {
            ElevResource elev = new ElevResource();
            elev.setSystemId(IdUtil.createId());
            elever.add(elev);
        }
    }
}