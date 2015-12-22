package com.despegar.p13n.hestia;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import com.despegar.p13n.hestia.config.SecurityConfig;

public class WebInitializer
    implements WebApplicationInitializer {

    private static final String DISPATCHER_NAME = "dispatcher";
    private static final Logger LOGGER = LoggerFactory.getLogger(WebInitializer.class);

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        WebApplicationContext rootContext = this.createRootContext(servletContext);
        this.configureSpringMvc(servletContext, rootContext);
        LOGGER.info("finish webinitializer");
    }

    private WebApplicationContext createRootContext(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(HestiaWebConfig.class, SecurityConfig.class);
        servletContext.addListener(new ContextLoaderListener(context));
        return context;
    }
 
    private void configureSpringMvc(ServletContext servletContext, WebApplicationContext rootContext) {
        DispatcherServlet dispatcher = new DispatcherServlet(rootContext);
        Dynamic servlet =  servletContext.addServlet(DISPATCHER_NAME, dispatcher);
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/*");
    }
}