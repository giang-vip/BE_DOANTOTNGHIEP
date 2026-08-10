package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.constant.StudentStatus;
import com.hungnhan.school_management.dto.request.StudentRequest;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.dto.response.StudentResponse;
import com.hungnhan.school_management.entity.Major;
import com.hungnhan.school_management.entity.SchoolClass;
import com.hungnhan.school_management.entity.Student;
import com.hungnhan.school_management.entity.User;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.mapper.StudentMapper;
import com.hungnhan.school_management.repository.*;
import com.hungnhan.school_management.service.StudentService;
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
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final MajorRepository majorRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final TeacherRepository teacherRepository;
    private final StudentMapper studentMapper;

    @Override
    public StudentResponse createStudent(StudentRequest request) {
        if (studentRepository.existsByStudentCode(request.getStudentCode())) {
            throw new AppException(ErrorCode.STUDENT_EXISTED);
        }

        if (studentRepository.existsByUserId(request.getUserId()) || teacherRepository.existsByUserId(request.getUserId())) {
            throw new AppException(ErrorCode.USER_ALREADY_LINKED);
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Major major = majorRepository.findById(request.getMajorId())
                .orElseThrow(() -> new AppException(ErrorCode.MAJOR_NOT_FOUND));

        SchoolClass schoolClass = null;
        if (request.getClassId() != null) {
            schoolClass = schoolClassRepository.findById(request.getClassId())
                    .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));
        }

        Student student = studentMapper.toStudent(request);
        student.setUser(user);
        student.setMajor(major);
        student.setSchoolClass(schoolClass);

        if (request.getStatus() != null) {
            student.setStatus(StudentStatus.valueOf(request.getStatus()));
        }

        return studentMapper.toStudentResponse(studentRepository.save(student));
    }

    @Override
    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));

        if (!student.getStudentCode().equals(request.getStudentCode()) && studentRepository.existsByStudentCode(request.getStudentCode())) {
            throw new AppException(ErrorCode.STUDENT_EXISTED);
        }

        if (!student.getUser().getId().equals(request.getUserId())) {
            if (studentRepository.existsByUserId(request.getUserId()) || teacherRepository.existsByUserId(request.getUserId())) {
                throw new AppException(ErrorCode.USER_ALREADY_LINKED);
            }
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Major major = majorRepository.findById(request.getMajorId())
                .orElseThrow(() -> new AppException(ErrorCode.MAJOR_NOT_FOUND));

        SchoolClass schoolClass = null;
        if (request.getClassId() != null) {
            schoolClass = schoolClassRepository.findById(request.getClassId())
                    .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));
        }

        studentMapper.updateStudent(student, request);
        student.setUser(user);
        student.setMajor(major);
        student.setSchoolClass(schoolClass);

        if (request.getStatus() != null) {
            student.setStatus(StudentStatus.valueOf(request.getStatus()));
        }

        return studentMapper.toStudentResponse(studentRepository.save(student));
    }

    @Override
    public PageResponse<StudentResponse> getStudents(String search, Long departmentId, Long majorId, Long classId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Student> studentPage = studentRepository.searchStudents(search, departmentId, majorId, classId, pageable);

        List<StudentResponse> content = studentPage.getContent().stream()
                .map(studentMapper::toStudentResponse)
                .collect(Collectors.toList());

        return PageResponse.<StudentResponse>builder()
                .content(content)
                .pageNumber(studentPage.getNumber())
                .pageSize(studentPage.getSize())
                .totalElements(studentPage.getTotalElements())
                .totalPages(studentPage.getTotalPages())
                .last(studentPage.isLast())
                .build();
    }

    @Override
    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
        return studentMapper.toStudentResponse(student);
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
        studentRepository.delete(student);
    }
}
