package com.mycompany.jcore.controller;

import com.mycompany.jcore.repositories.PersonRepository;
import com.mycompany.jcore.service.PersonService;
import java.sql.SQLException;
import java.sql.Statement;
import vendor.Security.Security;

/**
 * Пример контроллера с выводом переданных параметров клиенту.
 * @author User
 */
public class PersonController extends Security {
    public PersonService personService;
    
    public PersonController(Statement statement, PersonService personService) {
        super(statement);
        this.personService = personService;
    }
    
    /* telnet 127.0.0.1 8082
    запрос (через коммандную строку): PersonController/createPersonAction<endl>helloWorld!<endl>JCore!<endl>ivanov<security>pass<endl>
    */
    
    /**
     * Проверкалогина и пароля пользователя на соответствие из базы данных.
     * 
     * PersonController/checkLoginAndPassword<endl>ivanov<security>password123<endl>
     * params[0] - Security
     * 
     * @param params
     * @return true - если логин и пароль совпадают
     */
    public boolean checkLoginAndPassword(String[] params)
    {
        if(super.checkRole("Person", "login", "password", "role", "user", params)) { //проверка пользователя (Security-модуль)   
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Взятие членов канала, если текущий пользователь состоит в данном канале.
     * 
     * PersonController/getMembersByChannel<endl>1<endl>0<endl>50<endl>ivanov<security>password123<endl>
     * params[0] - channelId
     * params[1] - page
     * params[2] - size
     * params[3] - Security
     * 
     * @param params
     * @return true - если логин и пароль совпадают
     */
    public String getMembersByChannel(String[] params) throws SQLException
    {
        String result = "";
        
//        for(String param : params)
//        {
//            result += "param is -> " + param + "\r\n";
//            System.out.println("param is -> " + param);
//        }
        
        if(super.checkRole("Person", "login", "password", "role", "user", params)) { //проверка пользователя (Security-модуль)
            
            try {
                //берем логин, page и size из параметров
                long personId = personService.getPersonIdByLogin(super.extractLoginAndPasswordFromClientQuery(params)[0]);
                long channelId = Long.parseLong(params[0]);
                long page = Long.parseLong(params[1]);
                long size = Long.parseLong(params[2]);
                
                System.out.println("p0 = " + channelId + ", p1 = " + channelId + ", p2 = " + page + ", p3 = " + size);

                result = personService.getAllPersonsByChannel(
                        personId,
                        channelId,
                        page,
                        size
                );

                
            } catch (SQLException e) {
                return "ОШИБКА ВЫПОЛНЕНИЯ: " + e.getMessage();
            }
        } else {
            return super.returnException();
        }
        
        return result;
    }
}
