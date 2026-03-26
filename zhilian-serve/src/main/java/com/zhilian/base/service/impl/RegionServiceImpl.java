package com.zhilian.base.service.impl;

import com.zhilian.base.entity.Region;
import com.zhilian.base.mapper.RegionMapper;
import com.zhilian.base.service.RegionService;
import com.zhilian.core.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionServiceImpl extends BaseServiceImpl<RegionMapper, Region> implements RegionService {

    @Override
    public List<Region> getProvinceList() {
        return lambdaQuery().eq(Region::getLevel, 1).list();
    }

    @Override
    public List<Region> getCityList(Long provinceId) {
        return lambdaQuery().eq(Region::getParentId, provinceId).eq(Region::getLevel, 2).list();
    }

    @Override
    public List<Region> getDistrictList(Long cityId) {
        return lambdaQuery().eq(Region::getParentId, cityId).eq(Region::getLevel, 3).list();
    }
}