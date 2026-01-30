package com.mycompany.jcore.entities;

import java.sql.Statement;
import vendor.EntityOrm.Entity;
import vendor.EntityOrm.RelationField;

/**
 *
 * @author User
 */
public class Channel extends Entity {
    public String name; 
    public Long personId;
    
    public Channel(Statement statement) {
        super(statement); // передаем обьект для создания запросов родителю
        
        refs.add(new RelationField(Person.class, personId));
    }
}
