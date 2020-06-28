package com.lovesosa.guli.service.cms.controller.api;

import com.lovesosa.guli.common.base.result.R;
import com.lovesosa.guli.service.cms.entity.Ad;
import com.lovesosa.guli.service.cms.service.IAdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lovesosa
 */
@Api(tags = "广告推荐")
@RestController
@RequestMapping("/api/cms/ad")
@Slf4j
public class ApiAdController {

    @Autowired
    private IAdService adService;
    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("根据推荐位id显示广告推荐")
    @GetMapping("/list/{adTypeId}")
    public R listByAdTypeId(@ApiParam(value = "推荐位id", required = true) @PathVariable String adTypeId) {
        List<Ad> adList =  adService.seletByAdTypeId(adTypeId);
        return R.ok().data("items",adList);
    }


    @PostMapping("/save-test")
    public R saveAd(@RequestBody Ad ad) {
        redisTemplate.opsForValue().set("ad",ad);
        return R.ok();
    }


    @GetMapping("/get-test/{key}")
    public R getAd(@PathVariable String key) {
        Ad ad = (Ad) redisTemplate.opsForValue().get(key);
        return R.ok().data("ad",ad);
    }

    @DeleteMapping("/remove-test/{key}")
    public R removeAd(@PathVariable String key) {
        Boolean result = redisTemplate.delete(key);
        Boolean aBoolean = redisTemplate.hasKey(key);
        return R.ok();
    }
}
