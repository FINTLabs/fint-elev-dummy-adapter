package no.fint.ElevDummy.PersonProperties;

import lombok.Data;
import no.fint.model.resource.felles.kompleksedatatyper.AdresseResource;
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


    public AdresseResource getRandomAdresse() {
        Random random = new Random();
        AdresseResource adresseResource = new AdresseResource();
        adresseResource.setAdresselinje(List.of(adresselinje.get(random.nextInt(adresselinje.size()))));
        adresseResource.setPostnummer(postnummer.get(random.nextInt(postnummer.size())));
        adresseResource.setPoststed(poststed.get(random.nextInt(poststed.size())));
        return adresseResource;
    }
}
