package com.mycompany.jcore.repositories;

import vendor.EntityOrm.Entity;

public abstract class Repository<ENTITY extends Entity, DTO extends Entity> {
    private ENTITY entityClass;
    
    public Repository(ENTITY entityClass) {
        this.entityClass = entityClass;
    }
    
    public void init() throws Exception {
        // Создание таблицы на основе entityClass
        // На основании описанных выше обьектов, создаем таблицы
        System.out.println(entityClass.createTable(entityClass.initializeChild()));
    }
    
    public void setData(DTO data) throws Exception {
        // Вставка данных из DTO
        // Передаем данные обьекта таблице в БД
        System.out.println(entityClass.insertData(data.initializeChild()));
    }
    
}