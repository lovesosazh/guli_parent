package com.lovesosa.guli.service.cms.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lovesosa.guli.service.cms.entity.Ad;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lovesosa.guli.service.cms.entity.vo.AdVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 广告推荐 Mapper 接口
 * </p>
 *
 * @author lovesosa
 * @since 2020-06-01
 */
public interface AdMapper extends BaseMapper<Ad> {

    List<AdVO> selectPageByQueryWrapper(Page<AdVO> pageParam, @Param(Constants.WRAPPER) QueryWrapper<AdVO> queryWrapper);
}
