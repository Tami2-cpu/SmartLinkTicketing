package com.zhilian.buyer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhilian.buyer.entity.Buyer;
import com.zhilian.buyer.mapper.BuyerMapper;
import com.zhilian.buyer.service.BuyerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuyerServiceImpl extends ServiceImpl<BuyerMapper, Buyer> implements BuyerService {

    @Override
    public Buyer getByUserId(Long userId) {
        return lambdaQuery().eq(Buyer::getUserId, userId).one();
    }

    @Override
    public Buyer addBuyer(Buyer buyer) {
        save(buyer);
        return buyer;
    }

    @Override
    public List<Buyer> getBuyerList(Long userId) {
        return lambdaQuery().eq(Buyer::getUserId, userId).list();
    }

    @Override
    public Buyer getBuyerById(Long id) {
        return getById(id);
    }

    @Override
    public Buyer updateBuyer(Buyer buyer) {
        updateById(buyer);
        return buyer;
    }

    @Override
    public void deleteBuyer(Long id) {
        removeById(id);
    }
}