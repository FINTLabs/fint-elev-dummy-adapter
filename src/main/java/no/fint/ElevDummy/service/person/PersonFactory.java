package no.fint.ElevDummy.service.person;

import lombok.extern.slf4j.Slf4j;
import no.fint.ElevDummy.PersonProperties.AdresseProperties;
import no.fint.ElevDummy.PersonProperties.KontaktInfoProperties;
import no.fint.ElevDummy.PersonProperties.PersonProperties;
import no.fint.ElevDummy.service.IdUtil;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.felles.PersonResource;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class PersonFactory {

    private final PersonProperties personProperties;
    private final AdresseProperties adresseProperties;
    private final KontaktInfoProperties kontaktInfoProperties;

    public PersonFactory(PersonProperties personProperties, AdresseProperties adresseProperties, KontaktInfoProperties kontaktInfoProperties) {
        this.personProperties = personProperties;
        this.adresseProperties = adresseProperties;
        this.kontaktInfoProperties = kontaktInfoProperties;
    }

    public PersonResource createPerson() {
        PersonResource personResource = new PersonResource();
        Identifikator identifikator = IdUtil.createId();
        personResource.setNavn(personProperties.getNames().getRandomPersonnavn());
        personResource.setBostedsadresse(adresseProperties.getRandomAdresse());
        personResource.setFodselsnummer(identifikator);
        personResource.setFodselsdato(setFodselsdato());
        personResource.setPostadresse(adresseProperties.getRandomAdresse());
        personResource.setKontaktinformasjon(kontaktInfoProperties.getRandomKontaktinformasjon());
        return personResource;
    }

    private Date setFodselsdato() {
        Date fodselsdato = new Date();
        fodselsdato.setYear(2003);
        fodselsdato.setMonth(9);
        fodselsdato.setDate(26);
        return fodselsdato;
    }

}
