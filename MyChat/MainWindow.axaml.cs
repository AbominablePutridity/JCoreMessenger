using System.Threading.Tasks;
using Avalonia.Controls;
using Avalonia.Interactivity;

namespace MyChat;

public partial class MainWindow : Window
{
    public MainWindow()
    {
        InitializeComponent();
    }

    private void Button_Click(object sender, RoutedEventArgs e)
    {
        Task.Run(() => Client.getData());
    }
}