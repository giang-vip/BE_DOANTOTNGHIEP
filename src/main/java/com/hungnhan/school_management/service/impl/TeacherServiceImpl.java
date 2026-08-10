package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.constant.TeacherStatus;
import com.hungnhan.school_management.dto.request.TeacherRequest;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.dto.response.TeacherResponse;
import com.hungnhan.school_management.entity.Department;
import com.hungnhan.school_management.entity.Teacher;
import com.hungnhan.school_management.entity.User;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.mapper.TeacherMapper;
import com.hungnhan.school_management.repository.DepartmentRepository;
import com.hungnhan.school_management.repository.StudentRepository;
import com.hungnhan.school_management.repository.TeacherRepository;
import com.hungnhan.school_management.repository.UserRepository;
import com.hungnhan.school_management.service.TeacherService;
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
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final TeacherMapper teacherMapper;

    @Override
    public TeacherResponse createTeacher(TeacherRequest request) {
        if (teacherRepository.existsByTeacherCode(request.getTeacherCode())) {
            throw new AppException(ErrorCode.TEACHER_EXISTED);
        }

        if (teacherRepository.existsByUserId(request.getUserId()) || studentRepository.existsByUserId(request.getUserId())) {
            throw new AppException(ErrorCode.USER_ALREADY_LINKED);
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Department department = null;
        if (request.getDepartmentId() != null) {
            department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
        }

        Teacher teacher = teacherMapper.toTeacher(request);
        teacher.setUser(user);
        teacher.setDepartment(department);

        if (request.getStatus() != null) {
            teacher.setStatus(TeacherStatus.valueOf(request.getStatus()));
        }

        return teacherMapper.toTeacherResponse(teacherRepository.save(teacher));
    }

    @Override
    public TeacherResponse updateTeacher(Long id, TeacherRequest request) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));

        if (!teacher.getTeacherCode().equals(request.getTeacherCode()) && teacherRepository.existsByTeacherCode(request.getTeacherCode())) {
            throw new AppException(ErrorCode.TEACHER_EXISTED);
        }

        if (!teacher.getUser().getId().equals(request.getUserId())) {
            if (teacherRepository.existsByUserId(request.getUserId()) || studentRepository.existsByUserId(request.getUserId())) {
                throw new AppException(ErrorCode.USER_ALREADY_LINKED);
            }
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Department department = null;
        if (request.getDepartmentId() != null) {
            department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
        }

        teacherMapper.updateTeacher(teacher, request);
        teacher.setUser(user);
        teacher.setDepartment(department);

        if (request.getStatus() != null) {
            teacher.setStatus(TeacherStatus.valueOf(request.getStatus()));
        }

        return teacherMapper.toTeacherResponse(teacherRepository.save(teacher));
    }

    @Override
    public PageResponse<TeacherResponse> getTeachers(String search, Long departmentId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Teacher> teacherPage = teacherRepository.searchTeachers(search, departmentId, pageable);

        List<TeacherResponse> content = teacherPage.getContent().stream()
                .map(teacherMapper::toTeacherResponse)
                .collect(Collectors.toList());

        return PageResponse.<TeacherResponse>builder()
                .content(content)
                .pageNumber(teacherPage.getNumber())
                .pageSize(teacherPage.getSize())
                .totalElements(teacherPage.getTotalElements())
                .totalPages(teacherPage.getTotalPages())
                .last(teacherPage.isLast())
                .build();
    }

    @Override
    public TeacherResponse getTeacherById(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));
        return teacherMapper.toTeacherResponse(teacher);
    }

    @Override
    public void deleteTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));
        teacherRepository.delete(teacher);
    }
}
