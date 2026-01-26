package com.mycompany.jcore.service;

import com.mycompany.jcore.repositories.PersonRepository;

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
    
    //...
}
