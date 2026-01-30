package no.fint;

import no.fintlabs.dynamiskadapter.DynamicAdapterService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamicAdapterConfig {

    @Bean
    public DynamicAdapterService dynamicAdapterService() {
        return new DynamicAdapterService();
    }
}
