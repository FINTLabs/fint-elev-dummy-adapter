package no.fint.customcode.Properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Random;

@Data
@Configuration
@ConfigurationProperties("kontaktinformasjon")
public class KontaktInfoProperties {
    private List<String> epostadresse;
    private List<String> mobiltelefonnummer;

    public String getRandomEpostadresse() {
        Random random = new Random();
        return epostadresse.get(random.nextInt(epostadresse.size()));
    }

    public String getRandomMobiltelefonnummer() {
        Random random = new Random();
        return mobiltelefonnummer.get(random.nextInt(mobiltelefonnummer.size()));
    }

}
