
using Avalonia.Controls;
using Avalonia.Interactivity;


namespace MyChat.pages // ДОБАВЬТЕ .pages
{
    public partial class LoginPage : UserControl
    {
        public LoginPage()
        {
            InitializeComponent();
        }

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            Client.login = LoginTextBox.Text;
            Client.password = PasswordTextBox.Text;

            string response = Client.getData("PersonController/checkLoginAndPassword").Trim();

            //если ответ вернул true, тогда пользователь входит
            if(response.Equals("true"))
            {
                Client.contentControl.Content = new MainPage();
            } else
            {
                LoginTextBox.Text = "";
                PasswordTextBox.Text = "";
            }
        }
    }
}