package no.fint.customcode.Properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties("kontaktinformasjon")
public class KontaktInfo {
    private List<String> epostadresse;
    private List<String> mobiltelefonnummer;
}
