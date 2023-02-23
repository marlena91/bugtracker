package com.marlena.bugtracker.audit;

import java.util.List;
import java.util.stream.Collectors;

public class AuditDataMapper {

    public static List<AuditDataDTO> map(List<Object[]> rawRevisions) {
        return rawRevisions.stream()
                .map(r -> new AuditDataDTO((Object[]) r))
                .collect(Collectors.toList());
    }
}
