package no.fint.customcode.PersonProperties;

import no.fint.model.felles.kompleksedatatyper.Personnavn;

import java.util.List;
import java.util.Random;

public record NameProperties(
        List<String> firstname,
        List<String> lastname
) {

    public Personnavn getRandomPersonnavn() {
        Personnavn personnavn = new Personnavn();
        personnavn.setFornavn(firstname.get(new Random().nextInt(firstname.size())));
        personnavn.setEtternavn(lastname.get(new Random().nextInt(lastname.size())));
        return personnavn;
    }

}
