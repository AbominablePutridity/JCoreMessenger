package com.mycompany.jcore.controller;

import com.mycompany.jcore.service.ChannelService;
import java.sql.Statement;
import vendor.Security.Security;

/**
 *
 * @author User
 */
public class ChannelController extends Security {
    public ChannelService channelService;
    
    public ChannelController(ChannelService channelService, Statement statement)
    {
        super(statement);
        this.channelService = channelService;
    }
    
    /**
     * ChannelController/getMyChannelsAction<endl>helloWorld!<endl>JCore!<endl>ivanov<security>password123<endl>
     * 
     * params[0] - 
     * params[1] - 
     * params[2] - 
     * params[3] - Security
     * 
     * @param params Параметры запроса пользователя.
     * @return Ответ сервера в виде строки.
     */
    public String getMyChannelsAction(String params[])
    {
        String result = "";
        
        if(super.checkRole("Person", "login", "password", "role", "admin", params)) { //проверка пользователя (Security-модуль)
            for(String param : params)
            {
                result += "param is -> " + param + "\r\n";
                System.out.println("param is -> " + param);
            }
            
            return result;
        } else {
            return super.returnException();
        }
    }
}
