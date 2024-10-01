package no.fint.ElevDummy.PersonProperties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties("person")
public class PersonProperties {
    private NameProperties names;
    private List<String> birthdate;
}