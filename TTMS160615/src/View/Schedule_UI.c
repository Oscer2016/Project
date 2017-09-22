#include "Schedule_UI.h"
#include "Play_UI.h"

#include "../Common/List.h"
#include "../Common/ComFunction.h"
#include "../Service/Schedule.h"
#include "../Service/Play.h"
#include "../Service/Studio.h"
#include "../Service/EntityKey.h"
#include "../Persistence/Query_Persist.h"

#include <stdio.h>
#include <stdlib.h>

void Schedule_UI_ListAll(void) {
	int i, id;
	char choice;

	schedule_list_t head;
	schedule_node_t *p;
	Pagination_t paging;

	List_Init(head, schedule_node_t);
	
	paging.offset = 0;
	paging.pageSize = SCHEDULE_PAGE_SIZE;

	//载入数据
	paging.totalRecords = Schedule_Srv_FetchAll(head);
	
	Paging_Locate_FirstPage(head, paging);

	do {
		system("clear");
		printf("\n\t\t\033[32m=====================================================================================\033[0m\n\n");
		printf("\t\t\033[33m************************************\033[0m \033[36m演出计划 列表\033[0m \033[33m**********************************\033[0m\n\n");
		printf("\t\t%4s\t\t\033[33m%-20s %-8s\t%s\t\033[0m\033[32m%s\t\033[0m\033[36m%s\t\033[0m\n", "ID", "剧目ID", "演出厅ID",
				 "放映日期","放映时间","座位数");
		printf("\t\t\033[32m-------------------------------------------------------------------------------------\033[0m\n\n");

		//显示数据
		Paging_ViewPage_ForEach(head, paging, schedule_node_t, p, i){
			printf("\t\t%4d\t\t\033[33m%-20d %-8d\033[0m", p->data.id, p->data.play_id,p->data.studio_id);
			printf("\t\033[32m%4d-%02d-%02d\033[0m",p->data.date.year, p->data.date.month, p->data.date.day);
			printf("\t\033[36m%4d:%02d:%02d\033[0m", p->data.time.hour, p->data.time.minute, p->data.time.second);
			printf("\t\033[36m%5d\033[0m\n", p->data.seat_count);
		}

		printf("\n\t\t\033[32m----------------- 共 \033[0m\033[33m%d\033[0m\033[32m 条数据 --------------------------- 页码: %d\033[0m/\033[33m%d\033[0m\033[32m -----------------\033[0m\n\n",
						paging.totalRecords, Pageing_CurPage(paging),
						Pageing_TotalPages(paging));
		printf("\t\t\033[32m======================================================================================\033[0m\n\n");

		printf("\t\t\033[32m---------------\033[0m[\033[32mP\033[0m]\033[32m上页---------------\033[0m[\033[33mR\033[0m]\033[33m返回\033[0m\033[32m---------------\033[0m[\033[36mN\033[0m]\033[36m下页\033[0m\033[32m--------------------\033[0m\n\n");
		
		printf("\t\t\033[32m======================================================================================\033[0m\n\n");
		printf("\t\t\033[33m请选择: \033[0m");
		
		choice=l_getc();
	

		switch (choice) {
		
		
		case 'p':
		case 'P':
			if (!Pageing_IsFirstPage(paging)) {
				Paging_Locate_OffsetPage(head, paging, -1, schedule_node_t);
			}
			break;
		case 'n':
		case 'N':
			if (!Pageing_IsLastPage(paging)) {
				Paging_Locate_OffsetPage(head, paging, 1, schedule_node_t);
			}
			break;
		}
	} while (choice != 'r' && choice != 'R');
	//释放链表空间
	List_Destroy(head, schedule_node_t);
}

int Schedule_UI_Add(int play_id) {
	int er=0,sid,newRecCount=0;
       	char choice;
	schedule_t data;
	studio_t sdata;	
	play_t pdata;
		do { 

			system("clear");
			printf("\t\t\033[32m=======================================================================================\033[0m\n\n");
			printf("\t\t\033[32m------------------------------------ \033[0m\033[33m新添 演出计划\033[0m \033[32m--------------------------------------\033[0m\n\n");
			printf("\t\t\033[32m=======================================================================================\033[0m\n\n");
				
			data.id = EntKey_Srv_CompNewKey("schedule");


			data.play_id=play_id;


			printf("\t\t\033[33m请输入演播厅的ID: \033[0m");

			while(1){
				if(scanf("%d",&sid)==1){
				 cl_stdin();

				 if(!Studio_Srv_FetchByID(sid, &sdata)){
					printf("\n\t\t\033[31mID为 %d 的演出厅不存在，请重新输入: \033[0m",sid);
					continue;
				 }

				data.studio_id=sdata.id;
				data.seat_count=Seat_Number_Count(data.studio_id);

				 break;
				}
				else{ cl_stdin(); printf("\n\t\t\033[31m您的输入有误！请重新输入: \033[0m"); }
			}




			printf("\n\t\t\033[32m请输入开始放映日期(年月日之间用空格隔开): \033[0m");
			while(1){
				if(scanf("%d%d%d",&(data.date.year), &(data.date.month), &(data.date.day))==3){
					 cl_stdin();
					 if(data.date.month>12 || data.date.day>31){
						 printf("\t\t\033[31m您输入的 月 或 日 有误！请重新输入: \033[0m");
						continue;
					 }
					 if(DateCmp(data.date, DateNow())==-1){
						 printf("\t\t\033[31m您输入的日期早于今日！请重新输入: \033[0m");
						 continue;
					 
					 }
					 
					 break;
				}
				else{ cl_stdin(); printf("\t\t\033[31m您的输入有误！请重新输入: \033[0m"); }
			}


			printf("\n\t\t\033[33m请输入放映时间(小时和分钟之间用空格隔开): \033[0m");
			while(1){
				if(scanf("%d%d",&(data.time.hour), &(data.time.minute))==2){
					data.time.second=0;
					cl_stdin();
					if(data.time.hour>24 || data.time.minute>60){
						printf("\t\t\033[31m您输入的 时 或 分 有误！请重新输入: \033[0m");
						continue;
					 }
					 break;
				}
				else{ cl_stdin(); printf("\t\t\033[31m您的输入有误！请重新输入: \033[0m"); }
			}

			if(Schedule_Srv_Add(&data)){
				Ticket_Srv_AddBatch(data.id, data.studio_id);
				newRecCount++;
				printf("\t\t\033[33m添加成功！\033[0m\n\n");
			}else{
				
			}
			printf("\n\t\t\033[32m---- [A]继续添加 ------ [R]返回----\033[0m\n\n");
			printf("\t\t\033[33m请选择: \033[0m");

			choice=l_getc();
		} while ('a' == choice || 'A' == choice);
	return newRecCount;
			
     
}

int Schedule_UI_Modify(int id){
	int ret=0,sid;
       	char choice;
	schedule_t data;
	studio_t sdata;	
	
	if(Schedule_Perst_SelectByID(id, &data)){
		
		while(1){ 

				system("clear");
				printf("\t\t\033[32m=======================================================================================\033[0m\n\n");
				printf("\t\t\033[32m------------------------------------ \033[0m\033[33m修改 演出计划\033[0m \033[32m--------------------------------------\033[0m\n\n");
				printf("\t\t\033[32m=======================================================================================\033[0m\n\n");
				printf("\t\t%4s\t\t\033[33m%-20s %-8s\t%s\t\033[0m\033[32m%s\t\033[0m\033[36m%s\t\033[0m\n", "ID", "剧目ID", "演出厅ID",
								 "放映日期","放映时间","座位数");
				printf("\t\t\033[32m-------------------------------------------------------------------------------------\033[0m\n\n");




				printf("\t\t%4d\t\t\033[33m%-20d %-8d\033[0m", data.id, data.play_id,data.studio_id);
			
				printf("\t\033[32m%4d-%02d-%02d\033[0m",data.date.year, data.date.month, data.date.day);
				printf("\t\033[36m%4d:%02d:%02d\033[0m", data.time.hour, data.time.minute, data.time.second);
				printf(" \t\033[36m%5d\033[0m\n", data.seat_count);
				
				printf("\t\t\033[32m=======================================================================================\033[0m\n\n");
				printf("\t\t\033[33m------- [A]修改演出厅 ------------- [B]修改放映时间 ------------- [R]返回 ------------\033[0m\n");
				printf("\t\t\033[32m=======================================================================================\033[0m\n\n");

				printf("\n\t\t\033[33m请选择: \033[0m");

				choice=l_getc();
				if('r'==choice || 'R'==choice) break;	
				switch(choice){
					
					case 'a':
					case 'A':
				
						printf("\n\t\t\033[33m请输入演播厅的ID: \033[0m");
				
						while(1){
							if(scanf("%d",&sid)==1){
							 cl_stdin(); 
							 
							 if(!Studio_Srv_FetchByID(sid, &sdata)){
							 	printf("\n\t\t\033[31mID为 %d 的演出厅不存在，请重新输入: \033[0m",sid);
							 	continue;
							 }
							 
							data.studio_id=sdata.id;
							data.seat_count=Seat_Number_Count(data.studio_id);
							 
							 break;
							}
							else{ cl_stdin(); printf("\n\t\t\033[31m您的输入有误！请重新输入: \033[0m"); }
						}
						break;
		
					case 'b':
					case 'B':
						printf("\n\t\t\033[32m请输入开始放映日期(年月日之间用空格隔开): \033[0m");
						while(1){
							if(scanf("%d%d%d",&(data.date.year), &(data.date.month), &(data.date.day))==3){
								 cl_stdin();
								 if(data.date.month>12 || data.date.day>31){
									 printf("\t\t\033[31m您输入的 月 或 日 有误！请重新输入: \033[0m");
									continue;
								 }
								 if(DateCmp(data.date, DateNow())==-1){
									 printf("\t\t\033[31m您输入的日期早于今日！请重新输入: \033[0m");
									 continue;
								 
								 }
								 break;
							}
							else{ cl_stdin(); printf("\t\t\033[31m您的输入有误！请重新输入: \033[0m"); }
						}


						printf("\n\t\t\033[33m请输入放映时间(小时和分钟之间用空格隔开): \033[0m");
						while(1){
							if(scanf("%d%d",&(data.time.hour), &(data.time.minute))==2){
								data.time.second=0;
								cl_stdin();
								if(data.time.hour>24 || data.time.minute>60){
									printf("\t\t\033[31m您输入的 时 或 分 有误！请重新输入: \033[0m");
									continue;
								 }
								 break;
							}
							else{ cl_stdin(); printf("\t\t\033[31m您的输入有误！请重新输入: \033[0m"); }
						}
						
						break;
				
				}//switch

				if(Schedule_Srv_Modify(&data)){
					
					printf("\n\t\t\033[33m修改成功！\033[0m\n");
					ret=1;
				}else{
					printf("\n\t\t\033[31m修改失败！\033[0m\n");
				}
				
				printf("\n\t\t\033[33m按任意键继续....\033[0m");
		
				getchar();
		
		}//while
	}else{
		printf("\n\t\t\033[31m未找到ID为 %d 的演出计划！\033[0m\n",id);
		printf("\t\t\033[33m按任意键返回....\033[0m\n");
		//cl_stdin();
		getchar();
		return 0;
	}
       return ret;
}

int Schedule_UI_Delete(int id){

	int ret=0,sid;
       	char choice;
	schedule_t data;
	studio_t sdata;	
	
	if(Schedule_Srv_FetchByID(id, &data)){
		
		while(1){ 

				system("clear");
				printf("\t\t\033[32m=======================================================================================\033[0m\n\n");
				printf("\t\t\033[32m------------------------------------ \033[0m\033[33m删除 演出计划\033[0m \033[32m--------------------------------------\033[0m\n\n");
				printf("\t\t\033[32m=======================================================================================\033[0m\n\n");
				printf("\t\t%4s\t\t\033[33m%-20s %-8s\t%s\t\033[0m\033[32m%s\t\033[0m\033[36m%s\t\033[0m\n", "ID", "剧目ID", "演出厅ID",
								 "放映日期","放映时间","座位数");
				printf("\t\t\033[32m-------------------------------------------------------------------------------------\033[0m\n\n");

				printf("\t\t%4d\t\t\033[33m%-20d %-8d\033[0m", data.id, data.play_id,data.studio_id);
			
				printf("\t\033[32m%4d-%02d-%02d\033[0m",data.date.year, data.date.month, data.date.day);
				printf("\t\033[36m%4d:%02d:%02d\033[0m", data.time.hour, data.time.minute, data.time.second);
				printf(" \t\033[36m%5d\033[0m\n", data.seat_count);
				
				printf("\t\t\033[32m=======================================================================================\033[0m\n\n");
				printf("\t\t\033[32m--------------------\033[0m\033[31m [Q]确认删除 \033[0m\033[32m----------------------\033[0m\033[33m [R]返回 \033[0m\033[32m-----------------------\n\n");
				printf("\t\t=======================================================================================\033[0m\n\n");

				printf("\n\t\t\033[33m请选择: \033[0m");
				choice=l_getc();
				if('r'==choice || 'R'==choice) break;	
				
				if('q'==choice || 'Q'==choice){
					
					if(Schedule_Srv_DeleteByID(id)){
						printf("\n\t\t\033[33m删除成功!\033[0m\n");
						ret=1;
					}else{
						printf("\n\t\t\033[31m删除失败!\033[0m\n");
					}

					printf("\n\t\t\033[33m按任意键继续删除....\033[0m\n");
					cl_stdin();
					getchar();

				}
	
		}//while
	}else{
		printf("\n\t\t\033[31m未找到ID为 %d 的演出计划！\033[0m\n",id);
		printf("\t\t\033[33m按任意键返回....\033[0m\n");
		
		getchar();
		return 0;
	}
       return ret;
}

int Schedule_UI_Query(int id){

	
	int ret=0;
	
	schedule_t data;
	if(id==-1){
		printf("\n\t\t\033[33m请输入要查询演出计划的ID: \033[0m");
			
		while(1){
			if(scanf("%d",&id)==1){ cl_stdin(); break;}
			else{ cl_stdin(); printf("\n\t\t\033[31m您的输入有误！请重新输入: \033[0m"); }
		}
	}
	
	if(Schedule_Srv_FetchByID(id, &data)){
		//while(1){ 

				system("clear");
				printf("\t\t\033[32m=======================================================================================\033[0m\n\n");
				printf("\t\t\033[32m---------------------------------- \033[0m\033[33m根据ID查询 演出计划\033[0m \033[32m------------------------------------\033[0m\n\n");
				printf("\t\t\033[32m=======================================================================================\033[0m\n\n");
				printf("\t\t%4s\t\t\033[33m%-20s %-8s\t%s\t\033[0m\033[32m%s\t\033[0m\033[36m%s\t\033[0m\n", "ID", "剧目ID", "演出厅ID",
								 "放映日期","放映时间","座位数");
				printf("\t\t\033[32m-------------------------------------------------------------------------------------\033[0m\n\n");

				printf("\t\t%4d\t\t\033[33m%-20d %-8d\033[0m", data.id, data.play_id,data.studio_id);

				printf("\t\033[32m%4d-%02d-%02d\033[0m",data.date.year, data.date.month, data.date.day);
				printf("\t\033[36m%4d:%02d:%02d\033[0m", data.time.hour, data.time.minute, data.time.second);
				printf(" \t\033[36m%5d\033[0m\n", data.seat_count);

				printf("\t\t\033[32m=======================================================================================\033[0m\n\n");
	}else{
		printf("\n\t\t\033[31m未找到ID为 %d 的演出计划！\033[0m\n",id);
	}	
	printf("\n\t\t\033[33m按任意键返回....\033[0m");
	getchar();	

	return ret;
}

/*以列表模式显示给定剧目的演出计划信息*/
void Schedule_UI_ListByPlay(const play_t *play, schedule_list_t list, Pagination_t paging){

	//Schedule_UI_MgtEntry(play->play_id)
}

void Schedule_UI_MgtEntry(int play_id){

	int i, id;
	char choice;
	
	play_t pdata;
	
	schedule_list_t head;
	schedule_node_t *p;
	Pagination_t paging;
	
	Play_Srv_FetchByID(play_id, &pdata);
	
	List_Init(head, schedule_node_t);
	
	paging.offset = 0;
	paging.pageSize = SCHEDULE_PAGE_SIZE;

	//载入数据
	paging.totalRecords = Schedule_Srv_FetchByPlay(head,play_id);
	
	Paging_Locate_FirstPage(head, paging);

	do {
		system("clear");
		printf("\n\t\t\033[32m=====================================================================================\033[0m\n\n");
		printf("\t\t\033[33m************************************\033[0m \033[36m演出计划 列表\033[0m \033[33m**********************************\033[0m\n\n");
		printf("\t\t%4s\t\t\033[33m%-20s %-8s\t%s\t\033[0m\033[32m%s\t\033[0m\033[36m%s\t\033[0m\n", "ID", "剧目ID", "演出厅ID",
				 "放映日期","放映时间","座位数");
		printf("\t\t\033[32m-------------------------------------------------------------------------------------\033[0m\n\n");

		//显示数据
		Paging_ViewPage_ForEach(head, paging, schedule_node_t, p, i){
			printf("\t\t%4d\t\t\033[33m%-20d %-8d\033[0m", p->data.id, p->data.play_id,p->data.studio_id);
			printf("\t\033[32m%4d-%02d-%02d\033[0m",p->data.date.year, p->data.date.month, p->data.date.day);
			printf("\t\033[36m%4d:%02d:%02d\033[0m", p->data.time.hour, p->data.time.minute, p->data.time.second);
			printf("\t\033[36m%5d\033[0m\n", p->data.seat_count);
		}

		printf("\n\t\t\033[32m----------------- 共 \033[0m\033[33m%d\033[0m\033[32m 条数据 --------------------------- 页码: %d\033[0m/\033[33m%d\033[0m\033[32m -----------------\033[0m\n\n",
				paging.totalRecords, Pageing_CurPage(paging),
				Pageing_TotalPages(paging));
		printf("\t\t\033[32m======================================================================================\033[0m\n\n");
		printf("\t\t\033[33m----------- [A]新增演出计划 ---------------------------- [D]删除演出计划 -------------\033[0m\n\n");
		printf("\t\t\033[34m----------- [U]修改演出计划 -----------------------------[S] 浏览所有演出计划 --------\n\n");
		
		printf("\t\t\033[32m---------------\033[0m[\033[32mP\033[0m]\033[32m上页---------------\033[0m[\033[33mR\033[0m]\033[33m返回\033[0m\033[32m---------------\033[0m[\033[36mN\033[0m]\033[36m下页\033[0m\033[32m--------------------\033[0m\n\n");
		printf("\t\t\033[32m======================================================================================\033[0m\n\n");
		printf("\t\t\033[33m请选择: \033[0m");
		choice=l_getc();
	

		switch (choice) {
		case 'a':
		case 'A':
			{
				studio_list_t slist;
				
				List_Init(slist, studio_node_t);

				Studio_Srv_FetchAll(slist);

				if(!List_IsEmpty(slist) ){
					if (Schedule_UI_Add(pdata.id)) //新添加成功，跳到最后一页显示
					{
						paging.totalRecords =Schedule_Srv_FetchByPlay(head,play_id);
						Paging_Locate_LastPage(head, paging, schedule_node_t);
					}
				}else{
					if(List_IsEmpty(slist)){
						printf("\n\t\t\033[31m还未添加演出厅，不能添加演出计划!\033[0m");
					}
					printf("\t\t\033[33m按任意键返回....\033[0m");
					getchar();
				}
				
			}
			break;
		case 'd':
		case 'D':
			
			printf("\n\t\t\033[33m请输入要删除演出计划的ID:\033[0m");
			while(1){
					if(scanf("%d",&id)==1){ cl_stdin(); break;}
					else{ cl_stdin(); printf("\n\t\t\033[31m您的输入有误！请重新输入: \033[0m"); }
				}
			
			if (Schedule_UI_Delete(id)) {	//重新载入数据
					paging.totalRecords = Schedule_Srv_FetchByPlay(head,play_id);
					List_Paging(head, paging, schedule_node_t);
			}
			
			break;
		case 'u':
		case 'U':
			
				printf("\n\t\t\033[33m请输入要修改演出计划的ID: \033[0m");
			
				while(1){
					if(scanf("%d",&id)==1){ cl_stdin(); break;}
					else{ cl_stdin(); printf("\n\t\t\033[31m您的输入有误！请重新输入: \033[0m"); }
				}
			
				if (Schedule_UI_Modify(id)) {	//重新载入数据
					paging.totalRecords = Schedule_Srv_FetchByPlay(head,play_id);
					List_Paging(head, paging, schedule_node_t);
				}
			break;
			
		case 's':
		case 'S':
			Schedule_UI_ListAll();
			break;
			
		case 't':
		case 'T':
			//();
			break;
		
		case 'p':
		case 'P':
			if (!Pageing_IsFirstPage(paging)) {
				Paging_Locate_OffsetPage(head, paging, -1, schedule_node_t);
			}
			break;
		case 'n':
		case 'N':
			if (!Pageing_IsLastPage(paging)) {
				Paging_Locate_OffsetPage(head, paging, 1, schedule_node_t);
			}
			break;
		}
	} while (choice != 'r' && choice != 'R');
	//释放链表空间
	List_Destroy(head, schedule_node_t);
}
