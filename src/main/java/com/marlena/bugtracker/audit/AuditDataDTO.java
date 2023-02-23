package com.marlena.bugtracker.audit;


import com.marlena.bugtracker.models.Issue;
import com.marlena.bugtracker.models.Status;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.RevisionType;

import java.util.Date;

@Getter
@Setter
public class AuditDataDTO {

    Date date;
    String actor;

    RevisionType revisionType;

    String name;
    Status status;

    public AuditDataDTO(Object[] revision) {
        AuditedRevisionEntity revisionEntity = ((AuditedRevisionEntity) revision[1]);

        this.date = new Date(revisionEntity.getTimestamp());
        this.actor = revisionEntity.getActor();

        this.revisionType = (RevisionType) revision[2];

        Issue issue = (Issue) revision[0];
        this.name = issue.getName();
        this.status = issue.getStatus();
    }

}
