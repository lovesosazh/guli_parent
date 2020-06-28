package com.lovesosa.guli.service.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lovesosa.guli.common.base.result.R;
import com.lovesosa.guli.service.cms.entity.Ad;
import com.lovesosa.guli.service.cms.entity.vo.AdVO;
import com.lovesosa.guli.service.cms.feign.OssFileService;
import com.lovesosa.guli.service.cms.mapper.AdMapper;
import com.lovesosa.guli.service.cms.service.IAdService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.util.List;

/**
 * <p>
 * 广告推荐 服务实现类
 * </p>
 *
 * @author lovesosa
 * @since 2020-06-01
 */
@Service
public class AdServiceImpl extends ServiceImpl<AdMapper, Ad> implements IAdService {


    @Autowired
    private OssFileService ossFileService;

    @Override
    public IPage<AdVO> selectPage(Long page, Long limit) {
        QueryWrapper<AdVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("a.type_id", "a.sort");

        Page<AdVO> pageParam = new Page<>(page, limit);

        List<AdVO> records = baseMapper.selectPageByQueryWrapper(pageParam, queryWrapper);
        pageParam.setRecords(records);
        return pageParam;
    }

    @Override
    public boolean removeAdImageById(String id) {
        Ad ad = baseMapper.selectById(id);
        if(ad != null) {
            String imagesUrl = ad.getImageUrl();
            if(!StringUtils.isEmpty(imagesUrl)){
                //删除图片
                R r = ossFileService.removeFile(imagesUrl);
                return r.getSuccess();
            }
        }
        return false;
    }

    @Cacheable(value = "index", key="'selectByAdTypeId'")
    @Override
    public List<Ad> seletByAdTypeId(String adTypeId) {
        QueryWrapper<Ad> queryWrapper =  new QueryWrapper<>();
        queryWrapper.orderByAsc("sort","id");
        queryWrapper.eq("type_id",adTypeId);
        return baseMapper.selectList(queryWrapper);
    }
}
