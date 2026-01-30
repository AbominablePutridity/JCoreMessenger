package com.mycompany.jcore;

import com.mycompany.jcore.controller.ChannelController;
import com.mycompany.jcore.repositories.ChannelRepository;
import com.mycompany.jcore.repositories.MessageRepository;
import com.mycompany.jcore.repositories.PersonChannelRepository;
import com.mycompany.jcore.repositories.PersonRepository;
import java.io.IOException;
import java.sql.SQLException;
import vendor.ControllerComponent.Connection.Server;
import vendor.DI.ConfigDI;
import vendor.DI.ContainerDI;
import vendor.EntityOrm.DataSerializer;
import vendor.EntityOrm.Entity;

/**
 *
 * @author maxim
 */
public class JCore {

    public static void main(String[] args) throws SQLException, IllegalArgumentException, IllegalAccessException, IOException, Exception {
        //инициализация бинов в контейнере
        ConfigDI.setBeans();
        
        // создаем схемы таблицам БД, вызывая методы init() на бинах репозиториев сущностей
        ContainerDI.getBean(ChannelRepository.class).init();
        ContainerDI.getBean(PersonRepository.class).init();
        ContainerDI.getBean(PersonChannelRepository.class).init();
        ContainerDI.getBean(MessageRepository.class).init();
        
        //запуск сервера
        Server server = ContainerDI.getBean(Server.class); //берем бин сервера из DI-контейнера
        
        // регестрируем все наши контроллеры на сервере (для роутинга)
        //server.controllerPull.declaredControllers.add(new PersonController(ContainerDI.getBean(Statement.class)));
        server.controllerPull.declaredControllers.add(ContainerDI.getBean(ChannelController.class));
        
        server.startServer(); //запускаем сервер
        
        /*
        Настройки для PuTTY (по умолчанию, для тестирования роутов):
        1) Host name (or IP address) - 127.0.0.1 Port - 8082 - по умолчанию
        2) Connection type - Other -> Raw
        3) Close window on exit - Never
        */
    }
}
