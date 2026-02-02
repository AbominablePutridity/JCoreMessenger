package com.mycompany.jcore.service;

import com.mycompany.jcore.repositories.ChannelRepository;
import com.mycompany.jcore.repositories.PersonChannelRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import vendor.Security.Security;

/**
 *
 * @author User
 */
public class PersonChannelService {
    public PersonChannelRepository personChannelRepository;
    
    public PersonChannelService(PersonChannelRepository personChannelRepository)
    {
        this.personChannelRepository = personChannelRepository;
    }
    
    /**
     * Создать связку между Персоной и Каналом (Приглашение в канал - только для Автора)
     * @param personId id персоны для добавления в канал
     * @param channelId id канала, в которую добавить персону
     * @return
     * @throws SQLException 
     */
    public int createPersonChannel(long personId, long channelId) throws SQLException
    {
        return personChannelRepository.createPersonChannelRepository(personId, channelId);
    }
    
    /**
     * Удалить связку между Персоной и Каналом (Удаление из канала какой либо персоны - только для Автора)
     * @param channelId id канала, из которого удалить персону
     * @param personAuthorChannelId id aвторa канала (id персоны, выполняющая запрос)
     * @param personForDeleteId id персоны для удаления
     * @return
     * @throws SQLException 
     */
    public int deletePersonChannelByIdWithPersonId(long channelId, long personAuthorChannelId, long personForDeleteId) throws SQLException
    {
        return personChannelRepository.deletePersonChannelByIdWithPersonId(channelId, personAuthorChannelId, personForDeleteId);
    }
    
    public int addPersonChannelByIdWithPersonId(long channelId, long personAuthorChannelId, long personForAddId) throws SQLException
    {
        return personChannelRepository.addPersonChannelByIdWithPersonId(channelId, personAuthorChannelId, personForAddId);
    }
}
