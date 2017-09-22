#include "../Common/ComFunction.h"
#include "../Common/Common.h"
#include "../Common/List.h"
#include "Query_Play_UI.h"
#include "../Service/Play.h"
#include "../Persistence/Query_Persist.h"
#include "../Service/Schedule.h"
#include "../Persistence/Schedule_Persist.h"
#include "../Service/Studio.h"
#include "../Persistence/Ticket_Persist.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
void DisplayQueryPlay(void)
{
	play_t data;
	play_node_t *p;
	play_list_t list;
	char choice,str[30]="\0";
	int id,type=2; //查询类型 0 id  1 name
	
	List_Init(list, play_node_t);
	
	while(1){
		system("clear");
       		
		printf("\t\t\033[32m=======================================================================================\033[0m\n\n");
		printf("\t\t\033[32m------------------------------------- \033[0m\033[33m剧目 查询\033[0m \033[32m---------------------------------------\033[0m\n\n");
		printf("\t\t\033[32m=======================================================================================\033[0m\n\n");

		if(type==1){
			
			if(Play_Srv_FetchByName(list,str)){
				printf("\n\033[33m\t\t该剧目信息如下: \033[0m\n");
				printf("\t\t\033[32m=======================================================================================\033[0m\n\n");
				printf("\t\t%4s\t\033[33m%-20s %-8s\t%s\t\033[0m\033[32m%s\t\033[0m\033[36m%s\t\033[0m\033[31m%s\033[0m\n", "ID", "名称", "地区",
								"时长", "上映日期","下线日期","票价");
				printf("\t\t\033[32m-------------------------------------------------------------------------------------\033[0m\n\n");
				List_ForEach(list, p){	
					printf("\t\t%4d\t\033[33m%-20s %-8s\t%d\033[0m", p->data.id, p->data.name,p->data.area, p->data.duration);
					printf("\t\033[32m%4d-%02d-%02d\033[0m", p->data.start_date.year, p->data.start_date.month, p->data.start_date.day);
					printf("\t\033[36m%4d-%02d-%02d\033[0m",p->data.end_date.year, p->data.end_date.month, p->data.end_date.day);
					printf("\t\033[31m%5d\033[0m\n", p->data.price);
				}
				printf("\t\t\033[32m=======================================================================================\033[0m\n\n");
       		
			}else{
	       		printf("\n\t\t\033[31m未找到！\n\t\t按任意键返回....\n\033[0m");
	       		getchar();
	       		getchar();
			}
		}
       		
		if(type==0 ){
			if( Play_Srv_FetchByID(id, &data)){
				printf("\n\033[33m\t\t该剧目信息如下: \033[0m\n");
				printf("\t\t\033[32m=======================================================================================\033[0m\n\n");
				printf("\t\t%4s\t\033[33m%-20s %-8s\t%s\t\033[0m\033[32m%s\t\033[0m\033[36m%s\t\033[0m\033[31m%s\033[0m\n", "ID", "名称", "地区",
								"时长", "上映日期","下线日期","票价");
				printf("\t\t\033[32m-------------------------------------------------------------------------------------\033[0m\n\n");
				printf("\t\t%4d\t\033[33m%-20s %-8s\t%d\033[0m",data.id,data.name,data.area,data.duration);
				printf("\t\033[32m%4d-%02d-%02d\033[0m",data.start_date.year,data.start_date.month,data.start_date.day);
				printf("\t\033[36m%4d-%02d-%02d\033[0m",data.end_date.year,data.end_date.month,data.end_date.day);
				printf("\t\033[31m%5d\033[0m\n", data.price);
				//printf("\t\t\033[32m=======================================================================================\033[0m\n\n");
			}else{
	    		printf("\n\t\t\033[31m未找到！\n\t\t按任意键返回....\n\033[0m");
				getchar();
			}
		}
		printf("\t\t\033[32m=======================================================================================\033[0m\n\n");
		printf("\t\t\033[33m------ [A]根据ID查询 ----------------- [B]根据名称查询 ----------------- [R]返回 ------\033[0m\n\n");
		printf("\t\t\033[32m=======================================================================================\033[0m\n\n");
		printf("\t\t\033[33m请选择: \033[0m");
			
		choice=l_getc();
		if('r'==choice || 'R'==choice) break;	
		switch(choice){
					
			case 'a':
			case 'A':
				printf("\t\t\033[32m请输入要查询的ID: \033[0m");
				while(1){
					if(scanf("%d",&id)==1){ cl_stdin(); break;}
					else{ cl_stdin(); printf("\n\t\t\033[31m您的输入有误！请重新输入: \033[0m"); }
				}
				type=0;
				break;

			case 'b':
			case 'B':
				printf("\n\t\t\033[33m请输入要查询的剧目名称: \033[0m");
				scanf("%s",str);
				type=1;
				break;
		}
	}
}


