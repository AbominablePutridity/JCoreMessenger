package vendor.DI;

import com.mycompany.jcore.controller.ChannelController;
import com.mycompany.jcore.entities.Channel;
import com.mycompany.jcore.entities.Message;
import com.mycompany.jcore.entities.Person;
import com.mycompany.jcore.entities.PersonChannel;
import com.mycompany.jcore.repositories.ChannelRepository;
import com.mycompany.jcore.repositories.MessageRepository;
import com.mycompany.jcore.repositories.PersonChannelRepository;
import com.mycompany.jcore.repositories.PersonRepository;
import vendor.EntityOrm.Repository;
import com.mycompany.jcore.service.ChannelService;
import com.mycompany.jcore.service.PersonChannelService;
import com.mycompany.jcore.service.PersonService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import vendor.ControllerComponent.Connection.Server;
import vendor.ControllerComponent.Controller;
import vendor.EntityOrm.ConfigJDBC;

/**
 *  Это конфиг для регистрации бинов в проекте для удобного доступа к зависимостям извне
 * @author User
 */
public class ConfigDI {

    /**
     * Задаем базовые бины в контейнер (для дальнейшего внедрения).
     * @throws SQLException 
     */
    public static void setBeans() throws SQLException, Exception
    {
        //регестрируем бин для маршрутизации контроллеров в приложении
        ContainerDI.register(Controller.class, new Controller());
        
        //регестрируем бин сервера
        ContainerDI.register(Server.class, new Server(ContainerDI.getBean(Controller.class), 8082)); //на порту 8082
        
        //регестрируем бин для подключения БД
        ContainerDI.register(Connection.class, new ConfigJDBC().getConnectionDB());
        
        //регестрируем бин Statement для выполнения им SQL запросов в сущностях (для внедрения его в конструктор сущности)
        ContainerDI.register(Statement.class, ContainerDI.getBean(Connection.class).createStatement()); //создается на основе взятия обьекта подключения
        
        /*
        Ваши бины можете регестрировать здесь (только компоненты системы регистрируются как бины!!!)
        (Контроллеры (их обьекты) регестрируются в обьекте бина сервера, в обьекте Controller controllerPull,
        кладутся в List<Object> declaredControllers - это сделанно для роутинга)
        */
        
        //мои бины
        
        
        
        //бины репозиториев и сущностей
        
        //сущности
        ContainerDI.register(Channel.class, new Channel(ContainerDI.getBean(Statement.class)));
        ContainerDI.register(Person.class, new Person(ContainerDI.getBean(Statement.class)));
        ContainerDI.register(PersonChannel.class, new PersonChannel(ContainerDI.getBean(Statement.class)));
        ContainerDI.register(Message.class, new Message(ContainerDI.getBean(Statement.class)));
        
        //репозитории
        ContainerDI.register(ChannelRepository.class, new ChannelRepository(ContainerDI.getBean(Channel.class)));
        ContainerDI.register(PersonRepository.class, new PersonRepository(ContainerDI.getBean(Person.class)));
        ContainerDI.register(PersonChannelRepository.class, new PersonChannelRepository(ContainerDI.getBean(PersonChannel.class)));
        ContainerDI.register(MessageRepository.class, new MessageRepository(ContainerDI.getBean(Message.class)));
        
        
        
        
        //регистрация бинов сервисов и контроллеров
        
        //сервисы
        ContainerDI.register(ChannelService.class, new ChannelService(ContainerDI.getBean(ChannelRepository.class)));
        ContainerDI.register(PersonService.class, new PersonService(ContainerDI.getBean(PersonRepository.class)));
        ContainerDI.register(PersonChannelService.class, new PersonChannelService(ContainerDI.getBean(PersonChannelRepository.class)));
        
        //контроллеры
        ContainerDI.register(ChannelController.class, new ChannelController(
                ContainerDI.getBean(ChannelService.class),
                ContainerDI.getBean(PersonService.class),
                ContainerDI.getBean(Statement.class),
                ContainerDI.getBean(PersonChannelService.class)
            )
        );
    }
}
