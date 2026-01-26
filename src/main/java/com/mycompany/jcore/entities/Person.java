package com.mycompany.jcore.entities;
import java.sql.Statement;
import vendor.EntityOrm.Entity;
import vendor.EntityOrm.RelationField;

/**
 * 
 * @author maxim
 */
public class Person extends Entity {
    public String name;
    public String surname;
    
    public String login;
    public String password;
    public String role;
    public String identityCode;
    
    public Long channelId;

    public Person(Statement statement) {
        super(statement); // передаем обьект для создания запросов родителю
        
        refs.add(new RelationField(Channel.class, channelId));
    }    
}
