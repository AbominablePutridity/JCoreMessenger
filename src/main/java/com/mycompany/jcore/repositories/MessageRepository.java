package com.mycompany.jcore.repositories;

import vendor.EntityOrm.Repository;
import com.mycompany.jcore.entities.Message;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    
    public ResultSet getAllMessagesByPersonLoginAndChannelWithPagination(String loginPerson, long channelId, int page, int size) throws SQLException
    {
        
        // Запрос сработает в БД, но данные в Java не попадут
        ResultSet data = super.getEntity().executeSQL(
                """
                SELECT m.*
                FROM message m
                INNER JOIN personchannel pc_author ON m.personchannelid = pc_author.id
                WHERE pc_author.channelid = ?  -- Сообщения этого канала
                  AND EXISTS (                 -- Условие: Петрова должна быть участником этого канала
                      SELECT 1 
                      FROM personchannel pc_check
                      INNER JOIN person p ON pc_check.personid = p.id
                      WHERE pc_check.channelid = 1 
                        AND p.login = ?
                  )
                LIMIT ? OFFSET ?
                """,
                new Object[]
                {
                    channelId,
                    loginPerson,
                    
                    size,
                    (page * size)
                }
        );

        return data;
    }
}
