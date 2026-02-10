package com.mycompany.jcore.repositories;

import vendor.EntityOrm.Repository;
import com.mycompany.jcore.entities.Message;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import vendor.DI.ContainerDI;
import vendor.EntityOrm.Entity;

/**
 *
 * @author User
 */
public class MessageRepository extends Repository<Message, Message> {
    
    public MessageRepository(Message entityClass) {
        super(entityClass);
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
    public ResultSet getAllMessagesByPersonLoginAndChannelWithPagination(long personId, long channelId, long page, long size) throws SQLException
    {
        // Запрос сработает в БД, но данные в Java не попадут
        ResultSet data = super.getEntity().executeSQL(
                """
                SELECT m.id,
                m.description,
                m.date,
                p.name,
                p.surname,
                p.identityCode
                FROM message m
                INNER JOIN personchannel pc_author ON m.personchannelid = pc_author.id
                JOIN person p ON pc_author.personid = p.id
                WHERE pc_author.channelid = ?  -- Сообщения этого канала
                  AND EXISTS (                 -- Условие: пользоатнль должен быть участником этого канала
                      SELECT 1 
                      FROM personchannel pc_check
                      INNER JOIN person p ON pc_check.personid = p.id
                      WHERE pc_check.channelid = ?
                        AND p.id = ?
                  )
                ORDER BY m.date ASC
                LIMIT ? OFFSET ?
                """,
                new Object[]
                {
                    channelId,
                    channelId,
                    personId,
                    
                    size,
                    (page * size)
                }
        );

        return data;
    }
    
    /**
     * Создать сообщение пользователем, входящим в состав канала.
     * @param personId id персоны-автора сообщения в канале
     * @param channelId id канала, где написать сообщение
     * @param description содержимое сообщения
     * @return количество созданных строк
     * @throws SQLException 
     */
    public int createMessageByUser(long personId, long channelId, String description) throws SQLException
    {
        LocalDateTime date = LocalDateTime.now();
        
        // Запрос сработает в БД, но данные в Java не попадут
        int data = super.getEntity().executeUpdate(
                """
                INSERT INTO message (description, date, personchannelid)
                SELECT ?, ?, pc.id
                FROM personchannel pc
                WHERE pc.personid = ? AND channelid = ?;
                """,
                new Object[]
                {
                    description,
                    date,
                    
                    personId,
                    channelId
                }
        );

        return data;
    }
    
    /**
     * Удаление своего сообщения
     * @param personId id персоны, удаляющая сообщение
     * @param messageId id сообщения для удаления
     * @return количество удаленных строк
     * @throws SQLException 
     */
    public int deleteMyMessage(long personId, long messageId) throws SQLException
    {
        LocalDateTime date = LocalDateTime.now();
        
        // Запрос сработает в БД, но данные в Java не попадут
        int data = super.getEntity().executeUpdate(
                """
                DELETE FROM message m
                USING personchannel pc
                WHERE m.id = ? AND m.personchannelid = pc.id AND pc.personid = ?
                """,
                new Object[]
                {
                    messageId,
                    personId
                }
        );

        return data;
    }
    
    public int updateMyMessage(long personId, long messageId, String newDescription) throws SQLException
    {
        LocalDateTime date = LocalDateTime.now();
        
        // Запрос сработает в БД, но данные в Java не попадут
        int data = super.getEntity().executeUpdate(
                """
                UPDATE message m
                SET description = ?
                FROM personchannel pc
                WHERE m.id = ? AND m.personchannelid = pc.id AND pc.personid = ?
                """,
                new Object[]
                {
                    newDescription,
                    messageId,
                    personId
                }
        );

        return data;
    }
}
