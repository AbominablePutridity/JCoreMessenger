using System;
using System.Diagnostics;
using System.Threading.Tasks;
using Avalonia.Controls;
using Avalonia.Interactivity;
using MyChat.pages;

namespace MyChat.pages // ДОБАВЬТЕ .pages
{
    public partial class EditMessage : UserControl
    {
        private String messageText;
        private String messageId;

        public EditMessage(String messageText, String messageId)
        {
            InitializeComponent();

            this.messageText = messageText;
            this.messageId = messageId;

            editMessage();
        }

        private void editMessage()
        {
            EditTextBox.Text = messageText;
            
            //Debug.WriteLine("DATA->>>>> " + messageText + " " + messageId);
            //MessageController/updateMessageInChannel<endl>1<endl>NEW DESCRIPTION!!!<endl>ivanov<security>password123<endl>
        }

        public void Save_Btn(object sender, RoutedEventArgs e)
        {
            string response = Client.getData("MessageController/updateMessageInChannel<endl>" + messageId + "<endl>" + EditTextBox.Text);

            Client.messageContent.Content = new MessagesPage("", "Название группы");
        }

        public void Back_Btn(object sender, RoutedEventArgs e)
        {
            Client.messageContent.Content = new MessagesPage("", "Название группы");
        }

        public void Delete_Btn(object sender, RoutedEventArgs e)
        {
            //MessageController/deleteMessageInChannel<endl>1<endl>ivanov<security>password123<endl>
            string response = Client.getData("MessageController/deleteMessageInChannel<endl>" + messageId);

            Client.messageContent.Content = new MessagesPage("", "Название группы");
        }
    }
}