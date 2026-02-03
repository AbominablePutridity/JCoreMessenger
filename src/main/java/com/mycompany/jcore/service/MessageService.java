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
    
    /**
     * Создать сообщение в канале пользователя
     * @param personId id пользователя-создателя сообщения
     * @param channelId id канала, в котором создать сообщение
     * @param description содержимое сообщения
     * @return количество созданных строк
     * @throws SQLException 
     */
    public int createMessageInChannel(long personId, long channelId, String description) throws SQLException
    {
        return messageRepository.createMessageByUser(personId, channelId, description);
    }
    
    /**
     * Удалить сообщение пользователя в канале (если пользователь - создатель этого сообщения)
     * @param personId id пользователя, удаляющего сообщения
     * @param messageId id сообщения для удаления
     * @return количество удаленных строк
     */
    public int deleteMessageInChannel(long personId, long messageId) throws SQLException
    {
        return messageRepository.deleteMyMessage(personId, messageId);
    }
    
    /**
     * Обновить сообщение в канале
     * 
     * @param personId id персоны, обновляющая свое сообщение
     * @param messageId id сообщения для обновления
     * @param newDescription обновленное сообщение для обновления
     * @return количество обновленных строк
     * @throws SQLException 
     */
    public int updateMessageInChannel(long personId, long messageId, String newDescription) throws SQLException
    {
        return messageRepository.updateMyMessage(personId, messageId, newDescription);
    }
}
