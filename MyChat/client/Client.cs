using System;
using System.Diagnostics;
using System.IO;
using System.Net.Sockets;
using System.Text;

class Client
{
     public static string getData()
    {
        string server = "127.0.0.1";  
        int port = 8082;               
        
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
            string message = "ChannelController/getMyChannelsAction<endl><endl>0<endl>20<endl>ivanov<security>password123<endl>";
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