package com.zhilian.base.controller;

import com.zhilian.base.dto.ChannelDTO;
import com.zhilian.base.service.ChannelService;
import com.zhilian.base.vo.ChannelVO;
import com.zhilian.core.exception.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/base/channel")
@Api(tags = "渠道数据模块")
public class ChannelController {

    @Resource
    private ChannelService channelService;

    @GetMapping("/list")
    @ApiOperation("获取渠道列表")
    public Result<List<ChannelVO>> getChannelList() {
        return Result.success(channelService.getChannelList());
    }

    @PostMapping("/add")
    @ApiOperation("添加渠道")
    public Result<ChannelVO> addChannel(@RequestBody ChannelDTO channelDTO) {
        return Result.success(channelService.saveEntity(channelDTO));
    }
}