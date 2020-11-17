package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserInfoMapperTest {
    private UserInfoMapper userInfoMapper;

    @BeforeEach
    public void setUp() {
        userInfoMapper = new UserInfoMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(userInfoMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(userInfoMapper.fromId(null)).isNull();
    }
}
