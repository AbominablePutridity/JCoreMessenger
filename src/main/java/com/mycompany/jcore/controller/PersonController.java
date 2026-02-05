package com.mycompany.jcore.controller;

import com.mycompany.jcore.repositories.PersonRepository;
import com.mycompany.jcore.service.PersonService;
import java.sql.Statement;
import vendor.Security.Security;

/**
 * Пример контроллера с выводом переданных параметров клиенту.
 * @author User
 */
public class PersonController extends Security {
    public PersonService personService;
    
    public PersonController(Statement statement) {
        super(statement);
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
}
