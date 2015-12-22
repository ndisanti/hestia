package com.despegar.p13n.hestia;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.despegar.library.rest.serializers.json.JodaDateTimeJsonDeserializer;
import com.despegar.library.rest.serializers.json.JodaDateTimeJsonSerializer;
import com.despegar.library.runtime.interceptor.RuntimeActivityHandlerInterceptor;
import com.despegar.p13n.hestia.newrelic.NewRelicHeaderAttributeInterceptor;
import com.despegar.p13n.hestia.newrelic.NewRelicNameTransactionInterceptor;
import com.despegar.p13n.hestia.rest.interceptors.XUOWInterceptor;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.collect.Sets;


@Configuration
@EnableWebMvc
@ImportResource("classpath:application-context.xml")
public class HestiaWebConfig
    extends WebMvcConfigurerAdapter {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // Jackson Http Converter
        MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(this.getModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        jacksonConverter.setObjectMapper(objectMapper);
        converters.add(jacksonConverter);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RuntimeActivityHandlerInterceptor());
        registry.addInterceptor(new NewRelicNameTransactionInterceptor());
        NewRelicHeaderAttributeInterceptor headerInterceptor = new NewRelicHeaderAttributeInterceptor();
        headerInterceptor.setHeaderKeys(Sets.newHashSet("X-UOW"));
        registry.addInterceptor(headerInterceptor);
        registry.addInterceptor(new XUOWInterceptor());
        super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/html/**").addResourceLocations("classpath:/static/html/");
        registry.addResourceHandler("/static/css/**").addResourceLocations("classpath:/static/css/",
            "classpath:/com/despegar/api/documentation/views/static/css/");
        registry.addResourceHandler("/static/js/**").addResourceLocations("classpath:/static/js/",
            "classpath:/com/despegar/api/documentation/views/static/js/");
        registry.addResourceHandler("/static/img/**")
            .addResourceLocations("classpath:/com/despegar/api/documentation/static/img/");
        registry.addResourceHandler("/static/fonts/**").addResourceLocations("classpath:/static/fonts/");
    }

    private SimpleModule getModule() {
        // Register custom serializers
        SimpleModule module = new SimpleModule("DefaultModule", new Version(0, 0, 1, null, "com.despegar.p13n", "xselling"));

        DateTimeFormatter datetimeFormat = ISODateTimeFormat.dateTimeNoMillis();

        // Joda DateTime
        module.addDeserializer(DateTime.class, new JodaDateTimeJsonDeserializer(datetimeFormat));
        module.addSerializer(DateTime.class, new JodaDateTimeJsonSerializer(datetimeFormat));
        return module;
    }

}
