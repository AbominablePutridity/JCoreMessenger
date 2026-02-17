
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text.Json;
using System.Threading.Tasks;
using Avalonia;
using Avalonia.Controls;
using Avalonia.Controls.Documents;
using Avalonia.Interactivity;
using Avalonia.Layout;
using Avalonia.Media;
using Avalonia.Media.Imaging;
using Avalonia.Platform.Storage;
using Avalonia.Threading;
using MyChat.pages;


namespace MyChat.pages // ДОБАВЬТЕ .pages
{
    public partial class MessagesPage : UserControl
    {
        private DispatcherTimer _timer;
        private string? base64Image;

        public static string IdChannel = "";
        public static string groupName = "Название группы";
        private int page = 0;
        public static bool isOwn = false;

        public MessagesPage(string IdChannel, String groupName, bool isOwn)
        {
            InitializeComponent();

            MessagesPage.IdChannel = IdChannel;
            MessagesPage.groupName = groupName;
            MessagesPage.isOwn = isOwn;
            
            showMessagesByChannelId();

            // таймер на обновление сообщений каждые 5 мин (Pooling)
            _timer = new DispatcherTimer();
            _timer.Interval = TimeSpan.FromMinutes(5);
            _timer.Tick += (s, e) => showMessagesByChannelId();
            _timer.Start();
        }

        public void showMessagesByChannelId()
        {
            GroupName.Text = groupName;

            if(!IdChannel.Equals(""))
            {
                // 0. формируем url для получения данных по нему от сервера
                string url = "MessageController/getMessagesFromMyChannelAction<endl>" + IdChannel + "<endl>" + page + "<endl>100";

                // 1. получаем данные в формате json-строки от сервера
                string response = Client.getData(url);

                // Парсим JSON в список словарей
                var options = new JsonSerializerOptions { PropertyNameCaseInsensitive = true };
                var data = JsonSerializer.Deserialize<List<Dictionary<string, object>>>(response, options);

                // Очищаем старые чаты перед добавлением новых
                MessageContainer.Items.Clear();

                foreach (var item in data)
                {
                    //MessageContainer.Items.Add("|"+item["identitycode"] + " " + item["surname"] + " " + item["name"] + "| \n" + item["description"] + "\n" + "\t" + item["date"] + " ");

                    SolidColorBrush backgroundColor = new SolidColorBrush(Colors.LightGray);
                    HorizontalAlignment horizontalAlignment = Avalonia.Layout.HorizontalAlignment.Left;

                    /*если сообщение - наше (is_own полученный с Backend = true),
                     то красим текущее сообщение в зеленый и центрируем по правому краю*/
                    if(item["is_own"].ToString().Trim().Equals("True"))
                    {
                        backgroundColor = new SolidColorBrush(Colors.LightGreen);
                        horizontalAlignment = Avalonia.Layout.HorizontalAlignment.Right;
                    }

                    string[] parsedData = item["description"].ToString().Split("<img>");

                    TextBlock child = null;
                    if(parsedData.Length > 1)
                    {
                        // 1. Получаем массив байт из base64 строки
                        byte[] imageBytes = Convert.FromBase64String(parsedData[1]);

                        // 2. Создаем поток из байтов
                        MemoryStream stream = new MemoryStream(imageBytes);

                        // 3. Создаем изображение из потока
                        Bitmap bitmap = new Bitmap(stream);

                        // 4. Создаем Image контрол и вставляем в него Bitmap
                        Image image = new Image
                        {
                            Width = 800,
                            Height = 800,
                            Source = bitmap
                        };

                        // Оборачиваем в ViewBox
                        Viewbox viewbox = new Viewbox
                        {
                            Child = image,
                            Stretch = Stretch.Uniform,  // Масштабировать равномерно
                            MaxWidth = 800,
                            MaxHeight = 800
                        };

                        // 5. Вставляем Image в TextBlock
                        child = new TextBlock
                        {
                            Text = $"|{item["identitycode"]} {item["surname"]} {item["name"]}| \n{parsedData[0]}\n\t{item["date"]}",
                            TextWrapping = TextWrapping.Wrap,
                            FontSize = 15
                        };

                        child.Inlines.Add(new InlineUIContainer(viewbox));
                    } else
                    {
                        child = new TextBlock
                        {
                            Text = $"|{item["identitycode"]} {item["surname"]} {item["name"]}| \n{item["description"]}\n\t{item["date"]}",
                            TextWrapping = TextWrapping.Wrap,
                            FontSize = 15
                        };
                    }

                    /* Создаем Border для стилизации итемов сообщений
                    (но Border не имеет свойства Tag для задания данных, поэтому требуется его обернуть в ContentControl, который имеет это свойство)*/
                    var border = new Border
                    {
                        Background = backgroundColor,
                        CornerRadius = new CornerRadius(10),
                        Margin = new Thickness(0, 5, 0, 5),
                        Padding = new Thickness(15, 10),
                         Child = child, //new TextBlock
                        // {
                        //     Text = $"|{item["identitycode"]} {item["surname"]} {item["name"]}| \n{item["description"]}\n\t{item["date"]}",
                        //     TextWrapping = TextWrapping.Wrap,
                        //     FontSize = 15
                        // },
                        HorizontalAlignment = horizontalAlignment
                    };

                    // обарачиваем Border в ContentControl для задания свойства Tag
                    var contentControl = new ContentControl
                    {
                        Content = border,
                        Tag = (id: item["id"].ToString(), is_own: item["is_own"].ToString()), // Теперь есть Tag!
                    };
                    
                    // добавляем новый item со стилем в ListBox сообщений
                    MessageContainer.Items.Add(contentControl);

                    // Добавляем обработчик PointerPressed (тапа/клика), если это - сообщение текущего пользователя
                    if(item["is_own"].ToString().Trim().Equals("True"))
                    {
                        border.PointerPressed += (sender, e) =>
                        {
                            //передача аргументов из Tag текущего item на следующую страницу (страницу редактирования сообщения)
                            Client.messageContent.Content = new EditMessage(item["description"].ToString(), item["id"].ToString());
                        };
                    }
                }

                if (MessageContainer.Items.Count - 1 > 0) {
                    MessageContainer.ScrollIntoView(MessageContainer.Items[MessageContainer.Items.Count - 1]); //прокручиваем ListBox с сообщениями в самый конец (после вывода всех сообщений)
                }
            }
        }

        public void Send_Message_Btn(object sender, RoutedEventArgs e)
        {
            if(!string.IsNullOrEmpty(messageText.Text) && string.IsNullOrEmpty(base64Image)) {
                string url = "MessageController/createMessageInChannel<endl>" + IdChannel + "<endl>" + messageText.Text;

                string response = Client.getData(url);

                showMessagesByChannelId();

                messageText.Text = "";
            } else if(!string.IsNullOrEmpty(base64Image))
            {
                string url = "MessageController/createMessageInChannel<endl>" + IdChannel + "<endl>" + messageText.Text + "<img>" + base64Image;

                string response = Client.getData(url);

                showMessagesByChannelId();

                base64Image = string.Empty;
                messageText.Text = string.Empty;
            }
        }

        public void Members_Btn(object sender, RoutedEventArgs e)
        {
            if(isOwn) { // если текущий пользователь - автор данного канала
                Client.messageContent.Content = new EditMembers(IdChannel); // разрешаем открывать панель редактирования участников канала
            } else
            {
                Client.messageContent.Content = new Members(IdChannel); // иначе загружаем страницу с просмотром всех пользователей канала без возможности его редактирования
            }
        }

        public void Pag_Next_Btn(object sender, RoutedEventArgs e)
        {
            page = page + 1;

            showMessagesByChannelId();
        }

        public void Pag_Prev_Btn(object sender, RoutedEventArgs e)
        {
            if(page > 0)
            {
                page = page - 1;

                showMessagesByChannelId();
            }
        }

        public async void On_Open_File_Btn(object sender, RoutedEventArgs e)
        {
            // 1. Получаем доступ к хранилищу (при помощи окна, берем окно через текущую страницу - UserControl)
            var topLevel = TopLevel.GetTopLevel(this);

            // Если окно не пустое
            if(topLevel != null)
            {
                // 2. Используем StorageProvider из TopLevel
                var files = await topLevel.StorageProvider.OpenFilePickerAsync(
                    new FilePickerOpenOptions
                    {
                        Title = "Выберите фото",
                        FileTypeFilter = new[]
                        {
                            FilePickerFileTypes.ImageAll
                        },
                        AllowMultiple = false
                    }
                );

                // если кол-во загруженных файлов > 0
                if(files.Count > 0)
                {
                    // 3. Получаем путь к файлу (убираем приставку "file://")
                    string filePath = files[0].Path.LocalPath;

                    // 4. Читаем всё одной строчкой (проще некуда)
                    byte[] imageBytes = File.ReadAllBytes(filePath);
                    base64Image = Convert.ToBase64String(imageBytes);
                }
            }
        }

        // public TextBlock parseMessage(String text)
        // {
        //     string[] parsedData = text.Split("<img>");
        //     TextBlock child = null;

        //     if(parsedData.Length > 1)
        //     {
        //         TextBlock child = new TextBlock
        //         {
                    
        //         };
        //     }
        // }
    }
}