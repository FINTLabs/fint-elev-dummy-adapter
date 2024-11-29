package no.fint.adapter.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.fint.ProviderClient;
import no.fint.adapter.FintAdapterEndpoints;
import no.fint.event.model.Event;
import no.fint.event.model.HeaderConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

/**
 * Handles responses back to the provider status endpoint.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EventResponseService {

    private final FintAdapterEndpoints endpoints;
    private final ProviderClient providerClient;

    public void postResponse(String component, Event event) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HeaderConstants.ORG_ID, event.getOrgId());
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            String url = endpoints.getProviders().get(component) + endpoints.getResponse();
            log.info("{}: Posting response for {} ...", component, event.getAction());
            ResponseEntity<Void> response = providerClient.postEvent(url, event, headers);
            log.info("{}: Provider POST response: {}", component, response.getStatusCode());
        } catch (RestClientException e) {
            log.warn("{}: Provider POST response error: {}", component, e.getMessage());
        }
    }
}
