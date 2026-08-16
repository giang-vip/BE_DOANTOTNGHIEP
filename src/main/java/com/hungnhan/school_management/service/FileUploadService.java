package com.hungnhan.school_management.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * Service tải file lên Cloudinary (đám mây).
 * Nếu Cloudinary chưa được cấu hình, trả về thông báo lỗi rõ ràng.
 */
@Service
public class FileUploadService {
    
    @Autowired(required = false)
    private Cloudinary cloudinary;

    public String uploadFile(MultipartFile file) throws IOException {
        if (cloudinary == null) {
            throw new IOException("Cloudinary chưa được cấu hình. Vui lòng kiểm tra file .env");
        }
        // resource_type "auto" để tự nhận diện định dạng (PDF, Image, Video...)
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
            "resource_type", "auto"
        ));
        return uploadResult.get("secure_url").toString();
    }

    /**
     * Hàm gọi API Cloudinary để xóa file
     */
    public void deleteFileFromCloudinary(String fileUrl) {
        if (cloudinary == null || fileUrl == null || fileUrl.isEmpty()) {
            return;
        }
        try {
            String publicId = extractPublicId(fileUrl);
            if (publicId != null && !publicId.isEmpty()) {
                String resourceType = "image";
                String lowercaseUrl = fileUrl.toLowerCase();
                if (lowercaseUrl.endsWith(".pdf") || lowercaseUrl.endsWith(".zip") || 
                    lowercaseUrl.endsWith(".rar") || lowercaseUrl.endsWith(".docx") || 
                    lowercaseUrl.endsWith(".xlsx") || lowercaseUrl.endsWith(".txt")) {
                    resourceType = "raw";
                } else if (lowercaseUrl.endsWith(".mp4") || lowercaseUrl.endsWith(".avi") || 
                           lowercaseUrl.endsWith(".mov") || lowercaseUrl.endsWith(".mkv")) {
                    resourceType = "video";
                }

                Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.asMap(
                    "resource_type", resourceType
                ));
                System.out.println("Đã xóa file trên Cloud (" + resourceType + "): " + result.toString());
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa file trên Cloudinary: " + e.getMessage());
        }
    }

    /**
     * Hàm tiện ích để tách public_id từ đường link URL Cloudinary
     */
    private String extractPublicId(String url) {
        if (url == null || !url.contains("res.cloudinary.com")) {
            return null;
        }
        try {
            String[] parts = url.split("/upload/");
            if (parts.length > 1) {
                String path = parts[1];
                if (path.matches("^v\\d+/.*")) {
                    path = path.replaceFirst("^v\\d+/", "");
                }
                int lastDotIndex = path.lastIndexOf('.');
                if (lastDotIndex != -1) {
                    path = path.substring(0, lastDotIndex);
                }
                return path;
            }
        } catch (Exception e) {
            System.err.println("Không thể trích xuất public_id từ URL: " + url);
        }
        return null;
    }
}
