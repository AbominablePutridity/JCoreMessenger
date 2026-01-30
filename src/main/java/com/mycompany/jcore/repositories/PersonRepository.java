package com.mycompany.jcore.repositories;

import vendor.EntityOrm.Repository;
import com.mycompany.jcore.entities.Person;
import java.sql.ResultSet;
import java.sql.SQLException;
import vendor.EntityOrm.Entity;

/**
 *
 * @author User
 */
public class PersonRepository extends Repository<Person, Person>{
    
    public PersonRepository(Person entityClass) {
        super(entityClass);
    }
    
    /**
     * Взять id пользователя по логину из Базы Данных
     * @param loginPerson логин пользователя
     * @return id пользователя
     * @throws SQLException 
     */
    public ResultSet getIdByPersonLogin(String loginPerson) throws SQLException
    {
        ResultSet data = super.getEntity().executeSQL(
                """
                SELECT p.id
                FROM Person p
                WHERE p.login = ?
                LIMIT 1
                """,
                new Object[]
                {
                    loginPerson
                }
        );
        
        return data;
    }
}
