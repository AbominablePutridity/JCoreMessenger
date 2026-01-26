package com.mycompany.jcore.entities;

import java.sql.Statement;
import java.time.LocalDateTime;
import vendor.EntityOrm.Entity;
import vendor.EntityOrm.RelationField;

/**
 *
 * @author User
 */
public class Message extends Entity {
    public String description;
    public LocalDateTime date;
    
    public Long channelId;
    public Long personId;
    
    public Message(Statement statement) {
        super(statement);
        
        refs.add(new RelationField(Person.class, personId));
        refs.add(new RelationField(Channel.class, channelId));
    }
}
