package com.hungnhan.school_management.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi hệ thống không xác định", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1001, "Tên đăng nhập đã tồn tại", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1002, "Email đã tồn tại", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1003, "Không tìm thấy người dùng", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(1004, "Quyền (Role) không tồn tại", HttpStatus.NOT_FOUND),
    INVALID_KEY(1005, "Mã lỗi không hợp lệ", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1006, "Mật khẩu phải có tối thiểu 6 ký tự", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1007, "Tên đăng nhập phải có ít nhất 3 ký tự", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1008, "Sai tài khoản hoặc mật khẩu, hoặc thiếu Token", HttpStatus.UNAUTHORIZED),
    OLD_PASSWORD_INCORRECT(1009, "Mật khẩu cũ không chính xác", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(1010, "Không có quyền truy cập", HttpStatus.FORBIDDEN),
    DEPARTMENT_EXISTED(1012, "Mã khoa đã tồn tại", HttpStatus.BAD_REQUEST),
    DEPARTMENT_NOT_FOUND(1013, "Không tìm thấy khoa", HttpStatus.NOT_FOUND),
    DEPARTMENT_HAS_REFERENCES(1014, "Không thể xóa khoa vì đang có dữ liệu liên kết", HttpStatus.BAD_REQUEST),
    ACADEMIC_YEAR_EXISTED(1015, "Mã năm học đã tồn tại", HttpStatus.BAD_REQUEST),
    ACADEMIC_YEAR_NOT_FOUND(1016, "Không tìm thấy năm học", HttpStatus.NOT_FOUND),
    ACADEMIC_YEAR_INVALID_DATES(1017, "Ngày bắt đầu phải nhỏ hơn ngày kết thúc", HttpStatus.BAD_REQUEST),
    ACADEMIC_YEAR_HAS_REFERENCES(1018, "Không thể xóa năm học vì đang có dữ liệu liên kết", HttpStatus.BAD_REQUEST),
    SEMESTER_EXISTED(1019, "Mã học kỳ đã tồn tại trong năm học này", HttpStatus.BAD_REQUEST),
    SEMESTER_NOT_FOUND(1020, "Không tìm thấy học kỳ", HttpStatus.NOT_FOUND),
    SEMESTER_INVALID_DATES(1021, "Ngày bắt đầu phải nhỏ hơn ngày kết thúc", HttpStatus.BAD_REQUEST),
    SEMESTER_HAS_REFERENCES(1022, "Không thể xóa học kỳ vì đang có dữ liệu liên kết", HttpStatus.BAD_REQUEST),
    STUDENT_EXISTED(1023, "Mã sinh viên đã tồn tại", HttpStatus.BAD_REQUEST),
    STUDENT_NOT_FOUND(1024, "Không tìm thấy sinh viên", HttpStatus.NOT_FOUND),
    TEACHER_EXISTED(1025, "Mã giảng viên đã tồn tại", HttpStatus.BAD_REQUEST),
    TEACHER_NOT_FOUND(1026, "Không tìm thấy giảng viên", HttpStatus.NOT_FOUND),
    MAJOR_NOT_FOUND(1027, "Không tìm thấy ngành học", HttpStatus.NOT_FOUND),
    CLASS_NOT_FOUND(1028, "Không tìm thấy lớp hành chính", HttpStatus.NOT_FOUND),
    CLASS_EXISTED(1029, "Mã lớp hành chính đã tồn tại", HttpStatus.BAD_REQUEST),
    SUBJECT_EXISTED(1030, "Mã môn học đã tồn tại", HttpStatus.BAD_REQUEST),
    SUBJECT_NOT_FOUND(1031, "Không tìm thấy môn học", HttpStatus.NOT_FOUND),
    SUBJECT_INVALID_CREDITS(1032, "Số tín chỉ phải lớn hơn hoặc bằng 1", HttpStatus.BAD_REQUEST),
    USER_ALREADY_LINKED(1033, "Tài khoản người dùng này đã được liên kết với hồ sơ khác", HttpStatus.BAD_REQUEST),
    CLASS_SECTION_EXISTED(1034, "Mã lớp học phần đã tồn tại", HttpStatus.BAD_REQUEST),
    CLASS_SECTION_NOT_FOUND(1035, "Không tìm thấy lớp học phần", HttpStatus.NOT_FOUND),
    ROOM_SCHEDULE_CONFLICT(1036, "Trùng lịch phòng học", HttpStatus.BAD_REQUEST),
    TEACHER_SCHEDULE_CONFLICT(1037, "Giảng viên bị trùng lịch dạy", HttpStatus.BAD_REQUEST),
    CLASS_SECTION_FULL(1038, "Lớp học phần đã đủ số lượng sinh viên", HttpStatus.BAD_REQUEST),
    ENROLLMENT_EXISTED(1039, "Sinh viên đã đăng ký lớp học phần này", HttpStatus.BAD_REQUEST),
    ENROLLMENT_NOT_FOUND(1040, "Không tìm thấy thông tin đăng ký", HttpStatus.NOT_FOUND),
    INVALID_TIME(1041, "Thời gian bắt đầu phải trước thời gian kết thúc", HttpStatus.BAD_REQUEST),
    INVALID_DATE(1042, "Ngày bắt đầu phải trước ngày kết thúc", HttpStatus.BAD_REQUEST),
    SESSION_EXISTED(1043, "Phiên điểm danh cho ngày này đã tồn tại", HttpStatus.BAD_REQUEST),
    SESSION_NOT_FOUND(1044, "Không tìm thấy phiên điểm danh", HttpStatus.NOT_FOUND),
    RECORD_NOT_FOUND(1045, "Không tìm thấy bản ghi điểm danh", HttpStatus.NOT_FOUND),
    MATERIAL_NOT_FOUND(1046, "Không tìm thấy học liệu", HttpStatus.NOT_FOUND),
    ANNOUNCEMENT_NOT_FOUND(1047, "Không tìm thấy thông báo", HttpStatus.NOT_FOUND),
    ASSIGNMENT_NOT_FOUND(1048, "Không tìm thấy bài tập", HttpStatus.NOT_FOUND),
    SUBMISSION_NOT_FOUND(1049, "Không tìm thấy bài nộp", HttpStatus.NOT_FOUND),
    QUIZ_QUESTION_NOT_FOUND(1050, "Không tìm thấy câu hỏi trắc nghiệm", HttpStatus.NOT_FOUND),
    INVALID_WEIGHT_SUM(1051, "Tổng trọng số phải bằng 100", HttpStatus.BAD_REQUEST),
    CANNOT_DELETE_ADMIN(1052, "Không thể tự xóa admin", HttpStatus.BAD_REQUEST),
    INVALID_INPUT(1053, "Dữ liệu đầu vào không hợp lệ", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
