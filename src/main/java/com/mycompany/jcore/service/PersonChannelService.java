package com.mycompany.jcore.service;

import com.mycompany.jcore.repositories.ChannelRepository;
import com.mycompany.jcore.repositories.PersonChannelRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import vendor.Security.Security;

/**
 *
 * @author User
 */
public class PersonChannelService {
    public PersonChannelRepository personChannelRepository;
    
    public PersonChannelService(PersonChannelRepository personChannelRepository)
    {
        this.personChannelRepository = personChannelRepository;
    }
    
    public int createPersonChannel(long personId, long channelId) throws SQLException
    {
        return personChannelRepository.createPersonChannelRepository(personId, channelId);
    }
}
