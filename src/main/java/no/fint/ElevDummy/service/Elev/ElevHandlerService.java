package no.fint.ElevDummy.service.Elev;

import lombok.extern.slf4j.Slf4j;
import no.fint.DummyAdapterActions;
import no.fint.ElevDummy.service.Handler;
import no.fint.ElevDummy.service.person.PersonFactory;
import no.fint.adapter.event.EventResponseService;
import no.fint.adapter.event.EventStatusService;
import no.fint.event.model.Event;
import no.fint.model.resource.FintLinks;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.utdanning.elev.ElevResource;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class ElevHandlerService implements Handler {

    private EventResponseService eventResponseService;

    private EventStatusService eventStatusService;

    private List<ElevResource> elever;

    private ElevFactory elevFactory;

    public ElevHandlerService(EventResponseService eventResponseService, EventStatusService eventStatusService, List<ElevResource> elever, ElevFactory elevFactory) {
        this.eventResponseService = eventResponseService;
        this.eventStatusService = eventStatusService;
        this.elever = elever;
        this.elevFactory = elevFactory;
        populateCache(1);
    }

    private void populateCache (int i){
        for(int j = 0; j < i; j ++){
            elever.add(elevFactory.generateElev());
        }
        log.info("Elever created: {}", elever.toString());
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
    }
}
