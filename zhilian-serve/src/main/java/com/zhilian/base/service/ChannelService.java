package com.zhilian.base.service;

import com.zhilian.base.dto.ChannelDTO;
import com.zhilian.base.entity.Channel;
import com.zhilian.base.vo.ChannelVO;
import com.zhilian.core.service.BaseService;

public interface ChannelService extends BaseService<Channel> {
    java.util.List<ChannelVO> getChannelList();

    ChannelVO saveEntity(ChannelDTO channelDTO);
}