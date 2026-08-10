 Giao diện Swagger UI: http://localhost:8080/swagger-ui/index.html
Tài liệu API dạng JSON: http://localhost:8080/v3/api-docs

cd "D:\DO AN TRUONG HOC THONG MINH\backend\school-management"

.\mvnw.cmd clean test "-Dtest=AuthControllerTest,AuthServiceImplTest"

.\mvnw.cmd clean test "-Dtest=UserServiceImplTest,UserControllerTest,RoleControllerTest,DepartmentServiceImplTest,DepartmentControllerTest,AcademicYearServiceImplTest,AcademicYearControllerTest,SemesterServiceImplTest,SemesterControllerTest,SchoolClassServiceImplTest,SchoolClassControllerTest"
