package com.mycompany.jcore.controller;

import com.mycompany.jcore.service.ChannelService;
import com.mycompany.jcore.service.PersonChannelService;
import com.mycompany.jcore.service.PersonService;
import java.sql.SQLException;
import java.sql.Statement;
import vendor.EntityOrm.DataSerializer;
import vendor.Security.Security;

/**
 *
 * @author User
 */
public class ChannelController extends Security {
    public ChannelService channelService;
    public PersonService personService;
    public PersonChannelService personChannelService;
    
    public ChannelController(
            ChannelService channelService,
            PersonService personService,
            Statement statement,
            PersonChannelService personChannelService
    )
    {
        super(statement);
        this.channelService = channelService;
        this.personService = personService;
        this.personChannelService = personChannelService;
    }
    
    /**
     * Вывести все текущие группы.
     * 
     * ChannelController/getMyChannelsAction<endl>0<endl>20<endl>ivanov<security>password123<endl>
     * 
     * params[0] - page
     * params[1] - size
     * params[2] - Security
     * 
     * @param params Параметры запроса пользователя.
     * @return Ответ сервера в виде строки.
     */
    public String getMyChannelsAction(String params[]) throws SQLException
    {
        String result = "";
        
//        for(String param : params)
//        {
//            result += "param is -> " + param + "\r\n";
//            System.out.println("param is -> " + param);
//        }
        
        if(super.checkRole("Person", "login", "password", "role", "admin", params)) { //проверка пользователя (Security-модуль)
            result = channelService.getAllChannelsByUserLogin(
                    //берем логин из параметров
                    super.extractLoginAndPasswordFromClientQuery(params)[0],
                    //параметр page в запросе
                    Long.parseLong(params[0]),
                    //параметр size в запросе
                    Long.parseLong(params[1])
            );
            
            //return DataSerializer.convertToEncoding(result);
            return result;
        } else {
            return super.returnException();
        }
    }
    
    /**
     * Создать свою группу.
     * 
     * ChannelController/createMyChannelAction<endl>КАКАЯ ТО ГРУППА!!!<endl>ivanov<security>password123<endl>
     * 
     * params[0] - groupName
     * params[1] - Security 
     * 
     * @param params Параметры запроса пользователя.
     * @return Ответ сервера в виде строки.
     */
    public int createMyChannelAction(String params[]) throws SQLException
    {
        int result = -1;
        
        if(super.checkRole("Person", "login", "password", "role", "admin", params)) { //проверка пользователя (Security-модуль)

            long personId = personService.getPersonIdByLogin(super.extractLoginAndPasswordFromClientQuery(params)[0]);
            long channelId = channelService.createChannelWithReturningId(params[0], personId);
            
//            System.out.println("personId = " + personId);
//            System.out.println("channelId = " + channelId);
            
            result = personChannelService.createPersonChannel(personId, channelId);
            
            return result;
        } else {
            return result;
        }
    }
}
