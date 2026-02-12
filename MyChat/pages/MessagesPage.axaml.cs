
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
using MyChat.pages;


namespace MyChat.pages // ДОБАВЬТЕ .pages
{
    public partial class MessagesPage : UserControl
    {
        public static string IdChannel = "";
        public static string groupName = "Название группы";
        public static int page = 0;

        public MessagesPage(string IdChannel, String groupName)
        {
            InitializeComponent();

            MessagesPage.IdChannel = IdChannel;
            MessagesPage.groupName = groupName;
            
            showMessagesByChannelId();
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
            }
        }

        public void Send_Message_Btn(object sender, RoutedEventArgs e)
        {
            string url = "MessageController/createMessageInChannel<endl>" + IdChannel + "<endl>" + messageText.Text;

            string response = Client.getData(url);

            showMessagesByChannelId();
        }

        public void Members_Btn(object sender, RoutedEventArgs e)
        {
            Client.messageContent.Content = new EditMembers();
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