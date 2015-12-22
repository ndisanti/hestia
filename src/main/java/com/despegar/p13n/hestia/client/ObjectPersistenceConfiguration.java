package com.despegar.p13n.hestia.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.despegar.p13n.hbasecommons.hbase.tools.ObjectMapperPersistenceSerializer;
import com.despegar.p13n.hbasecommons.hbase.tools.api.ObjectPersistenceSerializer;

@Configuration
public class ObjectPersistenceConfiguration {

    @Bean
    public ObjectPersistenceSerializer getObejctPersistenceSerializer() {
        return new ObjectMapperPersistenceSerializer();
    }

}
