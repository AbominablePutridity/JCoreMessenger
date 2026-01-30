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
}
