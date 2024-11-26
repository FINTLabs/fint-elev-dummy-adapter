package no.fint.ElevDummy.PersonProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(value = "person")
public record PersonProperties(
        NameProperties names,
        List<String> birthdate
) {
}