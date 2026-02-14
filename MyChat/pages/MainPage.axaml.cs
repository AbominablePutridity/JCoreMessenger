
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
            Client.channelContent = ChannelContent;
            Client.channelContent.Content = new Channels();
        }

        public static void LoadMessages(string groupId, String groupName, bool isOwn)
        {
            // Здесь логика получения сообщений для конкретной группы
            Debug.WriteLine("selectedGroup => " + groupId);

            Client.messageContent.Content = new MessagesPage(groupId, groupName, isOwn);
        }
    }
}