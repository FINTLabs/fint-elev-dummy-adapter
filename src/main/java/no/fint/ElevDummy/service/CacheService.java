package no.fint.ElevDummy.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.ElevDummy.service.Elev.ElevFactory;
import no.fint.ElevDummy.service.person.PersonFactory;
import no.fint.model.felles.Person;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.Link;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.utdanning.elev.ElevResource;
import no.fint.model.utdanning.elev.Elev;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CacheService {

    private final ElevFactory elevFactory;

    private final PersonFactory personFactory;

    @Getter
    List<PersonResource> personer = new ArrayList<>();

    @Getter
    List<ElevResource> elever = new ArrayList<>();

    public CacheService(ElevFactory elevFactory, PersonFactory personFactory) {
        this.elevFactory = elevFactory;
        this.personFactory = personFactory;
        populateCache(3);
    }

    private void populateCache(int i) {
        for (int j = 0; j < i; j++) {
            PersonResource person = personFactory.createPerson();
            ElevResource elev = elevFactory.createElev();
            personer.add(person);
            elever.add(elev);

            addElev(person, elev.getSystemId());
            addPerson(elev, person.getFodselsnummer());
        }
        log.info("Persons created: {}", personer.toString());
        log.info("Elever created: {}", elever.toString());
    }

    public ElevResource addPerson(ElevResource elev, Identifikator id) {
        elev.addPerson(Link.with(Person.class, "fodselsnummer", id.getIdentifikatorverdi()));
        return elev;
    }

    public PersonResource addElev(PersonResource person, Identifikator id) {
        person.addElev(Link.with(Elev.class, "systemId", id.getIdentifikatorverdi()));
        return person;
    }
}
