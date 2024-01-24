package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.Client;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName ClientMapper
 * @Description 客户端相关Mapper
 * @Author su
 * @Date 2024/1/24 15:33
 */
@Mapper
public interface ClientMapper extends BaseMapper<Client> {
}
