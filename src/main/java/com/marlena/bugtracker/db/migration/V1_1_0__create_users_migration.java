package com.marlena.bugtracker.db.migration;

import com.marlena.bugtracker.models.Person;
import com.marlena.bugtracker.services.PersonService;
import lombok.AllArgsConstructor;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class V1_1_0__create_users_migration extends BaseJavaMigration {

    @Autowired
    private final PersonService personService;
    @Override
    public void migrate(Context context) throws Exception {

        for (int i=1; i<=10; i++){
            Person user = new Person();
            user.setLogin("user"+i);
            user.setEmail("user"+i+"@email.com");
            user.setPassword("password");
            user.setUserRealName("User"+i+" Name");
            personService.saveUserDetails(user);
        }

    }
}