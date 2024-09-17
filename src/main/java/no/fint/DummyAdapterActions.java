//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package no.fint;

import java.util.Arrays;
import java.util.List;

public enum DummyAdapterActions {
    GET_ALL_ELEV,
    GET_ALL_PERSON;

    private DummyAdapterActions() {
    }

    public static List<String> getActions() {
        return Arrays.asList(Arrays.stream(DummyAdapterActions.class.getEnumConstants()).map(Enum::name).toArray((x$0) -> {
            return new String[x$0];
        }));
    }
}
