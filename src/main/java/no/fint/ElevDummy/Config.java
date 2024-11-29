package no.fint.ElevDummy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class Config {

    private final ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        objectMapper.setDateFormat(new ISO8601DateFormat());
    }

    @Bean
    @Qualifier("responseHandler")
    public Executor responseHander(@Value("${fint.adapter.response.workers:1}") int workers) {
        if (workers != 1) {
            log.warn("Visma Web Service does not support {} workers!", workers);
        }
        return Executors.newFixedThreadPool(workers);
    }

}
