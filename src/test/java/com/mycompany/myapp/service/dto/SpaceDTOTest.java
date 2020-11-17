package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class SpaceDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpaceDTO.class);
        SpaceDTO spaceDTO1 = new SpaceDTO();
        spaceDTO1.setId(1L);
        SpaceDTO spaceDTO2 = new SpaceDTO();
        assertThat(spaceDTO1).isNotEqualTo(spaceDTO2);
        spaceDTO2.setId(spaceDTO1.getId());
        assertThat(spaceDTO1).isEqualTo(spaceDTO2);
        spaceDTO2.setId(2L);
        assertThat(spaceDTO1).isNotEqualTo(spaceDTO2);
        spaceDTO1.setId(null);
        assertThat(spaceDTO1).isNotEqualTo(spaceDTO2);
    }
}
