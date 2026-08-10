package com.hungnhan.school_management.mapper;

import com.hungnhan.school_management.dto.request.AssignmentRequest;
import com.hungnhan.school_management.dto.request.QuizQuestionRequest;
import com.hungnhan.school_management.dto.response.AssignmentResponse;
import com.hungnhan.school_management.dto.response.QuizQuestionResponse;
import com.hungnhan.school_management.dto.response.SubmissionResponse;
import com.hungnhan.school_management.entity.Assignment;
import com.hungnhan.school_management.entity.QuizQuestion;
import com.hungnhan.school_management.entity.Submission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AssignmentMapper {

    @Mapping(target = "classSection", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    Assignment toAssignment(AssignmentRequest request);

    @Mapping(source = "classSection.id", target = "classSectionId")
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.username", target = "createdByUsername")
    AssignmentResponse toAssignmentResponse(Assignment assignment);

    @Mapping(target = "classSection", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateAssignment(@MappingTarget Assignment assignment, AssignmentRequest request);

    @Mapping(target = "assignment", ignore = true)
    QuizQuestion toQuizQuestion(QuizQuestionRequest request);

    @Mapping(source = "assignment.id", target = "assignmentId")
    QuizQuestionResponse toQuizQuestionResponse(QuizQuestion question);

    @Mapping(source = "assignment.id", target = "assignmentId")
    @Mapping(source = "enrollment.id", target = "enrollmentId")
    @Mapping(source = "enrollment.student.studentCode", target = "studentCode")
    @Mapping(source = "enrollment.student.fullName", target = "studentName")
    SubmissionResponse toSubmissionResponse(Submission submission);
}
