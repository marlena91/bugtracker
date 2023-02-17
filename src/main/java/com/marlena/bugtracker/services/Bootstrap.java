package com.marlena.bugtracker.services;

import com.marlena.bugtracker.models.Authority;
import com.marlena.bugtracker.models.AuthorityName;
import com.marlena.bugtracker.repositories.AuthorityRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Bootstrap implements InitializingBean {

    @Value("${SPRING_CONFIG_LOCATION}")
    private String name;
    private final AuthorityRepository authorityRepository;

    public Bootstrap(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(">> spring.config.additional-location: " + name);

        for(AuthorityName name : AuthorityName.values()) {
            Optional<Authority> existingAuthority = authorityRepository.findByName(name);
            if(existingAuthority.isEmpty()) {
                Authority authority = new Authority();
                authority.setName(name);
                authorityRepository.save(authority);
            }
            System.out.println(name);
        }
    }
}
