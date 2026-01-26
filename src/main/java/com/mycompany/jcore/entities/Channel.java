package com.mycompany.jcore.entities;

import java.sql.Statement;
import vendor.EntityOrm.Entity;

/**
 *
 * @author User
 */
public class Channel extends Entity {
    public String name; 
    
    public Channel(Statement statement) {
        super(statement); // передаем обьект для создания запросов родителю
    }
}
