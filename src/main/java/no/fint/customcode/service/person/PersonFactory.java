package no.fint.customcode.service.person;

import lombok.extern.slf4j.Slf4j;
import no.fint.customcode.service.IdUtil;
import no.fint.model.felles.kompleksedatatyper.Personnavn;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.felles.kompleksedatatyper.AdresseResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
public class PersonFactory {

    private PersonProperties personProperties;

    @Value("${person.name.lastName}")
    private List<String> lastNames;

    @Value("${person.adress.adresselinje}")
    private List<String> adresseLinje;

    @Value("${person.adress.postnummer}")
    private List<String> postNummer;

    @Value("${person.adress.poststed}")
    private List<String> postSted;


    Random random = new Random();

    public PersonFactory(PersonProperties personProperties) {
        this.personProperties = personProperties;
        log.info(personProperties.toString());
    }

    public PersonResource createPerson() {
        PersonResource personResource = new PersonResource();
        personResource.setNavn(randomPersonnavn(random));
        personResource.setBostedsadresse(randomAdresse(random));
        personResource.setFodselsnummer(IdUtil.createId());
        personResource.setFodselsdato(randomFodselsDato(random));
        return personResource;
    }

    private Date randomFodselsDato(Random random) {
        Date fodselsdato = new Date();
        fodselsdato.setYear(2003);
        fodselsdato.setMonth(9);
        fodselsdato.setDate(26);
        return fodselsdato;
    }

    private Personnavn randomPersonnavn(Random random) {
//        Personnavn personnavn = new Personnavn();
//        personnavn.setFornavn(firstNames.get(random.nextInt(firstNames.size())));
//        personnavn.setFornavn(lastNames.get(random.nextInt(lastNames.size())));
//        return personnavn;
        return null;
    }

    private AdresseResource randomAdresse(Random random) {
        AdresseResource adresseResource = new AdresseResource();
        adresseResource.setAdresselinje(List.of(adresseLinje.get(random.nextInt(adresseLinje.size()))));
        adresseResource.setPostnummer(postNummer.get(random.nextInt(postNummer.size())));
        adresseResource.setPoststed(postSted.get(random.nextInt(postSted.size())));
        return adresseResource;
    }

}
