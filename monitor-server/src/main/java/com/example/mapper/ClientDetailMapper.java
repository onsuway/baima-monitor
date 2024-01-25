package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.ClientDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName ClientDetailMapper
 * @Description 客户端详细信息Mapper
 * @Author su
 * @Date 2024/1/25 14:41
 */
@Mapper
public interface ClientDetailMapper extends BaseMapper<ClientDetail> {
}
