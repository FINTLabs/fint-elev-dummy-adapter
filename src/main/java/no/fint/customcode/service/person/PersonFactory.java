package no.fint.customcode.service.person;

import lombok.extern.slf4j.Slf4j;
import no.fint.customcode.service.IdUtil;
import no.fint.customcode.service.person.Properties.AdresseProperties;
import no.fint.customcode.service.person.Properties.PersonProperties;
import no.fint.model.felles.kompleksedatatyper.Personnavn;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.felles.kompleksedatatyper.AdresseResource;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;

@Slf4j
@Component
public class PersonFactory {

    private final PersonProperties personProperties;
    private final AdresseProperties adresseProperties;

    public PersonFactory(PersonProperties personProperties, AdresseProperties adresseProperties) {
        this.personProperties = personProperties;
        this.adresseProperties = adresseProperties;
    }

    public PersonResource createPerson() {
        PersonResource personResource = new PersonResource();
        personResource.setNavn(randomPersonnavn());
        personResource.setBostedsadresse(randomAdresse());
        personResource.setFodselsnummer(IdUtil.createId());
        personResource.setFodselsdato(randomFodselsDato());
        personResource.setPostadresse(randomAdresse());
        return personResource;
    }

    private Date randomFodselsDato() {
        Random random = new Random();
        Date fodselsdato = new Date();
        fodselsdato.setYear(2003);
        fodselsdato.setMonth(9);
        fodselsdato.setDate(26);
        return fodselsdato;
    }

    private Personnavn randomPersonnavn() {
        Personnavn personnavn = new Personnavn();
        personnavn.setFornavn(personProperties.getNames().getRandomFirstname());
        personnavn.setEtternavn(personProperties.getNames().getRandomLastname());
        return personnavn;
    }

    private AdresseResource randomAdresse() {
        AdresseResource adresseResource = new AdresseResource();
        adresseResource.setAdresselinje(adresseProperties.getRandomAdresselinje());
        adresseResource.setPostnummer(adresseProperties.getRandomPostnummer());
        adresseResource.setPoststed(adresseProperties.getRandomPoststed());
        return adresseResource;
    }

}
