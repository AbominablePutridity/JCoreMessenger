package com.mycompany.jcore.controller;

import com.mycompany.jcore.service.PersonChannelService;
import com.mycompany.jcore.service.PersonService;
import java.sql.SQLException;
import java.sql.Statement;
import vendor.Security.Security;

/**
 *
 * @author User
 */
public class PersonChannelController extends Security {
    public PersonChannelService personChannelService;
    public PersonService personService;
    
    public PersonChannelController(
            PersonChannelService personChannelService,
            PersonService personService,
            Statement statement
    )
    {
        super(statement);
        this.personChannelService = personChannelService;
        this.personService = personService;
    }
    
    /**
     * Удалить персону из своей группы.
     * 
     * PersonChannelController/deletePersonFromMyChannelAction<endl>1<endl>2<endl>ivanov<security>password123<endl>
     * 
     * params[0] - channelId
     * params[1] - personForDeleteId
     * params[2] - Security 
     * 
     * @param params Параметры запроса пользователя.
     * @return Ответ сервера в виде строки.
     */
    public String deletePersonFromMyChannelAction(String params[])
    {
        long result = -1;
        
        if(super.checkRole("Person", "login", "password", "role", "user", params)) { //проверка пользователя (Security-модуль)
            try {
                long authorChannelId = personService.getPersonIdByLogin(super.extractLoginAndPasswordFromClientQuery(params)[0]);
                long personForDelete = Long.parseLong(params[1]);
                long channelId = Long.parseLong(params[0]);
           
                result = personChannelService.deletePersonChannelByIdWithPersonId(
                        channelId, authorChannelId, personForDelete
                );
                
                return "201 - успешное выполнение: удалено - " + result;
            } catch (SQLException e)
            {
                return "ОШИБКА ПРИ ОПЕРАЦИИ: " + e.getMessage();
            }
        } else {
            return "403";
        }
    }
    
    /**
     * Добавить персону из своей группы.
     * 
     * PersonChannelController/addPersonFromMyChannelAction<endl>1<endl>2<endl>ivanov<security>password123<endl>
     * 
     * params[0] - channelId
     * params[1] - personForAddId
     * params[2] - Security 
     * 
     * @param params Параметры запроса пользователя.
     * @return Ответ сервера в виде строки.
     */
    public String addPersonFromMyChannelAction(String params[])
    {
        long result = -1;
        
        if(super.checkRole("Person", "login", "password", "role", "user", params)) { //проверка пользователя (Security-модуль)
            try {
                long authorChannelId = personService.getPersonIdByLogin(super.extractLoginAndPasswordFromClientQuery(params)[0]);
                long personForAdd = Long.parseLong(params[1]);
                long channelId = Long.parseLong(params[0]);
           
                result = personChannelService.addPersonChannelByIdWithPersonId(
                        channelId, authorChannelId, personForAdd
                );
                
                return "201 - успешное выполнение: удалено - " + result;
            } catch (SQLException e)
            {
                return "ОШИБКА ПРИ ОПЕРАЦИИ: " + e.getMessage();
            }
        } else {
            return "403";
        }
    }
    
    /**
     * Вывести всех участников группы (если пользователь сам учавствует в этой группе)
     * 
     * PersonChannelController/getPersonsFromChannelAction<endl>1<endl>0<endl>20<endl>ivanov<security>password123<endl>
     * 
     * params[0] - channelId
     * params[1] - page
     * params[2] - size
     * params[3] - Security 
     * 
     * @param params Параметры запроса пользователя.
     * @return Ответ сервера в виде строки.
     */
    public String getPersonsFromChannelAction(String params[])
    {
        try {
            //берем логин из параметров
            long personId = personService.getPersonIdByLogin(super.extractLoginAndPasswordFromClientQuery(params)[0]);
            long page = Long.parseLong(params[1]);
            long size = Long.parseLong(params[2]);
            
            //выводим сериализованный лист с данными в формате json
            return personChannelService.getPersonsFromChannel(personId, personId, page, size);
        } catch (SQLException e)
        {
            return("ОШИБКА ВЫПОЛНЕНИЯ: " + e.getMessage());
        }
    }
}
