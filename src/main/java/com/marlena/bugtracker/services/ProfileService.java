package com.marlena.bugtracker.services;

import com.marlena.bugtracker.models.Person;
import com.marlena.bugtracker.models.ProfileData;
import com.marlena.bugtracker.models.UserPassword;
import com.marlena.bugtracker.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final PersonRepository personRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ProfileData getLoggedInPersonData(Person user){
        ProfileData profileData = new ProfileData();
        profileData.setUserRealName(user.getUserRealName());
        profileData.setEmail(user.getEmail());
        return profileData;
    }

    public void saveUserData(Person user, ProfileData profileData) {
        user.setUserRealName(profileData.getUserRealName());
        user.setEmail(profileData.getEmail());
        personRepository.save(user);
    }

    public void saveUserPassword(Person user, UserPassword userPassword){
        user.setPassword(passwordEncoder.encode(userPassword.getPassword()));
        personRepository.save(user);
    }
}
