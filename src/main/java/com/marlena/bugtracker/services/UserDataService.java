package com.marlena.bugtracker.services;

import com.marlena.bugtracker.models.Person;
import com.marlena.bugtracker.models.UserData;
import org.springframework.stereotype.Service;

@Service
public class UserDataService {

    public UserData getUserDataForEditUser(Person user){
        UserData userData = new UserData();
        userData.setUserRealName(user.getUserRealName());
        userData.setLogin(user.getLogin());
        userData.setEmail(user.getEmail());
        userData.setUserId(user.getId());
        return userData;
    }
}
