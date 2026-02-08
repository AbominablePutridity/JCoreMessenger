using System.Diagnostics;
using System.Threading.Tasks;
using Avalonia.Controls;
using Avalonia.Interactivity;
using MyChat.pages;

namespace MyChat;

public partial class MainWindow : Window
{
    public MainWindow()
    {
        InitializeComponent();

        //задаем основной компонент для смены страниц
        Client.contentControl = MainContent;

        // Загружаем страницу при создании окна
        LoadLoginPage();
    }

    private void LoadLoginPage()
    {   
        //отрисоввываем страницу входа     
        Client.contentControl.Content = new LoginPage();
    }
}