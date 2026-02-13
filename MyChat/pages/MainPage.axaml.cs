
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text.Json;
using Avalonia.Controls;
using Avalonia.Interactivity;


namespace MyChat.pages // ДОБАВЬТЕ .pages
{
    public partial class MainPage : UserControl
    {
        int page = 0;
        int size = 20;

        public MainPage()
        {
            InitializeComponent();

            // загружаем страницу сообщений справа
            Client.messageContent = MainContent;
            Client.messageContent.Content = new MessagesPage("", "Название группы", false);

            // загружаем список всех групп на первой странице слева
            getChannelsData("");
        }

        public void Search_Button_Click(object sender, RoutedEventArgs e)
        {
            getChannelsData(SearchField.Text);

            SearchField.Text = "";
        }

        public void Pag_Next_Btn(object sender, RoutedEventArgs e)
        {
            page = page + 1;

            getChannelsData("");
        }

        public void Pag_Prev_Btn(object sender, RoutedEventArgs e)
        {
            if (page > 0) {
                page = page - 1;

                getChannelsData("");
            }
        }

        public void getChannelsData(string searchField)
        {
            // 0. формируем url для получения данных по нему от сервера
            string url = "ChannelController/getMyChannelsAction<endl>" + searchField + "<endl>" + page + "<endl>" + size;

            // 1. получаем данные в формате json-строки от сервера
            string response = Client.getData(url);

            // Парсим JSON в список словарей
            var options = new JsonSerializerOptions { PropertyNameCaseInsensitive = true };
            var data = JsonSerializer.Deserialize<List<Dictionary<string, object>>>(response, options);

            // Очищаем старые чаты перед добавлением новых
            ChatContainer.Children.Clear();

            //перебираем каждый элемент в json-ответе
            foreach (var item in data)
            {
                // 1. Создаем кнопку вручную (и описываем каждой кнопке ее свойства в разметке)
                var btn = new Button
                {
                    Content = item["name"].ToString(), // Текст из JSON
                    Tag = item["id"].ToString(),       // ID сохраняем в Tag
                    HorizontalAlignment = Avalonia.Layout.HorizontalAlignment.Stretch,
                    Margin = new Avalonia.Thickness(0, 2),
                    Height = 50,
                    HorizontalContentAlignment = Avalonia.Layout.HorizontalAlignment.Center,
                    VerticalContentAlignment = Avalonia.Layout.VerticalAlignment.Center
                };

                // 2. Вешаем событие клика прямо здесь
                btn.Click += (s, e) => 
                {
                    // Этот код сработает при нажатии
                    string id = btn.Tag.ToString();
                    LoadMessages(id, item["name"].ToString(), bool.Parse(item["is_own"].ToString().Trim())); // Твой метод загрузки сообщений
                };

                // 3. Добавляем кнопку в наш StackPanel
                ChatContainer.Children.Add(btn);
            }
        }

        private void LoadMessages(string groupId, String groupName, bool isOwn)
        {
            // Здесь логика получения сообщений для конкретной группы
            Debug.WriteLine("selectedGroup => " + groupId);

            Client.messageContent.Content = new MessagesPage(groupId, groupName, isOwn);
        }
    }
}