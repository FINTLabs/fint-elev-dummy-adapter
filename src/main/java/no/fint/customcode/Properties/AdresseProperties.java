package no.fint.customcode.Properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Random;


@Data
@Configuration
@ConfigurationProperties("adress")
public class AdresseProperties {
    private List<String> adresselinje;
    private List<String> postnummer;
    private List<String> poststed;

    public List<String> getRandomAdresselinje() {
        Random random = new Random();
        return List.of(adresselinje.get(random.nextInt(adresselinje.size())));
    }

    public String getRandomPostnummer() {
        Random random = new Random();
        return postnummer.get(random.nextInt(postnummer.size()));
    }

    public String getRandomPoststed() {
        Random random = new Random();
        return poststed.get(random.nextInt(poststed.size()));
    }

}
