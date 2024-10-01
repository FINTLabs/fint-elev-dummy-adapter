package no.fint.customcode.PersonProperties;


import lombok.Data;
import no.fint.model.felles.kompleksedatatyper.Kontaktinformasjon;
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

    public Kontaktinformasjon getRandomKontaktinformasjon() {
        Random random = new Random();
        Kontaktinformasjon kontaktinformasjon = new Kontaktinformasjon();
        kontaktinformasjon.setMobiltelefonnummer(mobiltelefonnummer.get(random.nextInt(mobiltelefonnummer.size())));
        kontaktinformasjon.setEpostadresse(epostadresse.get(random.nextInt(epostadresse.size())));
        return kontaktinformasjon;
    }

}
