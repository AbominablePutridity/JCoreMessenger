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
    
    public Long personChannelId;
    
    public Message(Statement statement) {
        super(statement);
        
        refs.add(new RelationField(PersonChannel.class, personChannelId));
    }
}
