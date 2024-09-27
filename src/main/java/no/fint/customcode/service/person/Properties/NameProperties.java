package no.fint.customcode.service.person.Properties;

import java.util.List;
import java.util.Random;

public record NameProperties(
        List<String> firstname,
        List<String> lastname
) {
    public String getRandomFirstname() {
        Random random = new Random();
        return firstname.get(random.nextInt(firstname.size()));
    }

    public String getRandomLastname() {
        Random random = new Random();
        return lastname.get(random.nextInt(lastname.size()));
    }
}
