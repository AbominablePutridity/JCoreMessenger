package com.mycompany.jcore.service;

import com.mycompany.jcore.repositories.ChannelRepository;
import java.sql.SQLException;
import vendor.EntityOrm.DataSerializer;

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
    
    public String getAllChannelsByUserLogin(String userLogin, long page, long size) throws SQLException
    {
        //выводим сериализованный лист с данными в формате json
        return DataSerializer.convertToJson(
            DataSerializer.serializeFromResultDataToList(
                channelRepository
                        .getAllGroupsByPersonLoginWithPagination(userLogin, page, size)
            )
        );
    }
}
