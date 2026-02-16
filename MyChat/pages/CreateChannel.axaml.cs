
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text.Json;
using Avalonia.Controls;
using Avalonia.Interactivity;


namespace MyChat.pages // ДОБАВЬТЕ .pages
{
    public partial class CreateChannel : Window
    {
        public CreateChannel()
        {
            InitializeComponent();
        }

        public void Create_Channel_Btn(object sender, RoutedEventArgs e)
        {
            string url = "ChannelController/createMyChannelAction<endl>" + InputField.Text;

            string response = Client.getData(url);

            Client.channelContent.Content = new Channels(); // обновляем список каналов

            this.Close(); //закрываем текущее окно
        }
    }
}