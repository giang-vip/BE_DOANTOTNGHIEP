package com.hungnhan.school_management.service.impl;

import com.hungnhan.school_management.dto.request.DocumentRequest;
import com.hungnhan.school_management.dto.response.DocumentResponse;
import com.hungnhan.school_management.dto.response.PageResponse;
import com.hungnhan.school_management.entity.Document;
import com.hungnhan.school_management.entity.User;
import com.hungnhan.school_management.exception.AppException;
import com.hungnhan.school_management.exception.ErrorCode;
import com.hungnhan.school_management.mapper.DocumentMapper;
import com.hungnhan.school_management.repository.DocumentRepository;
import com.hungnhan.school_management.repository.UserRepository;
import com.hungnhan.school_management.service.SubjectMaterialService;
import com.hungnhan.school_management.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SubjectMaterialServiceImpl implements SubjectMaterialService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final DocumentMapper documentMapper;
    private final FileUploadService fileUploadService;

    @Override
    public PageResponse<DocumentResponse> getMaterials(Long subjectId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Document> documentPage = documentRepository.findByOwnerTypeAndOwnerIdOrderByUploadedAtDesc("SUBJECT", subjectId, pageable);

        List<DocumentResponse> content = documentPage.getContent().stream()
                .map(documentMapper::toDocumentResponse)
                .collect(Collectors.toList());

        return PageResponse.<DocumentResponse>builder()
                .content(content)
                .pageNumber(documentPage.getNumber())
                .pageSize(documentPage.getSize())
                .totalElements(documentPage.getTotalElements())
                .totalPages(documentPage.getTotalPages())
                .last(documentPage.isLast())
                .build();
    }

    @Override
    public DocumentResponse uploadMaterial(String username, Long subjectId, DocumentRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Document document = documentMapper.toDocument(request);
        document.setOwnerType("SUBJECT");
        document.setOwnerId(subjectId);
        document.setUploadedBy(user);
        document.setStatus(Document.DocumentStatus.PROCESSED); // Assuming already processed (e.g. cloudinary)

        return documentMapper.toDocumentResponse(documentRepository.save(document));
    }

    @Override
    public void deleteMaterial(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DOCUMENT_NOT_FOUND));

        if (document.getStorageKey() != null && !document.getStorageKey().isEmpty()) {
            fileUploadService.deleteFileFromCloudinary(document.getStorageKey());
        }
        
        documentRepository.delete(document);
    }
}
