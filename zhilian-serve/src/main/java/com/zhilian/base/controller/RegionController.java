package com.zhilian.base.controller;

import com.zhilian.base.entity.Region;
import com.zhilian.base.service.RegionService;
import com.zhilian.core.exception.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/base/region")
@Api(tags = "地区数据模块")
public class RegionController {

    @Resource
    private RegionService regionService;

    @GetMapping("/province")
    @ApiOperation("获取省份列表")
    public Result<List<Region>> getProvinceList() {
        return Result.success(regionService.getProvinceList());
    }

    @GetMapping("/city")
    @ApiOperation("获取城市列表")
    public Result<List<Region>> getCityList(@RequestParam Long provinceId) {
        return Result.success(regionService.getCityList(provinceId));
    }

    @GetMapping("/district")
    @ApiOperation("获取区县列表")
    public Result<List<Region>> getDistrictList(@RequestParam Long cityId) {
        return Result.success(regionService.getDistrictList(cityId));
    }
}