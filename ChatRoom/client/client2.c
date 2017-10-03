/*************************************************************************
	> File Name: client2.c
	> Author: hepan
	> Mail: hepansos@gmail.com
	> Created Time: 2016年08月04日 星期四 15时30分00秒
 ************************************************************************/

#include <gtk/gtk.h>
#include <gdk/gdk.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <sys/socket.h>
#include <unistd.h>
#include <pthread.h>
#include <arpa/inet.h>

#define PORT    8888
#define MAXLEN 	512

void init_login_widget();
void result_upload();
void result_download();
void make_dialog(GtkWidget *widget,gpointer data);
void user_enroll(GtkWidget *widget,gpointer data);
void enroll(GtkWidget *widget,gpointer data);
void gtk_dialog_destroy(GtkWidget *widget, gpointer data);
void quit(GtkWidget *widget, gpointer data);
void gtk_win_chat(GtkWidget *widget, gpointer data);
void chat_single(GtkWidget *widget, gpointer data);
void chat_group(GtkWidget *widget, gpointer data);
void send_file(GtkWidget *widget, gpointer data);
void modify_passwd(GtkWidget *widget, gpointer data);
void modify_pwd(GtkWidget *widget, gpointer data);
void manager_entrance(GtkWidget *widget, gpointer data);
void file_ok_sel( GtkWidget *w, GtkFileSelection *fs );
void my_err(const char *err_string, int line);
void send_user(GtkWidget *widget, gpointer data);
void send_message(GtkWidget *widget, gpointer data);
void *recv_message_single(void *arg);
void *recv_message_group(void *arg);
void process(void);
void verify_user(void);
void single_history(GtkWidget *widget, gpointer data);
void group_history(GtkWidget *widget, gpointer data);
void clear_single(GtkWidget *widget, gpointer data);
void clear_group(GtkWidget *widget, gpointer data);
void manage_friends(GtkWidget *widget, gpointer data);

void fresh_friend(GtkWidget *widget, gpointer data);
void gtk_friend_destroy(GtkWidget *widget, gpointer data);
void agree_request(GtkWidget *widget, gpointer data);
void disagree_request(GtkWidget *widget, gpointer data);
void ignore_request_request(GtkWidget *widget, gpointer data);
void add_friend(GtkWidget *widget, gpointer data);
void delete_friend(GtkWidget *widget, gpointer data);

void ftp(GtkWidget *widget, gpointer data);
void ftp_upload(GtkWidget *widget, gpointer data);
void upload(GtkWidget *widget, GtkFileSelection *fs);
void ftp_download(GtkWidget *widget,gpointer data);
void download(GtkWidget *widget, gpointer data);
void *recv_filelist(void *arg);
void chatroom_help(GtkWidget *widget, gpointer data);
void chatroom_set(GtkWidget *widget, gpointer data);
void select_font(GtkWidget *widget, gpointer label);
void select_color(GtkWidget *widget, gpointer label);
void set_font(GtkWidget *widget, gpointer label);
void set_color(GtkWidget *widget, gpointer label);
void game(GtkWidget *widget, gpointer data);
void saolei(GtkWidget *widget, gpointer data);
void wuziqi(GtkWidget *widget, gpointer data);
GdkPixbuf *create_pixbuf(const gchar* filename);
GtkWidget *create_button1(void);
GtkWidget *create_button2(void);
GtkWidget *create_button3(void);
GtkWidget *create_button4(void);

//用户结构体
struct usr {
	char username[16];
	char password[17];
}info;

//消息结构体
struct message {
    int flag;
    int statu;
    int sock_fd;
    char username[16];
	char password[17];
    char friend[16];
    char state[5];
    char request[50];
    char msg[MAXLEN];
    char filename[256];
    char file[100];
}user;

int s;
char flag[10]; 		        //判断用户密码是否正确
char pathname[MAXLEN];      //保存全路径名
char delete[MAXLEN];        //存放要删除的文件名
char filename[MAXLEN];      //存放要下载的文件名
time_t nowtime;

GtkTextBuffer *text_buffer;

//将登陆窗体独立成一个结构体
struct login_widget{
        GtkWidget *window;          //主窗体
        GtkWidget *label_tips;      //用于信息提示的标签
        GtkWidget *button_login;    //两个按钮，一个点击后登录，一个点击后退出
        GtkWidget *button_enroll; 
        GtkWidget *button_cancel; 
        GtkWidget *entry_username;  //两个文本框，用于输入用户名和密码
        GtkWidget *entry_pwd;
        GtkWidget *entry_pwd2;
        GtkWidget *entry;
        GtkWidget *entry1;
        GtkWidget *entry2;
        GtkWidget *label_username;  //两个标签用于显示username文本框和password文本框提示
        GtkWidget *label_pwd;
        GtkWidget *text1;
        GtkWidget *text2;
        GtkWidget *label_pwd2;
        GtkWidget *vbox;            //垂直布局盒子，包含以下三个水平布局盒子
        GtkWidget *hbox_username;   //包含用户名提示标签和用户名文本框，下面的HBOX作用类似
        GtkWidget *hbox_pwd;
        GtkWidget *hbox_pwd2;
        GtkWidget *hbox_button;
        GtkWidget *verify_pwd;      //确认密码
}wgt;

//创建自己按钮的函数
GtkWidget *create_button1(void)
{
    GtkWidget *box;
    GtkWidget *image;
    GtkWidget *label;
    GtkWidget *button;

    char *title = "灵动棋";
    image = gtk_image_new_from_file("lingdongqi.jpg");
    label = gtk_label_new(title);
    box = gtk_vbox_new(FALSE,2);
    gtk_container_set_border_width(GTK_CONTAINER(box),5);
    gtk_box_pack_start(GTK_BOX(box),image,FALSE,FALSE,3);
    gtk_box_pack_start(GTK_BOX(box),label,FALSE,FALSE,3);
    gtk_widget_show(image);
    gtk_widget_show(label);
    button = gtk_button_new();
    gtk_container_add(GTK_CONTAINER(button),box);
    gtk_widget_show(box);
    return button;
}

GtkWidget *create_button2(void)
{
    GtkWidget *box;
    GtkWidget *image;
    GtkWidget *label;
    GtkWidget *button;

    char *title = "扫雷";
    image = gtk_image_new_from_file("saolei3.jpg");
    label = gtk_label_new(title);
    box = gtk_vbox_new(FALSE,2);
    gtk_container_set_border_width(GTK_CONTAINER(box),5);
    gtk_box_pack_start(GTK_BOX(box),image,FALSE,FALSE,3);
    gtk_box_pack_start(GTK_BOX(box),label,FALSE,FALSE,3);
    gtk_widget_show(image);
    gtk_widget_show(label);
    button = gtk_button_new();
    gtk_container_add(GTK_CONTAINER(button),box);
    gtk_widget_show(box);
    return button;
}

GtkWidget *create_button3(void)
{
    GtkWidget *box;
    GtkWidget *image;
    GtkWidget *label;
    GtkWidget *button;

    char *title = "贪吃蛇";
    image = gtk_image_new_from_file("6159252dd42a2834b1c7cf5b59b5c9ea15cebf79.jpg");
    label = gtk_label_new(title);
    box = gtk_vbox_new(FALSE,2);
    gtk_container_set_border_width(GTK_CONTAINER(box),5);
    gtk_box_pack_start(GTK_BOX(box),image,FALSE,FALSE,3);
    gtk_box_pack_start(GTK_BOX(box),label,FALSE,FALSE,3);
    gtk_widget_show(image);
    gtk_widget_show(label);
    button = gtk_button_new();
    gtk_container_add(GTK_CONTAINER(button),box);
    gtk_widget_show(box);
    return button;
}

GtkWidget *create_button4(void)
{
    GtkWidget *box;
    GtkWidget *image;
    GtkWidget *label;
    GtkWidget *button;

    char *title = "五子棋";
    image = gtk_image_new_from_file("wuziqi.jpg");
    label = gtk_label_new(title);
    box = gtk_vbox_new(FALSE,2);
    gtk_container_set_border_width(GTK_CONTAINER(box),5);
    gtk_box_pack_start(GTK_BOX(box),image,FALSE,FALSE,3);
    gtk_box_pack_start(GTK_BOX(box),label,FALSE,FALSE,3);
    gtk_widget_show(image);
    gtk_widget_show(label);
    button = gtk_button_new();
    gtk_container_add(GTK_CONTAINER(button),box);
    gtk_widget_show(box);
    return button;
}

void make_dialog(GtkWidget *widget, gpointer window)
{
    GtkWidget *dialog;
    GtkWidget *label;
    GtkWidget *button1;
    GtkWidget *button2;
    GtkWidget *vbox;
    GtkWidget *hbox;
    GdkColor color1;

    gdk_color_parse("red",&color1);
    dialog = gtk_dialog_new();
    //向对话框中加入一个文本标签
    vbox = GTK_DIALOG(dialog)->vbox;
    label = gtk_label_new("确认退出?");
    gtk_widget_modify_fg(label,GTK_STATE_NORMAL,&color1);
    gtk_box_pack_start(GTK_BOX(vbox),label,TRUE,TRUE,30);

    //向对话框中加入两个按钮
    hbox = GTK_DIALOG(dialog)->action_area;
    button1 = gtk_button_new_with_label("是");
    gtk_box_pack_start(GTK_BOX(hbox),button1,FALSE,FALSE,0);
    button2 = gtk_button_new_with_label("否");
    gtk_box_pack_start(GTK_BOX(hbox),button2,FALSE,FALSE,0);
    gtk_widget_show_all(dialog);

    g_signal_connect(G_OBJECT(button1),"clicked",G_CALLBACK(gtk_main_quit),NULL);
    g_signal_connect(G_OBJECT(button2),"clicked",G_CALLBACK(gtk_dialog_destroy),(void *)dialog);
}

void quit(GtkWidget *widget, gpointer data)
{
    memset(user.state,0,sizeof(user.state));
    user.flag = 0;
    strcpy(user.state,"yes");
    printf("***%s\n",user.username);
    send(s,&user,sizeof(user),0);
    gtk_main_quit();
}

void gtk_dialog_destroy(GtkWidget *widget, gpointer data)
{
    gtk_widget_destroy((GtkWidget *)data);
}

void gtk_win_chat(GtkWidget *widget, gpointer data)
{
    GtkWidget *button1;
    GtkWidget *button2;
    GtkWidget *button3;
    GtkWidget *button4;
    GtkWidget *button5;
    GtkWidget *button6;
    GtkWidget *button7;
    GtkWidget *window;
    GtkWidget *image1;
    GtkWidget *image2;
    GtkWidget *sep;
    GtkWidget *label;
    GtkWidget *vbox;
    GtkWidget *hbox;
    GdkColor color1;
    GdkColor color2;
    GdkColor color3;
    GdkColor color4;
    GdkColor color5;
    GdkColor color6;
    GdkColor color7;
    const char *name;
    const char *pwd;

    gdk_color_parse("LawnGreen",&color1);
    gdk_color_parse("blue",&color2);
    gdk_color_parse("brown",&color3);
    gdk_color_parse("red",&color4);
    gdk_color_parse("DarkBlue",&color5);
    gdk_color_parse("black",&color6);
    gdk_color_parse("gray",&color7);

    window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_window_set_position(GTK_WINDOW(window), GTK_WIN_POS_CENTER);
    gtk_window_set_default_size(GTK_WINDOW(window), 240, 470);
    gtk_container_set_border_width(GTK_CONTAINER(window),10);
    gtk_window_set_title(GTK_WINDOW(window),"聊天室");
    gtk_window_set_resizable(GTK_WINDOW(window),TRUE);
    gtk_window_set_icon(GTK_WINDOW(window), create_pixbuf("qq2_2.jpg")); 
    gtk_widget_modify_base(window, GTK_STATE_NORMAL, &color7);
    
    vbox = gtk_vbox_new(TRUE,10);
    hbox = gtk_hbox_new(TRUE,10);
    image1 = gtk_image_new_from_file("qq1_1.jpg");

    //gtk_box_pack_start(GTK_BOX(vbox),image1,FALSE,FALSE,10);
    button1 = gtk_button_new_with_label("私聊");
    gtk_widget_modify_fg(GTK_BIN(button1)->child,GTK_STATE_PRELIGHT,&color1);
    gtk_box_pack_start(GTK_BOX(vbox),button1,FALSE,FALSE,10);
    button2 = gtk_button_new_with_label("群聊");
    gtk_widget_modify_fg(GTK_BIN(button2)->child,GTK_STATE_PRELIGHT,&color2);
    gtk_box_pack_start(GTK_BOX(vbox),button2,FALSE,FALSE,10);
    button3 = gtk_button_new_with_label("FTP");
    gtk_widget_modify_fg(GTK_BIN(button3)->child,GTK_STATE_PRELIGHT,&color3);
    gtk_box_pack_start(GTK_BOX(vbox),button3,FALSE,FALSE,10);
    button4 = gtk_button_new_with_label("修改密码");
    gtk_widget_modify_fg(GTK_BIN(button4)->child,GTK_STATE_PRELIGHT,&color4);
    gtk_box_pack_start(GTK_BOX(vbox),button4,FALSE,FALSE,10);
    button5 = gtk_button_new_with_label("游戏乐园");
    gtk_widget_modify_fg(GTK_BIN(button5)->child,GTK_STATE_PRELIGHT,&color5);
    gtk_box_pack_start(GTK_BOX(vbox),button5,FALSE,FALSE,10);
    sep = gtk_hseparator_new();
    gtk_box_pack_start(GTK_BOX(vbox),sep,FALSE,FALSE,0);
    button7 = gtk_button_new_with_label("系统设置");
    gtk_widget_modify_fg(GTK_BIN(button7)->child,GTK_STATE_PRELIGHT,&color6);
    gtk_box_pack_start(GTK_BOX(hbox),button7,FALSE,FALSE,10);
    button6 = gtk_button_new_with_label("使用说明");
    gtk_widget_modify_fg(GTK_BIN(button6)->child,GTK_STATE_PRELIGHT,&color6);
    gtk_box_pack_end(GTK_BOX(hbox),button6,FALSE,FALSE,10);
    gtk_container_add(GTK_CONTAINER(vbox),hbox);
    label = gtk_label_new("客服: 18709261963\n邮箱: hepan@xiyoulinux.org\nCopyright©2016-?,designed by hp,All Rights Reserved");
    gtk_widget_modify_fg(label,GTK_STATE_NORMAL,&color2);
    gtk_box_pack_start(GTK_BOX(vbox),label,FALSE,FALSE,10);
    gtk_container_add(GTK_CONTAINER(window),vbox);
    /*
    gtk_widget_modify_fg(button1,GTK_STATE_PRELIGHT,&color1);
    gtk_widget_modify_fg(button2,GTK_STATE_PRELIGHT,&color1);
    gtk_widget_modify_fg(button3,GTK_STATE_PRELIGHT,&color1);
    gtk_widget_modify_fg(button4,GTK_STATE_PRELIGHT,&color1);
    gtk_widget_modify_fg(button5,GTK_STATE_PRELIGHT,&color1);
    gtk_widget_modify_fg(button6,GTK_STATE_PRELIGHT,&color1);
    gtk_widget_modify_fg(button7,GTK_STATE_PRELIGHT,&color1);
    */
    //获取用户信息
    struct login_widget *get;
    get = (struct loging_widget *)data;
    
    //获取输入的信息
    name = gtk_entry_get_text(GTK_ENTRY(get->entry_username));
    pwd = gtk_entry_get_text(GTK_ENTRY(get->entry_pwd));
    
    strcpy(user.username,name);
    strcpy(user.password,pwd);

    printf("%s\n",user.username);
    printf("%s\n",user.password);
	user.flag = 2;
    verify_user();
    if( strcmp(flag,"yes") == 0 ) {
        gtk_widget_hide(wgt.window);
        gtk_widget_show_all(window);
    } else if ( strcmp(flag,"no") == 0 ) {
        gtk_widget_modify_fg(get->label_tips,GTK_STATE_NORMAL,&color4);
        gtk_label_set_text(GTK_LABEL(get->label_tips),"用户名或密码错误");
        gtk_entry_set_text(GTK_ENTRY(get->entry_username),"");
        gtk_entry_set_text(GTK_ENTRY(get->entry_pwd),"");
    } else {
        gtk_widget_modify_fg(get->label_tips,GTK_STATE_NORMAL,&color5);
        gtk_label_set_text(GTK_LABEL(get->label_tips),"该用户已登录");
        gtk_entry_set_text(GTK_ENTRY(get->entry_username),"");
        gtk_entry_set_text(GTK_ENTRY(get->entry_pwd),"");
    }

    g_signal_connect(GTK_OBJECT(window),"delete_event",GTK_SIGNAL_FUNC(quit),NULL);
    //g_signal_connect(GTK_OBJECT(window),"destroy",GTK_SIGNAL_FUNC(make_dialog),NULL);
   
    g_signal_connect(GTK_OBJECT(button1),"clicked",GTK_SIGNAL_FUNC(chat_single),NULL);
    g_signal_connect(GTK_OBJECT(button2),"clicked",GTK_SIGNAL_FUNC(chat_group),NULL);
    g_signal_connect(GTK_OBJECT(button3),"clicked",GTK_SIGNAL_FUNC(ftp),NULL);
    g_signal_connect(GTK_OBJECT(button4),"clicked",GTK_SIGNAL_FUNC(modify_passwd),NULL);
    g_signal_connect(GTK_OBJECT(button5),"clicked",GTK_SIGNAL_FUNC(game),NULL);
    g_signal_connect(GTK_OBJECT(button6),"clicked",GTK_SIGNAL_FUNC(chatroom_help),NULL);
    g_signal_connect(GTK_OBJECT(button7),"clicked",GTK_SIGNAL_FUNC(chatroom_set),NULL);
	//g_signal_connect_swapped(G_OBJECT(window),"destroy",G_CALLBACK(gtk_main_quit), NULL);
}

//游戏乐园
void game(GtkWidget *widget, gpointer data)
{
 	GtkWidget *window;
    GtkWidget *hbox1;
    GtkWidget *hbox2;
    GtkWidget *vbox;
    GtkWidget *button1;
    GtkWidget *button2;
    GtkWidget *button3;
    GtkWidget *button4;

    char *title = "游戏乐园";

    window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_window_set_title(GTK_WINDOW(window),title);
    gtk_window_set_position(GTK_WINDOW(window), GTK_WIN_POS_CENTER);
    gtk_container_set_border_width(GTK_CONTAINER(window),20);

    hbox1 = gtk_hbox_new(FALSE,0);
    hbox2 = gtk_hbox_new(FALSE,0);
    vbox = gtk_hbox_new(FALSE,0);

    button1 = create_button1();
    button2 = create_button2();
    button3 = create_button3();
    button4 = create_button4();
    
    gtk_box_pack_start(GTK_BOX(hbox1),button1,FALSE,FALSE,5);
    gtk_box_pack_start(GTK_BOX(hbox1),button2,FALSE,FALSE,5);
    //gtk_box_pack_start(GTK_BOX(hbox2),button3,FALSE,FALSE,5);
    gtk_box_pack_start(GTK_BOX(hbox2),button4,FALSE,FALSE,5);

    gtk_container_add(GTK_CONTAINER(vbox),hbox1);
    gtk_container_add(GTK_CONTAINER(vbox),hbox2);
    gtk_container_add(GTK_CONTAINER(window),vbox);

	//g_signal_connect(G_OBJECT(button1),"clicked",G_CALLBACK(),NULL);
	g_signal_connect(G_OBJECT(button2),"clicked",G_CALLBACK(saolei),NULL);
	//g_signal_connect(G_OBJECT(button3),"clicked",G_CALLBACK(),NULL);
	g_signal_connect(G_OBJECT(button4),"clicked",G_CALLBACK(wuziqi),NULL);
	
    gtk_widget_show_all(window);
}

//俄罗斯方块
void saolei(GtkWidget *widget, gpointer data)
{
    system("./saolei");
}

//五子棋
void wuziqi(GtkWidget *widget, gpointer data)
{
    system("./renju");
}

void select_font(GtkWidget *widget, gpointer label)
{
	GtkResponseType result;
	GtkWidget *dialog = gtk_font_selection_dialog_new("Select Font");
	result = gtk_dialog_run(GTK_DIALOG(dialog));
	if (result == GTK_RESPONSE_OK || result == GTK_RESPONSE_APPLY) {
		PangoFontDescription *font_desc;
		gchar *fontname = gtk_font_selection_dialog_get_font_name(GTK_FONT_SELECTION_DIALOG(dialog));
		font_desc = pango_font_description_from_string(fontname);
		gtk_widget_modify_font(GTK_WIDGET(label), font_desc);
		g_free(fontname);
	}
	gtk_widget_destroy(dialog);
}

void set_font(GtkWidget *widget, gpointer data)
{
	GtkWidget *window;
	GtkWidget *label;GtkWidget *vbox;
	GtkWidget *toolbar;
	GtkToolItem *font;
	
	window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
	gtk_window_set_position(GTK_WINDOW(window), GTK_WIN_POS_CENTER);
	gtk_window_set_default_size(GTK_WINDOW(window), 280, 200);
	gtk_window_set_title(GTK_WINDOW(window), "Font Selection Dialog");
	vbox = gtk_vbox_new(FALSE, 0);
	gtk_container_add(GTK_CONTAINER(window), vbox);
	toolbar = gtk_toolbar_new();
	gtk_toolbar_set_style(GTK_TOOLBAR(toolbar), GTK_TOOLBAR_ICONS);
	gtk_container_set_border_width(GTK_CONTAINER(toolbar), 2);
	font = gtk_tool_button_new_from_stock(GTK_STOCK_SELECT_FONT);
	gtk_toolbar_insert(GTK_TOOLBAR(toolbar), font, -1);
	gtk_box_pack_start(GTK_BOX(vbox), toolbar, FALSE, FALSE, 5);
	label = gtk_label_new("ZetCode");
	gtk_label_set_justify(GTK_LABEL(label), GTK_JUSTIFY_CENTER);
	gtk_box_pack_start(GTK_BOX(vbox), label, TRUE, FALSE, 5);
	g_signal_connect(G_OBJECT(font), "clicked", G_CALLBACK(select_font),label);
	gtk_widget_show_all(window);
}

void set_color(GtkWidget *widget, gpointer data)
{
	GtkWidget *window;
	GtkWidget *label;
	GtkWidget *vbox;
	GtkWidget *toolbar;
	GtkToolItem *font;
	
	window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
	gtk_window_set_position(GTK_WINDOW(window), GTK_WIN_POS_CENTER);
	gtk_window_set_default_size(GTK_WINDOW(window), 280, 200);
	gtk_window_set_title(GTK_WINDOW(window), "Color Selection Dialog");
	vbox = gtk_vbox_new(FALSE, 0);
	gtk_container_add(GTK_CONTAINER(window), vbox);
	toolbar = gtk_toolbar_new();
	gtk_toolbar_set_style(GTK_TOOLBAR(toolbar), GTK_TOOLBAR_ICONS);
	gtk_container_set_border_width(GTK_CONTAINER(toolbar), 2);
	font = gtk_tool_button_new_from_stock(GTK_STOCK_SELECT_COLOR);
	gtk_toolbar_insert(GTK_TOOLBAR(toolbar), font, -1);
	gtk_box_pack_start(GTK_BOX(vbox), toolbar, FALSE, FALSE, 5);
	label = gtk_label_new("ZetCode");
	gtk_label_set_justify(GTK_LABEL(label), GTK_JUSTIFY_CENTER);
	gtk_box_pack_start(GTK_BOX(vbox), label, TRUE, FALSE, 5);
	g_signal_connect(G_OBJECT(font), "clicked", G_CALLBACK(select_font),label);
	gtk_widget_show_all(window);
}

void select_color(GtkWidget *widget, gpointer label)
{
	GtkResponseType result;
	GtkColorSelection *colorsel;
	GtkWidget *dialog = gtk_color_selection_dialog_new("Font Color");
	result = gtk_dialog_run(GTK_DIALOG(dialog));
	if (result == GTK_RESPONSE_OK){
		GdkColor color;
		colorsel = GTK_COLOR_SELECTION(
		GTK_COLOR_SELECTION_DIALOG(dialog)->colorsel);
		gtk_color_selection_get_current_color(colorsel, &color);
		gtk_widget_modify_fg(GTK_WIDGET(label),GTK_STATE_NORMAL,&color);
	}
	gtk_widget_destroy(dialog);
}
//系统设置
void chatroom_set(GtkWidget *widget, gpointer data)
{
	GtkWidget *button1;
	GtkWidget *button2;
    GdkColor color1;

    gdk_color_parse("LawnGreen",&color1);
    
    wgt.window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_window_set_position(GTK_WINDOW(wgt.window), GTK_WIN_POS_CENTER);
    gtk_container_set_border_width(GTK_CONTAINER(wgt.window),10);
    gtk_window_set_default_size(GTK_WINDOW(wgt.window), 200, 300);

    gtk_window_set_title(GTK_WINDOW(wgt.window),"系统设置");
    gtk_window_set_resizable(GTK_WINDOW(wgt.window),TRUE);

    wgt.vbox = gtk_vbox_new(FALSE,20);
   	
    button1 = gtk_button_new_with_label("字体设置");
   	button2 = gtk_button_new_with_label("颜色设置");
    
    gtk_widget_modify_bg(button1, GTK_STATE_PRELIGHT, &color1);
    gtk_widget_modify_bg(button2, GTK_STATE_PRELIGHT, &color1);
    
    gtk_box_pack_start(GTK_BOX(wgt.vbox),button1,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.vbox),button2,FALSE,FALSE,10);

    gtk_container_add(GTK_CONTAINER(wgt.window),wgt.vbox);
	g_signal_connect(G_OBJECT(button1),"clicked",G_CALLBACK(set_font),NULL);
	g_signal_connect(G_OBJECT(button2),"clicked",G_CALLBACK(set_color),NULL);
    gtk_widget_show_all(wgt.window);        
}

//聊天室使用说明
void chatroom_help(GtkWidget *widget, gpointer data)
{
    GtkTextIter start, end;
	GtkWidget *scroll_box;
	GtkWidget *text;
	GtkWidget *button;
    GdkColor color1;
    GdkColor color2;
    GdkColor color3;
    char help[1000];
    int fd;

    gdk_color_parse("green",&color1);
    gdk_color_parse("DarkBlue",&color2);
    gdk_color_parse("gray",&color3);
    
    wgt.window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_window_set_position(GTK_WINDOW(wgt.window), GTK_WIN_POS_CENTER);
    gtk_container_set_border_width(GTK_CONTAINER(wgt.window),10);
    gtk_window_set_default_size(GTK_WINDOW(wgt.window), 400, 500);

    gtk_window_set_title(GTK_WINDOW(wgt.window),"帮助");
    gtk_window_set_resizable(GTK_WINDOW(wgt.window),TRUE);

    wgt.vbox = gtk_vbox_new(FALSE,20);

	scroll_box = gtk_scrolled_window_new(NULL,NULL);
    text = gtk_text_view_new();
    gtk_widget_modify_base(text, GTK_STATE_NORMAL, &color3);
    gtk_widget_modify_text(text, GTK_STATE_NORMAL, &color2);
    gtk_scrolled_window_set_policy(GTK_SCROLLED_WINDOW(scroll_box), GTK_POLICY_AUTOMATIC,GTK_POLICY_AUTOMATIC);
    gtk_container_add(GTK_CONTAINER(scroll_box), text);
    gtk_widget_set_size_request(scroll_box,380,480);

   	button = gtk_button_new_with_label("返回");
    gtk_widget_modify_bg(button, GTK_STATE_PRELIGHT, &color3);
    
    gtk_box_pack_start(GTK_BOX(wgt.vbox),scroll_box,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.vbox),button,FALSE,FALSE,10);
	
    g_signal_connect(G_OBJECT(button),"clicked",G_CALLBACK(gtk_dialog_destroy),(void *)wgt.window);
	
    if ((fd = open("help.dat",O_RDONLY)) < 0) {
		my_err("open",__LINE__);
	}
    read(fd, help, sizeof(help));
	close(fd);
    text_buffer = gtk_text_view_get_buffer(GTK_TEXT_VIEW(text));
    gtk_text_buffer_get_bounds(GTK_TEXT_BUFFER(text_buffer),&start,&end);
    gtk_text_buffer_insert(GTK_TEXT_BUFFER(text_buffer),&end,help,strlen(help));

    gtk_container_add(GTK_CONTAINER(wgt.window),wgt.vbox);
    gtk_widget_show_all(wgt.window);        
}

//私聊
void chat_single(GtkWidget *widget, gpointer data)
{
    GtkWidget *hbox;
    GtkWidget *hbox1;
    GtkWidget *hbox2;
    GtkWidget *hbox3;
    GtkWidget *hbox4;
    GtkWidget *vbox1;
    GtkWidget *vbox2;
    GtkWidget *vbox3;
    GtkWidget *scroll_box1;
    GtkWidget *scroll_box2;
    GtkWidget *image;
    GtkWidget *button_history;
    GtkWidget *button_friend;
    GtkWidget *button_fresh;
    GtkWidget *button_send;
    GtkWidget *button_chat;
    GtkWidget *label1;
    GtkWidget *label2;
    GtkWidget *sep;
    GdkColor color1;
    GdkColor color2;
    GdkColor color3;
    GdkColor color4;
    GdkColor color5;
    GdkColor color6;
    pthread_t thid1;
    char title[20];

    sep = gtk_vseparator_new();
    gdk_color_parse("green",&color1);
    gdk_color_parse("black",&color2);
    gdk_color_parse("gray",&color3);
    gdk_color_parse("Turquoise3",&color4);
    gdk_color_parse("yellow",&color5);
    gdk_color_parse("DarkBlue",&color6);

    wgt.window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_window_set_position(GTK_WINDOW(wgt.window), GTK_WIN_POS_CENTER);
    gtk_window_set_default_size(GTK_WINDOW(wgt.window), 500, 700);

    sprintf(title,"%s%s",user.username,"私聊窗口");
    gtk_window_set_title(GTK_WINDOW(wgt.window),title);
    gtk_window_set_resizable(GTK_WINDOW(wgt.window),TRUE);
    gtk_window_set_icon(GTK_WINDOW(wgt.window), create_pixbuf("qq2_2.jpg")); 

    image = gtk_image_new_from_file("qq2_1.jpg");
    button_history = gtk_button_new_with_label("聊天记录");
    gtk_widget_modify_fg(GTK_BIN(button_history)->child,GTK_STATE_NORMAL,&color2);
    gtk_widget_modify_fg(GTK_BIN(button_history)->child,GTK_STATE_ACTIVE,&color4);
    gtk_widget_modify_fg(GTK_BIN(button_history)->child,GTK_STATE_PRELIGHT,&color1);
    button_friend = gtk_button_new_with_label("好友管理");
    gtk_widget_modify_fg(GTK_BIN(button_friend)->child,GTK_STATE_NORMAL,&color2);
    gtk_widget_modify_fg(GTK_BIN(button_friend)->child,GTK_STATE_ACTIVE,&color4);
    gtk_widget_modify_fg(GTK_BIN(button_friend)->child,GTK_STATE_PRELIGHT,&color1);
    
    label1 = gtk_label_new("好友列表");
    label2 = gtk_label_new("输入聊天好友");
    gtk_widget_modify_bg(label1, GTK_STATE_NORMAL,&color1);
    gtk_widget_modify_bg(label2, GTK_STATE_NORMAL,&color1);
    
    wgt.entry1 = gtk_entry_new_with_max_length(147);
    gtk_widget_modify_text(wgt.entry1, GTK_STATE_NORMAL,&color1);
    gtk_widget_set_size_request(wgt.entry1,340,30);
    wgt.entry2 = gtk_entry_new_with_max_length(15);
    gtk_widget_modify_text(wgt.entry2, GTK_STATE_NORMAL,&color2);
    button_fresh = gtk_button_new_with_label("刷新好友列表");
    gtk_widget_modify_bg(button_fresh, GTK_STATE_PRELIGHT, &color5);
    button_send = gtk_button_new_with_label("发送");
    //gtk_widget_modify_bg(button_send, GTK_STATE_NORMAL, &color3);
    gtk_widget_modify_bg(button_send, GTK_STATE_ACTIVE, &color2);
    gtk_widget_modify_bg(button_send, GTK_STATE_PRELIGHT, &color4);

    button_chat = gtk_button_new_with_label("开始聊天");
    gtk_widget_modify_bg(button_chat, GTK_STATE_PRELIGHT, &color5);
    
    hbox = gtk_hbox_new(FALSE,0);
    vbox1 = gtk_vbox_new(FALSE,0);
    vbox2 = gtk_vbox_new(FALSE,0);
    vbox3 = gtk_vbox_new(FALSE,0);
    hbox1 = gtk_hbox_new(FALSE,0);
    hbox2 = gtk_hbox_new(FALSE,0);
    hbox3 = gtk_hbox_new(FALSE,0);
    hbox4 = gtk_hbox_new(FALSE,0);
    scroll_box1 = gtk_scrolled_window_new(NULL,NULL);
    wgt.text1 = gtk_text_view_new();
    gtk_widget_modify_base(wgt.text1, GTK_STATE_NORMAL, &color3);
    gtk_widget_modify_text(wgt.text1, GTK_STATE_NORMAL, &color2);
    //gtk_editable_set_editable(GTK_EDITABLE(text1),FALSE);
    scroll_box2 = gtk_scrolled_window_new(NULL,NULL);
    wgt.text2 = gtk_text_view_new();
    gtk_widget_modify_base(wgt.text2, GTK_STATE_NORMAL, &color3);
    gtk_widget_modify_text(wgt.text2, GTK_STATE_NORMAL, &color6);
    //gtk_editable_set_editable(GTK_EDITABLE(text2),FALSE);
    
    gtk_scrolled_window_set_policy(GTK_SCROLLED_WINDOW(scroll_box1), GTK_POLICY_AUTOMATIC,GTK_POLICY_AUTOMATIC);
    gtk_scrolled_window_set_policy(GTK_SCROLLED_WINDOW(scroll_box2), GTK_POLICY_AUTOMATIC, GTK_POLICY_AUTOMATIC);
    gtk_container_add(GTK_CONTAINER(scroll_box1), wgt.text1);
    gtk_container_add(GTK_CONTAINER(scroll_box2), wgt.text2);
    gtk_widget_set_size_request(scroll_box1,400,600);
    gtk_widget_set_size_request(scroll_box2,200,500);

    gtk_box_pack_start(GTK_BOX(hbox),vbox1,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(hbox),sep,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(hbox),vbox2,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(hbox1),image,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(hbox1),button_history,FALSE,FALSE,10);
    gtk_box_pack_end(GTK_BOX(hbox1),button_friend,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(hbox2),scroll_box1,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(hbox3),wgt.entry1,FALSE,FALSE,10);
    gtk_box_pack_end(GTK_BOX(hbox3),button_send,FALSE,FALSE,10);

    gtk_box_pack_start(GTK_BOX(hbox4),label1,FALSE,FALSE,10);
    gtk_box_pack_end(GTK_BOX(hbox4),button_fresh,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(vbox3),scroll_box2,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(vbox3),label2,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(vbox3),wgt.entry2,FALSE,FALSE,10);
    gtk_box_pack_end(GTK_BOX(vbox3),button_chat,FALSE,FALSE,10);

    gtk_container_add(GTK_CONTAINER(wgt.window),hbox);
    gtk_container_add(GTK_CONTAINER(vbox1),hbox1);
    gtk_container_add(GTK_CONTAINER(vbox1),hbox2);
    gtk_container_add(GTK_CONTAINER(vbox1),hbox3);
    gtk_container_add(GTK_CONTAINER(vbox2),hbox4);
    gtk_container_add(GTK_CONTAINER(vbox2),vbox3);

    gtk_widget_show_all(wgt.window);
    
    user.flag = 3;
    pthread_create(&thid1, NULL, recv_message_single, NULL);
    
    g_signal_connect(GTK_OBJECT(button_fresh),"clicked",GTK_SIGNAL_FUNC(fresh_friend),&wgt);
    g_signal_connect(GTK_OBJECT(button_send),"clicked",GTK_SIGNAL_FUNC(send_message),&wgt);
    g_signal_connect(GTK_OBJECT(button_chat),"clicked",GTK_SIGNAL_FUNC(send_user),&wgt);
    g_signal_connect(GTK_OBJECT(button_history),"clicked",GTK_SIGNAL_FUNC(single_history),NULL);
    g_signal_connect(GTK_OBJECT(button_friend),"clicked",GTK_SIGNAL_FUNC(manage_friends),NULL);
}

//刷新在线好友
void fresh_friend(GtkWidget *widget, gpointer data)
{
    strcpy(user.request,"fresh");
    if (send(s, &user, sizeof(user), 0) < 0) {
        my_err("send",__LINE__);
    }
}

//私聊记录
void single_history(GtkWidget *widget, gpointer data)
{
	GtkWidget *scroll_box;
	GtkWidget *hbox;
	GtkWidget *text;
	GtkWidget *button1;
	GtkWidget *button2;
    GtkTextIter start, end;
    GdkColor color1;
    GdkColor color2;
    GdkColor color3;
    int fd;

    gdk_color_parse("red",&color1);
    gdk_color_parse("DarkBlue",&color2);
    gdk_color_parse("gray",&color3);

    wgt.window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_window_set_position(GTK_WINDOW(wgt.window), GTK_WIN_POS_CENTER);
    gtk_window_set_default_size(GTK_WINDOW(wgt.window), 400, 600);

    gtk_window_set_title(GTK_WINDOW(wgt.window),"聊天记录");
    gtk_window_set_resizable(GTK_WINDOW(wgt.window),TRUE);

    wgt.vbox = gtk_vbox_new(FALSE,20);
    hbox = gtk_hbox_new(FALSE,20);

	scroll_box = gtk_scrolled_window_new(NULL,NULL);
    text = gtk_text_view_new();
    gtk_widget_modify_base(text, GTK_STATE_NORMAL, &color3);
    gtk_widget_modify_text(text, GTK_STATE_NORMAL, &color2);
    
    gtk_scrolled_window_set_policy(GTK_SCROLLED_WINDOW(scroll_box), GTK_POLICY_AUTOMATIC,GTK_POLICY_AUTOMATIC);
    gtk_container_add(GTK_CONTAINER(scroll_box), text);
    gtk_widget_set_size_request(scroll_box,250,560);

   	button1 = gtk_button_new_with_label("清空");
    button2 = gtk_button_new_with_label("返回");
    
    gtk_widget_modify_bg(button1, GTK_STATE_PRELIGHT, &color1);
    gtk_widget_modify_bg(button2, GTK_STATE_PRELIGHT, &color3);
    
    gtk_box_pack_start(GTK_BOX(wgt.vbox),scroll_box,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(hbox),button1,FALSE,FALSE,10);
    gtk_box_pack_end(GTK_BOX(hbox),button2,FALSE,FALSE,10);

    gtk_container_add(GTK_CONTAINER(wgt.vbox),hbox);
    gtk_container_add(GTK_CONTAINER(wgt.window),wgt.vbox);
    gtk_widget_show_all(wgt.window);  

    text_buffer = gtk_text_view_get_buffer(GTK_TEXT_VIEW(text));
    gtk_text_buffer_get_bounds(GTK_TEXT_BUFFER(text_buffer),&start,&end);
    
    if (access(delete,0) == 0) {
		if ((fd = open(delete,O_RDONLY)) < 0) {
		    //my_err("open",__LINE__);
		}
		while (read(fd, user.msg, sizeof(user.msg)) > 0) {
            //printf("***%s\n",user.msg);
		    gtk_text_buffer_insert(GTK_TEXT_BUFFER(text_buffer),&end,user.msg,strlen(user.msg));
		}
		close(fd);
	}
    
    g_signal_connect(GTK_OBJECT(button1),"clicked",GTK_SIGNAL_FUNC(clear_single),NULL);
    g_signal_connect(GTK_OBJECT(button2),"clicked",GTK_SIGNAL_FUNC(gtk_dialog_destroy),(void *)wgt.window);
}

//清除私聊记录
void clear_single(GtkWidget *widget, gpointer data)
{
    gtk_text_buffer_set_text(text_buffer,"",0);
    if (unlink(delete) < 0) {
        perror("unlink");
    }
}

//好友管理
void manage_friends(GtkWidget *widget, gpointer data)
{
	GtkWidget *scroll_box;
	GtkWidget *button1;
	GtkWidget *button2;
	GtkWidget *button3;
	GtkWidget *button4;
	GtkWidget *button5;
	GtkWidget *hbox1;
	GtkWidget *hbox2;
	GtkWidget *hbox3;
	GtkWidget *label;

    pthread_t thid;

    wgt.window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_window_set_position(GTK_WINDOW(wgt.window), GTK_WIN_POS_CENTER);
    gtk_window_set_default_size(GTK_WINDOW(wgt.window), 230, 100);
    gtk_container_set_border_width(GTK_CONTAINER(wgt.window),10);

    gtk_window_set_title(GTK_WINDOW(wgt.window),"好友管理");
    gtk_window_set_resizable(GTK_WINDOW(wgt.window),TRUE);

    label = gtk_label_new("好友验证消息");
    wgt.entry1 = gtk_entry_new_with_max_length(16);
    wgt.entry2 = gtk_entry_new_with_max_length(16);
    wgt.vbox = gtk_vbox_new(FALSE,20);
    hbox1 = gtk_hbox_new(FALSE,20);
    hbox2 = gtk_hbox_new(FALSE,20);
    hbox3 = gtk_hbox_new(FALSE,20);

	scroll_box = gtk_scrolled_window_new(NULL,NULL);
    wgt.text1 = gtk_text_view_new();
    gtk_scrolled_window_set_policy(GTK_SCROLLED_WINDOW(scroll_box), GTK_POLICY_AUTOMATIC,GTK_POLICY_AUTOMATIC);
    gtk_container_add(GTK_CONTAINER(scroll_box), wgt.text1);
    gtk_widget_set_size_request(scroll_box,200,100);

   	button1 = gtk_button_new_with_label("同意");
    button2 = gtk_button_new_with_label("忽略");
    button3 = gtk_button_new_with_label("添加");   
    button4 = gtk_button_new_with_label("删除");
    button5 = gtk_button_new_with_label("不同意");
    
    gtk_box_pack_start(GTK_BOX(wgt.vbox),label,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.vbox),scroll_box,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(hbox1),button1,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(hbox1),button5,FALSE,FALSE,10);
    gtk_box_pack_end(GTK_BOX(hbox1),button2,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(hbox2),wgt.entry1,FALSE,FALSE,10);
    gtk_box_pack_end(GTK_BOX(hbox2),button3,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(hbox3),wgt.entry2,FALSE,FALSE,10);
    gtk_box_pack_end(GTK_BOX(hbox3),button4,FALSE,FALSE,10);

    gtk_container_add(GTK_CONTAINER(wgt.vbox),hbox1);
    gtk_container_add(GTK_CONTAINER(wgt.vbox),hbox2);
    gtk_container_add(GTK_CONTAINER(wgt.vbox),hbox3);
    gtk_container_add(GTK_CONTAINER(wgt.window),wgt.vbox);
    gtk_widget_show_all(wgt.window);        
    
    //g_signal_connect(GTK_OBJECT(button1),"clicked",GTK_SIGNAL_FUNC(agree_request),NULL);
    //g_signal_connect(GTK_OBJECT(button5),"clicked",GTK_SIGNAL_FUNC(disagree_request),NULL);
    //g_signal_connect(GTK_OBJECT(button2),"clicked",GTK_SIGNAL_FUNC(ignore_request),NULL);
    //g_signal_connect(GTK_OBJECT(button3),"clicked",GTK_SIGNAL_FUNC(add_friend),&wgt);
    //g_signal_connect(GTK_OBJECT(button4),"clicked",GTK_SIGNAL_FUNC(delete_friend),&wgt);
    g_signal_connect(GTK_OBJECT(wgt.window),"destroy",GTK_SIGNAL_FUNC(gtk_dialog_destroy),(void *)wgt.window);
}

void gtk_friend_destroy(GtkWidget *widget, gpointer data)
{
    gtk_widget_destroy((GtkWidget *)data);
}

//同意请求
void agree_request(GtkWidget *widget, gpointer data)
{
    strcpy(user.msg,"agree");
	if (send(s, &user, sizeof(user), 0) < 0) {
    	my_err("send",__LINE__);
	}
}

//不同意请求
void disagree_request(GtkWidget *widget, gpointer data)
{
    strcpy(user.msg,"disagree");
	if (send(s, &user, sizeof(user), 0) < 0) {
    	my_err("send",__LINE__);
	}
}

//忽略请求
void ignore_request_request(GtkWidget *widget, gpointer data)
{
    
}

//添加好友
void add_friend(GtkWidget *widget, gpointer data)
{
	GtkTextIter start, end;
    int fd;
    char buf[30];
    const char *friend;

    user.flag = 5;
    struct login_widget *get;
    get = (struct loging_widget *)data;
    
    //获取输入的信息
    friend = gtk_entry_get_text(GTK_ENTRY(get->entry1));
    strcpy(user.friend,friend);

	if (send(s, &user, sizeof(user), 0) < 0) {
    	my_err("send",__LINE__);
	}

    while (1) {
        
        memset(user.request, 0, sizeof(user.request));       

        recv(s, &user, sizeof(user), 0);
        printf("***%s\n",user.request);

        if (strcmp(user.request,"yes") == 0) {
            strcpy(buf,"添加成功 !\n");
        } else {
            strcpy(buf,"添加失败, 查无此人!\n");
        }
        text_buffer = gtk_text_view_get_buffer(GTK_TEXT_VIEW(wgt.text1));
        gtk_text_buffer_get_bounds(GTK_TEXT_BUFFER(text_buffer),&start,&end);
        gtk_text_buffer_insert(GTK_TEXT_BUFFER(text_buffer),&end,buf,sizeof(buf));
        //write(1, buf, sizeof(buf));
    }
}

//删除好友
void delete_friend(GtkWidget *widget, gpointer data)
{
    const char *friend;
    struct login_widget *get;
    get = (struct loging_widget *)data;
    
    //获取输入的信息
    friend = gtk_entry_get_text(GTK_ENTRY(get->entry2));
    strcpy(user.friend,friend);

    /*
	if (send(s, &user, sizeof(user), 0) < 0) {
    	my_err("send",__LINE__);
    }
    */
}

//群聊
void chat_group(GtkWidget *widget, gpointer data)
{
    GtkWidget *hbox;
    GtkWidget *hbox1;
    GtkWidget *hbox2;
    GtkWidget *hbox3;
    GtkWidget *vbox1;
    GtkWidget *vbox2;
    GtkWidget *scroll_box1;
    GtkWidget *scroll_box2;
    GtkWidget *image;
    GtkWidget *button_history;
    GtkWidget *button_fresh;
    GtkWidget *button_send;
    GtkWidget *sep;
    GdkColor color1;
    GdkColor color2;
    GdkColor color3;
    GdkColor color4;
    GdkColor color5;
    GdkColor color6;
    pthread_t thid2;
    
    sep = gtk_vseparator_new();
    gdk_color_parse("green",&color1);
    gdk_color_parse("black",&color2);
    gdk_color_parse("gray",&color3);
    gdk_color_parse("Turquoise3",&color4);
    gdk_color_parse("yellow",&color5);
    gdk_color_parse("DarkBlue",&color6);

    wgt.window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_window_set_position(GTK_WINDOW(wgt.window), GTK_WIN_POS_CENTER);
    gtk_window_set_default_size(GTK_WINDOW(wgt.window), 500, 700);

    gtk_window_set_title(GTK_WINDOW(wgt.window),"群聊窗口");
    gtk_window_set_resizable(GTK_WINDOW(wgt.window),TRUE);
    gtk_window_set_icon(GTK_WINDOW(wgt.window), create_pixbuf("qq2_2.jpg"));

    image = gtk_image_new_from_file("qq2_1.png");
    button_history = gtk_button_new_with_label("聊天记录");
    gtk_widget_modify_fg(GTK_BIN(button_history)->child,GTK_STATE_NORMAL,&color2);
    gtk_widget_modify_fg(GTK_BIN(button_history)->child,GTK_STATE_ACTIVE,&color4);
    gtk_widget_modify_fg(GTK_BIN(button_history)->child,GTK_STATE_PRELIGHT,&color1);

    wgt.entry1 = gtk_entry_new_with_max_length(147);
    gtk_widget_modify_text(wgt.entry1, GTK_STATE_NORMAL,&color1);
    gtk_widget_set_size_request(wgt.entry1,340,30);
    button_fresh = gtk_button_new_with_label("刷新在线用户");
    gtk_widget_modify_bg(button_fresh, GTK_STATE_PRELIGHT, &color5);
    button_send = gtk_button_new_with_label("发送");
    //gtk_widget_modify_bg(button_send, GTK_STATE_NORMAL, &color3);
    gtk_widget_modify_bg(button_send, GTK_STATE_ACTIVE, &color2);
    gtk_widget_modify_bg(button_send, GTK_STATE_PRELIGHT, &color4);

    hbox = gtk_hbox_new(FALSE,0);
    vbox1 = gtk_vbox_new(FALSE,0);
    vbox2 = gtk_vbox_new(FALSE,0);
    hbox1 = gtk_hbox_new(FALSE,0);
    hbox2 = gtk_hbox_new(FALSE,0);
    hbox3 = gtk_hbox_new(FALSE,0);
    scroll_box1 = gtk_scrolled_window_new(NULL,NULL);
    wgt.text1 = gtk_text_view_new();
    gtk_widget_modify_base(wgt.text1, GTK_STATE_NORMAL, &color3);
    gtk_widget_modify_text(wgt.text1, GTK_STATE_NORMAL, &color2);
    //gtk_editable_set_editable(GTK_EDITABLE(text1),FALSE);
    scroll_box2 = gtk_scrolled_window_new(NULL,NULL);
    wgt.text2 = gtk_text_view_new();
    gtk_widget_modify_base(wgt.text2, GTK_STATE_NORMAL, &color3);
    gtk_widget_modify_text(wgt.text2, GTK_STATE_NORMAL, &color6);
    //gtk_editable_set_editable(GTK_EDITABLE(text2),FALSE);
    
    gtk_scrolled_window_set_policy(GTK_SCROLLED_WINDOW(scroll_box1), GTK_POLICY_AUTOMATIC,GTK_POLICY_AUTOMATIC);
    gtk_scrolled_window_set_policy(GTK_SCROLLED_WINDOW(scroll_box2), GTK_POLICY_AUTOMATIC, GTK_POLICY_AUTOMATIC);
    gtk_container_add(GTK_CONTAINER(scroll_box1), wgt.text1);
    gtk_container_add(GTK_CONTAINER(scroll_box2), wgt.text2);
    gtk_widget_set_size_request(scroll_box1,400,600);
    gtk_widget_set_size_request(scroll_box2,200,600);

    gtk_box_pack_start(GTK_BOX(hbox),vbox1,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(hbox),sep,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(hbox),vbox2,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(hbox1),image,FALSE,FALSE,10);
    gtk_box_pack_end(GTK_BOX(hbox1),button_history,FALSE,FALSE,10);

    gtk_box_pack_start(GTK_BOX(hbox2),scroll_box1,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(hbox3),wgt.entry1,FALSE,FALSE,10);
    gtk_box_pack_end(GTK_BOX(hbox3),button_send,FALSE,FALSE,10);

    gtk_box_pack_start(GTK_BOX(vbox2),button_fresh,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(vbox2),scroll_box2,FALSE,FALSE,10);

    gtk_container_add(GTK_CONTAINER(wgt.window),hbox);
    gtk_container_add(GTK_CONTAINER(vbox1),hbox1);
    gtk_container_add(GTK_CONTAINER(vbox1),hbox2);
    gtk_container_add(GTK_CONTAINER(vbox1),hbox3);

    gtk_widget_show_all(wgt.window);
    
    user.flag = 4;
    pthread_create(&thid2, NULL, recv_message_group, NULL);
    
    //g_signal_connect(GTK_OBJECT(button_chat),"clicked",GTK_SIGNAL_FUNC(),NULL);
    g_signal_connect(GTK_OBJECT(button_fresh),"clicked",GTK_SIGNAL_FUNC(fresh_friend),&wgt);
    g_signal_connect(GTK_OBJECT(button_send),"clicked",GTK_SIGNAL_FUNC(send_message),&wgt);
    g_signal_connect(GTK_OBJECT(button_history),"clicked",GTK_SIGNAL_FUNC(group_history),NULL);
}

//群聊天记录
void group_history(GtkWidget *widget, gpointer data)
{
	GtkWidget *scroll_box;
	GtkWidget *hbox;
	GtkWidget *text;
	GtkWidget *button1;
	GtkWidget *button2;
    GtkTextIter start, end;
    GdkColor color1;
    GdkColor color2;
    GdkColor color3;
    int fd;

    gdk_color_parse("red",&color1);
    gdk_color_parse("DarkBlue",&color2);
    gdk_color_parse("gray",&color3);

    wgt.window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_window_set_position(GTK_WINDOW(wgt.window), GTK_WIN_POS_CENTER);
    gtk_window_set_default_size(GTK_WINDOW(wgt.window), 400, 600);

    gtk_window_set_title(GTK_WINDOW(wgt.window),"聊天记录");
    gtk_window_set_resizable(GTK_WINDOW(wgt.window),TRUE);

    wgt.vbox = gtk_vbox_new(FALSE,20);
    hbox = gtk_hbox_new(FALSE,20);

	scroll_box = gtk_scrolled_window_new(NULL,NULL);
    text = gtk_text_view_new();
    gtk_widget_modify_base(text, GTK_STATE_NORMAL, &color3);
    gtk_widget_modify_text(text, GTK_STATE_NORMAL, &color2);
    
    gtk_scrolled_window_set_policy(GTK_SCROLLED_WINDOW(scroll_box), GTK_POLICY_AUTOMATIC,GTK_POLICY_AUTOMATIC);
    gtk_container_add(GTK_CONTAINER(scroll_box), text);
    gtk_widget_set_size_request(scroll_box,250,560);

   	button1 = gtk_button_new_with_label("清空");
    button2 = gtk_button_new_with_label("返回");
    
    gtk_widget_modify_bg(button1, GTK_STATE_PRELIGHT, &color1);
    gtk_widget_modify_bg(button2, GTK_STATE_PRELIGHT, &color3);
    
    gtk_box_pack_start(GTK_BOX(wgt.vbox),scroll_box,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(hbox),button1,FALSE,FALSE,10);
    gtk_box_pack_end(GTK_BOX(hbox),button2,FALSE,FALSE,10);

    gtk_container_add(GTK_CONTAINER(wgt.vbox),hbox);
    gtk_container_add(GTK_CONTAINER(wgt.window),wgt.vbox);
    gtk_widget_show_all(wgt.window);  

    text_buffer = gtk_text_view_get_buffer(GTK_TEXT_VIEW(text));
    gtk_text_buffer_get_bounds(GTK_TEXT_BUFFER(text_buffer),&start,&end);
	if (access("group.dat",0) == 0) {
		if ((fd = open("group.dat",O_RDONLY)) < 0) {
		    //my_err("open",__LINE__);
		}
		while (read(fd, &user, sizeof(user)) > 0) {
		    gtk_text_buffer_insert(GTK_TEXT_BUFFER(text_buffer),&end,user.msg,strlen(user.msg));
		}
		close(fd);
	}
    g_signal_connect(GTK_OBJECT(button1),"clicked",GTK_SIGNAL_FUNC(clear_group),NULL);
    g_signal_connect(GTK_OBJECT(button2),"clicked",GTK_SIGNAL_FUNC(gtk_dialog_destroy),(void *)wgt.window);
}

//清除群聊天记录
void clear_group(GtkWidget *widget, gpointer data)
{
    gtk_text_buffer_set_text(text_buffer,"",0);
    if (unlink("group.dat") < 0) {
        perror("unlink");
    }
}

void ftp(GtkWidget *widget, gpointer data)
{
	GtkWidget *button1;
	GtkWidget *button2;
	GtkWidget *hbox;
	GtkWidget *vbox;
	GtkWidget *image;
    GdkColor color1;
    GdkColor color2;
	
    gdk_color_parse("LawnGreen",&color1);
    gdk_color_parse("DarkBlue",&color2);
    
    image = gtk_image_new_from_file("file1.jpg");
	wgt.window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_window_set_position(GTK_WINDOW(wgt.window), GTK_WIN_POS_CENTER);
    gtk_window_set_default_size(GTK_WINDOW(wgt.window), 150, 70);
    gtk_container_set_border_width(GTK_CONTAINER(wgt.window),10);
    
	gtk_window_set_title(GTK_WINDOW(wgt.window),"FTP");
    gtk_window_set_resizable(GTK_WINDOW(wgt.window),TRUE);
    
    hbox = gtk_hbox_new(FALSE,20);
    vbox = gtk_vbox_new(FALSE,20);
   	
    button1 = gtk_button_new_with_label("上传");
    button2 = gtk_button_new_with_label("下载");
    
    gtk_widget_modify_bg(button1, GTK_STATE_PRELIGHT, &color1);
    gtk_widget_modify_bg(button2, GTK_STATE_PRELIGHT, &color2);
    
    gtk_box_pack_start(GTK_BOX(vbox),image,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(hbox),button1,FALSE,FALSE,10);
    gtk_box_pack_end(GTK_BOX(hbox),button2,FALSE,FALSE,10);
    
    gtk_container_add(GTK_CONTAINER(vbox),hbox);
    gtk_container_add(GTK_CONTAINER(wgt.window),vbox);
    gtk_widget_show_all(wgt.window); 
    
    g_signal_connect(GTK_OBJECT(button1),"clicked",GTK_SIGNAL_FUNC(ftp_upload),(void *)wgt.window);
    g_signal_connect(GTK_OBJECT(button2),"clicked",GTK_SIGNAL_FUNC(ftp_download),(void *)wgt.window);
}

void ftp_upload(GtkWidget *widget, gpointer data)
{
	GtkWidget *scroll_box;
	GtkWidget *hbox;
	GtkWidget *button1;
	GtkWidget *button2;
	GtkWidget *button3;
	GtkWidget *tmp;
    GdkColor color1;
    GdkColor color2;
    GdkColor color3;

    gdk_color_parse("DarkBlue",&color1);
    gdk_color_parse("LawnGreen",&color2);
    gdk_color_parse("gray",&color3);
    
    tmp = (GtkWidget *)data;
    gtk_widget_hide(tmp);

    wgt.window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_window_set_position(GTK_WINDOW(wgt.window), GTK_WIN_POS_CENTER);
    gtk_window_set_default_size(GTK_WINDOW(wgt.window), 400, 200);
    gtk_container_set_border_width(GTK_CONTAINER(wgt.window),10);

    gtk_window_set_title(GTK_WINDOW(wgt.window),"文件上传");
    gtk_window_set_resizable(GTK_WINDOW(wgt.window),TRUE);

    wgt.vbox = gtk_vbox_new(FALSE,20);
    hbox = gtk_hbox_new(FALSE,20);

	scroll_box = gtk_scrolled_window_new(NULL,NULL);
    wgt.text1 = gtk_text_view_new();
    gtk_widget_modify_base(wgt.text1, GTK_STATE_NORMAL, &color3);
    gtk_widget_modify_text(wgt.text1, GTK_STATE_NORMAL, &color1);
    gtk_scrolled_window_set_policy(GTK_SCROLLED_WINDOW(scroll_box), GTK_POLICY_AUTOMATIC,GTK_POLICY_AUTOMATIC);
    gtk_container_add(GTK_CONTAINER(scroll_box), wgt.text1);
    gtk_widget_set_size_request(scroll_box,390,100);

   	button1 = gtk_button_new_with_label("选择文件");
    button2 = gtk_button_new_with_label("确认上传");
    button3 = gtk_button_new_with_label("取消上传");   
    
    gtk_widget_modify_bg(button1, GTK_STATE_PRELIGHT, &color1);
    gtk_widget_modify_bg(button2, GTK_STATE_PRELIGHT, &color2);
    gtk_widget_modify_bg(button3, GTK_STATE_PRELIGHT, &color3);
    
    gtk_box_pack_start(GTK_BOX(wgt.vbox),button1,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.vbox),scroll_box,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(hbox),button2,FALSE,FALSE,10);
    gtk_box_pack_end(GTK_BOX(hbox),button3,FALSE,FALSE,10);

    gtk_container_add(GTK_CONTAINER(wgt.window),wgt.vbox);
    gtk_container_add(GTK_CONTAINER(wgt.vbox),hbox);
    gtk_widget_show_all(wgt.window);      
    
    g_signal_connect(GTK_OBJECT(button1),"clicked",GTK_SIGNAL_FUNC(send_file),NULL);
    g_signal_connect(GTK_OBJECT(button2),"clicked",GTK_SIGNAL_FUNC(upload),NULL);
    g_signal_connect(GTK_OBJECT(button3),"clicked",GTK_SIGNAL_FUNC(gtk_dialog_destroy),(void *)wgt.window);
}

void upload(GtkWidget *widget, GtkFileSelection *fs)
{
    char tmp[MAXLEN];
    char name[256];
    int i, j, fd;

    //从路径中解析出文件名
	for (i=0, j=0; i<strlen(pathname); i++) {
        if (pathname[i] == '/') {
            j = 0;
            continue;
        }
        name[j++] = pathname[i];
    }
    name[j] = '\0';
    printf("%s\n",pathname);
    printf("%s\n",name);
    
    if ((fd = open(pathname,O_RDONLY)) < 0) {
        my_err("open",__LINE__);
    }
    strcpy(user.filename,name);

    user.flag = 6;
    printf(">>>%s\n",user.filename);
    while (read(fd, user.file, 2) > 0) {
        //printf("***%s\n",user.filename);
        send(s,&user,sizeof(user),0);
        memset(user.file,0,sizeof(user.file));
    }
    close(fd);
    result_upload();
}

void result_upload()
{
    GtkWidget *button;
    GtkWidget *label;
    GdkColor color1;
    GdkColor color2;

    gdk_color_parse("DarkBlue",&color1);
    gdk_color_parse("LawnGreen",&color2);

    wgt.window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_window_set_position(GTK_WINDOW(wgt.window), GTK_WIN_POS_CENTER);
    gtk_window_set_default_size(GTK_WINDOW(wgt.window), 200, 150);
    gtk_container_set_border_width(GTK_CONTAINER(wgt.window),10);
    gtk_window_set_title(GTK_WINDOW(wgt.window),"文件传输结果");

    button = gtk_button_new_with_label("确定");
    gtk_widget_modify_bg(button, GTK_STATE_PRELIGHT, &color2);
    label = gtk_label_new("文件上传成功!");
    gtk_widget_modify_bg(label, GTK_STATE_NORMAL, &color1);
    
    wgt.vbox = gtk_vbox_new(FALSE,20);
    
    gtk_box_pack_start(GTK_BOX(wgt.vbox),label,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.vbox),button,TRUE,FALSE,10);
    
    g_signal_connect(G_OBJECT(button),"clicked",G_CALLBACK(gtk_dialog_destroy),(void *)wgt.window);

    gtk_container_add(GTK_CONTAINER(wgt.window),wgt.vbox);
    gtk_widget_show_all(wgt.window);
}

void ftp_download(GtkWidget *widget,gpointer data)
{
	GtkWidget *scroll_box;
	GtkWidget *hbox;
	GtkWidget *button1;
	GtkWidget *button2;
	GtkWidget *tmp;
	GtkWidget *label1;
	GtkWidget *label2;
    GdkColor color1;
    GdkColor color2;
    GdkColor color3;
    pthread_t thid;

    gdk_color_parse("DarkBlue",&color1);
    gdk_color_parse("LawnGreen",&color2);
    gdk_color_parse("gray",&color3);

    tmp = (GtkWidget *)data;
    gtk_widget_hide(tmp);

    wgt.window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_window_set_position(GTK_WINDOW(wgt.window), GTK_WIN_POS_CENTER);
    gtk_window_set_default_size(GTK_WINDOW(wgt.window), 400, 200);
    gtk_container_set_border_width(GTK_CONTAINER(wgt.window),10);

    gtk_window_set_title(GTK_WINDOW(wgt.window),"文件下载");
    gtk_window_set_resizable(GTK_WINDOW(wgt.window),TRUE);

    label1 = gtk_label_new("下载文件列表");
    label2 = gtk_label_new("请输入要下载的文件名");
    wgt.entry = gtk_entry_new_with_max_length(100);
    wgt.vbox = gtk_vbox_new(FALSE,20);
    hbox = gtk_hbox_new(FALSE,20);

	scroll_box = gtk_scrolled_window_new(NULL,NULL);
    wgt.text1 = gtk_text_view_new();
    gtk_widget_modify_base(wgt.text1, GTK_STATE_NORMAL, &color3);
    gtk_widget_modify_text(wgt.text1, GTK_STATE_NORMAL, &color1);
    gtk_scrolled_window_set_policy(GTK_SCROLLED_WINDOW(scroll_box), GTK_POLICY_AUTOMATIC,GTK_POLICY_AUTOMATIC);
    gtk_container_add(GTK_CONTAINER(scroll_box), wgt.text1);
    gtk_widget_set_size_request(scroll_box,390,180);
    gtk_widget_set_size_request(wgt.entry,340,30);

    button1 = gtk_button_new_with_label("确认下载");
    button2= gtk_button_new_with_label("取消下载");   
    
    gtk_widget_modify_bg(button1, GTK_STATE_PRELIGHT, &color2);
    gtk_widget_modify_bg(button2, GTK_STATE_PRELIGHT, &color3);
    
    gtk_box_pack_start(GTK_BOX(wgt.vbox),label1,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.vbox),scroll_box,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.vbox),label2,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.vbox),wgt.entry,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(hbox),button1,FALSE,FALSE,10);
    gtk_box_pack_end(GTK_BOX(hbox),button2,FALSE,FALSE,10);

    gtk_container_add(GTK_CONTAINER(wgt.window),wgt.vbox);
    gtk_container_add(GTK_CONTAINER(wgt.vbox),hbox);
    gtk_widget_show_all(wgt.window);      
    
    user.flag = 8;
    printf("%s\n",user.username);
    send(s,&user,sizeof(user),0);

    pthread_create(&thid, NULL, recv_filelist, NULL);
    
    g_signal_connect(GTK_OBJECT(button1),"clicked",GTK_SIGNAL_FUNC(download),&wgt);
    g_signal_connect(GTK_OBJECT(button2),"clicked",GTK_SIGNAL_FUNC(gtk_dialog_destroy),(void *)wgt.window);   
}

void download(GtkWidget *widget, gpointer data)
{
    int fd, t;
    const char *file;
    struct login_widget *get;
    get = (struct loging_widget *)data;

    file = gtk_entry_get_text(GTK_ENTRY(get->entry));
    strcpy(filename,file);
    user.flag = 7;
    strcpy(user.filename,filename);
    send(s,&user,sizeof(user),0);
    while (1) {
        memset(user.file,0,sizeof(user.file));
        recv(s,&user,sizeof(user),0);
        if ((fd = open(filename,O_RDWR|O_CREAT|O_APPEND,0666)) < 0) {
            my_err("open",__LINE__);
        }
        if (write(fd,user.file,2) <= 0) {
            close(fd);
            break;
        }
        close(fd);
    }
    printf("______________________------->>>>>>\n");
    result_download();
}

void result_download()
{
    GtkWidget *button;
    GtkWidget *label;
    GdkColor color1;
    GdkColor color2;

    gdk_color_parse("DarkBlue",&color1);
    gdk_color_parse("LawnGreen",&color2);

    wgt.window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_window_set_position(GTK_WINDOW(wgt.window), GTK_WIN_POS_CENTER);
    gtk_window_set_default_size(GTK_WINDOW(wgt.window), 200, 150);
    gtk_container_set_border_width(GTK_CONTAINER(wgt.window),10);
    gtk_window_set_title(GTK_WINDOW(wgt.window),"文件传输结果");

    button = gtk_button_new_with_label("确定");
    gtk_widget_modify_bg(button, GTK_STATE_PRELIGHT, &color2);
    label = gtk_label_new("文件下载成功!");
    gtk_widget_modify_bg(label, GTK_STATE_NORMAL, &color1);
    
    wgt.vbox = gtk_vbox_new(FALSE,20);
    
    gtk_box_pack_start(GTK_BOX(wgt.vbox),label,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.vbox),button,TRUE,FALSE,10);
    
    g_signal_connect(G_OBJECT(button),"clicked",G_CALLBACK(gtk_dialog_destroy),(void *)wgt.window);

    gtk_container_add(GTK_CONTAINER(wgt.window),wgt.vbox);
    gtk_widget_show_all(wgt.window);
}

void file_ok_sel( GtkWidget *w, GtkFileSelection *fs )
{
    GtkTextIter start, end;
    const char *buf;
	//g_print ("%s\n", gtk_file_selection_get_filename(GTK_FILE_SELECTION (fs)));
	
    buf = gtk_file_selection_get_filename(GTK_FILE_SELECTION (fs));
	strcpy(pathname,buf);
    text_buffer = gtk_text_view_get_buffer(GTK_TEXT_VIEW(wgt.text1));
    gtk_text_buffer_get_bounds(GTK_TEXT_BUFFER(text_buffer),&start,&end);
    gtk_text_buffer_insert(GTK_TEXT_BUFFER(text_buffer),&end,buf,strlen(buf));
    
    gtk_widget_destroy((GtkWidget *)fs);
}

void send_file(GtkWidget *widget, gpointer data)
{
    GtkWidget *filew;

	//创建一个新的文件选择构件 
	filew = gtk_file_selection_new ("文件选择");
	
    //为 ok_button 按钮设置回调函数,连接到 file_ok_sel function 函数
	g_signal_connect (G_OBJECT (GTK_FILE_SELECTION (filew)->ok_button),"clicked",G_CALLBACK(file_ok_sel), (void *)filew);

    //为 cancel_button 设置回调函数,销毁构件
	//g_signal_connect_swapped(G_OBJECT(GTK_FILE_SELECTION(filew)->cancel_button),"clicked",G_CALLBACK (gtk_dialog_destroy), (void *)filew);
	g_signal_connect(G_OBJECT(GTK_FILE_SELECTION(filew)->cancel_button),"clicked",G_CALLBACK (gtk_dialog_destroy), (void *)filew);
	
    //设置文件名,比如这个一个文件保存对话框,我们给了一个缺省文件名
	//gtk_file_selection_set_filename (GTK_FILE_SELECTION(filew),"qq1_1.jpg");
	gtk_widget_show (filew);
    //g_signal_connect(GTK_OBJECT(filew),"delete_event",GTK_SIGNAL_FUNC(gtk_dialog_destroy), (void *)filew);
	g_signal_connect (G_OBJECT (filew), "destroy",G_CALLBACK (gtk_dialog_destroy), (void *)filew);
}

void modify_passwd(GtkWidget *widget, gpointer data)
{
    GdkColor color1;
    GdkColor color2;
    GdkColor color3;
    gdk_color_parse("red",&color1);
    gdk_color_parse("LawnGreen",&color2);
    gdk_color_parse("gray",&color3);
    
    wgt.window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_window_set_position(GTK_WINDOW(wgt.window), GTK_WIN_POS_CENTER);
    wgt.label_tips = gtk_label_new("修改密码");
    gtk_widget_modify_fg(wgt.label_tips, GTK_STATE_NORMAL, &color1);
    
    wgt.button_enroll = gtk_button_new_with_label("确认修改");
    gtk_widget_modify_bg(wgt.button_enroll, GTK_STATE_PRELIGHT, &color2);
    wgt.button_cancel = gtk_button_new_with_label("取消修改");
    gtk_widget_modify_bg(wgt.button_cancel, GTK_STATE_PRELIGHT, &color3);
   
    wgt.entry_pwd = gtk_entry_new_with_max_length(17);
    wgt.entry_pwd2 = gtk_entry_new_with_max_length(17);
    wgt.verify_pwd = gtk_entry_new_with_max_length(17);
    wgt.label_username = gtk_label_new("旧密码");
    wgt.label_pwd = gtk_label_new("新密码");
    wgt.label_pwd2 = gtk_label_new("确认密码");
    wgt.vbox = gtk_vbox_new(FALSE,20);
    wgt.hbox_username = gtk_hbox_new(FALSE,20);
    wgt.hbox_pwd = gtk_hbox_new(FALSE,20);
    wgt.hbox_pwd2 = gtk_hbox_new(FALSE,20);
    wgt.hbox_button = gtk_hbox_new(FALSE,20);

    gtk_window_set_title(GTK_WINDOW(wgt.window),"密码修改窗口");
    gtk_window_set_resizable(GTK_WINDOW(wgt.window),FALSE);

    gtk_box_pack_start(GTK_BOX(wgt.hbox_username),wgt.label_username,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.hbox_username),wgt.entry_pwd,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.hbox_pwd),wgt.label_pwd,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.hbox_pwd),wgt.entry_pwd2,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.hbox_pwd2),wgt.label_pwd2,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.hbox_pwd2),wgt.verify_pwd,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.hbox_button),wgt.button_enroll,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.hbox_button),wgt.button_cancel,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.vbox),wgt.label_tips,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.vbox),wgt.hbox_username,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.vbox),wgt.hbox_pwd,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.vbox),wgt.hbox_pwd2,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.vbox),wgt.hbox_button,TRUE,FALSE,10);
    
    gtk_entry_set_visibility(GTK_ENTRY(wgt.entry_pwd),FALSE);
    gtk_entry_set_invisible_char(GTK_ENTRY(wgt.entry_pwd),'*');
    gtk_entry_set_visibility(GTK_ENTRY(wgt.verify_pwd),FALSE);
    gtk_entry_set_invisible_char(GTK_ENTRY(wgt.verify_pwd),'*');
    gtk_entry_set_visibility(GTK_ENTRY(wgt.entry_pwd2),FALSE);
    gtk_entry_set_invisible_char(GTK_ENTRY(wgt.entry_pwd2),'*');
    
    g_signal_connect(G_OBJECT(wgt.button_cancel),"clicked",G_CALLBACK(gtk_dialog_destroy),(void *)wgt.window);
    g_signal_connect(G_OBJECT(wgt.button_enroll),"clicked",G_CALLBACK(modify_pwd),&wgt);
    
    gtk_container_add(GTK_CONTAINER(wgt.window),wgt.vbox);
    gtk_widget_show_all(wgt.window);
}

void modify_pwd(GtkWidget *widget, gpointer data)
{
    char buf[10];
    GtkWidget *button;
    GtkWidget *window;
    GtkWidget *label;
    GdkColor color1;
    GdkColor color2;

    gdk_color_parse("red",&color1);
    gdk_color_parse("green",&color2);

    gtk_widget_hide(wgt.window);
    struct login_widget *get;
    const char *verify_pwd;
    get = (struct loging_widget *)data;
    const char *pwd1, *pwd2;

    window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_window_set_position(GTK_WINDOW(window), GTK_WIN_POS_CENTER);
    gtk_window_set_default_size(GTK_WINDOW(window), 200, 150);
    gtk_container_set_border_width(GTK_CONTAINER(window),10);
    button = gtk_button_new_with_label("确定");
    wgt.vbox = gtk_vbox_new(FALSE,20);

    gtk_window_set_title(GTK_WINDOW(window),"密码修改提示");
    
    pwd1 = gtk_entry_get_text(GTK_ENTRY(get->entry_pwd));
    pwd2 = gtk_entry_get_text(GTK_ENTRY(get->entry_pwd2));
    verify_pwd = gtk_entry_get_text(GTK_ENTRY(get->verify_pwd));
    strcpy(user.password,pwd1);
    memset(user.file,0,sizeof(user.file));
    strcpy(user.file,pwd2);
    printf("%s\n%s\n%s\n",user.username,user.password,user.file);
    if (strcmp(pwd2, verify_pwd) == 0) {
        user.flag = 9;
        if (send(s, &user, sizeof(user), 0) < 0) {
            my_err("send",__LINE__);
        }
    } else {
        gtk_widget_modify_fg(get->label_tips,GTK_STATE_NORMAL,&color1);
        gtk_label_set_text(GTK_LABEL(get->label_tips),"两次密码输入不一致!");
        gtk_entry_set_text(GTK_ENTRY(get->entry_pwd),"");
        gtk_entry_set_text(GTK_ENTRY(get->entry_pwd2),"");
        gtk_entry_set_text(GTK_ENTRY(get->verify_pwd),"");
        gtk_widget_show_all(wgt.window);
        return wgt.window;        
    }

	if(recv(s, buf, sizeof(buf), 0) < 0) {
		my_err("recv",__LINE__);
	}

    printf("----->%s\n",buf);
    
    if (strcmp(buf,"yes") == 0) {
        label = gtk_label_new("密码修改成功!");
        gtk_widget_modify_fg(label, GTK_STATE_NORMAL, &color2);
    } else {
        label = gtk_label_new("密码修改失败!");
        gtk_widget_modify_fg(label, GTK_STATE_NORMAL, &color1);
    }

    gtk_box_pack_start(GTK_BOX(wgt.vbox),label,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.vbox),button,TRUE,FALSE,10);
    
    g_signal_connect(G_OBJECT(button),"clicked",G_CALLBACK(gtk_dialog_destroy),(void *)window);

    gtk_container_add(GTK_CONTAINER(window),wgt.vbox);
    gtk_widget_show_all(window);
}

void manager_entrance(GtkWidget *widget, gpointer data)
{
	GtkWidget *scroll_box;
	GtkWidget *text;
	GtkWidget *button1;
	GtkWidget *button2;
	GtkWidget *button3;

    wgt.window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_window_set_position(GTK_WINDOW(wgt.window), GTK_WIN_POS_CENTER);
    gtk_window_set_default_size(GTK_WINDOW(wgt.window), 400, 600);

    gtk_window_set_title(GTK_WINDOW(wgt.window),"管理窗口");
    gtk_window_set_resizable(GTK_WINDOW(wgt.window),TRUE);

    wgt.vbox = gtk_vbox_new(FALSE,20);

	scroll_box = gtk_scrolled_window_new(NULL,NULL);
    text = gtk_text_view_new();
    gtk_scrolled_window_set_policy(GTK_SCROLLED_WINDOW(scroll_box), GTK_POLICY_AUTOMATIC,GTK_POLICY_AUTOMATIC);
    gtk_container_add(GTK_CONTAINER(scroll_box), text);
    gtk_widget_set_size_request(scroll_box,250,400);

   	button1 = gtk_button_new_with_label("显示在线用户");
    button2 = gtk_button_new_with_label("查看聊天记录");
    button3 = gtk_button_new_with_label("踢除用户");   
    
    gtk_box_pack_start(GTK_BOX(wgt.vbox),scroll_box,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.vbox),button1,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.vbox),button2,FALSE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.vbox),button3,FALSE,FALSE,10);

    gtk_container_add(GTK_CONTAINER(wgt.window),wgt.vbox);
    gtk_widget_show_all(wgt.window);        
}

//用户注册
void user_enroll(GtkWidget *widget,gpointer data)
{
    char buf[10];
    GtkWidget *button;
    GtkWidget *window;
    GtkWidget *label;
    GdkColor color1;
    GdkColor color2;

    gdk_color_parse("red",&color1);
    gdk_color_parse("green",&color2);

    gtk_widget_hide(wgt.window);
    struct login_widget *get;
    const char *verify_pwd;
    get = (struct loging_widget *)data;
    const char *usr, *pwd1;

    window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_window_set_position(GTK_WINDOW(window), GTK_WIN_POS_CENTER);
    gtk_window_set_default_size(GTK_WINDOW(window), 200, 150);
    gtk_container_set_border_width(GTK_CONTAINER(window),10);
    button = gtk_button_new_with_label("确定");
    wgt.vbox = gtk_vbox_new(FALSE,20);

    gtk_window_set_title(GTK_WINDOW(window),"注册提示");
    
    usr = gtk_entry_get_text(GTK_ENTRY(get->entry_username));
    pwd1 = gtk_entry_get_text(GTK_ENTRY(get->entry_pwd));
    verify_pwd = gtk_entry_get_text(GTK_ENTRY(get->verify_pwd));
    strcpy(user.username,usr);
    strcpy(user.password,pwd1);
    printf("%s\n%s\n%s\n",user.username,user.password,verify_pwd);
    //if (strcmp(user.password, verify_pwd) == 0 && strlen(user.username) != 0 && strlen(user.password) != 0) {
    if (strcmp(user.password, verify_pwd) == 0) {
        user.flag = 1;
        if (send(s, &user, sizeof(user), 0) < 0) {
            my_err("send",__LINE__);
        }
    } else {
        gtk_widget_modify_fg(get->label_tips,GTK_STATE_NORMAL,&color1);
        gtk_label_set_text(GTK_LABEL(get->label_tips),"两次密码输入不一致!");
        gtk_entry_set_text(GTK_ENTRY(get->entry_username),"");
        gtk_entry_set_text(GTK_ENTRY(get->entry_pwd),"");
        gtk_entry_set_text(GTK_ENTRY(get->verify_pwd),"");
        gtk_widget_show_all(wgt.window);
        return wgt.window;
    }

    printf("hello\n");
	if(recv(s, buf, sizeof(buf), 0) < 0) {
		my_err("recv",__LINE__);
	}
    
    if (strcmp(buf,"yes") == 0) {
        label = gtk_label_new("注册成功!");
        gtk_widget_modify_fg(label, GTK_STATE_NORMAL, &color2);
    } else {
        label = gtk_label_new("注册失败,该用户已存在!");
        gtk_widget_modify_fg(label, GTK_STATE_NORMAL, &color1);
    }

    gtk_box_pack_start(GTK_BOX(wgt.vbox),label,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.vbox),button,TRUE,FALSE,10);
    
    g_signal_connect(G_OBJECT(button),"clicked",G_CALLBACK(gtk_dialog_destroy),(void *)window);

    gtk_container_add(GTK_CONTAINER(window),wgt.vbox);
    gtk_widget_show_all(window);
}

//注册窗口
void enroll(GtkWidget *widget,gpointer data)
{
    GdkColor color1;
    GdkColor color2;
    GdkColor color3;
    gdk_color_parse("brown",&color1);
    gdk_color_parse("LawnGreen",&color2);
    gdk_color_parse("gray",&color3);
    
    wgt.window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_window_set_position(GTK_WINDOW(wgt.window), GTK_WIN_POS_CENTER);
    gtk_window_set_icon(GTK_WINDOW(wgt.window), create_pixbuf("qq2_2.jpg")); 
    wgt.label_tips = gtk_label_new("欢迎注册");
    gtk_widget_modify_fg(wgt.label_tips, GTK_STATE_NORMAL, &color1);
    wgt.button_enroll = gtk_button_new_with_label("注册");
    gtk_widget_modify_bg(wgt.button_enroll, GTK_STATE_PRELIGHT, &color2);
    wgt.button_cancel = gtk_button_new_with_label("取消");
    gtk_widget_modify_bg(wgt.button_cancel, GTK_STATE_PRELIGHT, &color3);
   
    wgt.entry_username = gtk_entry_new_with_max_length(15);
    wgt.entry_pwd = gtk_entry_new_with_max_length(16);
    wgt.verify_pwd = gtk_entry_new_with_max_length(16);
    wgt.label_username = gtk_label_new("用户名");
    wgt.label_pwd = gtk_label_new("密码");
    wgt.label_pwd2 = gtk_label_new("确认密码");
    wgt.vbox = gtk_vbox_new(FALSE,20);
    wgt.hbox_username = gtk_hbox_new(FALSE,20);
    wgt.hbox_pwd = gtk_hbox_new(FALSE,20);
    wgt.hbox_pwd2 = gtk_hbox_new(FALSE,20);
    wgt.hbox_button = gtk_hbox_new(FALSE,20);

    gtk_window_set_title(GTK_WINDOW(wgt.window),"注册窗口");
    gtk_window_set_resizable(GTK_WINDOW(wgt.window),FALSE);

    gtk_box_pack_start(GTK_BOX(wgt.hbox_username),wgt.label_username,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.hbox_username),wgt.entry_username,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.hbox_pwd),wgt.label_pwd,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.hbox_pwd),wgt.entry_pwd,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.hbox_pwd2),wgt.label_pwd2,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.hbox_pwd2),wgt.verify_pwd,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.hbox_button),wgt.button_enroll,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.hbox_button),wgt.button_cancel,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.vbox),wgt.label_tips,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.vbox),wgt.hbox_username,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.vbox),wgt.hbox_pwd,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.vbox),wgt.hbox_pwd2,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.vbox),wgt.hbox_button,TRUE,FALSE,10);
    
    gtk_entry_set_visibility(GTK_ENTRY(wgt.verify_pwd),FALSE);
    gtk_entry_set_invisible_char(GTK_ENTRY(wgt.verify_pwd),'*');
    gtk_entry_set_visibility(GTK_ENTRY(wgt.entry_pwd),FALSE);
    gtk_entry_set_invisible_char(GTK_ENTRY(wgt.entry_pwd),'*');
    g_signal_connect(G_OBJECT(wgt.button_cancel),"clicked",G_CALLBACK(gtk_dialog_destroy),(void *)wgt.window);
    g_signal_connect(G_OBJECT(wgt.button_enroll),"clicked",G_CALLBACK(user_enroll),&wgt);
    
    gtk_container_add(GTK_CONTAINER(wgt.window),wgt.vbox);
    gtk_widget_show_all(wgt.window);
}

GdkPixbuf *create_pixbuf(const gchar* filename)  
{  
    GdkPixbuf *pixbuf;  
    GError *error = NULL;  
    pixbuf = gdk_pixbuf_new_from_file(filename, &error);  
    if(!pixbuf) {  
        fprintf(stderr, "%s\n", error->message);  
        g_error_free(error);  
    }  
    return pixbuf;  
} 

void init_login_widget()
{
    GtkWidget *image;
    GtkWidget *frame;
    GdkColor color1;
    GdkColor color2;
    GdkColor color3;
    GdkColor color4;
    GdkColor color5;
    GdkColor color6;
    const char *name;
    const char *pwd;

    gdk_color_parse("blue",&color1);
    gdk_color_parse("gray",&color2);
    gdk_color_parse("black",&color3);
    gdk_color_parse("yellow",&color4);
    gdk_color_parse("red",&color5);
    gdk_color_parse("LawnGreen",&color6);

    image = gtk_image_new_from_file("qq1_1.jpg");
    wgt.window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_window_set_position(GTK_WINDOW(wgt.window), GTK_WIN_POS_CENTER);
    //frame = gtk_frame_new(NULL);
    //gtk_container_add(GTK_CONTAINER(frame),image);
    //gtk_container_add(GTK_CONTAINER(wgt.window),frame);
    wgt.label_tips = gtk_label_new("欢迎登录聊天室");
    gtk_widget_modify_fg(wgt.label_tips, GTK_STATE_NORMAL, &color1);
    wgt.button_login = gtk_button_new_with_label("登录");
    //gtk_widget_modify_bg(wgt.button_login, GTK_STATE_NORMAL, &color6);
    //gtk_widget_modify_bg(wgt.button_login, GTK_STATE_ACTIVE, &color3);
    gtk_widget_modify_bg(wgt.button_login, GTK_STATE_PRELIGHT, &color6);
    wgt.button_enroll = gtk_button_new_with_label("注册");
    gtk_widget_modify_bg(wgt.button_enroll, GTK_STATE_PRELIGHT, &color4);
    wgt.entry_username = gtk_entry_new_with_max_length(15);
    wgt.entry_pwd = gtk_entry_new_with_max_length(16);
    wgt.label_username = gtk_label_new("用户名");
    gtk_widget_modify_fg(wgt.label_username, GTK_STATE_NORMAL, &color3);
    wgt.label_pwd = gtk_label_new("密码");
    gtk_widget_modify_fg(wgt.label_pwd, GTK_STATE_NORMAL, &color3);
    wgt.vbox = gtk_vbox_new(FALSE,20);
    wgt.hbox_username = gtk_hbox_new(FALSE,20);
    wgt.hbox_pwd = gtk_hbox_new(FALSE,20);
    wgt.hbox_button = gtk_hbox_new(FALSE,20);
    
    //设置窗体
    gtk_window_set_title(GTK_WINDOW(wgt.window),"登录窗口");
    gtk_window_set_resizable(GTK_WINDOW(wgt.window),FALSE);
    gtk_window_set_icon(GTK_WINDOW(wgt.window), create_pixbuf("qq2_2.jpg")); 
    
    //设置布局盒子
    gtk_box_pack_start(GTK_BOX(wgt.hbox_username),wgt.label_username,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.hbox_username),wgt.entry_username,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.hbox_pwd),wgt.label_pwd,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.hbox_pwd),wgt.entry_pwd,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.hbox_button),wgt.button_login,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.hbox_button),wgt.button_enroll,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.vbox),wgt.label_tips,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.vbox),wgt.hbox_username,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.vbox),wgt.hbox_pwd,TRUE,FALSE,10);
    gtk_box_pack_start(GTK_BOX(wgt.vbox),wgt.hbox_button,TRUE,FALSE,10);
    
    //设置密码框不可见，用户输入时显示“*”
    gtk_entry_set_visibility(GTK_ENTRY(wgt.entry_pwd),FALSE);
    gtk_entry_set_invisible_char(GTK_ENTRY(wgt.entry_pwd),'*');
    
    //g_signal_connect(GTK_OBJECT(wgt.window),"delete_event",GTK_SIGNAL_FUNC(make_dialog),(gpointer)wgt.window);
    g_signal_connect(GTK_OBJECT(wgt.window),"delete_event",GTK_SIGNAL_FUNC(gtk_main_quit),NULL);
    g_signal_connect(GTK_OBJECT(wgt.button_enroll),"clicked",GTK_SIGNAL_FUNC(enroll),NULL);
    g_signal_connect(G_OBJECT(wgt.button_login),"clicked",G_CALLBACK(gtk_win_chat),&wgt);
    gtk_container_add(GTK_CONTAINER(wgt.window),wgt.vbox);
    gtk_widget_show_all(wgt.window);
}

//错误处理函数
void my_err(const char *err_string, int line)
{
    fprintf(stderr, "line: %d ",line);
    perror(err_string);
    exit(1);
}

//客户端处理
void process(void)
{
    char buf[MAXLEN];
    pid_t pid;
    struct sockaddr_in server_addr;
    
    if ( (s = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
        my_err("socket",__LINE__);
    }
    g_print("%d\n",s);

    //设置服务器地址
    bzero(&server_addr, sizeof(server_addr));
    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = htonl(INADDR_ANY);
    server_addr.sin_port = htons(PORT);

    //将用户输入的字符串类型的IP地址转为整型
    inet_pton(AF_INET, "127.0.0.1", &server_addr.sin_addr);

    //连接服务器
    if ( (connect(s, (struct sockaddr *)&server_addr, sizeof(struct sockaddr))) < 0) {
		my_err("connect",__LINE__);
	}

    //close(s);
}

void verify_user(void)
{
    int i;
	if (send(s, &user, sizeof(user), 0) < 0) {
		my_err("send",__LINE__);
    } 
	if(recv(s, flag, sizeof(flag), 0) < 0) {
		my_err("recv",__LINE__);
	} 
	//printf("%d\n",i);
	//printf("%s\n",flag);
}

void send_user(GtkWidget *widget, gpointer data)
{
    const char *usr;
    struct login_widget *wgt;
    wgt = (struct loging_widget *)data;

    usr = gtk_entry_get_text(GTK_ENTRY(wgt->entry2));
    memset(user.request,0,sizeof(user.request));
    strcpy(user.request,"chat");
    strcpy(user.friend,usr);
    printf("%s\n",user.friend);
    if (send(s, &user, sizeof(user), 0) < 0) {
        my_err("send",__LINE__);
    }
}

//发送消息
void send_message(GtkWidget *widget, gpointer data)
{	
    GtkTextIter start, end;
    char timebuf[MAXLEN];
    const char *msg;
    char *tmp;
    char a[MAXLEN];
    struct login_widget *get;
    int fd;

    get = (struct loging_widget *)data;
    memset(user.msg, 0, sizeof(user.msg));
    
    //获取输入的信息
    msg = gtk_entry_get_text(GTK_ENTRY(get->entry1));
    strcpy(user.msg,msg);
    gtk_entry_set_text(GTK_ENTRY(get->entry1),"");
    /*
    strcpy(a,msg);
    printf("****%d\n",strlen(a));
    if (strlen(a) == 0) {
        return;
    }
    */
    strcpy(user.request,"chat");
	if (send(s, &user, sizeof(user), 0) < 0) {
    	my_err("send",__LINE__);
	}

    time(&nowtime);
    tmp = ctime(&nowtime);
    sprintf(timebuf,"%s%s%s%s",tmp,"I: ",user.msg,"\n");
    printf("%s\n",timebuf);

    text_buffer = gtk_text_view_get_buffer(GTK_TEXT_VIEW(wgt.text1));
    gtk_text_buffer_get_bounds(GTK_TEXT_BUFFER(text_buffer),&start,&end);
    gtk_text_buffer_insert(GTK_TEXT_BUFFER(text_buffer),&end,timebuf,strlen(timebuf));
    
    memset(delete,0,sizeof(delete));
    strcpy(delete,user.username);
    strcat(delete,".dat");

	if (user.flag == 3) {
		if ((fd = open(delete,O_RDWR|O_CREAT|O_APPEND,0666)) < 0) {
		    my_err("open",__LINE__);
		}
		write(fd, timebuf, sizeof(timebuf));
		close(fd);
	}
	
	if (user.flag == 4) {
		if ((fd = open("group.dat",O_RDWR|O_CREAT|O_APPEND,0666)) < 0) {
		    my_err("open",__LINE__);
		}
		write(fd, timebuf, sizeof(timebuf));
		close(fd);
	}
}

//接收私聊消息
void *recv_message_single(void *arg)
{
    GtkTextIter start, end;
    int fd;
    //char buf[MAXLEN];
    memset(delete,0,sizeof(delete));
    strcpy(delete,user.username);
    strcat(delete,".dat");
    
    while (1) {
        
        memset(user.msg, 0, sizeof(user.msg));       
        recv(s, &user, sizeof(user), 0);
        printf("<>%s\n",user.request);
        if (strcmp(user.request,"chat") == 0) {
            text_buffer = gtk_text_view_get_buffer(GTK_TEXT_VIEW(wgt.text1));
            gtk_text_buffer_get_bounds(GTK_TEXT_BUFFER(text_buffer),&start,&end);
            gtk_text_buffer_insert(GTK_TEXT_BUFFER(text_buffer),&end,user.msg,strlen(user.msg));
        
            write(1, user.msg, sizeof(user.msg));
        
		    if ((fd = open(delete,O_RDWR|O_CREAT|O_APPEND,0666)) < 0) {
		        my_err("open",__LINE__);
		    }
            write(fd, user.msg, sizeof(user.msg));
		    close(fd);
        } else if (strcmp(user.request,"fresh") == 0) {
            printf("%s",user.msg);
            text_buffer = gtk_text_view_get_buffer(GTK_TEXT_VIEW(wgt.text2));
            gtk_text_buffer_get_bounds(GTK_TEXT_BUFFER(text_buffer),&start,&end);
            gtk_text_buffer_insert(GTK_TEXT_BUFFER(text_buffer),&end,user.msg,strlen(user.msg));
        }
    }
}

//接收下载文件列表
void *recv_filelist(void *arg)
{
    GtkTextIter start, end;
    int fd;
    //char buf[MAXLEN];
    while (1) {
        memset(user.filename, 0, sizeof(user.filename));       
        recv(s, &user, sizeof(user), 0);

        text_buffer = gtk_text_view_get_buffer(GTK_TEXT_VIEW(wgt.text1));
        gtk_text_buffer_get_bounds(GTK_TEXT_BUFFER(text_buffer),&start,&end);
        gtk_text_buffer_insert(GTK_TEXT_BUFFER(text_buffer),&end,user.filename,strlen(user.filename));
    }
}

//接收群聊消息
void *recv_message_group(void *arg)
{
    GtkTextIter start, end;
    int fd;
    //char buf[MAXLEN];
    while (1) {
        
        memset(user.msg, 0, sizeof(user.msg));       
        recv(s, &user, sizeof(user), 0);
        //printf("%s\n",buf);
        //write(1,buf,sizeof(buf));
        if (strcmp(user.request,"chat") == 0) {
            text_buffer = gtk_text_view_get_buffer(GTK_TEXT_VIEW(wgt.text1));
            gtk_text_buffer_get_bounds(GTK_TEXT_BUFFER(text_buffer),&start,&end);
            gtk_text_buffer_insert(GTK_TEXT_BUFFER(text_buffer),&end,user.msg,strlen(user.msg));
            //g_print("server: %s\n",buf);
            write(1, user.msg, sizeof(user.msg));
        
		    if ((fd = open("group.dat",O_RDWR|O_CREAT|O_APPEND,0666)) < 0) {
		        my_err("open",__LINE__);
		    }
            write(fd, user.msg, sizeof(user.msg));
		    close(fd);
        } else if (strcmp(user.request,"fresh") == 0) {
            printf("%s",user.msg);
            text_buffer = gtk_text_view_get_buffer(GTK_TEXT_VIEW(wgt.text2));
            gtk_text_buffer_get_bounds(GTK_TEXT_BUFFER(text_buffer),&start,&end);
            gtk_text_buffer_insert(GTK_TEXT_BUFFER(text_buffer),&end,user.msg,strlen(user.msg));
        }
    }
}

int main(int argc,char *argv[])
{
	process();

    gtk_init(&argc,&argv);
    
    init_login_widget();
    
    gtk_main();
      
    return 0;
}
