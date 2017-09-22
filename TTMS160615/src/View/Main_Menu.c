#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include "../Common/ComFunction.h"
#include "Main_Menu.h"
#include "Schedule_UI.h"
#include "Studio_UI.h"
#include "Play_UI.h"
#include "Queries_Menu.h"
#include "Account_UI.h"
#include "Sale_UI.h"
#include "SalesAnalysis_UI.h"

extern account_t gl_CurUser;

void Main_Menu(void) {
	char choice;
	while(1) {
		system("clear");
		printf("\t\t\033[40;31m<================================================================================================>\033[0m\n");
		printf("\t\t\033[40;36m<\033[0m                                                                                    	         \033[40;36m>\033[0m\n");
		printf("\t\t\033[40;31m<\033[0m\033[33m********************************\033[0m \033[36mTheater Ticket Management System\033[0m\033[33m ******************************\033[0m\033[40;31m>\033[0m\n");
		printf("\t\t\033[40;36m<\033[0m                                                                                    	         \033[40;36m>\033[0m\n");
		printf("\t\t\033[40;31m<\033[0m\t[\033[33mS\033[0m]\033[32mtudio Management.\033[0m\t\t\t\t\t\t[\033[31mU\033[0m]\033[31mpdate password.\t \033[0m\033[40;31m>\033[0m\n");
		printf("\t\t\033[40;36m<\033[0m                                                                                     	         \033[40;36m>\033[0m\n");
		printf("\t\t\033[40;31m<\033[0m\t[\033[33mP\033[0m]\033[32mlay Management.\033[0m\t\t\t\t\t\t[\033[33mL\033[0m]\033[32mogout.\033[0m\t\t \033[40;31m>\033[0m\n");
		printf("\t\t\033[40;36m<\033[0m                                                                                       	 \033[40;36m>\033[0m\n");
		printf("\t\t\033[40;31m<\033[0m\t[\033[33mT\033[0m]\033[32micket Sale.\033[0m  \t\t\t\t\t\t[\033[33mM\033[0m]\033[32msaleanalysis.\033[0m\t \033[40;31m>\033[0m\n");
		printf("\t\t\033[40;36m<\033[0m                                                                                       	 \033[40;36m>\033[0m\n");
		printf("\t\t\033[40;31m<\033[0m\t[\033[33mR\033[0m]\033[32meturn Ticket.\033[0m  \t\t\t\t\t\t[\033[31mA\033[0m]\033[31mccount Management.\t \033[0m\033[40;31m>\033[0m\n");
		printf("\t\t\033[40;36m<\033[0m                                                                                        	 \033[40;36m>\033[0m\n");
		printf("\t\t\033[40;31m<\033[0m\t[\033[33mQ\033[0m]\033[32mueries.\033[0m     \t\t\t\t\t\t\t[\033[33mE\033[0m]\033[32mxit.\033[0m\t\t\t \033[40;31m>\033[0m\n");
		printf("\t\t\033[40;36m<\033[0m                                                                                     	         \033[40;36m>\033[0m\n");
		printf("\t\t\033[40;31m<================================================================================================>\033[0m\n\n");
		printf("\t\t\t\033[32mYour choice: \033[0m");
		
		choice = l_getc();
		if('l'==choice || 'L'==choice){
			if( SysLogout() ) main();
		}
		
		if('e'==choice || 'E'==choice){
			printf("\t\t\t\=======================================================================================\n\n");

			printf("\t\t\t\t\033[33m当前用户为:%s\033[0m\n",gl_CurUser.username);
			choice=prom("\t\t\t\t\033[31m真的要退出吗？\033[0m",'y','n');
			if(1==choice){

				system("clear");
				printf("\n\n\n\n\n\n\n\n\n\n\n\n");
				printf("\t\t\t\t\t\t\t\033[31m 正在退出,请稍候....\033[0m\n");
				sleep(1);
				system("clear");
				system("sl -aF");
				system("clear");
				printf("\n\n\n\n\n\n\n\n\n\n\n\n");
				printf("\n\n\t\t\t\t\t\t\t\033[33m欢迎再次使用！！！\033[0m\n");
				sleep(3);
				system("clear");
				exit(0);
			}
			
		}
		
		switch (choice) {
		case 'S':
		case 's':
			switch(gl_CurUser.type){
				case USR_CLERK:  
				case USR_MANG:  
						printf("\n\t\t\t\t\033[31m您的权限不足，无法进入该菜单！\n\t\t\t\t请按任意键返回....\033[0m");
						cl_stdin();
						getchar();
						break;
						
				case USR_ADMIN:  
						Studio_UI_MgtEntry();
						break;
			}
			
			break;
		case 'P':
		case 'p':
			switch(gl_CurUser.type){
				case USR_CLERK:  
				  
						Play_UI_MgtEntry(1);
						break;
				case USR_MANG:	
				case USR_ADMIN:  
						Play_UI_MgtEntry(0);
						break;
			}
			
			break;
		case 'Q':
		case 'q':
			Queries_Menu();
			break;
		case 'T':
		case 't':
			Sale_UI_MgtEntry();
			break;
		case 'R':
		case 'r':
			Sale_UI_ReturnTicket();
			break;
		//case 'N':
		//case 'n':
		//	SalesAanalysis_UI_MgtEntry();
		//	break;
		case 'A':
		case 'a':
			switch(gl_CurUser.type){
				case USR_CLERK:  
				case USR_MANG:  
					printf("\n\t\t\t\t\033[31m您的权限不足，无法进入该菜单！\n\t\t\t\t请按任意键返回....\033[0m");
						cl_stdin();
						getchar();
						break;
						
				case USR_ADMIN:  
						Account_UI_MgtEntry();
						break;
			}
			break;
			
		case 'u':
		case 'U':
			Change_Passwd();
			break;
			
		case 'm':
		case  'M':
			SalesAanalysis_UI_MgtEntry();
			break;

		}
	} 
}

