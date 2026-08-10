package com.hungnhan.school_management.repository;

import com.hungnhan.school_management.entity.RegistrationPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrationPeriodRepository extends JpaRepository<RegistrationPeriod, Long> {
    Optional<RegistrationPeriod> findBySemesterId(Long semesterId);
    Optional<RegistrationPeriod> findBySemesterIsCurrentTrue();
}
