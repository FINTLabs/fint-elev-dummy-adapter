package no.fint.customcode.service;

import no.fint.model.felles.kompleksedatatyper.Identifikator;

import java.util.UUID;

public class IdUtil {

    public static Identifikator createId(){
        Identifikator identifikator = new Identifikator();
        identifikator.setIdentifikatorverdi(UUID.randomUUID().toString());
        return identifikator;
    }
}
