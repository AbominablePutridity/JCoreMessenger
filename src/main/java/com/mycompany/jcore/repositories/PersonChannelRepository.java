package com.mycompany.jcore.repositories;

import vendor.EntityOrm.Repository;
import com.mycompany.jcore.entities.PersonChannel;
import vendor.EntityOrm.Entity;

/**
 *
 * @author User
 */
public class PersonChannelRepository extends Repository<PersonChannel, PersonChannel>{
    
    public PersonChannelRepository(PersonChannel entityClass) {
        super(entityClass);
    }
    
}
