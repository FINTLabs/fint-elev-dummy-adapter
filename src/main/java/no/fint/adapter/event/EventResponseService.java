package no.fint.adapter.event;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import no.fint.adapter.FintAdapterEndpoints;
import no.fint.adapter.FintAdapterProps;
import no.fint.event.model.Event;
import no.fint.event.model.HeaderConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Handles responses back to the provider status endpoint.
 */
@Slf4j
@Service
public class EventResponseService {

    private FintAdapterEndpoints endpoints;
    private RestTemplate restTemplate;

    public EventResponseService(RestTemplate restTemplate, FintAdapterEndpoints endpoints) {
        this.restTemplate = restTemplate;
        this.endpoints = endpoints;
    }

    public void postResponse(String component, Event event) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HeaderConstants.ORG_ID, event.getOrgId());
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            String url = endpoints.getProviders().get(component) + endpoints.getResponse();
            log.info("{}: Posting response for {} ...", component, event.getAction());
            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(event, headers), Void.class);
            log.info("{}: Provider POST response: {}", component, response.getStatusCode());
        } catch (RestClientException e) {
            log.warn("{}: Provider POST response error: {}", component, e.getMessage());
        }
    }
}
