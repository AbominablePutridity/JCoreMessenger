package com.mycompany.jcore.service;

import com.mycompany.jcore.repositories.MessageRepository;
import java.sql.SQLException;
import vendor.EntityOrm.DataSerializer;

/**
 *
 * @author User
 */
public class MessageService {
    public MessageRepository messageRepository;
    
    public MessageService(MessageRepository messageRepository)
    {
        this.messageRepository = messageRepository;
    }
    
    /**
     * Вывести все сообщения канала, в котором состоит пользователь по пагинации 
     * @param personId id персоны
     * @param channelId id канала для взятия сообщений
     * @param page страница
     * @param size количество элементов на странице
     * @return данные из БД
     * @throws SQLException 
     */
    public String getAllMessagesByPersonLoginAndChannelWithPagination(long personId, long channelId, long page, long size) throws SQLException
    {
        return DataSerializer.convertToJson(
            DataSerializer.serializeFromResultDataToList(
                messageRepository.getAllMessagesByPersonLoginAndChannelWithPagination(personId, channelId, page, size)
            )
        );
    }
}
