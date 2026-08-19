package com.hungnhan.school_management.mapper;

import com.hungnhan.school_management.dto.request.DocumentRequest;
import com.hungnhan.school_management.dto.response.DocumentResponse;
import com.hungnhan.school_management.entity.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    Document toDocument(DocumentRequest request);

    @Mapping(target = "uploadedBy", source = "uploadedBy.fullName")
    DocumentResponse toDocumentResponse(Document document);
}
