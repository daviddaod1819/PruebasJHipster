package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.UsermDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Userm} and its DTO {@link UsermDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserInfoMapper.class })
public interface UsermMapper extends EntityMapper<UsermDTO, Userm> {
    @Mapping(source = "userInfo.id", target = "userInfoId")
    UsermDTO toDto(Userm userm);

    @Mapping(source = "userInfoId", target = "userInfo")
    Userm toEntity(UsermDTO usermDTO);

    default Userm fromId(Long id) {
        if (id == null) {
            return null;
        }
        Userm userm = new Userm();
        userm.setId(id);
        return userm;
    }
}
