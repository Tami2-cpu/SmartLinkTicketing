package com.zhilian.buyer.controller;

import com.zhilian.buyer.entity.Buyer;
import com.zhilian.buyer.service.BuyerService;
import com.zhilian.core.exception.Result;
import com.zhilian.core.utils.UserActionLogger;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/buyer")
@Api(tags = "购票人模块")
public class BuyerController {

    @Resource
    private BuyerService buyerService;

    @PostMapping("/add")
    @ApiOperation("添加购票人")
    public Result<Buyer> addBuyer(@RequestBody Buyer buyer, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String username = UserActionLogger.getCurrentUsername();
        try {
            // 实际项目中需要从上下文获取用户ID
            buyer.setUserId(1L); // 模拟用户ID
            Buyer addedBuyer = buyerService.addBuyer(buyer);
            UserActionLogger.logBuyerAdd(username, buyer.getName(), true, ip);
            return Result.success(addedBuyer);
        } catch (Exception e) {
            UserActionLogger.logBuyerAdd(username, buyer.getName(), false, ip);
            throw e;
        }
    }

    @GetMapping("/list")
    @ApiOperation("获取购票人列表")
    public Result<List<Buyer>> getBuyerList() {
        // 实际项目中需要从上下文获取用户ID
        Long userId = 1L; // 模拟用户ID
        List<Buyer> buyers = buyerService.getBuyerList(userId);
        return Result.success(buyers);
    }

    @GetMapping("/detail")
    @ApiOperation("获取购票人详情")
    public Result<Buyer> getBuyerDetail(@RequestParam Long id) {
        Buyer buyer = buyerService.getBuyerById(id);
        return Result.success(buyer);
    }

    @PutMapping("/update")
    @ApiOperation("更新购票人信息")
    public Result<Buyer> updateBuyer(@RequestBody Buyer buyer, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String username = UserActionLogger.getCurrentUsername();
        try {
            Buyer updatedBuyer = buyerService.updateBuyer(buyer);
            UserActionLogger.logBuyerUpdate(username, buyer.getName(), true, ip);
            return Result.success(updatedBuyer);
        } catch (Exception e) {
            UserActionLogger.logBuyerUpdate(username, buyer.getName(), false, ip);
            throw e;
        }
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除购票人")
    public Result<String> deleteBuyer(@RequestParam Long id, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String username = UserActionLogger.getCurrentUsername();
        try {
            buyerService.deleteBuyer(id);
            UserActionLogger.logBuyerDelete(username, id, true, ip);
            return Result.success("删除成功");
        } catch (Exception e) {
            UserActionLogger.logBuyerDelete(username, id, false, ip);
            throw e;
        }
    }
}