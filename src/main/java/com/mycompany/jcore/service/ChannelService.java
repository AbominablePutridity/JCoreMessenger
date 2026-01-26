package com.mycompany.jcore.service;

import com.mycompany.jcore.repositories.ChannelRepository;

/**
 *
 * @author User
 */
public class ChannelService {
    public ChannelRepository channelRepository;
    
    public ChannelService(ChannelRepository channelRepository)
    {
        this.channelRepository = channelRepository;
    }
    
    
}
