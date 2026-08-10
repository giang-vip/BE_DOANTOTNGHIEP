package com.hungnhan.school_management.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MajorSubjectId implements Serializable {

    private Long majorId;
    private Long subjectId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MajorSubjectId that)) return false;
        return Objects.equals(majorId, that.majorId) && Objects.equals(subjectId, that.subjectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(majorId, subjectId);
    }
}
