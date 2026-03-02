using System.IO;
using System.Text;

namespace MyChat.pages.Storage // ДОБАВЬТЕ .pages
{
    public static class JsonStorage
    {
        public static string _filePath = "storage.json";

        // Метод для записи (перезаписи)
        public static void SaveJson(string jsonContent)
        {
            // FileMode.Create — если файл есть, он будет перезаписан, если нет — создан.
            using (FileStream fs = new FileStream(_filePath, FileMode.Create, FileAccess.Write))
            {
                using (StreamWriter writer = new StreamWriter(fs, Encoding.UTF8))
                {
                    writer.Write(jsonContent);
                }
            }
        }

        // Метод для чтения
        public static string LoadJson()
        {
            if (!File.Exists(_filePath)) return string.Empty;

            using (FileStream fs = new FileStream(_filePath, FileMode.Open, FileAccess.Read))
            {
                using (StreamReader reader = new StreamReader(fs, Encoding.UTF8))
                {
                    return reader.ReadToEnd();
                }
            }
        }
    }
}