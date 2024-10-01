package no.fint.ElevDummy.ElevProperties;


import lombok.Data;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Random;

@Data
@Configuration
@ConfigurationProperties("elev")
public class ElevProperties {
    private List<String> brukerNavn;
    private List<String> feideNavn;

    public Identifikator getRandomBrukerNavn() {
        Random random = new Random();
        Identifikator identifikator = new Identifikator();
        identifikator.setIdentifikatorverdi(brukerNavn.get(random.nextInt(brukerNavn.size())));
        return identifikator;
    }

    public Identifikator getRandomFeideNavn() {
        Random random = new Random();
        Identifikator identifikator = new Identifikator();
        identifikator.setIdentifikatorverdi(feideNavn.get(random.nextInt(feideNavn.size())));
        return identifikator;
    }

}
