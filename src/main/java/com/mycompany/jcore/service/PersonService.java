package com.mycompany.jcore.service;

import com.mycompany.jcore.repositories.PersonRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import vendor.EntityOrm.DataSerializer;

/**
 *
 * @author User
 */
public class PersonService {
    public PersonRepository personRepository;
    
    public PersonService(PersonRepository personRepository) throws Exception
    {
        this.personRepository = personRepository;
    }
    
    /**
     * Взять id пользователя по его логину.
     * @param personLogin Логин пользователя.
     * @return id пользователя.
     * @throws SQLException 
     */
    public long getPersonIdByLogin(String personLogin) throws SQLException
    {
        ResultSet rs = personRepository.getIdByPersonLogin(personLogin);
        long id = -1;
        
        if (rs.next()) { // Перемещаем курсор на первую строку
            // Читаем данные из первой строки
            id = rs.getLong("id");
        }
        
        rs.close();
        
        if(id == -1){
            System.out.println("ОШИБКА - id пользователя с логином = " + personLogin + " не найден!");
        }
        
        return id;
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
    public String getAllPersonsByChannel(long personId, long channelId, long page, long size) throws SQLException
    {
        //выводим сериализованный лист с данными в формате json
        return DataSerializer.convertToJson(
            DataSerializer.serializeFromResultDataToList(
                personRepository
                        .getPersonsByChannel(personId, channelId, page, size)
            )
        );
    }
}
