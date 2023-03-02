package com.marlena.bugtracker.db.migration;

import com.marlena.bugtracker.models.Person;
import com.marlena.bugtracker.models.Project;
import com.marlena.bugtracker.services.PersonService;
import com.marlena.bugtracker.services.ProjectService;
import lombok.AllArgsConstructor;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@AllArgsConstructor
public class V2_1_0__create_projects_migration extends BaseJavaMigration {

    @Autowired
    private final ProjectService projectService;
    @Autowired
    private final PersonService personService;

    @Override
    public void migrate(Context context){

        for (int i=1; i<=100; i++){
            Project project = new Project();
            project.setDateCreated(new Date());
            project.setName("Projekt"+i);
            project.setDescription("Opis do projektu "+i);
            Person person = personService.getRandom();
            project.setCreator(person);
            projectService.save(project);
        }
    }
}
