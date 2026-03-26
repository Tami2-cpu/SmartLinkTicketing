package com.zhilian.base.service.impl;

import com.zhilian.base.dto.ChannelDTO;
import com.zhilian.base.entity.Channel;
import com.zhilian.base.mapper.ChannelMapper;
import com.zhilian.base.service.ChannelService;
import com.zhilian.base.vo.ChannelVO;
import com.zhilian.core.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChannelServiceImpl extends BaseServiceImpl<ChannelMapper, Channel> implements ChannelService {

    @Override
    public List<ChannelVO> getChannelList() {
        List<Channel> channels = lambdaQuery().eq(Channel::getStatus, 1).list();
        return channels.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public ChannelVO saveEntity(ChannelDTO channelDTO) {
        Channel channel = convertToEntity(channelDTO);
        channel.setStatus(1); // 默认启用
        save(channel);
        return convertToVO(channel);
    }

    /**
     * 将ChannelDTO转换为Channel实体
     */
    private Channel convertToEntity(ChannelDTO channelDTO) {
        Channel channel = new Channel();
        channel.setName(channelDTO.getName());
        // 这里可以添加更多字段的转换
        return channel;
    }

    /**
     * 将Channel实体转换为ChannelVO
     */
    private ChannelVO convertToVO(Channel channel) {
        ChannelVO channelVO = new ChannelVO();
        channelVO.setId(channel.getId());
        channelVO.setName(channel.getName());
        channelVO.setCode(channel.getCode());
        channelVO.setStatus(channel.getStatus());
        channelVO.setCreateTime(channel.getCreateTime());
        channelVO.setUpdateTime(channel.getUpdateTime());
        return channelVO;
    }
}