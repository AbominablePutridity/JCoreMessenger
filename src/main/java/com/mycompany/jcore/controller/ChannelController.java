package com.mycompany.jcore.controller;

import com.mycompany.jcore.service.ChannelService;
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
    
    public ChannelController(ChannelService channelService, Statement statement)
    {
        super(statement);
        this.channelService = channelService;
    }
    
    /**
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
        
        for(String param : params)
        {
            result += "param is -> " + param + "\r\n";
            System.out.println("param is -> " + param);
        }
        
        if(super.checkRole("Person", "login", "password", "role", "admin", params)) { //проверка пользователя (Security-модуль)
//            for(String param : params)
//            {
//                result += "param is -> " + param + "\r\n";
//                System.out.println("param is -> " + param);
//            }
//            
//            return result;

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
}
