package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.SpaceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Space} and its DTO {@link SpaceDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserInfoMapper.class })
public interface SpaceMapper extends EntityMapper<SpaceDTO, Space> {
    @Mapping(source = "userInfo.id", target = "userInfoId")
    SpaceDTO toDto(Space space);

    @Mapping(source = "userInfoId", target = "userInfo")
    Space toEntity(SpaceDTO spaceDTO);

    default Space fromId(Long id) {
        if (id == null) {
            return null;
        }
        Space space = new Space();
        space.setId(id);
        return space;
    }
}
