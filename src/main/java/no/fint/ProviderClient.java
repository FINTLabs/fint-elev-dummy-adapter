package no.fint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.fint.event.model.Event;
import no.fint.oauth.TokenService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProviderClient {

    private final TokenService tokenService;
    private final RestClient restClient = RestClient.builder().build();

    public ResponseEntity<Void> postEvent(String url, Event event, HttpHeaders headers) {
        attachAuthorizationHeader(headers);
        return restClient.post()
                .uri(url)
                .headers(header -> header.addAll(headers))
                .body(event)
                .retrieve()
                .toBodilessEntity();
    }

    private void attachAuthorizationHeader(HttpHeaders headers) {
        headers.add(HttpHeaders.AUTHORIZATION, tokenService.getBearerToken());
    }

}
