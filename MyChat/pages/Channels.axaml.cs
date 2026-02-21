
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text.Json;
using Avalonia.Controls;
using Avalonia.Interactivity;
using Avalonia.Threading;


namespace MyChat.pages
{
    public partial class Channels : UserControl
    {
        private DispatcherTimer _timer;

        int page = 0;
        int size = 20;

        public Channels()
        {
            InitializeComponent();

            // загружаем список всех групп на первой странице слева
            getChannelsData("");

            // таймер на обновление чатов каждые 5 мин (Pooling)
            _timer = new DispatcherTimer();
            _timer.Interval = TimeSpan.FromMinutes(5);
            _timer.Tick += (s, e) => getChannelsData(SearchField.Text);
            _timer.Start();
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
                    MainPage.LoadMessages(id, item["name"].ToString(), bool.Parse(item["is_own"].ToString().Trim())); // метод загрузки сообщений
                };

                // 3. Добавляем кнопку в наш StackPanel
                ChatContainer.Children.Add(btn);
            }
        }

        public void Create_Channel_Btn(object sender, RoutedEventArgs e)
        {
            var newWindow = new CreateChannel();
            newWindow.Show(); // Обычное окно
        }
    }
}