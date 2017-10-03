/*************************************************************************
	> File Name: server5.c
	> Author: hepan
	> Mail: hepansos@gmail.com
	> Created Time: 2016年08月04日 星期四 14时45分05秒
 ************************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <error.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <dirent.h>
#include <unistd.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <signal.h>
#include <pthread.h>

#define PORT        8888            //侦听端口地址
#define BACKLOG     12              //侦听队列长度
#define MAXLEN 		512			    //最大消息长度

void handler_sigint(int signo);                 	//信号处理函数
void my_err(const char *err_string, int line);      //错误处理函数
void *process(void *ss);                            //对客户端的处理
void match(char *name, char *passwd);               //匹配用户名和密码
void enroll(char *name);                            //注册函数
void change_pwd(char *name,char *passwd);                        //修改密码函数

char flag[10]; 		    //判断用户信息是否正确
char flag_enroll[10];   //判断该用户是否已经注册
char flag_change[10];   //判断修改密码是否成功
int k = 0;              //记录在线用户个数

time_t nowtime;    

//保存在线用户信息的结构体
struct userinfo {
    int sock_fd;        //在线用户套接字        
    char username[16];  //用户名
}usr[MAXLEN];

//消息结构体
struct message {  
    int flag;               //标志
    int statu;              //用户状态
    int sock_fd;            //在线用户套接字        
    char username[16];      //用户名
    char password[17];      //密码
    char friend[16];        //聊天好友
    char state[5];          //离线提示
    char request[50];       //好友请求
    char msg[MAXLEN];       //消息
    char filename[256];     //文件名
    char file[100];         //文件内容
    //struct message *next;
}inf,info[MAXLEN];

//信号处理函数
void handler_sigint(int signo)
{
}

//错误处理函数
void my_err(const char *err_string, int line)
{
    char *tmp;
    char err[100];
    int fd, errno;

    time(&nowtime);
    tmp = ctime(&nowtime);
    fprintf(stderr, "line: %d ",line);
    perror(err_string);
    sprintf(err,"%s%s%s%s%s",tmp,err_string,": ",strerror(errno),"\n");
    fd = open("error.dat",O_RDWR|O_CREAT|O_APPEND,0666);            //错误日志
    write(fd,err,strlen(err));
    exit(1);
}

//注册函数
void enroll(char *name)
{
    int fd, i;
    memset(flag_enroll,0,sizeof(flag_enroll));

	for (i=0; i<k; i++) {
        if (strcmp(info[i].username,name) == 0) {
            strcpy(flag_enroll,"no");
            return;
        }
    }
    strcpy(flag_enroll,"yes");

    if ((fd = open("users.dat", O_RDWR|O_CREAT|O_APPEND, 0666)) < 0) {
        my_err("open",__LINE__);
    }
    
    write(fd, &inf, sizeof(inf));
    close(fd);

    //printf(">>>%s\n",flag_enroll);
    printf("注册成功\n");
}

//修改密码
void change_pwd(char *name, char *passwd)
{
    int i, fd;

	for (i=0; i<k; i++) {
        if (strcmp(info[i].username,name) == 0 && strcmp(info[i].password,passwd) == 0) {
            if (access("users.dat",0) == 0){
                fd = open("users.dat", O_RDWR|O_CREAT, 0666);   
                lseek(fd,sizeof(inf)*i,SEEK_SET);
                memset(inf.password,0,sizeof(inf.password));
                strcpy(inf.password,inf.file);
                write(fd, &inf, sizeof(inf));
            }
            strcpy(flag_change,"yes");
            printf("密码修改成功\n");
            return;
        }
    }
    strcpy(flag_change, "no");
}

//匹配用户名和密码
void match(char *name, char *passwd)
{
    int fd, i;
    char *tmp;
    char login[50];

    time(&nowtime);
    tmp = ctime(&nowtime);
    //printf("-->%d\n",k);

    memset(flag,0,sizeof(flag));

    //如果用户已登录,则不允许再次登录
    for (i=0; i<k; i++) {
        //printf("<-*->%d\n",info[i].statu);
        if (strcmp(info[i].username, name) == 0 && info[i].statu == 1) {
            //printf("***%d\n",info[i].statu);
            strcpy(flag,"login");
            return;
        }
    }
    
    //匹配用户名和密码
	for (i=0; i<k; i++) {
	    if (strcmp(info[i].username, name) == 0) {
	    	if (strcmp(info[i].password, passwd) == 0) {
	    		strcpy(flag,"yes");
                sprintf(login,"%s%s%s",tmp,info[i].username," login\n");
                fd = open("login.dat",O_RDWR|O_CREAT|O_APPEND,0666);            //登录日志
                write(fd,login,strlen(login));
                return;
            } else {
                strcpy(flag,"no");
                return;
            }
        }
    }
    strcpy(flag,"no");
}

/* 服务器对客户端的处理 */
void *process(void *ss)
{
	char timebuf[MAXLEN];
    char buf[20];
    char a[MAXLEN];
    char *tmp;
    int sc = *(int *)ss;
    int i, fd;
    int t;
    
    /*
    for (i=0; i<k; i++) {
        printf("%s\n",info[i].username);
    }
    printf("***%d\n",sc);
    */

    while(1) {
        
        memset(&inf,0,sizeof(inf));
        
        //接收消息
        if ( (t = recv(sc, &inf, sizeof(inf),0)) < 0) {
         	close(sc);
            pthread_exit(NULL);
        }
        //printf("flag:%d\n",inf.flag);
        //printf("**%s\n",inf.username);
        if (strcmp(inf.state,"yes") == 0) {
            //printf("--->%s\n",inf.username);
            for (i=0; i<k; i++) {
                if (strcmp(inf.username,info[i].username) == 0) {
                    //printf("***%d\n",info[i].statu);
                    info[i].statu = 0;
                    printf("%s 下线\n",inf.username);
                    break;
                }
            }
        }
        //printf("--->%s\n",inf.username);
        //printf("***%d\n",info[i].statu);
        for (i=0; i<k; i++) {
            if (info[i].sock_fd == sc) {
                strcpy(buf, info[i].username);
            }
        }
        strcpy(a,inf.msg);      //消息为空不发送
        time(&nowtime);
        tmp = ctime(&nowtime);
        sprintf(inf.msg,"%s%s%s%s%s",tmp,buf,": ",a,"\n");
        
        DIR *dir;
        struct dirent *ptr;
        char path[MAXLEN];

        switch (inf.flag) {
        //注册
        case 1:
            enroll(inf.username);
            send(sc, flag_enroll, sizeof(flag_enroll),0);
            break;
        //登录
        case 2:
            match(inf.username, inf.password);
            //printf("login_flag>>>%s\n",flag);
            if (strcmp(flag,"yes") == 0) {
                for (i=0; i<k; i++) {
                    if (strcmp(info[i].username,inf.username) == 0) {
                        info[i].sock_fd = sc;
                        info[i].statu = 1;
                        printf("%s 登录\n",inf.username);
                        //memset(inf.msg,0,sizeof(inf.msg));
                        //sprintf(inf.msg,"%s%s",inf.username," login\n");
                    }
                }
            }
            send(sc, flag, sizeof(flag), 0);
            
            /*
            for (i=0; i<k; i++) {
                if (info[i].sock_fd != sc) {
                    printf("--->%s\n",inf.msg);
                    send(info[i].sock_fd, &inf, sizeof(inf), 0);
                }
            }
            */

            break;
        //私聊
        case 3:
            //printf("--->%s\n",inf.request);
            if (strcmp(inf.request,"chat") == 0) {
                for (i=0; i<k; i++) {
                    if (strcmp(info[i].username,inf.friend) == 0) {
                        if (strlen(a) == 0) {
                            break;
                        }
                        send(info[i].sock_fd, &inf, sizeof(inf), 0);
                    }
                }
            } else if (strcmp(inf.request,"fresh") == 0) {
                memset(inf.msg,0,sizeof(inf.msg));
                for (i=0; i<k; i++) {
                    if (info[i].statu == 1) {
                        //printf("***-->%s\n",info[i].username);
                        sprintf(inf.msg,"%s%s%s",inf.msg,info[i].username,"\t\t\t在线\n");
                    } 
                }
                for (i=0; i<k; i++) {
                    if (info[i].statu == 0) {
                        sprintf(inf.msg,"%s%s%s",inf.msg,info[i].username,"\t\t\t离线\n");
                    }
                }
                //printf("%s",inf.msg);
                //printf("%s",inf.request);
                send(sc,&inf,sizeof(inf),0);
            }
            break;
        //群聊
        case 4:
            if (strcmp(inf.request,"chat") == 0) {
                for (i=0; i<k; i++) {
                    if (info[i].sock_fd != sc) {
                        send(info[i].sock_fd, &inf, sizeof(inf), 0);
                    }
                }
            } else if (strcmp(inf.request,"fresh") == 0) {
                memset(inf.msg,0,sizeof(inf.msg));
                for (i=0; i<k; i++) {
                    if (info[i].statu == 1) {
                        //printf("***-->%s\n",info[i].username);
                        sprintf(inf.msg,"%s%s%s",inf.msg,info[i].username,"\t\t\t在线\n");
                    } 
                }
                for (i=0; i<k; i++) {
                    if (info[i].statu == 0) {
                        sprintf(inf.msg,"%s%s%s",inf.msg,info[i].username,"\t\t\t离线\n");
                    }
                }
                //printf("%s",inf.msg);
                //printf("%s",inf.request);
                send(sc,&inf,sizeof(inf),0);
            }
            break;
        //加好友
        case 5:
            memset(inf.request,0,sizeof(inf.request));
            if (access("users.dat",0) == 0){
                fd = open("users.dat", O_RDONLY);   
            }
	        for (i=0; i<k; i++) {
                if (strcmp(info[i].username,inf.friend) == 0) {
                    strcpy(inf.request,"yes");
                    send(sc, &inf, sizeof(inf), 0);
                }
            }
            break;
        //收文件
        case 6:
            if ((fd = open(inf.filename, O_RDWR|O_CREAT|O_APPEND, 0666)) < 0) {
                my_err("open",__LINE__);
            }
            write(fd, inf.file, 2);
            close(fd);
            //printf("接收 %s 发送的文件成功!",inf.username);
            break;
        //发文件
        case 7:
            /*
            for (i=0; i<k; i++) {
                if (strcmp(info[i].username,inf.username) == 0) {
                    break;
                }
            }
            */
            if ((fd = open(inf.filename,O_RDONLY)) < 0) {
                my_err("open",__LINE__);
            }
            memset(inf.file,0,sizeof(inf.file));
            while (1) {
                if (read(fd, inf.file, 2) < 0) {
                    break;
                }          
                send(sc, &inf, sizeof(inf), 0);
                printf(">>>\n");
                memset(inf.file,0,sizeof(inf.file));
            }
            close(fd);
            break;
        //发送文件列表
        case 8:
            for (i=0; i<k; i++) {
                if (strcmp(info[i].username,inf.username) == 0) {
                    break;
                }
            }
            getcwd(path,MAXLEN);
            if ((dir = opendir(path)) == NULL) {
                perror("opendir");
            }
            while ((ptr = readdir(dir)) != NULL) {
                if (strcmp(ptr->d_name,".") == 0 || strcmp(ptr->d_name,"..") == 0) {
                    continue;
                }
                strcpy(inf.filename,ptr->d_name);
                strcat(inf.filename,"\n");
                send(info[i].sock_fd, &inf, sizeof(inf), 0);
                memset(inf.filename,0,sizeof(inf.filename));
            }
            closedir(dir);
            break;
        //修改密码
        case 9:
            //printf("------->>%s\n",inf.username);
            //printf("------->>%s\n",inf.file);
            change_pwd(inf.username, inf.password);
            //printf("--->flag_change: %s\n",flag_change);
            send(sc, flag_change, sizeof(flag_change),0);
            break;
        default:
            break;
        }  
    }
}

//主函数
int main(int argc, char *argv[])
{
    int ss, sc;                         //ss为服务器的socket描述符，sc为客户端的socket描述符
    struct sockaddr_in server_addr;     //服务器地址结构
    struct sockaddr_in client_addr;     //客户端地址结构
    int err;                            //返回值
    int optval;
    int fd;
    pthread_t thid;
    char msg[MAXLEN];
    int i = 0;
    int n = 1;

 	signal(SIGINT,handler_sigint); 		//屏蔽Ctrl+C
 	
    if (access("users.dat",0) == 0){
        fd = open("users.dat", O_RDONLY);   
        for (k=0; (read(fd, &info[k], sizeof(info[k]))) > 0; k++) {
            info[k].statu = 0;
            //printf("****%s\n",info[k].username);
        }
    }

    //printf("----->%d\n",k);
    //建立一个流式套接字
    if ( (ss = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
        my_err("socket",__LINE__);
    }

	//设置该套接字使之可以重新绑定端口
    optval = 1;
    if (setsockopt(ss, SOL_SOCKET, SO_REUSEADDR, (void *)&optval, sizeof(int)) < 0) {
        my_err("setsockopt",__LINE__);
    }
    
    //设置服务器地址
    bzero(&server_addr, sizeof(server_addr));   	        //清零
    server_addr.sin_family = AF_INET;           	        //协议族
	//inet_aton("192.168.30.166",&server_addr.sin_addr);    //本机地址
    server_addr.sin_port = htons(PORT);                     //服务器端口
    server_addr.sin_addr.s_addr = htonl(INADDR_ANY);

    //绑定地址结构到套接字描述符
    if ( (err = bind(ss, (struct sockaddr*)&server_addr, sizeof(server_addr))) < 0) {
        my_err("bind",__LINE__);
    }

    //设置侦听
    if ( (err = listen(ss, BACKLOG)) < 0) {
        my_err("listen",__LINE__);
    }

    socklen_t addrlen = sizeof(struct sockaddr);
    
	while(1) {
		if((sc = accept(ss,(struct sockaddr*)&client_addr,&addrlen))<0) {
			my_err("accept",__LINE__);
        }
        printf("新的连接ip: %s\nsocket_fd: %d\n",inet_ntoa(client_addr.sin_addr),sc);
        pthread_create(&thid, NULL, process, (void *)&sc);
	}

    return 0;
}

