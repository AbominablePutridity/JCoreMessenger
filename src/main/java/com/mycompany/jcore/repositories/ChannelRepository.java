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
    
    /**
     * Взять все каналы текущей персоны
     * @param personId id персоны
     * @param page страница для вывода
     * @param size количество элементов на странице
     * @return данные из БД
     * @throws SQLException 
     */
    public ResultSet getAllGroupsByPersonLoginWithPagination(long personId, long page, long size) throws SQLException
    {
        ResultSet data = super.getEntity().executeSQL(
                """
                SELECT channel.id, channel.name FROM channel
                INNER JOIN personchannel pc ON channel.id = pc.channelid
                INNER JOIN person p ON pc.personid = p.id
                WHERE p.id = ?
                LIMIT ? OFFSET ?
                """,
                new Object[]
                {
                    personId,
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
    
    /**
     * Обновляем данные о канале по его автору
     * @param authorId id автора канала (текущего пользователя)
     * @param channelId id канала, у которого меняем данные
     * @param newName новое название канала для замены
     * @return
     * @throws SQLException 
     */
    public int updateChannelByAuthor(long authorId, long channelId, String newName) throws SQLException
    {
        int result = super.getEntity().executeUpdate(
                """
                UPDATE channel c
                SET
                    name = ?
                WHERE
                    c.personid = ? AND c.id = ?
                """,
                new Object[]
                {
                    newName,
                    authorId,
                    channelId
                }
        );
        
        return result;
    }
}
