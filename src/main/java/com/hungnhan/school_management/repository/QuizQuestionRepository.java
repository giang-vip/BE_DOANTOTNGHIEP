package com.hungnhan.school_management.repository;

import com.hungnhan.school_management.entity.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {
    List<QuizQuestion> findByAssignmentIdOrderByOrderIndexAsc(Long assignmentId);
}
