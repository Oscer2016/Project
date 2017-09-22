#include "Play_UI.h"
#include "Schedule_UI.h"
#include "../Common/List.h"
#include "../Common/Common.h"
#include "../Common/ComFunction.h"
#include "../Service/Play.h"
#include "../Service/EntityKey.h"

#include <stdio.h>
#include <stdlib.h>

/*void Play_UI_ShowList(play_list_t list, Pagination_t paging) {
	// 请补充完整
}
*/
void Play_UI_MgtEntry(int flag){
	int i, id;
	char choice;

	play_list_t head;
	play_node_t *p;
	Pagination_t paging;

	List_Init(head, play_node_t);
	
	paging.offset = 0;
	paging.pageSize = PLAY_PAGE_SIZE;

	//载入数据
	paging.totalRecords = Play_Srv_FetchAll(head);
	
	Paging_Locate_FirstPage(head, paging);

	do {
		system("clear");
		printf("\n\t\t\033[32m=====================================================================================\033[0m\n\n");
		printf("\t\t\033[33m**************************************\033[0m \033[36m剧目 列表\033[0m \033[33m************************************\033[0m\n\n");
		printf("\t\t%4s\t\033[33m%-20s %-8s\t%s\t\033[0m\033[32m%s\t\033[0m\033[36m%s\t\033[0m\033[31m%s\033[0m\n", "ID", "名称", "地区",
				"时长", "上映日期","下线日期","票价");
		printf("\t\t\033[32m-------------------------------------------------------------------------------------\033[0m\n\n");
		
		
		//显示数据
		Paging_ViewPage_ForEach(head, paging, play_node_t, p, i){
			printf("\t\t%4d\t\033[33m%-20s %-8s\t%d\033[0m", p->data.id, p->data.name,p->data.area, p->data.duration);
			printf("\t\033[32m%4d-%02d-%02d\033[0m", p->data.start_date.year, p->data.start_date.month, p->data.start_date.day);
			printf("\t\033[36m%4d-%02d-%02d\033[0m",p->data.end_date.year, p->data.end_date.month, p->data.end_date.day);
			printf("\t\033[31m%5d\033[0m\n", p->data.price);
		}

		printf("\n\t\t\033[32m----------------- 共 \033[0m\033[33m%d\033[0m\033[32m 条数据 --------------------------- 页码: %d\033[0m/\033[33m%d\033[0m\033[32m -----------------\033[0m\n\n",
				paging.totalRecords, Pageing_CurPage(paging),
				Pageing_TotalPages(paging));
		printf("\t\t\033[32m======================================================================================\033[0m\n\n");
		
		if(flag==0){
			printf("\t\t\033[32m----\033[0m[\033[32mA\033[0m]\033[32m新增---------------\033[0m[\033[31mD\033[0m]\033[31m删除\033[0m\033[32m--------------\033[0m[\033[33mU\033[0m]\033[33m修改\033[0m\033[32m-------------\033[0m[\033[32mS\033[0m]\033[32m演出计划管理----\033[0m\n\n");
		}
		printf("\t\t\033[32m---------------\033[0m[\033[32mP\033[0m]\033[32m上页---------------\033[0m[\033[33mR\033[0m]\033[33m返回\033[0m\033[32m---------------\033[0m[\033[36mN\033[0m]\033[36m下页\033[0m\033[32m--------------------\033[0m\n\n");
		printf("\t\t\033[32m======================================================================================\033[0m\n\n");
		printf("\t\t\033[33m请选择: \033[0m");
		
		choice=l_getc();
	
		switch (choice) {
		case 'a':
		case 'A':
			if (flag==0 && Play_UI_Add()) //新添加成功，跳到最后一页显示
			{
				paging.totalRecords = Play_Srv_FetchAll(head);
				Paging_Locate_LastPage(head, paging, play_node_t);
			}
			break;
		case 'd':
		case 'D':
			if(flag==0){
				printf("\n\t\t\033[32m请输入要删除剧目的ID: \033[0m");
				while(1){
					if(scanf("%d",&id)==1){ cl_stdin(); break;}
					else{ cl_stdin(); printf("\t\t\033[31m您的输入有误！请重新输入: \033[0m"); }
				}
			
				if (Play_UI_Delete(id)) {	//重新载入数据
					paging.totalRecords = Play_Srv_FetchAll(head);
					List_Paging(head, paging, play_node_t);
				}
			}
			break;
		case 'u':
		case 'U':
			if(flag==0){
				printf("\n\t\t\033[32m请输入要修改剧目的ID: \033[0m");
			
				while(1){
					if(scanf("%d",&id)==1){ cl_stdin(); break;}
					else{ cl_stdin(); printf("\t\t\033[31m您的输入有误！请重新输入: \033[0m"); }
				}
			
				if (Play_UI_Modify(id)) {	//重新载入数据
					paging.totalRecords = Play_Srv_FetchAll(head);
					List_Paging(head, paging, play_node_t);
				}
			}
			break;
			
		case 's':
		case 'S':
			if(flag==0){
				printf("\n\t\t\033[32m请输入要计划演出的剧目ID: \033[0m");
			
				while(1){
						if(scanf("%d",&id)==1){ cl_stdin(); break;}
						else{ cl_stdin(); printf("\n\t\t\033[31m您的输入有误！请重新输入: \033[0m"); }
				}
				play_t data;
				if(Play_Srv_FetchByID(id, &data)){
					Schedule_UI_MgtEntry(id);
				}else{
					printf("\n\t\t\033[33m未找到ID为 %d 的剧目！\n",id);
					printf("\n\t\t按任意键返回！\033[0m\n");
					cl_stdin();
					getchar();
				
				}
			}
			break;
		
		case 'p':
		case 'P':
			if (!Pageing_IsFirstPage(paging)) {
				Paging_Locate_OffsetPage(head, paging, -1, play_node_t);
			}
			break;
		case 'n':
		case 'N':
			if (!Pageing_IsLastPage(paging)) {
				Paging_Locate_OffsetPage(head, paging, 1, play_node_t);
			}
			break;
		}
	} while (choice != 'r' && choice != 'R');
	//释放链表空间
	List_Destroy(head, play_node_t);
}

int Play_UI_Add(void)
{
		int er=0,newRecCount=0;
		char choice;
		play_t data;	
		do { 
			system("clear");
			printf("\t\t\033[32m=======================================================================================\033[0m\n\n");
			printf("\t\t\033[32m------------------------------------- \033[0m\033[33m新添 剧目\033[0m \033[32m---------------------------------------\033[0m\n\n");
			printf("\t\t\033[32m=======================================================================================\033[0m\n\n");

			data.id = EntKey_Srv_CompNewKey("play");

			printf("\t\t\033[33m请输入剧目名称: ");
			scanf("%s",data.name);
			//cl_stdin();
			//fgets(data.name,30,stdin);
			//getchar();

			printf("\n\t\t请输入出品地区: ");
			scanf("%s",data.area);

			printf("\n\t\t----[1].电影----[2].戏曲----[3].音乐----\n");
			printf("\t\t请选择剧目类型: \033[0m");
			while(1){
				er=0;
				choice=l_getc();
				switch(choice){
					case '1': data.type= PLAY_TYPE_FILE;   break;
					case '2': data.type=PLAY_TYPE_OPEAR;   break;
					case '3': data.type=PLAY_TYPE_CONCERT; break;
					default : er=1; printf("\n\t\t\033[31m您的选择有误！请重新选择: \033[0m");
				}
				if(er!=1) break;
			}
			printf("\n\t\t\033[32m----[1].儿童----[2].少年----[3].成人----\n");
			printf("\t\t请选择剧目等级: \033[0m");
			while(1){
				er=0;
				choice=l_getc();
				switch(choice){
					case '1': data.rating= PLAY_RATE_CHILD;   break;
					case '2': data.rating= PLAY_RATE_TEENAGE; break;
					case '3': data.rating= PLAY_RATE_ADULT;   break;
					default : er=1; printf("\n\t\t\033[31m您的选择有误！请重新选择: \033[0m");
				}
				if(er!=1) break;
			}
			
			printf("\n\t\t\033[33m请输入剧目时长(分钟): \033[0m");
			while(1){
				if(scanf("%d",&(data.duration))==1){ cl_stdin(); break;}
				else{ cl_stdin(); printf("\t\t\033[31m您输入的时长有误！请重新输入: \033[0m"); }
			}

			printf("\n\t\t\033[32m请输入开始放映日期(年月日之间用空格隔开): \033[0m");
			while(1){
				if(scanf("%d%d%d",&(data.start_date.year), &(data.start_date.month), &(data.start_date.day))==3){
					 cl_stdin();
					 if(data.start_date.month>12 || data.start_date.day>31){
						printf("\t\t\033[31m您输入的 月 或 日 有误！请重新输入: \033[0m");
						continue;
					 }
					 if(DateCmp(data.start_date, DateNow())==-1){
						printf("\t\t\033[31m您输入的日期早于今日！请重新输入: \033[0m");
						continue;
					 }
					 break;
				}
				else{ cl_stdin(); printf("\t\t\033[31m您的输入有误！请重新输入: \033[0m"); }
			}


			printf("\n\t\t\033[36m请输入结束放映日期: \033[0m");
			while(1){
				if(scanf("%d%d%d",&(data.end_date.year), &(data.end_date.month), &(data.end_date.day))==3){
					 cl_stdin();
					 if(data.end_date.month>12 || data.end_date.day>31){
						printf("\t\t\033[31m您输入的 月 或 日 有误！请重新输入: \033[0m");
						continue;
					 }
					 if(DateCmp(data.end_date,data.start_date)==-1){
						printf("\t\t\033[31m您输入的结束放映日期早于开始放映日期！请重新输入: \033[0m");
						continue;

					 }

					 break;
				}
				else{ cl_stdin(); printf("\t\t\033[31m您的输入有误！请重新输入: \033[0m"); }
			}

			printf("\n\t\t\033[33m请输入票价: \033[0m");
			while(1){
				if(scanf("%d",&(data.price))==1){ cl_stdin(); break;}
				else{ cl_stdin(); printf("\t\t\033[31m您的输入有误！请重新输入: \033[0m"); }
			}

			if(Play_Srv_Add(&data)){
				newRecCount++;
				printf("\t\t\033[33m添加成功！\033[0m\n\n");
			}else{ printf("\n\t\t\t\033[31m演出厅添加失败\033[0m\n"); }
			printf("\n\t\t\033[32m---- [A]继续添加 ------ [R]返回----\033[0m\n\n");
			printf("\t\t\033[33m请选择: \033[0m");

			choice=l_getc();
	} while ('a' == choice || 'A' == choice);
	return newRecCount;
}

int Play_UI_Modify(int id){

	int er=0,ret=0;
	char choice;
	play_t data;
	
	if(Play_Srv_FetchByID(id, &data)){
		while(1){ 

			system("clear");
			printf("\t\t\033[32m=======================================================================================\033[0m\n\n");
			printf("\t\t\033[32m------------------------------------- \033[0m\033[33m修改 剧目\033[0m \033[32m---------------------------------------\033[0m\n\n");
			printf("\t\t\033[32m=======================================================================================\033[0m\n\n");

			printf("\t\t\033[32m该剧目信息如下: \033[0m\n\n");
			printf("\t\t%4s\t\033[33m%-20s %-8s\t%s\t\033[0m\033[32m%s\t\033[0m\033[36m%s\t\033[0m\033[31m%s\033[0m\n", "ID", "名称", "地区",
					"时长", "上映日期","下线日期","票价");

			printf("\t\t\033[32m-------------------------------------------------------------------------------------\033[0m\n\n");
			printf("\t\t%4d\t\033[33m%-20s %-8s\t%d\033[0m",data.id,data.name,data.area,data.duration);
			printf("\t\033[32m%4d-%02d-%02d\033[0m",data.start_date.year,data.start_date.month,data.start_date.day);
			printf("\t\033[36m%4d-%02d-%02d\033[0m",data.end_date.year,data.end_date.month,data.end_date.day);
			printf("\t\033[31m%5d\033[0m\n", data.price);

			printf("\t\t\033[32m=======================================================================================\033[0m\n\n");
			printf("\t\t\033[33m------ [A]修改名称 ------------- [B]修改出品地区 ------------- [C]修改类型 ------------\033[0m\n\n");
			printf("\t\t\033[32m------ [D]修改等级 ------------- [E]修改时长	 ------------- [F]修改开始放映日期 ----\033[0m\n\n");
			printf("\t\t\033[36m------ [G]修改结束放映日期 ------ [H]修改票价 ---------------- [R]返回 ----------------\033[0m\n\n");
			printf("\t\t\033[32m=======================================================================================\033[0m\n\n");

			printf("\n\t\t\033[33m请选择: \033[0m");
			
			choice=l_getc();
			if('r'==choice || 'R'==choice) break;	
			switch(choice){
				case 'a':
				case 'A':
					printf("\n\t\t\033[32m请输入剧目名称: \033[0m");
					scanf("%s",data.name);
					cl_stdin();
					//fgets(data.name,30,stdin);
					//getchar();
					break;
					
				case 'b':
				case 'B':
					printf("\n\t\t\033[36m请输入出品地区: \033[0m");
					scanf("%s",data.area);
					break;

				case 'c':
				case 'C':
					printf("\n\t\t\033[33m----[1].电影----[2].戏曲----[3].音乐----\n");
					printf("\t\t请选择剧目类型: \033[0m");
					while(1){
						er=0;
						choice=l_getc();
						switch(choice){
							case '1': data.type= PLAY_TYPE_FILE;   break;
							case '2': data.type=PLAY_TYPE_OPEAR;   break;
							case '3': data.type=PLAY_TYPE_CONCERT; break;
							default : er=1; printf("\t\t\033[31m您的选择有误！请重新选择: \033[0m\n");
						}
						if(er!=1) break;
					}

					break;

				case 'd':
				case 'D':
					printf("\n\t\t\033[32m----[1].儿童----[2].少年----[3].成人----\n");
					printf("\t\t请选择剧目等级: \033[0m");
					while(1){
						er=0;
						choice=l_getc();
						switch(choice){
							case '1': data.rating= PLAY_RATE_CHILD;   break;
							case '2': data.rating= PLAY_RATE_TEENAGE; break;
							case '3': data.rating= PLAY_RATE_ADULT;   break;
							default : er=1; printf("\t\t\033[31m您的选择有误！请重新选择: \033[0m\n");
						}
						if(er!=1) break;
					}

					break;

				case 'e':
				case 'E':
					printf("\n\t\t\033[33m请输入剧目时长(分钟): \033[0m");
					while(1){
						if(scanf("%d",&(data.duration))==1){ cl_stdin(); break;}
						else{ cl_stdin(); printf("\t\t\033[31m您输入的时长有误！请重新输入: \033[0m"); }
					}
					break;

				case 'f':
				case 'F':
					printf("\n\t\t\033[36m请输入开始放映日期(年月日之间用空格隔开): \033[0m");
					while(1){
						if(scanf("%d%d%d",&(data.start_date.year), &(data.start_date.month), &(data.start_date.day))==3){
							 cl_stdin();
							 if(data.start_date.month>12 || data.start_date.day>31){
								printf("\t\t\033[31m您输入的 月 或 日 有误！请重新输入: \033[0m");
								continue;
							 }
							 if(DateCmp(data.start_date, DateNow())==-1){
								printf("\t\t\033[31m您输入的日期早于今日！请重新输入: \033[0m");
								continue;
							 }
							 break;
						}else{ cl_stdin(); printf("\t\t\033[31m您的输入有误！请重新输入: \033[0m"); }
					}
					break;

				case 'g':
				case 'G':
					printf("\n\t\t\033[36m请输入结束放映日期(年月日之间用空格隔开): \033[0m");
					while(1){
						if(scanf("%d%d%d",&(data.end_date.year), &(data.end_date.month), &(data.end_date.day))==3){
							 cl_stdin();
							 if(data.end_date.month>12 || data.end_date.day>31){
								printf("\t\t\033[31m您输入的 月 或 日 有误！请重新输入: \033[0m");
								continue;
							 }
							 if(DateCmp(data.end_date,data.start_date)==-1){
								printf("\t\t\033[31m您输入的结束放映日期早于开始放映日期！请重新输入: \033[0m");
								continue;
							 }
							 break;
						}
						else{ cl_stdin(); printf("\t\t\033[31m您的输入有误！请重新输入: \033[0m"); }
					}
					break;

				case 'h':
				case 'H':

					printf("\n\t\t\033[33m请输入票价: \033[0m");
					while(1){
						if(scanf("%d",&(data.price))==1){ cl_stdin(); break;}
						else{ cl_stdin(); printf("\t\t\033[31m您的输入有误！请重新输入: \033[0m"); }
					}
					break;
			}//switch
				
				if(Play_Srv_Modify(&data)){
					printf("\n\t\t\033[33m修改成功！\033[0m\n");
					ret=1;
				}else{
					printf("\n\t\t\033[31m修改失败！\033[0m\n");
				}
				printf("\n\t\t\033[32m按任意键继续....\033[0m");
				getchar();
		}//while
		
	}else{
		
		printf("\t\t\033[31m未找到ID为 %d 的剧目！\033[0m\n",id);
		printf("\t\t\033[32m按任意键返回！\033[0m\n");
		cl_stdin();
		getchar();
		return 0;
	}

	return ret;
}

int Play_UI_Delete(int id){

	play_t data;
	char choice;
	int ret=0;
	if(Play_Srv_FetchByID(id, &data)){

		system("clear");

		printf("\t\t\033[32m=======================================================================================\033[0m\n\n");
		printf("\t\t\033[32m------------------------------------- \033[0m\033[31m删除 剧目\033[0m \033[32m---------------------------------------\033[0m\n\n");
		printf("\t\t\033[32m=======================================================================================\033[0m\n\n");

		printf("\t\t\033[32m该剧目信息如下: \033[0m\n\n");
		printf("\t\t%4s\t\033[33m%-20s %-8s\t%s\t\033[0m\033[32m%s\t\033[0m\033[36m%s\t\033[0m\033[31m%s\033[0m\n", "ID", "名称", "地区",
				"时长", "上映日期","下线日期","票价");
		printf("\t\t\033[32m---------------------------------------------------------------------------------------\033[0m\n\n");
		printf("\t\t%4d\t\033[33m%-20s %-8s\t%d\033[0m",data.id,data.name,data.area,data.duration);
		printf("\t\033[32m%4d-%02d-%02d\033[0m",data.start_date.year,data.start_date.month,data.start_date.day);
		printf("\t\033[36m%4d-%02d-%02d\033[0m",data.end_date.year,data.end_date.month,data.end_date.day);
		printf("\t\033[31m%5d\033[0m\n", data.price);
		printf("\t\t\033[32m=======================================================================================\033[0m\n\n");
		printf("\t\t\033[32m--------------------\033[0m\033[31m [Q]确认删除 \033[0m\033[32m----------------------\033[0m\033[33m [R]返回 \033[0m\033[32m-----------------------\n\n");
		printf("\t\t=======================================================================================\033[0m\n\n");

		printf("\n\t\t\033[33m请选择: \033[0m");
		while(1){
			choice=l_getc();
	
			if('r'==choice || 'R'==choice) break;
			else if('q'==choice || 'Q'==choice){
			
					if(Play_Srv_DeleteByID(id)){
						printf("\n\t\t\033[33m删除成功!\033[0m\n");
						ret=1;
					}else{
						printf("\n\t\t\033[31m删除失败!\033[0m\n");
					}
					getchar();
			}else{
				printf("\n\t\t\033[31m输入错误!\033[0m\n");
			}
		}
	}else{
		printf("\t\t\033[33m未找到ID为%d的剧目！\033[0m\n",id);
		printf("\t\t\033[32m按任意键返回....\033[0m\n");
		getchar();
		return 0;
	}

	return ret;
}




