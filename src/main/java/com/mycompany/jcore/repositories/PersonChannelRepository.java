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
}
