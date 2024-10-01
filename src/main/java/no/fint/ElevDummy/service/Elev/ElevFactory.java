package no.fint.ElevDummy.service.Elev;

import no.fint.ElevDummy.ElevProperties.ElevProperties;
import no.fint.ElevDummy.PersonProperties.AdresseProperties;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.utdanning.elev.ElevResource;

import java.util.UUID;

public class ElevFactory {

    private final ElevProperties properties;
    private final AdresseProperties adresseProperties;

    public ElevFactory(ElevProperties properties, AdresseProperties adresseProperties) {
        this.properties = properties;
        this.adresseProperties = adresseProperties;
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
        return elev;
    }
}
