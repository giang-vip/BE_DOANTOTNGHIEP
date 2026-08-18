package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.constant.SubjectType;
import com.hungnhan.school_management.dto.request.MajorSubjectRequest;
import com.hungnhan.school_management.dto.response.SubjectResponse;
import com.hungnhan.school_management.entity.Major;
import com.hungnhan.school_management.entity.MajorSubject;
import com.hungnhan.school_management.entity.MajorSubjectId;
import com.hungnhan.school_management.entity.Subject;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.mapper.SubjectMapper;
import com.hungnhan.school_management.repository.MajorRepository;
import com.hungnhan.school_management.repository.MajorSubjectRepository;
import com.hungnhan.school_management.repository.SubjectRepository;
import com.hungnhan.school_management.service.MajorSubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MajorSubjectServiceImpl implements MajorSubjectService {

    private final MajorSubjectRepository majorSubjectRepository;
    private final MajorRepository majorRepository;
    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;

    @Override
    public SubjectResponse addSubjectToMajor(Long majorId, MajorSubjectRequest request) {
        Major major = majorRepository.findById(majorId)
                .orElseThrow(() -> new AppException(ErrorCode.MAJOR_NOT_FOUND));

        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new AppException(ErrorCode.SUBJECT_NOT_FOUND));

        MajorSubjectId msId = new MajorSubjectId(majorId, subject.getId());
        
        if (majorSubjectRepository.existsById(msId)) {
            throw new AppException(ErrorCode.SUBJECT_EXISTED); // Already in major
        }

        MajorSubject ms = new MajorSubject();
        ms.setId(msId);
        ms.setMajor(major);
        ms.setSubject(subject);
        
        SubjectType type = SubjectType.COMPULSORY;
        if (request.getType() != null) {
            try {
                type = SubjectType.valueOf(request.getType());
            } catch (Exception e) {
                // Default to COMPULSORY
            }
        }
        ms.setSubjectType(type);
        ms.setRecommendedSemester(request.getSemesterIndex());
        
        majorSubjectRepository.save(ms);

        SubjectResponse response = subjectMapper.toSubjectResponse(subject);
        response.setMajorId(majorId);
        response.setMajorName(major.getName());
        response.setType(ms.getSubjectType().name());
        response.setSemesterIndex(ms.getRecommendedSemester());
        return response;
    }

    @Override
    public SubjectResponse updateSubjectInMajor(Long majorId, Long subjectId, MajorSubjectRequest request) {
        MajorSubject ms = majorSubjectRepository.findByMajorIdAndSubjectId(majorId, subjectId)
                .orElseThrow(() -> new AppException(ErrorCode.SUBJECT_NOT_FOUND)); // Or custom error code for MajorSubject not found

        SubjectType type = ms.getSubjectType();
        if (request.getType() != null) {
            try {
                type = SubjectType.valueOf(request.getType());
            } catch (Exception e) {}
        }
        ms.setSubjectType(type);
        
        if (request.getSemesterIndex() != null) {
            ms.setRecommendedSemester(request.getSemesterIndex());
        }

        majorSubjectRepository.save(ms);

        SubjectResponse response = subjectMapper.toSubjectResponse(ms.getSubject());
        response.setMajorId(majorId);
        response.setMajorName(ms.getMajor().getName());
        response.setType(ms.getSubjectType().name());
        response.setSemesterIndex(ms.getRecommendedSemester());
        return response;
    }

    @Override
    public void removeSubjectFromMajor(Long majorId, Long subjectId) {
        if (!majorSubjectRepository.findByMajorIdAndSubjectId(majorId, subjectId).isPresent()) {
            throw new AppException(ErrorCode.SUBJECT_NOT_FOUND);
        }
        majorSubjectRepository.deleteByMajorIdAndSubjectId(majorId, subjectId);
    }
}
