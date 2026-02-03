package com.mycompany.jcore.controller;

import com.mycompany.jcore.service.MessageService;
import com.mycompany.jcore.service.PersonService;
import java.sql.SQLException;
import java.sql.Statement;
import vendor.Security.Security;

/**
 *
 * @author User
 */
public class MessageController extends Security {
    private MessageService messageService;
    private PersonService personService;
    
    public MessageController(
            MessageService messageService,
            PersonService personService,
            Statement statement
    )
    {
        super(statement);
        this.messageService = messageService;
        this.personService = personService;
    }
    
    /**
     * MessageController/getMessagesFromMyChannelAction<endl>1<endl>0<endl>20<endl>ivanov<security>password123<endl>
     * 
     * params[0] - channelId
     * params[1] - page
     * params[2] - size
     * params[3] - Security
     * 
     * @param params
     * @return 
     */
    public String getMessagesFromMyChannelAction(String params[])
    {
        String result = "";
        
        if(super.checkRole("Person", "login", "password", "role", "user", params)) { //проверка пользователя (Security-модуль)
            
            try {
                //берем логин, page и size из параметров
                long personId = personService.getPersonIdByLogin(super.extractLoginAndPasswordFromClientQuery(params)[0]);
                long channelId = Long.parseLong(params[0]);
                long page = Long.parseLong(params[1]);
                long size = Long.parseLong(params[2]);

                result = messageService.getAllMessagesByPersonLoginAndChannelWithPagination(
                        personId,
                        channelId,
                        page,
                        size
                );

                return result;
            } catch (SQLException e) {
                return "ОШИБКА ВЫПОЛНЕНИЯ: " + e.getMessage();
            }
        } else {
            return super.returnException();
        }
    }
}
