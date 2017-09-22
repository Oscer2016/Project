#include "Play_UI.h"
#include "Schedule_UI.h"
#include "Sale_UI.h"
#include "Ticket_UI.h"
#include "../Service/Ticket.h"
#include "../Service/Seat.h"
#include "../Service/Studio.h"
#include "../Service/EntityKey.h"
#include "../Service/Sale.h"
#include "../Common/ComFunction.h"
#include "../Common/Common.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

//引用登陆用户的全局数据结构
extern account_t gl_CurUser;

//根据计划ID，显示演出票
void Sale_UI_ShowTicket(int schID){
	// 请补充完整
	
}

int Sale_UI_SellTicket(ticket_list_t tickList, seat_list_t seatList){
	
	int id;
	ticket_list_t head;
	schedule_t sch;
	
	
	printf("\n\t\t\033[33m请输入演出计划的ID: \033[0m");
			
	while(1){
		if(scanf("%d",&id)==1){ cl_stdin(); break;}
		else{ cl_stdin(); printf("\n\t\t\033[31m您的输入有误！请重新输入: \033[0m"); }
	}
	
	if(!Schedule_Srv_FetchByID(id, &sch)){
		printf("\n\t\t\033[33m未找到ID为%d的演出计划，按任意键返回....\033[0m",id);
		getchar();
		return 0;
	}
	
	Ticket_Srv_FetchBySchID(tickList, sch.id);
			
	Seat_Srv_FetchValidByRoomID(seatList, sch.studio_id);
	
	Ticket_UI_ListBySch(&sch,tickList,seatList);

    return 1;
}

//根据剧目ID显示演出计划
void Sale_UI_ShowScheduler(int playID) {
	int i, id;
	char choice;
	
	play_t pdata;
	ticket_list_t tickList;
	seat_list_t   seatList;
	
	schedule_list_t head;
	schedule_node_t *p;
	Pagination_t paging;
	
	Play_Srv_FetchByID(playID, &pdata);
	
	List_Init(head, schedule_node_t);
	
	paging.offset = 0;
	paging.pageSize = SCHEDULE_PAGE_SIZE;

	//载入数据
	paging.totalRecords = Schedule_Srv_FetchByPlay(head,playID);
	
	Paging_Locate_FirstPage(head, paging);

	do {
		system("clear");
		printf("\n\t\t\033[32m=====================================================================================\033[0m\n\n");
		printf("\t\t\033[36m************************* 剧目：%10s 的演出计划 列表 *************************\033[0m\n\n",pdata.name);
		printf("\t\t\033[33m%-5s\t%-5s\t\t%-5s\033[0m\033[32m\t%-10s\033[0m\033[36m\t%-10s\033[0m\033[33m\t%-5s\033[0m\n", "ID", "剧目ID", "演出厅ID",
				 "放映日期","放映时间","座位数");
		printf("\t\t\033[32m-------------------------------------------------------------------------------------\033[0m\n\n");
		
		if(!List_IsEmpty(head)){
			//显示数据
			Paging_ViewPage_ForEach(head, paging, schedule_node_t, p, i){
				printf("\033[33m\t\t%-5d\t%-5d\t\t%-5d\033[0m", p->data.id, p->data.play_id,p->data.studio_id);
				printf("\033[32m\t\t%4d-%02d-%02d\033[0m",p->data.date.year, p->data.date.month, p->data.date.day);
				printf("\033[36m\t%2d:%02d:%02d\033[0m", p->data.time.hour, p->data.time.minute, p->data.time.second);
				printf("\t\033[33m%5d\033[0m\n", p->data.seat_count);
			}
		}else{
			printf("\n\t\t\033[31m暂无数据！\033[0m\n");
		}

		printf("\n\t\t\033[32m----------------- 共 \033[0m\033[33m%d\033[0m\033[32m 条数据 --------------------------- 页码: %d\033[0m/\033[33m%d\033[0m\033[32m -----------------\033[0m\n\n",
				paging.totalRecords, Pageing_CurPage(paging),
				Pageing_TotalPages(paging));
		printf("\t\t\033[32m======================================================================================\033[0m\n\n");
		printf("\t\t\033[33m----------------------------------- [S]进入售票系统 ----------------------------------\n\n");
		printf("\t\t\033[32m---------- [P]上页 -------------------- [R]返回 ------------------- [N]下页 ----------\n\n");
		printf("\t\t\033[32m======================================================================================\033[0m\n\n");
		printf("\t\t\033[33m请选择: \033[0m");
		
		choice=l_getc();

		switch (choice) {
		
		case 'u':
		case 'U':
			
				printf("\t\t请输入要修改演出计划的ID:");
			
				while(1){
					if(scanf("%d",&id)==1){ cl_stdin(); break;}
					else{ cl_stdin(); printf("\t\t您的输入有误！请重新输入:"); }
				}
			break;
			
		case 's':
		case 'S':
			{
				List_Init(tickList, ticket_node_t);
				List_Init(seatList,   seat_node_t);

				Sale_UI_SellTicket(tickList,seatList);
				
				List_Destroy(tickList, ticket_node_t);
				List_Destroy(seatList,   seat_node_t);
			}
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

void Sale_UI_MgtEntry() {
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
		printf("\t\t\033[32m-------------------------------\033[0m\033[36m [A]根据剧目ID列出演出计划 \033[0m\033[32m----------------------------\033[0m\n\n");
		printf("\t\t\033[33m------[P]上页 -------------------------- [R]返回 ---------------------- [N]下页 ------\033[0m\n\n");
		printf("\t\t\033[32m======================================================================================\033[0m\n\n");
		printf("\t\t\033[33m请选择: \033[0m");
		
		choice=l_getc();

		switch (choice) {
		case 'a':
		case 'A':
				
				printf("\n\t\t\033[33m请输入要浏览的剧目ID: \033[0m");
				while(1){
					if(scanf("%d",&id)==1){ cl_stdin(); break;}
					else{ cl_stdin(); printf("\t\t\033[31m您的输入有误！请重新输入: \033[0m"); }
				}
				Sale_UI_ShowScheduler(id);
				break;
		/*
		case 'u':
		case 'U':
			
			
			break;
		
		*/
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

//退票
void Sale_UI_ReturnTicket(){
	char choice;
	int id,t=0;
	while(1){

		system("clear");
		printf("\t\t\033[32m=======================================================================================\033[0m\n\n");
		printf("\t\t\033[32m--------------------------------------- \033[0m\033[31m退 票\033[0m \033[32m-----------------------------------------\033[0m\n\n");
		printf("\t\t\033[32m=======================================================================================\033[0m\n\n");
		
		printf("\t\t\033[33m请输入票的ID: \033[0m");

	while(1){
			if(scanf("%d",&id)==1){ cl_stdin(); break;}
			else{ cl_stdin(); printf("\t\t\033[31m您的输入有误！请重新输入: \033[0m"); }
	}
		
		
	ticket_t buf;
	schedule_t scd;

	Schedule_Srv_FetchByID(buf.schedule_id, &scd);
	if( Ticket_Srv_FetchByID(id,&buf) ){
		if(buf.status==TICKET_SOLD){
				Ticket_UI_Print(id);
				user_time_t nowtime=TimeNow();
				if(DateCmp(DateNow(), scd.date)==-1 || (DateCmp(DateNow(), scd.date)==0 && scd.time.hour<nowtime.hour && scd.time.minute<nowtime.minute ) ){
				t=1;
				Ticket_UI_Print(id);
				}else{
					printf("\n\t\t\033[31m该票已过有效期，无法退票!\033[0m");
					printf("\n\n\t\t\033[33m按任意键继续....\033[0m\n");
					getchar();
					break;
				}
			}else{

				printf("\n\t\t\033[31m该票不存在或未售出，无法退票!\033[0m");
				printf("\n\n\t\t\033[33m按任意键继续....\033[0m\n");
				getchar();
				break;
			}
		}

	if(t){
		printf("\033[32m\t\t----- [Q]确认退票 -------- [R]返回 -----\033[0m\n");
		printf("\t\t\033[33m请选择: \033[0m");
		choice=l_getc();
		if('r'==choice || 'R'==choice) break;
		if('q'==choice || 'Q'==choice) {

			buf.status=TICKET_AVL;
			sale_t data;

			data.id=EntKey_Srv_CompNewKey("sale");
			data.user_id=gl_CurUser.id;
			data.ticket_id=id;
			data.date=DateNow();
			data.time=TimeNow();
			data.value=buf.price;
			data.type=SALE_RETURN;

			Sale_Srv_Add(&data);

			Ticket_Srv_Modify(&buf);

			printf("\n\t\t\033[33m退票成功！\033[0m");
			printf("\n\n\t\t\033[32m按任意键继续....\033[0m\n");
			cl_stdin();
			getchar();

			break;
			}
		}
	}
}
