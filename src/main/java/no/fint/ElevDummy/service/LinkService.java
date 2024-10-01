package no.fint.ElevDummy.service;


import no.fint.ElevDummy.service.person.PersonHandlerService;
import no.fint.model.felles.Person;
import no.fint.model.resource.Link;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.utdanning.elev.ElevResource;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class LinkService {

    private final PersonHandlerService personHandlerService;

    public LinkService(PersonHandlerService personHandlerService) {
        this.personHandlerService = personHandlerService;
    }

    private String getId() {
        Random random = new Random();
        PersonResource personResource = personHandlerService.getPersoner().get(random.nextInt(personHandlerService.personSize()));
        return personResource.getFodselsnummer().getIdentifikatorverdi();
    }

    public ElevResource addPerson(ElevResource elev) {
        elev.addPerson(Link.with(Person.class, "fodselsnummer", getId()));
        return elev;
    }
}
