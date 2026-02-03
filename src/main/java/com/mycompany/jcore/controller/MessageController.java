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
    
    /**
     * Создать сообщение в канале участнику канала.
     * 
     * MessageController/createMessageInChannel<endl>1<endl>HELLO WORLD!!!<endl>ivanov<security>password123<endl>
     * 
     * params[0] - channelId
     * params[1] - description
     * params[2] - Security
     * 
     * @param params Параметры запроса пользователя.
     * @return Ответ сервера в виде строки.
     */
    public String createMessageInChannel(String params[])
    {
        int result = -1;
        
        if(super.checkRole("Person", "login", "password", "role", "user", params)) { //проверка пользователя (Security-модуль)
            
            try {
                //берем логин, page и size из параметров
                long personId = personService.getPersonIdByLogin(super.extractLoginAndPasswordFromClientQuery(params)[0]);
                long channelId = Long.parseLong(params[0]);
                String description = params[1];

                result = messageService.createMessageInChannel(personId, channelId, description);

                return "201 - успешное выполнение: создано - " + result;
            } catch (SQLException e) {
                return "ОШИБКА ВЫПОЛНЕНИЯ: " + e.getMessage();
            }
        } else {
            return super.returnException();
        }
    }
    
    /**
     * Удалить свое сообщение участнику канала.
     * 
     * MessageController/deleteMessageInChannel<endl>1<endl>ivanov<security>password123<endl>
     * 
     * params[0] - messageId
     * params[1] - Security
     * 
     * @param params Параметры запроса пользователя.
     * @return Ответ сервера в виде строки.
     */
    public String deleteMessageInChannel(String params[])
    {
        int result = -1;
        
        if(super.checkRole("Person", "login", "password", "role", "user", params)) { //проверка пользователя (Security-модуль)
            
            try {
                //берем логин, page и size из параметров
                long personId = personService.getPersonIdByLogin(super.extractLoginAndPasswordFromClientQuery(params)[0]);
                long messageId = Long.parseLong(params[0]);

                result = messageService.deleteMessageInChannel(personId, messageId);

                return "201 - успешное выполнение: удалено - " + result;
            } catch (SQLException e) {
                return "ОШИБКА ВЫПОЛНЕНИЯ: " + e.getMessage();
            }
        } else {
            return super.returnException();
        }
    }
    
    /**
     * Обновить свое сообщение в канале участнику канала.
     * 
     * MessageController/updateMessageInChannel<endl>1<endl>NEW DESCRIPTION!!!<endl>ivanov<security>password123<endl>
     * 
     * params[0] - messageId
     * params[1] - newDescription
     * 
     * @param params
     * @return 
     */
    public String updateMessageInChannel(String params[])
    {
        int result = -1;
        
        if(super.checkRole("Person", "login", "password", "role", "user", params)) { //проверка пользователя (Security-модуль)
            
            try {
                //берем логин, page и size из параметров
                long personId = personService.getPersonIdByLogin(super.extractLoginAndPasswordFromClientQuery(params)[0]);
                long messageId = Long.parseLong(params[0]);
                String newDescription = params[1];

                result = messageService.updateMessageInChannel(personId, messageId, newDescription);

                return "201 - успешное выполнение: обновлено - " + result;
            } catch (SQLException e) {
                return "ОШИБКА ВЫПОЛНЕНИЯ: " + e.getMessage();
            }
        } else {
            return super.returnException();
        }
    }
}
