package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.dto.request.LearningMaterialRequest;
import com.hungnhan.school_management.dto.response.LearningMaterialResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.entity.ClassSection;
import com.hungnhan.school_management.entity.LearningMaterial;
import com.hungnhan.school_management.entity.Teacher;
import com.hungnhan.school_management.entity.User;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.mapper.LearningMaterialMapper;
import com.hungnhan.school_management.repository.ClassSectionRepository;
import com.hungnhan.school_management.repository.LearningMaterialRepository;
import com.hungnhan.school_management.repository.TeacherRepository;
import com.hungnhan.school_management.repository.UserRepository;
import com.hungnhan.school_management.service.TeacherMaterialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TeacherMaterialServiceImpl implements TeacherMaterialService {

    private final LearningMaterialRepository learningMaterialRepository;
    private final ClassSectionRepository classSectionRepository;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final LearningMaterialMapper learningMaterialMapper;

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private void checkTeacherPermission(User user, ClassSection classSection) {
        Teacher teacher = teacherRepository.findByUserId(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));

        if (!classSection.getTeacher().getId().equals(teacher.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }

    @Override
    public PageResponse<LearningMaterialResponse> getMaterials(String username, Long classSectionId, int page, int size) {
        User user = getUserByUsername(username);
        ClassSection classSection = classSectionRepository.findById(classSectionId)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_SECTION_NOT_FOUND));

        checkTeacherPermission(user, classSection);

        Pageable pageable = PageRequest.of(page, size);
        Page<LearningMaterial> materialPage = learningMaterialRepository.findByClassSectionIdOrderByUploadedAtDesc(classSectionId, pageable);

        List<LearningMaterialResponse> content = materialPage.getContent().stream()
                .map(learningMaterialMapper::toLearningMaterialResponse)
                .collect(Collectors.toList());

        return PageResponse.<LearningMaterialResponse>builder()
                .content(content)
                .pageNumber(materialPage.getNumber())
                .pageSize(materialPage.getSize())
                .totalElements(materialPage.getTotalElements())
                .totalPages(materialPage.getTotalPages())
                .last(materialPage.isLast())
                .build();
    }

    @Override
    public LearningMaterialResponse uploadMaterial(String username, Long classSectionId, LearningMaterialRequest request) {
        User user = getUserByUsername(username);
        ClassSection classSection = classSectionRepository.findById(classSectionId)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_SECTION_NOT_FOUND));

        checkTeacherPermission(user, classSection);

        LearningMaterial material = learningMaterialMapper.toLearningMaterial(request);
        material.setClassSection(classSection);
        material.setUploadedBy(user);

        return learningMaterialMapper.toLearningMaterialResponse(learningMaterialRepository.save(material));
    }

    @Override
    public void deleteMaterial(String username, Long id) {
        User user = getUserByUsername(username);
        LearningMaterial material = learningMaterialRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MATERIAL_NOT_FOUND));

        checkTeacherPermission(user, material.getClassSection());

        learningMaterialRepository.delete(material);
    }
}
