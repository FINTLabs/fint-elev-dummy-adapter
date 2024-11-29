package no.fint.adapter.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.fint.adapter.sse.SseInitializer;
import no.fint.sse.FintSse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {

    private final SseInitializer sseInitializer;

    @GetMapping("/sse")
    public List<FintSse> getSseConnections() {
        return sseInitializer.getSseClients();
    }

    @DeleteMapping("/sse")
    public void destroySseConnections() {
        sseInitializer.cleanup();
    }

    @PostMapping("/sse")
    public void initSseConnections() {
        sseInitializer.init();
    }

}
