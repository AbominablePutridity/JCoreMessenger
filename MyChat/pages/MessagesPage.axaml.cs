
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text.Json;
using Avalonia;
using Avalonia.Controls;
using Avalonia.Interactivity;
using Avalonia.Layout;
using Avalonia.Media;
using Avalonia.Threading;
using MyChat.pages;


namespace MyChat.pages // ДОБАВЬТЕ .pages
{
    public partial class MessagesPage : UserControl
    {
        private DispatcherTimer _timer;

        public static string IdChannel = "";
        public static string groupName = "Название группы";
        private int page = 0;
        public static bool isOwn = false;

        public MessagesPage(string IdChannel, String groupName, bool isOwn)
        {
            InitializeComponent();

            MessagesPage.IdChannel = IdChannel;
            MessagesPage.groupName = groupName;
            MessagesPage.isOwn = isOwn;
            
            showMessagesByChannelId();

            // таймер на обновление сообщений каждые 5 мин (Pooling)
            _timer = new DispatcherTimer();
            _timer.Interval = TimeSpan.FromMinutes(5);
            _timer.Tick += (s, e) => showMessagesByChannelId();
            _timer.Start();
        }

        public void showMessagesByChannelId()
        {
            GroupName.Text = groupName;

            if(!IdChannel.Equals(""))
            {
                // 0. формируем url для получения данных по нему от сервера
                string url = "MessageController/getMessagesFromMyChannelAction<endl>" + IdChannel + "<endl>" + page + "<endl>100";

                // 1. получаем данные в формате json-строки от сервера
                string response = Client.getData(url);

                // Парсим JSON в список словарей
                var options = new JsonSerializerOptions { PropertyNameCaseInsensitive = true };
                var data = JsonSerializer.Deserialize<List<Dictionary<string, object>>>(response, options);

                // Очищаем старые чаты перед добавлением новых
                MessageContainer.Items.Clear();

                foreach (var item in data)
                {
                    //MessageContainer.Items.Add("|"+item["identitycode"] + " " + item["surname"] + " " + item["name"] + "| \n" + item["description"] + "\n" + "\t" + item["date"] + " ");

                    SolidColorBrush backgroundColor = new SolidColorBrush(Colors.LightGray);
                    HorizontalAlignment horizontalAlignment = Avalonia.Layout.HorizontalAlignment.Left;

                    if(item["is_own"].ToString().Trim().Equals("True"))
                    {
                        backgroundColor = new SolidColorBrush(Colors.LightGreen);
                        horizontalAlignment = Avalonia.Layout.HorizontalAlignment.Right;
                    }

                    /* Создаем Border для стилизации итемов сообщений
                    (но Border не имеет свойства Tag для задания данных, поэтому требуется его обернуть в ContentControl, который имеет это свойство)*/
                    var border = new Border
                    {
                        Background = backgroundColor,
                        CornerRadius = new CornerRadius(10),
                        Margin = new Thickness(0, 5, 0, 5),
                        Padding = new Thickness(15, 10),
                        Child = new TextBlock
                        {
                            Text = $"|{item["identitycode"]} {item["surname"]} {item["name"]}| \n{item["description"]}\n\t{item["date"]}",
                            TextWrapping = TextWrapping.Wrap,
                            FontSize = 15
                        },
                        HorizontalAlignment = horizontalAlignment
                    };

                    // обарачиваем Border в ContentControl для задания свойства Tag
                    var contentControl = new ContentControl
                    {
                        Content = border,
                        Tag = (id: item["id"].ToString(), is_own: item["is_own"].ToString()), // Теперь есть Tag!
                    };
                    
                    // добавляем новый item со стилем в ListBox сообщений
                    MessageContainer.Items.Add(contentControl);

                    // Добавляем обработчик PointerPressed (тапа/клика), если это - сообщение текущего пользователя
                    if(item["is_own"].ToString().Trim().Equals("True"))
                    {
                        border.PointerPressed += (sender, e) =>
                        {
                            //передача аргументов из Tag текущего item на следующую страницу (страницу редактирования сообщения)
                            Client.messageContent.Content = new EditMessage(item["description"].ToString(), item["id"].ToString());
                        };
                    }
                }

                if (MessageContainer.Items.Count - 1 > 0) {
                    MessageContainer.ScrollIntoView(MessageContainer.Items[MessageContainer.Items.Count - 1]); //прокручиваем ListBox с сообщениями в самый конец (после вывода всех сообщений)
                }
            }
        }

        public void Send_Message_Btn(object sender, RoutedEventArgs e)
        {
            if(!messageText.Text.Equals("")) {
                string url = "MessageController/createMessageInChannel<endl>" + IdChannel + "<endl>" + messageText.Text;

                string response = Client.getData(url);

                showMessagesByChannelId();

                messageText.Text = "";
            }
        }

        public void Members_Btn(object sender, RoutedEventArgs e)
        {
            if(isOwn) { // если текущий пользователь - автор данного канала
                Client.messageContent.Content = new EditMembers(IdChannel); // разрешаем открывать панель редактирования участников канала
            } else
            {
                Client.messageContent.Content = new Members(IdChannel); // иначе загружаем страницу с просмотром всех пользователей канала без возможности его редактирования
            }
        }

        public void Pag_Next_Btn(object sender, RoutedEventArgs e)
        {
            page = page + 1;

            showMessagesByChannelId();
        }

        public void Pag_Prev_Btn(object sender, RoutedEventArgs e)
        {
            if(page > 0)
            {
                page = page - 1;

                showMessagesByChannelId();
            }
        }
    }
}