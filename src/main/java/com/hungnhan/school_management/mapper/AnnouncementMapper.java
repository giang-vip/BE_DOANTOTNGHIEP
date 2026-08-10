package com.hungnhan.school_management.mapper;

import com.hungnhan.school_management.dto.request.AnnouncementRequest;
import com.hungnhan.school_management.dto.response.AnnouncementResponse;
import com.hungnhan.school_management.entity.Announcement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnnouncementMapper {

    @Mapping(target = "classSection", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    Announcement toAnnouncement(AnnouncementRequest request);

    @Mapping(source = "classSection.id", target = "classSectionId")
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.username", target = "createdByUsername")
    AnnouncementResponse toAnnouncementResponse(Announcement announcement);
}
