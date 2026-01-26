package com.mycompany.jcore.repositories;

import com.mycompany.jcore.entities.Channel;
import vendor.EntityOrm.Entity;

/**
 *
 * @author User
 */
public class ChannelRepository extends Repository<Channel, Channel> {
    
    public ChannelRepository(Channel entityClass) {
        super(entityClass);
    }
    
}
