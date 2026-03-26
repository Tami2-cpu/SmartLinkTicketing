package com.zhilian.base.service;

import com.zhilian.base.entity.Region;
import com.zhilian.core.service.BaseService;

public interface RegionService extends BaseService<Region> {
    java.util.List<Region> getProvinceList();

    java.util.List<Region> getCityList(Long provinceId);

    java.util.List<Region> getDistrictList(Long cityId);
}