package no.fint.ElevDummy.service.Elev;

import no.fint.ElevDummy.ElevProperties.ElevProperties;
import no.fint.ElevDummy.PersonProperties.AdresseProperties;
import no.fint.ElevDummy.service.IdUtil;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.utdanning.elev.ElevResource;
import org.springframework.stereotype.Service;

@Service
public class ElevFactory {

    private final ElevProperties properties;
    private final AdresseProperties adresseProperties;


    public ElevFactory(ElevProperties properties, AdresseProperties adresseProperties) {
        this.properties = properties;
        this.adresseProperties = adresseProperties;
    }

    public ElevResource createElev() {
        ElevResource elev = new ElevResource();
        Identifikator identifikator = IdUtil.createId();
        identifikator.setIdentifikatorverdi(identifikator.getIdentifikatorverdi());

        elev.setBrukernavn(properties.getRandomBrukerNavn());
        elev.setFeidenavn(properties.getRandomFeideNavn());
        elev.setSystemId(identifikator);
        elev.setHybeladresse(adresseProperties.getRandomAdresse());
        elev.setElevnummer(identifikator);
        return elev;
    }
}
