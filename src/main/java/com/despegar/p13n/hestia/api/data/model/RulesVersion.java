package com.despegar.p13n.hestia.api.data.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum RulesVersion {

	
	 // all rules but not closed packages id and combined products
    DYNAMIC_SERVICE("dynamicService"),
    MONO_DESTINATION("monoDestination"), // multi producto, mono destino por fila
    MULTI_DESTINATION("multiDestination"), // multi producto, multi destino por fila 
    DYNAMIC_PRODUCT("dynamicProduct"); 

    private static Map<String, RulesVersion> dictFromString = new HashMap<String, RulesVersion>();
    static {
        for (RulesVersion type : RulesVersion.values()) {
            for (String flowCode : type.values) {
                dictFromString.put(flowCode, type);
            }
            dictFromString.put(type.name(), type);// also works as valueOf(String name)
        }
    }

    public static EnumSet<RulesVersion> ENABLED = EnumSet.of(//
        MULTI_DESTINATION,//
        DYNAMIC_SERVICE, //
        MONO_DESTINATION);

    private final List<String> values;

    private RulesVersion(String... values) {
        this.values = Arrays.asList(values);
    }

    public List<String> getValues() {
        return this.values;
    }

    public static Collection<String> getAllNames() {
        return dictFromString.keySet();
    }

    public static RulesVersion fromString(String code) {
        return dictFromString.get(code);
    }

    public static RulesVersion getDefault() {
        return RulesVersion.DYNAMIC_SERVICE;
    }

}
