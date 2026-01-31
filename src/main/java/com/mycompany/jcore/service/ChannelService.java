package com.mycompany.jcore.service;

import com.mycompany.jcore.repositories.ChannelRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import vendor.EntityOrm.DataSerializer;

/**
 *
 * @author User
 */
public class ChannelService {
    public ChannelRepository channelRepository;
    
    public ChannelService(ChannelRepository channelRepository)
    {
        this.channelRepository = channelRepository;
    }
    
    /**
     * Взять все каналы пользователя по его логину
     * @param userLogin логин пользователя
     * @param page страница.
     * @param size количество элементов на странице.
     * @return каналы пользователя.
     * @throws SQLException 
     */
    public String getAllChannelsByUserLogin(String userLogin, long page, long size) throws SQLException
    {
        //выводим сериализованный лист с данными в формате json
        return DataSerializer.convertToJson(
            DataSerializer.serializeFromResultDataToList(
                channelRepository
                        .getAllGroupsByPersonLoginWithPagination(userLogin, page, size)
            )
        );
    }
    
    /**
     * Создание канала с возвращением его id.
     * @param groupName Имя создаваемого канала.
     * @param personId Id персоны-создателя канала
     * @return Id создаваемой записи.
     * @throws SQLException 
     */
    public long createChannelWithReturningId(String groupName, long personId) throws SQLException
    {
        ResultSet rs = channelRepository.createGroupWithReturningId(groupName, personId);
        long id = -1;
        
        if (rs.next()) { // Перемещаем курсор на первую строку
            // Читаем данные из первой строки
            id = rs.getLong("id");
        }
        
        rs.close();
        
        if(id == -1){
            System.out.println("ОШИБКА - id созданного канала не найден!");
        }
        
        return id;
    }
    
    /**
     * Удалить группу его же автором
     * @param channelId id группы
     * @param personId id автора
     * @return
     * @throws SQLException 
     */
    public long deleteChannel(long channelId, long personId) throws SQLException
    {
        return channelRepository.deleteChannelByIdWithPersonId(channelId, channelId);
    }
}
