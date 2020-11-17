package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class UsermDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UsermDTO.class);
        UsermDTO usermDTO1 = new UsermDTO();
        usermDTO1.setId(1L);
        UsermDTO usermDTO2 = new UsermDTO();
        assertThat(usermDTO1).isNotEqualTo(usermDTO2);
        usermDTO2.setId(usermDTO1.getId());
        assertThat(usermDTO1).isEqualTo(usermDTO2);
        usermDTO2.setId(2L);
        assertThat(usermDTO1).isNotEqualTo(usermDTO2);
        usermDTO1.setId(null);
        assertThat(usermDTO1).isNotEqualTo(usermDTO2);
    }
}
