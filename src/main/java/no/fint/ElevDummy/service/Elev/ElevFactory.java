package no.fint.ElevDummy.service.Elev;

import no.fint.ElevDummy.ElevProperties.ElevProperties;
import no.fint.ElevDummy.PersonProperties.AdresseProperties;
import no.fint.ElevDummy.service.LinkService;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.utdanning.elev.ElevResource;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ElevFactory {

    private final ElevProperties properties;
    private final AdresseProperties adresseProperties;
    private final LinkService linkService;

    public ElevFactory(ElevProperties properties, AdresseProperties adresseProperties, LinkService linkService) {
        this.properties = properties;
        this.adresseProperties = adresseProperties;
        this.linkService = linkService;
    }

    public ElevResource generateElev() {
        ElevResource elev = new ElevResource();
        Identifikator identifikator = new Identifikator();
        identifikator.setIdentifikatorverdi(UUID.randomUUID().toString());
        elev.setBrukernavn(properties.getRandomBrukerNavn());
        elev.setFeidenavn(properties.getRandomFeideNavn());
        elev.setSystemId(identifikator);
        elev.setHybeladresse(adresseProperties.getRandomAdresse());
        elev.setElevnummer(identifikator);
        linkService.addPerson(elev);
        return elev;
    }
}
