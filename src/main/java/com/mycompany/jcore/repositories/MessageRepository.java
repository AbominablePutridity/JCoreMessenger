package com.mycompany.jcore.repositories;

import com.mycompany.jcore.entities.Message;
import vendor.EntityOrm.Entity;

/**
 *
 * @author User
 */
public class MessageRepository extends Repository<Message, Message> {
    
    public MessageRepository(Message entityClass) {
        super(entityClass);
    }
    
}
