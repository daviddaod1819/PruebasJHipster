package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.SpaceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Space} and its DTO {@link SpaceDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SpaceMapper extends EntityMapper<SpaceDTO, Space> {
    default Space fromId(Long id) {
        if (id == null) {
            return null;
        }
        Space space = new Space();
        space.setId(id);
        return space;
    }
}
