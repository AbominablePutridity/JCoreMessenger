
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text.Json;
using System.Threading.Channels;
using Avalonia;
using Avalonia.Controls;
using Avalonia.Interactivity;
using Avalonia.Layout;
using Avalonia.Media;
using MyChat.pages;


namespace MyChat.pages // ДОБАВЬТЕ .pages
{
    public partial class EditMembers : UserControl
    {
        private string idChannel;

        public EditMembers(string idChannel)
        {
            InitializeComponent();

            this.idChannel = idChannel;

            MembersContent.Content = new Members(idChannel);
        }

        public void Delete_Member_Btn(object sender, RoutedEventArgs e)
        {
            if(!DeleteField.Text.Equals("")) {
                string url = "PersonChannelController/deletePersonFromMyChannelAction<endl>" + idChannel + "<endl>" + DeleteField.Text;

                string response = Client.getData(url);

                MembersContent.Content = new Members(idChannel);

                DeleteField.Text = "";
            }
        }

        public void Add_Member_Btn(object sender, RoutedEventArgs e)
        {
            if(!AddField.Text.Equals("")) {
                string url = "PersonChannelController/addPersonFromMyChannelAction<endl>" + idChannel + "<endl>" + AddField.Text;

                string response = Client.getData(url);

                MembersContent.Content = new Members(idChannel);

                AddField.Text = "";
            }
        }

        public void Delete_Channel_Btn(object sender, RoutedEventArgs e)
        {
            string url = "ChannelController/deleteMyChannelAction<endl>" + idChannel;

            string response = Client.getData(url);

            Client.messageContent.Content = new MessagesPage("", "Название группы", false);

            Client.channelContent.Content = new Channels(); // обновляем список каналов
        }
    }
}