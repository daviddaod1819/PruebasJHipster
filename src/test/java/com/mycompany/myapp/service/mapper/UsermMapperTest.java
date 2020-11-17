package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UsermMapperTest {
    private UsermMapper usermMapper;

    @BeforeEach
    public void setUp() {
        usermMapper = new UsermMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(usermMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(usermMapper.fromId(null)).isNull();
    }
}
