
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text.Json;
using Avalonia.Controls;
using Avalonia.Interactivity;


namespace MyChat.pages // ДОБАВЬТЕ .pages
{
    public partial class Members : UserControl
    {
        private string idChannel;
        int page = 0;
        int size = 20;

        public Members(string idChannel)
        {
            InitializeComponent();

            this.idChannel = idChannel;

            getAllMembersByChannel();
        }

        public void getAllMembersByChannel()
        {
            // 0. формируем url для получения данных по нему от сервера
            string url = "PersonController/getMembersByChannel<endl>" + idChannel + "<endl>" + page + "<endl>" + size;

            // 1. получаем данные в формате json-строки от сервера
            string response = Client.getData(url);

            

            // Парсим JSON в список словарей
            var options = new JsonSerializerOptions { PropertyNameCaseInsensitive = true };
            var data = JsonSerializer.Deserialize<List<Dictionary<string, object>>>(response, options);

            // Очищаем старых персон перед добавлением новых
            MembersList.Items.Clear();

            // перебираем каждый элемент из листа и создаем элемент с данными из этого листа
            foreach (var item in data)
            {
                MembersList.Items.Add(item["identitycode"] + "\n" + item["surname"] + " " + item["name"]);
            }
        }

        public void Back_Btn(object sender, RoutedEventArgs e)
        {
            Client.messageContent.Content = new MessagesPage(MessagesPage.IdChannel, MessagesPage.groupName, MessagesPage.isOwn);
        }

        public void Pag_Next_Btn(object sender, RoutedEventArgs e)
        {
            page = page + 1;

            getAllMembersByChannel();
        }

        public void Pag_Prev_Btn(object sender, RoutedEventArgs e)
        {
            if(page > 0)
            {
                page = page - 1;

                getAllMembersByChannel();
            }
        }
    }
}