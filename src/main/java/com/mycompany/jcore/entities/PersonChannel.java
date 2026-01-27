package com.mycompany.jcore.entities;

import java.sql.Statement;
import vendor.EntityOrm.Entity;
import vendor.EntityOrm.RelationField;

/**
 *
 * @author User
 */
public class PersonChannel extends Entity {
    public Long personId;
    public Long channelId;
    
    public PersonChannel(Statement statement)
    {
        super(statement);
        
        refs.add(new RelationField(Person.class, personId));
        refs.add(new RelationField(Channel.class, channelId));
    }
}
