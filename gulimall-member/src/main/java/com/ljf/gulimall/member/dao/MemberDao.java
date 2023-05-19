package com.ljf.gulimall.member.dao;

import com.ljf.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author ll
 * @email 861624779@qq.com
 * @date 2022-09-02 23:48:18
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
