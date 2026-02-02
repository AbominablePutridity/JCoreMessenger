package com.mycompany.jcore.repositories;

import vendor.EntityOrm.Repository;
import com.mycompany.jcore.entities.PersonChannel;
import java.sql.ResultSet;
import java.sql.SQLException;
import vendor.EntityOrm.Entity;

/**
 *
 * @author User
 */
public class PersonChannelRepository extends Repository<PersonChannel, PersonChannel>{
    
    public PersonChannelRepository(PersonChannel entityClass) {
        super(entityClass);
    }
    
    public int createPersonChannelRepository(long personId, long channelId) throws SQLException
    {
        int result = super.getEntity().executeUpdate(
                """
                INSERT INTO personchannel (personid, channelid) VALUES(?, ?)
                """,
                new Object[]
                {
                    personId,
                    channelId
                }
        );
        
        return result;
    }
    
    /**
     * Удалить пользователя из канала (только автором канала)
     * @param channelId id канала, из которого удаляем персону
     * @param personAuthorChannelId id автора канала
     * @param personForDeleteId id персоны, которой удаляем
     * @return количество удаленных строк
     * @throws java.sql.SQLException 
     */
    public int deletePersonChannelByIdWithPersonId(long channelId, long personAuthorChannelId, long personForDeleteId) throws SQLException
    {
        int result = super.getEntity().executeUpdate(
                """
                DELETE FROM PersonChannel pc
                USING person p, channel c
                WHERE
                 pc.personid = ?
                 AND pc.channelid = ?
                 AND p.id = ?
                 AND c.personid = ?
                 AND c.id = ?;
                """,
                new Object[]
                {
                    personForDeleteId,
                    channelId,
                    personAuthorChannelId,
                    personAuthorChannelId,
                    channelId,
                }
        );
        
        return result;
    }
    
    /**
     * Добавить персону в канал по id канала и персоны
     * @param channelId id канала, в который добавить персону
     * @param personAuthorChannelId id автора канала (текущего пользователя)
     * @param personForAddId id персоны для добавления в канал
     * @return количество вставленных строк
     * @throws SQLException 
     */
    public int addPersonChannelByIdWithPersonId(long channelId, long personAuthorChannelId, long personForAddId) throws SQLException
    {
        int result = super.getEntity().executeUpdate(
                """
                INSERT INTO personchannel(personid, channelid)
                SELECT ?, ?
                FROM personchannel pc
                JOIN channel c ON c.personid = pc.personid
                WHERE c.personid = ? AND c.id = ? AND pc.channelid = ?
                AND NOT EXISTS (
                    SELECT 1 FROM personchannel pc_exist
                    WHERE pc_exist.personid = ?   -- Проверяем, что добавляемый человек
                    AND pc_exist.channelid = ?  -- Еще не состоит в этом канале
                )
                """,
                new Object[]
                {
                    personForAddId,
                    channelId,
                    personAuthorChannelId,
                    channelId,
                    channelId,
                    
                    personForAddId,
                    channelId
                }
        );
        
        return result;
    }
    
    /**
     * Просмотр всех персон в данном канале
     * @param idChannel id канала для просмотра персон, состоящих в данном канале
     * @param personId id персоны, состоящей в данном канале (id текущего пользователя)
     * @param page страница для просмотра
     * @param size количество элементов на странице
     * @return данные пользователей из БД
     */
    public ResultSet getPersonsFromChannel(long idChannel, long personId, long page, long size) throws SQLException
    {
        ResultSet data = super.getEntity().executeSQL(
                """
                SELECT
                    p.id,
                    p.identityCode,
                    p.name,
                    p.surname,
                    p.role
                FROM personchannel pc
                JOIN person p ON pc.personid = p.id
                WHERE pc.channelid IN(
                    SELECT pc.channelid
                    FROM personchannel pc
                    WHERE pc.personid = ? AND pc.channelid = ?
                )
                LIMIT ? OFFSET ?
                """,
                new Object[]
                {
                    personId,
                    idChannel,
                    size,
                    (page * size)
                }
        );
        
        return data;
    }
}
