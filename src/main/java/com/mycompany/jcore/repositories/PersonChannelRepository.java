package com.mycompany.jcore.repositories;

import vendor.EntityOrm.Repository;
import com.mycompany.jcore.entities.PersonChannel;
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
    
    /*
    -- Добавим пользователя 'alex' только если не существует пользователя с таким именем
    INSERT INTO users (username, email)
    SELECT 'alex', 'alex@example.com'
    WHERE NOT EXISTS (
        SELECT 1 FROM users WHERE username = 'alex'
    );
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
}
