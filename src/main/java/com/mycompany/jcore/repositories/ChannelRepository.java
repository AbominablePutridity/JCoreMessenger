package com.mycompany.jcore.repositories;

import vendor.EntityOrm.Repository;
import com.mycompany.jcore.entities.Channel;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
