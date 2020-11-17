package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SpaceMapperTest {
    private SpaceMapper spaceMapper;

    @BeforeEach
    public void setUp() {
        spaceMapper = new SpaceMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(spaceMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(spaceMapper.fromId(null)).isNull();
    }
}
