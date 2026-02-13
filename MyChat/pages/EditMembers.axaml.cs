
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text.Json;
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
    }
}