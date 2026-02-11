using System;
using System.Diagnostics;
using System.IO;
using System.Net.Sockets;
using System.Text;
using Avalonia.Controls;

class Client
{
    private static string server = "127.0.0.1";  
    private static int port = 8082;

    public static string login;
    public static string password;

    public static ContentControl contentControl;
    public static ContentControl messageContent;

    public static string initializeSecurity()
    {
        return "<endl>" + login + "<security>" + password + "<endl>";
    }

    //"ChannelController/getMyChannelsAction<endl><endl>0<endl>20"
    public static string getData(string url)
    {
        url = url + initializeSecurity();

        try
        {
            using TcpClient client = new TcpClient(server, port);
            
            // Используем StreamReader/Writer для работы с текстом
            using NetworkStream stream = client.GetStream();
            
            // Указываем явно кодировку UTF-8 БЕЗ BOM (Byte Order Mark)
            Encoding utf8WithoutBom = new UTF8Encoding(false); // false = без BOM
            
            using StreamWriter writer = new StreamWriter(stream, utf8WithoutBom) { AutoFlush = true };
            using StreamReader reader = new StreamReader(stream, utf8WithoutBom);
            
            // ОТПРАВЛЯЕМ
            string message = url;
            writer.WriteLine(message);
            Debug.WriteLine($"Отправлено: {message}");
            
            // ЧИТАЕМ ОТВЕТ
            System.Threading.Thread.Sleep(200);
            
            string response = reader.ReadToEnd();
            Debug.WriteLine($"Получено: {response}");
            
            return response;
        }
        catch (Exception ex)
        {
            Debug.WriteLine($"Ошибка: {ex.Message}");
            return $"Ошибка: {ex.Message}";
        }
    }
}