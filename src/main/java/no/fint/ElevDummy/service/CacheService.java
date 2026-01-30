package no.fint.ElevDummy.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.FintResource;
import no.fint.model.resource.Link;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.utdanning.elev.ElevResource;
import no.fintlabs.dynamiskadapter.DynamicAdapterService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Service
public class CacheService {

    public final DynamicAdapterService dynamicAdapterService;

    @Getter
    List<FintResource> elever = new ArrayList<>();

    @Getter
    List<FintResource> personer = new ArrayList<>();


    public CacheService(DynamicAdapterService dynamicAdapterService) {
        this.dynamicAdapterService = dynamicAdapterService;
    }

    @PostConstruct
    public void populateCache() {
        List<ElevResource> elevResources = dynamicAdapterService.create(ElevResource.class, 100).stream().map(ElevResource.class::cast).toList();
        List<PersonResource> personResources = dynamicAdapterService.create(PersonResource.class, 100).stream().map(PersonResource.class::cast).toList();

        IntStream.range(0, elevResources.size()).forEach(i -> {
            log.info("Adding person {} to elev {}", personResources.get(i).getFodselsnummer().getIdentifikatorverdi(), elevResources.get(i).getElevnummer());
            ElevResource elev = elevResources.get(i);
            elev.addPerson(Link.with(PersonResource.class, "fodselsnummer", personResources.get(i).getFodselsnummer().getIdentifikatorverdi()));
        });
    }
}
