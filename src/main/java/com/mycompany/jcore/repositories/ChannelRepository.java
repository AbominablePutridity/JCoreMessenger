package com.mycompany.jcore.repositories;

import vendor.EntityOrm.Repository;
import com.mycompany.jcore.entities.Channel;
import java.sql.ResultSet;
import java.sql.SQLException;
import vendor.DI.ContainerDI;
import vendor.EntityOrm.Entity;

/**
 *
 * @author User
 */
public class ChannelRepository extends Repository<Channel, Channel> {
    
    public ChannelRepository(Channel entityClass) {
        super(entityClass);
    }
    
    public ResultSet getAllGroupsByPersonLoginWithPagination(String loginPerson, long page, long size) throws SQLException
    {
        ResultSet data = super.getEntity().executeSQL(
                "SELECT channel.id, channel.name FROM channel " +
                "INNER JOIN personchannel pc ON channel.id = pc.channelid " +
                "INNER JOIN person p ON pc.personid = p.id " +
                "WHERE p.login = ? " +
                "LIMIT ? OFFSET ?",
                new Object[]
                {
                    loginPerson,
                    size,
                    (page * size)
                }
        );
        
        return data;
    }
    
    /**
     * Создаем группу по названию и id автора с возвращением его id.
     * @param groupName Имя новой группы
     * @param personId id персоны-создателя
     * @return id создаваемой записи
     * @throws SQLException 
     */
    public ResultSet createGroupWithReturningId(String groupName, long personId) throws SQLException
    {
        ResultSet result = super.getEntity().executeSQL(
                """
                INSERT INTO channel (name, personid) VALUES(?, ?)
                RETURNING id;
                """,
                new Object[]
                {
                    groupName,
                    personId
                }
        );
        
        return result;
    }
    
    /**
     * Удаляем канал по его id и id-Автора
     * @param channelId id удаляемого канала
     * @param personAuthorChannelId id удаляемого автора
     * @return
     * @throws SQLException 
     */
    public int deleteChannelByIdWithPersonId(long channelId, long personAuthorChannelId) throws SQLException
    {
        int result = super.getEntity().executeUpdate(
                """
                DELETE FROM channel c
                USING person p
                WHERE c.personid = ? AND c.id = ?
                """,
                new Object[]
                {
                    personAuthorChannelId,
                    channelId
                }
        );
        
        return result;
    }
}
