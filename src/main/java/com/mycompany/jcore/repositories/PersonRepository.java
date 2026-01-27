package com.mycompany.jcore.repositories;

import vendor.EntityOrm.Repository;
import com.mycompany.jcore.entities.Person;
import vendor.EntityOrm.Entity;

/**
 *
 * @author User
 */
public class PersonRepository extends Repository<Person, Person>{
    
    public PersonRepository(Person entityClass) {
        super(entityClass);
    }
    
}
