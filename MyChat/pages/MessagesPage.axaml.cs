
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text.Json;
using Avalonia;
using Avalonia.Controls;
using Avalonia.Interactivity;
using Avalonia.Media;


namespace MyChat.pages // ДОБАВЬТЕ .pages
{
    public partial class MessagesPage : UserControl
    {
        private string IdChannel;
        private string groupName;

        public MessagesPage(string IdChannel, String groupName)
        {
            InitializeComponent();

            this.IdChannel = IdChannel;
            this.groupName = groupName;
            
            showMessagesByChannelId();
        }

        public void showMessagesByChannelId()
        {
            GroupName.Text = groupName;

            if(!IdChannel.Equals(""))
            {
                // 0. формируем url для получения данных по нему от сервера
                string url = "MessageController/getMessagesFromMyChannelAction<endl>" + IdChannel + "<endl>0<endl>100";

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

                    // Создаем Border для стилизации
                    var border = new Border
                    {
                        Background = new SolidColorBrush(Colors.LightGray),
                        CornerRadius = new CornerRadius(10),
                        Margin = new Thickness(0, 5, 0, 5),
                        Padding = new Thickness(15, 10),
                        Child = new TextBlock
                        {
                            Text = $"|{item["identitycode"]} {item["surname"]} {item["name"]}| \n{item["description"]}\n\t{item["date"]}",
                            TextWrapping = TextWrapping.Wrap,
                            FontSize = 15
                        }
                    };
                    
                    MessageContainer.Items.Add(border);
                }
            }
        }
    }
}