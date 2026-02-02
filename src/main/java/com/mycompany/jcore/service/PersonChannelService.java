package com.mycompany.jcore.service;

import com.mycompany.jcore.repositories.ChannelRepository;
import com.mycompany.jcore.repositories.PersonChannelRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import vendor.EntityOrm.DataSerializer;
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
    
    /**
     * Добавить персону в канал по id канала и персоны
     * @param channelId id канала, в который добавить персону
     * @param personAuthorChannelId id автора канала (текущего пользователя)
     * @param personForAddId id персоны для добавления в канал
     * @return количество вставленных строк
     * @throws SQLException 
     */
    public int addPersonChannelByIdWithPersonId(long channelId, long personAuthorChannelId, long personForAddId) throws SQLException
    {
        return personChannelRepository.addPersonChannelByIdWithPersonId(channelId, personAuthorChannelId, personForAddId);
    }
    
    /**
     * Просмотр всех персон в данном канале
     * @param idChannel id канала для просмотра персон, состоящих в данном канале
     * @param personId id персоны, состоящей в данном канале (id текущего пользователя)
     * @param page страница для просмотра
     * @param size количество элементов на странице
     * @return данные пользователей из БД
     */
    public String getPersonsFromChannel(long idChannel, long personId, long page, long size) throws SQLException
    {
        return DataSerializer.convertToJson(
            DataSerializer.serializeFromResultDataToList(
                personChannelRepository.getPersonsFromChannel(idChannel, personId, page, size)
            )
        );
    }
}
