/*
 * Account_UI.c
 *
 *  Created on: 2015年5月8日
 *      Author: Administrator
 */

#include "Account_UI.h"

#include "../Common/List.h"
#include "../Common/ComFunction.h"
#include "../Service/Account.h"
#include "../Service/EntityKey.h"
#include "Account_UI.h"
#include <stdio.h>
#include <assert.h>
#include <string.h>
#include <unistd.h>
#include <sys/time.h>

#define MAX_TIMES 3

extern account_t gl_CurUser;

int SysLogin() {

	int i=MAX_TIMES;
	char uname[30],pwd[30];
	
	Account_Srv_InitSys();
	
	while(i--){
		system("clear");
		
		printf("\t\t\033[40;33m=======================================================================================\033[0m\n\n");
		printf("\t\t\033[40;32m------------------------------\033[0m\033[40;36m 欢迎使用剧院票务管理系统 \033[0m\033[40;32m-------------------------------\033[0m\n\n");
		printf("\t\t\033[40;33m***************************************************************************************\033[0m\n\n");
		printf("\t\t\033[40;32m**************************************\033[0m\033[40;36m 用户 登录 \033[0m\033[40;32m**************************************\033[0m\n\n");
       		
		printf("\t\t\t\t\033[32m用户名: \033[0m");
		scanf("%s",uname);

		printf("\n");

		printf("\t\t\t\t\033[33m密  码: \033[0m");
		cl_stdin();
		getpwd(30,pwd); //input passwd
       		

		if(Account_Srv_Verify(uname,pwd)){
			printf("\n\n\t\t\t");

			system("echo '\033[40;36m您已登录成功!请稍后....\033[0m' | pv -qL 15");
			sleep(1);
			system("sl");

			return 1;
		}else{

			if(i>0){  
				printf("\n\n\t\t\t\t\033[31m用户名或密码输入错误!请重新输入....\033[0m\n");
				printf("\n\t\t\t\t\033[33m您还有\033[0m\033[31m%d\033[0m\033[33m次机会!\033[0m",i);
				sleep(2);
			}
		}
	}
	return 0;
}

int SysLogout() {


	int choice;
	
	
       	printf("\t\t\t\033[33m=======================================================================================\033[0m\n\n");
       		
       		
       	printf("\t\t\t\t\033[33m当前用户为:%s\033[0m\n",gl_CurUser.username);
        choice=prom("\t\t\t\t\033[31m真的要注销吗？\033[0m",'y','n');
        if(1==choice){
	       	if(Account_Srv_Logout()){
				printf("\n\n\t\t\t\033[32m注销成功!请稍后....\033[0m\n");
				sleep(1);
				return 1;
		}
		else{
			printf("\n\t\t\t\033[40;31m注销失败，请稍后重试!!!\033[0m\n");

			sleep(1);
			return 0;
		}
	}
	return 0;
		
}

void Change_Passwd(){
	char oldpwd[30],newpwd[30],newpwd1[30];
	account_t data=gl_CurUser;
	int i=MAX_TIMES,q=0;
	system("clear");
		
	printf("\t\t\033[32m=======================================================================================\033[0m\n\n");
	printf("\t\t\033[33m-----------------------------------\033[0m \033[36m用户 密码 修改\033[0m \033[33m------------------------------------\033[0m\n\n");
	printf("\t\t\033[32m***************************************************************************************\033[0m\n\n");

	printf("\t\t\t\t\033[33m当前用户为:%s\033[0m\n",gl_CurUser.username);

		printf("\n\t\t\t\t\033[31m请输入原始密码: \033[0m");
		cl_stdin();

		while(i--){
			getpwd(30,oldpwd); //input passwd

			if( strcmp(oldpwd,gl_CurUser.password)==0 ){
				q=1;
				break;
			}else{

				if(i>0){
					printf("\n\t\t\t\033[31m用户名或密码输入错误!请重新输入....\033[0m\n");
					printf("\n\t\t\t您还有\033[40;32m%d\033[0m次机会!",i);
				}
			}
		}
		while(q){
				printf("\n\t\t\t\t\033[32m请输入新密码：\033[0m");
				//cl_stdin();
				getpwd(30,newpwd);
				printf("\n\t\t\t\t\033[33m请再次输入新密码：\033[0m");
							
				getpwd(30,newpwd1);
				if( strcmp(newpwd,newpwd1)==0 ){
						strcpy(data.password,newpwd);
						
						if(Account_Srv_Modify(&data)){
							gl_CurUser=data;
							printf("\n\n\t\t\t\033[40;36m密码修改成功!!!\033[0m\n");
			
						}else{

							printf("\n\n\t\t\t\033[40;31m密码修改失败!请稍后重试....\033[0m\n");
						}
						
						break;
				}else{
							
						printf("\n\t\t\t\033[31m两次密码输入不一致！请重新输入：\033[0m");
				}
		}
		
		printf("\n\t\t\t按任意键继续....");
		
		getchar();
}


//管理系统用户功能入口函数，显示用户账号管理菜单
void Account_UI_MgtEntry() {
	
	int i;
	char choice,uname[30];

	account_t data;
	account_list_t head;
	account_node_t *p;
	Pagination_t paging;

	List_Init(head, account_node_t);
	
	paging.offset = 0;
	paging.pageSize = ACCOUNT_PAGE_SIZE;

	//载入数据
	paging.totalRecords = Account_Srv_FetchAll(head);
	
	Paging_Locate_FirstPage(head, paging);

	do {
		system("clear");
		
		printf("\t\t\033[31m=======================================================================================\033[0m\n\n");
		printf("\t\t\033[32m------------------------------------- \033[36m用户 列表\033[0m \033[32m---------------------------------------\033[0m\n\n");
		printf("\t\t\t\033[32m%s\t\t\t\t%s\t\t\t%s\033[0m\n\n", "ID", "用户类型", "用户名");
		printf("\t\t\033[33m***************************************************************************************\033[0m\n\n");
		
		
		//显示数据
		Paging_ViewPage_ForEach(head, paging, account_node_t, p, i){
			printf("\t\t\t\033[33m%d\033[0m\t\t", p->data.id);
			
			switch(p->data.type){
				case USR_CLERK:  printf("\033[32m\t%18s\033[0m\t\t\t\t","销售员"); 			break;
				case USR_MANG:	 printf("\033[33m\t%15s\033[0m\t\t\t\t","经理");  			break;
				case USR_ADMIN:  printf("\033[31m\t %22s\033[0m\t\t\t","系统管理员"); 		break;
			
			
			}
			//销售员  经理  系统管理员 

			printf("%s\n", p->data.username);
		}

		printf("\n\t\t\033[32m--------------- 共 \033[0m\033[33m%2d\033[0m \033[32m个用户 -------------------------- 页码：\033[0m\033[32m%2d\033[0m /\033[33m%2d\033[0m \033[32m------------------\033[0m\n\n",
				paging.totalRecords, Pageing_CurPage(paging),
				Pageing_TotalPages(paging));
		printf("\t\t\033[32m=======================================================================================\033[0m\n\n");
		printf("\t\t\033[32m----------------\033[0m[\033[33mA\033[0m]\033[32m新增\033[0m-------------[\033[36mU\033[0m]\033[36m修改\033[0m-------------[\033[31mD\033[0m]\033[40;31m删除\033[0m--------------\n\n");
		
		printf("\t\t\033[32m----------------\033[0m[\033[33mP\033[0m]\033[32m上页-------------\033[0m[\033[33mR\033[0m]\033[32m返回-------------\033[0m[\033[33mN\033[0m]\033[32m下页------------------------\033[0m\n\n");
		
		
		printf("\t\t\033[32m***************************************************************************************\033[0m\n\n");
		printf("\t\t\033[33m请选择: \033[0m");
		
		choice=l_getc();
	
		switch (choice) {
		
		case 'a':
		case 'A':
			if (Account_UI_Add()) //新添加成功，跳到最后一页显示
			{
				paging.totalRecords = Account_Srv_FetchAll(head);
				Paging_Locate_LastPage(head, paging, account_node_t);
			}
			break;
		
		case 'd':
		case 'D':
			
			printf("\t\t\033[31m请输入要删除的用户名:\033[0m");
			scanf("%s",uname);
			
			
			
			if (Account_UI_Delete(uname)) {	//从新载入数据
					paging.totalRecords = Account_Srv_FetchAll(head);
					List_Paging(head, paging, account_node_t);
			}
			
			break;
		
		case 'u':
		case 'U':
			
			printf("\t\t\033[32m请输入要修改用户的用户名: \033[0m");
			scanf("%s",uname);
			
			
			if (Account_UI_Modify(uname)) {
					//从新载入数据
					paging.totalRecords = Account_Srv_FetchAll(head);
					List_Paging(head, paging, account_node_t);
			}
			
			break;
		/*
		case 'q':
		case 'Q':
			printf("请输入要查询的用户名：");
			cl_stdin();
			scanf("%s",uname);
			if ( Account_UI_Query(uname) )
				printf("你要查询的用户存在，用户类型为：%s\n",data.type);
			break;
		*/
		case 'p':
		case 'P':
			if (!Pageing_IsFirstPage(paging)) {
				Paging_Locate_OffsetPage(head, paging, -1,account_node_t);
			}
			break;
		case 'n':
		case 'N':
			if (!Pageing_IsLastPage(paging)) {
				Paging_Locate_OffsetPage(head, paging, 1, account_node_t);
			}
			break;
		
		}
	} while (choice != 'r' && choice != 'R');
	//释放链表空间
	List_Destroy(head, account_node_t);
	
	
}


//添加一个用户账号信息
int Account_UI_Add() {
	
	int er=0,newRecCount=0;
       		char choice;
		account_t data,buf;	
		do { 

				system("clear");
				
				printf("\t\t\033[32m=======================================================================================\033[0m\n\n");
				printf("\t\t\033[32m--------------------------------\033[0m\033[33m新添 用户\033[0m\033[32m---------------------------------\033[0m\n\n");
				printf("\t\t\033[32m***************************************************************************************\033[0m\n\n");

				data.id = EntKey_Srv_CompNewKey("user");
				
				printf("\t\t\t请输入用户名：");
				while(1){
					scanf("%s",data.username);
					if( Account_Srv_FetchByName(data.username,&buf) ) {
						printf("\t\t\t用户名已存在，请重新输入!!!\n\t\t\t");
					}else break;
				}
		
				printf("\t\t\t请输入用户密码：");
				scanf("%s",data.password);
				

				printf("\t\t\t\033[32m=======================================================================================\033[0m\n\n");
				printf("\t\t\t\033[32m[1]销售员\033[0m                                \033[33m[2]经理\033[0m                             \033[31m[3]系统管理员\033[0m\n\n");
				printf("\t\t\t请选择用户类型：");
				while(1){
					er=0;
					choice=l_getc();
					switch(choice){
						case '1': data.type= USR_CLERK;  	break;
						case '2': data.type= USR_MANG;   	break;
						case '3': data.type= USR_ADMIN; 	break;
						default : er=1; printf("\t\t\t\033[31m您的选择有误！请重新选择....\033[0m\n");
					}
					if(er!=1) break;
				}
			
				
				if(Account_Srv_Add(&data)){
					newRecCount++;
					printf("\t\t\t添加成功!!!\n");
				}else{
					
				}
				printf("\t\t\t----- [A]继续添加 ----------- [R]返回 -----:");
				choice=l_getc();
		} while ('a' == choice || 'A' == choice);
	return newRecCount;
}

//根据用户账号名修改用户账号密码，不存在这个用户账号名，提示出错信息
int Account_UI_Modify(char usrName[]) {

		int er=0,ret=0,q=0;
		char choice;
		account_t data;	
		if( Account_Srv_FetchByName(usrName,&data) ){
			if(gl_CurUser.id==data.id){
				printf("\t\t\t\t无法修改自己的信息！按任意键返回....");
				cl_stdin();
				getchar();
				return 0;
			}
			while(1) { 

				system("clear");
				
				printf("\t\t\033[40;32m=======================================================================================\033[0m\n\n");
				printf("\t\t\t\t----------------------------\033[32m修改 用户\033[0m-------------------------------\n\n");
				printf("\t\t\033[40;32m****************************************************************************************\033[0m\n\n");
				printf("\t\t\t\033[40;32m%5s\033[0m \t \033[40;32m%10s\033[0m \t \033[40;32m%20s\033[0m \n", "ID", "用户类型", "用户名");
				printf("\t\t\t\033[40;32m-------------------------------------------------------------------------\033[0m\n\n");
		
				printf("\t\t\t%5d\t ", data.id);
			
				switch(data.type){
					case USR_CLERK:  printf("%10s","销售员"); 		break;
					case USR_MANG:	 printf("%10s","经理");  		break;
					case USR_ADMIN:  printf("%10s","系统管理员"); 	break;
				}
				printf("\t%20s\n", data.username);
				printf("\t\t\033[40;32m=======================================================================================\033[0m\n\n");
				printf("\t\t\t----[A]修改用户类型---------[B]重置用户密码-----------[R]返回--------\n\n");
				printf("\t\t\033[40;32m***************************************************************************************\033[0m\n\n");
				printf("\t\t\t\033[31m 该功能涉及系统安全，请谨慎操作！！！\033[0m\n\n");
				
				printf("\t\t\t请选择:");
			
				choice=l_getc();
				if('r'==choice || 'R'==choice) break;	
				switch(choice){
					
						
					case 'b':
					case 'B':
						{
							char fpwd[30],spwd[30];
							while(1){
								printf("\n\t\t\t请输入用户密码：");
								cl_stdin();
								getpwd(30,fpwd);
								printf("\n\t\t\t请再次输入用户密码：");

								getpwd(30,spwd);
								if( strcmp(fpwd,spwd)==0 ){
									strcpy(data.password,fpwd);
									q=1;
									break;
								}else{
									printf("\n\t\t\t\033[31m两次密码输入不一致！请重新输入：\033[0m");
								}
							}
						}
						break;
						
					case 'a':
					case 'A':
						printf("\t\t\t请选择用户类型：\n\t\t\t[1]销售员          [2]经理          [3]系统管理员\n");
						while(1){
							er=0;
							choice=l_getc();
							switch(choice){
								case '1':
									if(data.type!= USR_CLERK){
										data.type= USR_CLERK;
									}else{
										printf("\n\t\t\t类型相同，不用修改！");
									} 	
									break;
								case '2':
									if(data.type!= USR_MANG){
										data.type= USR_MANG;
										q=1;
									}else{
										printf("\n\t\t\t类型相同，不用修改！");
										q=0;
									} 	
									break;
								case '3': 
								
									if(data.type!= USR_ADMIN){
										data.type= USR_ADMIN;
										q=1;
									}
									else{
										printf("\n\t\t\t类型相同，不用修改！");
										q=0;
									} 	
									break;
								default : er=1; printf("\n\t\t\t您的选择有误！请重新选择。\n\t\t\t");
							}
							if(er!=1) break;
						}
						break;
					}
			
				
				if(q && Account_Srv_Modify(&data)){
					printf("\n\t\t\t修改成功！\n");
					ret=1;
				}else{
					if(q!=0) printf("\n\t\t\t修改失败！\n");
				}
				
				printf("\n\t\t\t按任意键继续。。。");
				cl_stdin();
				getchar();
		
		}
       
       }else{
       		printf("\n\t\t\t未找到！");
       		printf("\n\t\t\t按任意键返回。。。");
		cl_stdin();
		getchar();
       	}
       return ret;
}

//根据用户账号名删除一个已经存在的用户账号信息，如果不存在这个用户账号名，提示出错信息
int Account_UI_Delete(char usrName[]) {

		int ret=0;
       		char choice;
		account_t data;	
		if( Account_Srv_FetchByName(usrName,&data) ){
			if(gl_CurUser.id==data.id){
				printf("\t\t\t\t无法删除自己的信息！按任意键返回。。。");
				cl_stdin();
				getchar();
				return 0;
			}
			while(1){
				system("clear");
				
				printf("\t\t\033[40;32m=======================================================================================\033[0m\n\n");
				printf("\t\t\t\033[40;32m----------------------------\033[0m\033[40;31m删除 用户\033[0m\033[40;32m-------------------------------\033[0m\n\n");
				printf("\t\t\033[40;32m***************************************************************************************\033[0m\n\n");
				printf("\t\t\t%5s \t %10s \t %20s \n", "ID", "用户类型", "用户名");
				printf("\t\t\t\033[40;32m-------------------------------------------------------------------------------------\033[0m\n\n");

				printf("\t\t\t%5d ", data.id);
			
				switch(data.type){
					case USR_CLERK:  printf("%10s","销售员"); 	break;
					case USR_MANG:	 printf("%10s","经理");  	break;
					case USR_ADMIN:  printf("%10s","系统管理员"); 	break;
				}
				printf("\t%20s\n", data.username);
				printf("\t\t\t========================================================================\n\n");
				printf("\t\t\t----[A]确认删除用户--------------------------------------[R]返回----------\n\n");
				printf("\t\t\t========================================================================\n\n");
				printf("\t\t\t\033[31m 该功能涉及系统安全，请谨慎操作！！！\033[0m\n");
				
				printf("\t\t\t请选择:");
			
				choice=l_getc();
				if('r'==choice || 'R'==choice) break;	
				
				if('a'==choice || 'A'==choice){
					
					if(Account_Srv_DeleteByID(data.id)){
						printf("\n\t\t\t删除成功！\n");
						ret=1;
					}else{
						 printf("\n\t\t\t删除失败！\n");
					}
				
					printf("\n\t\t\t按任意键继续。。。");
					
					getchar();
				}
			}
		
		
       
       }else{
       		printf("\n\t\t\t未找到！");
       		printf("\n\t\t\t按任意键返回。。。");
		cl_stdin();
		getchar();
       	}
       
       return ret;
}
//根据用户账号名查找该用户账号是否存在，存在返回1，否则返回0，并提示错误信息
/*
int Account_UI_Query(char usrName[]) {
	account_t data;
	if( Account_Srv_FetchByName(usrName,&data) ){
		return 1;
	}
	printf("\n\t\t\t该用户不存在！");
	return 0;
}
*/

