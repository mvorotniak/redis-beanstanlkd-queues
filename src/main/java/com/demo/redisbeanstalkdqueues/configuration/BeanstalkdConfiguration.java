package com.demo.redisbeanstalkdqueues.configuration;

import com.surftools.BeanstalkClient.Client;
import com.surftools.BeanstalkClientImpl.ClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanstalkdConfiguration {
    
    @Bean
    public Client beanstalkdClient() {
        return new ClientImpl();
    }
}
