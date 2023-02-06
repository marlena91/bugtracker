package com.marlena.bugtracker.services;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Bootstrap implements InitializingBean {

    @Value("${SPRING_CONFIG_LOCATION}")
    private String name;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(">> spring.config.additional-location: " + name);
    }
}
