package com.zhilian.buyer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhilian.buyer.entity.Buyer;

import java.util.List;

public interface BuyerService extends IService<Buyer> {
    /**
     * 根据用户ID获取买家信息
     * 
     * @param userId 用户ID
     * @return 买家信息
     */
    Buyer getByUserId(Long userId);

    /**
     * 添加买家信息
     * 
     * @param buyer 买家信息
     * @return 添加的买家信息
     */
    Buyer addBuyer(Buyer buyer);

    /**
     * 获取买家列表
     * 
     * @param userId 用户ID
     * @return 买家列表
     */
    List<Buyer> getBuyerList(Long userId);

    /**
     * 根据ID获取买家信息
     * 
     * @param id 买家ID
     * @return 买家信息
     */
    Buyer getBuyerById(Long id);

    /**
     * 更新买家信息
     * 
     * @param buyer 买家信息
     * @return 更新后的买家信息
     */
    Buyer updateBuyer(Buyer buyer);

    /**
     * 删除买家信息
     * 
     * @param id 买家ID
     */
    void deleteBuyer(Long id);
}