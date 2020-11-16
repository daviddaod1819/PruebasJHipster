package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class UsermTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Userm.class);
        Userm userm1 = new Userm();
        userm1.setId(1L);
        Userm userm2 = new Userm();
        userm2.setId(userm1.getId());
        assertThat(userm1).isEqualTo(userm2);
        userm2.setId(2L);
        assertThat(userm1).isNotEqualTo(userm2);
        userm1.setId(null);
        assertThat(userm1).isNotEqualTo(userm2);
    }
}
