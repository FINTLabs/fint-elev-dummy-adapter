package no.fint.customcode.service.person;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "person.name")
public record PersonProperties(
        List<String> names

) {
}