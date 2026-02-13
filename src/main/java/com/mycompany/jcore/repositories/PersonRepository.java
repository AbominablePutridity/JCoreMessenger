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
    
    /**
     * Взять всех персон, которые состоят в определенном канале, в котором состоит текущий пользователь
     * @param personId id текущей персоны
     * @param channelId id канала вывода персон
     * @param page страница
     * @param size количество элементов на странице
     * @return
     * @throws SQLException 
     */
    public ResultSet getPersonsByChannel(long personId, long channelId, long page, long size) throws SQLException
    {
         ResultSet data = super.getEntity().executeSQL(
                """
                SELECT 
                    p.id,
                    p.identitycode,
                    p.name,
                    p.surname
                FROM Person p
                WHERE 
                    -- Сначала проверяем, что пользователь 1 состоит в канале 1
                    EXISTS (
                        SELECT 1 
                        FROM personchannel pc 
                        WHERE pc.personid = ? 
                          AND pc.channelid = ?
                    )
                    -- Затем выводим всех участников канала
                    AND EXISTS (
                        SELECT 1 
                        FROM personchannel pc 
                        WHERE pc.personid = p.id 
                          AND pc.channelid = ?
                    )
                LIMIT ? OFFSET ?
                """,
                new Object[]
                {
                    personId,
                    channelId,
                    channelId,
                    size,
                    (page * size)
                }
        );
        
        return data;
    }
}
