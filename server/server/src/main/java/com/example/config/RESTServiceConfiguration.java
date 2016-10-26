/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Configuration
@EnableWebMvc

/**
 *
 * @author shannah
 */
public class RESTServiceConfiguration extends WebMvcConfigurerAdapter {

    private static String DB_URL;
    private static String DB_USERNAME;
    private static String DB_PASSWORD;
    private static String GCM_SERVER_API_KEY;
    private static String PUSH_TOKEN;
    private static String IOS_PUSH_CERT_URL;
    private static String IOS_PUSH_CERT_PASSWORD;
    
    /** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        
        
        registry.addInterceptor(new HandlerInterceptorAdapter() {

            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                System.out.println("About to handle request");
                response.getWriter().write("In handler");
                return super.preHandle(request, response, handler); //To change body of generated methods, choose Tools | Templates.
            }
            
        });
        
        
        
        registry.addWebRequestInterceptor(new WebRequestInterceptor() {

            public void preHandle(WebRequest wr) throws Exception {
                System.out.println("About to handle web request");
                
            }

            public void postHandle(WebRequest wr, ModelMap mm) throws Exception {
                
            }

            public void afterCompletion(WebRequest wr, Exception excptn) throws Exception {
                
            }
        });
        super.addInterceptors(registry); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        System.out.println("Configuring message converters");
        converters.add(serializedDTOObjectConverter());
        super.configureMessageConverters(converters); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        
        super.configureHandlerExceptionResolvers(exceptionResolvers); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        
        super.configureContentNegotiation(configurer); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        
        System.out.println("Extending converters");
        converters.clear();
        converters.add(serializedDTOObjectConverter());
        //System.out.println("Converters "+converters);
    }
     
    @Bean
    public CN1DataMapperMessageConverter serializedDTOObjectConverter() {
        return new CN1DataMapperMessageConverter();
    }
    
    @Bean
    public DataSource getDataSource() {
        if (DB_USERNAME == null || DB_PASSWORD == null || DB_URL == null) {
            loadRuntimeSettings();
        }
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(DB_URL);
        dataSource.setUsername(DB_USERNAME);
        dataSource.setPassword(DB_PASSWORD);
        
        return dataSource;
    }
    
    
    public static String getGcmApiKey() {
        if (GCM_SERVER_API_KEY == null) {
            System.out.println("Getting gcm api key "+GCM_SERVER_API_KEY);
            loadRuntimeSettings();
        }
        return GCM_SERVER_API_KEY;
    }
    
    public static String getIOSPushCertURL() {
        if (IOS_PUSH_CERT_URL == null) {
            System.out.println("Cert url not set.  Loading runtime settings");
            loadRuntimeSettings();
        }
        return IOS_PUSH_CERT_URL;
    }
    
    public static String getIOSPushCertPassword() {
        if (IOS_PUSH_CERT_PASSWORD == null) {
            System.out.println("Cert password not set.  Loading runtime settings");
            loadRuntimeSettings();
        }
        System.out.println("Push cert password is"+IOS_PUSH_CERT_PASSWORD);
        return IOS_PUSH_CERT_PASSWORD;
    }
    
    public static String getPushToken() {
        return PUSH_TOKEN;
    }
    
    private static void loadRuntimeSettings() {
        Properties p = new Properties();
        Map props = new HashMap();
        try {
            InputStream in = RESTServiceConfiguration.class.getResourceAsStream("/runtime.properties");
            if (in != null) {
                try {
                    p.load(in);
                    props.putAll(p);
                } finally {
                    if (in != null) {
                        try {in.close();} catch (Throwable t){}
                    }
                }
            }
            in = RESTServiceConfiguration.class.getResourceAsStream("/runtime.override.properties");
            if (in != null) {
                try {
                    p.load(in);
                    props.putAll(p);
                } finally {
                    if (in != null) {
                        try {in.close();} catch (Throwable t){}
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("In LoadRuntimeSettings");
        System.out.println(p);
        if (p.containsKey("DB_URL")) {
            DB_URL = (String)p.get("DB_URL");
            DB_PASSWORD = (String)p.get("DB_PASSWORD");
            DB_USERNAME = (String)p.get("DB_USERNAME");
        }
        if (p.containsKey("GCM_SERVER_API_KEY")) {
            GCM_SERVER_API_KEY = (String)p.get("GCM_SERVER_API_KEY");
        }
        if (p.containsKey("PUSH_TOKEN")) {
            PUSH_TOKEN = (String)p.get("PUSH_TOKEN");
        }
        if (p.containsKey("IOS_PUSH_CERT_URL")) {
            IOS_PUSH_CERT_URL = (String)p.getProperty("IOS_PUSH_CERT_URL");
        }
        if (p.containsKey("IOS_PUSH_CERT_PASSWORD")) {
            IOS_PUSH_CERT_PASSWORD = (String)p.getProperty("IOS_PUSH_CERT_PASSWORD");
        }
        
    }
}
