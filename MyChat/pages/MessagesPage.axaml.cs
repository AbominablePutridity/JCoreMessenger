
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text.Json;
using Avalonia.Controls;
using Avalonia.Interactivity;


namespace MyChat.pages // ДОБАВЬТЕ .pages
{
    public partial class MessagesPage : UserControl
    {
        private string IdChannel;
        public MessagesPage(string IdChannel)
        {
            InitializeComponent();

            this.IdChannel = IdChannel;
            
            showMessagesByChannelId();
        }

        public void showMessagesByChannelId()
        {
            if(!IdChannel.Equals(""))
            {
                string url = "MessageController/getMessagesFromMyChannelAction<endl>" + IdChannel + "<endl>0<endl>100";

                string result = Client.getData(url);
            }
        }
    }
}