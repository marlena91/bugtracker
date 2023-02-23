package com.marlena.bugtracker.filters;

import com.marlena.bugtracker.models.Person;
import com.marlena.bugtracker.models.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectFilter {

    private String name;
    private Person creator;

    private String globalSearch;

    public Specification<Project> buildQuery() {
        return Specification.anyOf(
                ilike("name", globalSearch),
                ilike("description", globalSearch)
        ).and(
                Specification.allOf(
                        ilike("name", name),
                        equalTo("enabled", true),
                        equalTo("creator", creator)
                )
        );
    }

    private Specification<Project> equalTo(String property, Object value) {
        if (value == null) {
            return Specification.where(null);
        }

        return (root, query, builder) -> builder.equal(root.get(property), value);
    }

    private Specification<Project> ilike(String property, String value) {
        if (value == null) {
            return Specification.where(null);
        }
        return (root, query, builder) -> builder.like(builder.lower(root.get(property)), "%" + value.toLowerCase() + "%");
    }

    public String toQueryString(Integer page) {
        return "page=" + page +
                (name != null ? "&name=" + name : "") +
                (creator != null ? "&creator=" + creator.getId() : "") +
                (globalSearch != null ? "&globalSearch=" + globalSearch : "");
    }
}
