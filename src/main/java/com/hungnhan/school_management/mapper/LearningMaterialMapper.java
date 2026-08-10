package com.hungnhan.school_management.mapper;

import com.hungnhan.school_management.dto.request.LearningMaterialRequest;
import com.hungnhan.school_management.dto.response.LearningMaterialResponse;
import com.hungnhan.school_management.entity.LearningMaterial;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LearningMaterialMapper {

    @Mapping(target = "classSection", ignore = true)
    @Mapping(target = "uploadedBy", ignore = true)
    LearningMaterial toLearningMaterial(LearningMaterialRequest request);

    @Mapping(source = "classSection.id", target = "classSectionId")
    @Mapping(source = "uploadedBy.id", target = "uploadedById")
    @Mapping(source = "uploadedBy.username", target = "uploadedByUsername")
    LearningMaterialResponse toLearningMaterialResponse(LearningMaterial material);
}
