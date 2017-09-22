/*
 * Studio_UI.c
 *
 *  Created on: 2016年6月17日
 *      Author: Administrator
 */
#include "Studio_UI.h"
#include "../Common/List.h"
#include "../Common/ComFunction.h"
#include "../Service/Studio.h"
#include "../Service/Seat.h"
#include "../Service/EntityKey.h"
#include "Seat_UI.h"

static const int STUDIO_PAGE_SIZE = 4;
#include <stdio.h>

void Studio_UI_MgtEntry(void) {

	int i, id;
	char choice;

	studio_list_t head;
	studio_node_t *pos;
	Pagination_t paging;

	List_Init(head, studio_node_t);
	paging.offset = 0;
	paging.pageSize = STUDIO_PAGE_SIZE;

	//载入数据
	paging.totalRecords = Studio_Srv_FetchAll(head);
	Paging_Locate_FirstPage(head, paging);

	do {
		system("clear");
		printf("\t\t\t\033[32m======================================================================================\n\n");
		printf("\t\t\t*******************************\033[0m\033[36m 演出厅 管理 界面 \033[0m\033[32m*************************************\033[0m\n\n");
		printf("\t\t\t\t\033[34m%3s\t%s\033[0m\033[33m\t%s\033[0m\t\033[36m%s\033[0m\033[34m\t%s\033[0m\n", "演出厅ID", "演出厅名称", "座位行数",
				"座位列数", "座位总数");
		printf("\t\t\t\033[32m--------------------------------------------------------------------------------------\033[0m\n\n");
		//显示数据
		Paging_ViewPage_ForEach(head, paging, studio_node_t, pos, i){
			printf("\t\t\t\t\033[34m%5d\t\t%s\033[0m\033[33m\t%5d\033[0m\t\t\033[36m%5d\033[0m\033[34m\t\t%5d\033[0m\n", pos->data.id,
					pos->data.name, pos->data.rowsCount, pos->data.colsCount,
					pos->data.seatsCount);
		}

		printf("\n\t\t\t\033[32m----------------- 共 \033[0m\033[33m%d\033[0m\033[32m 条数据 --------------------------- 页码: %d\033[0m/\033[33m%d\033[0m\033[32m -----------------\033[0m\n\n",
				paging.totalRecords, Pageing_CurPage(paging),
				Pageing_TotalPages(paging));
		printf("\t\t\t\033[32m======================================================================================\033[0m\n\n");

		printf("\t\t\t\033[33m----- [A]添加 ------------ [D]删除 ------------ [U]修改 ------------ [S]座位管理 -----\033[0m\n\n");
		printf("\t\t\t\033[32m---------------\033[0m[\033[32mP\033[0m]\033[32m上页---------------\033[0m[\033[33mR\033[0m]\033[33m返回\033[0m\033[32m---------------\033[0m[\033[36mN\033[0m]\033[36m下页\033[0m\033[32m--------------------\033[0m\n\n");
		printf("\t\t\t\033[32m======================================================================================\033[0m\n\n");
		printf("\t\t\t\033[33m请选择: \033[0m");

		choice=l_getc();

		switch (choice) {
		case 'a':
		case 'A':
			if (Studio_UI_Add()) //新添加成功，跳到最后一页显示
			{
				paging.totalRecords = Studio_Srv_FetchAll(head);
				Paging_Locate_LastPage(head, paging, studio_node_t);
			}
			break;
		case 'd':
		case 'D':
			{char ch;
			printf("\n\t\t\t\033[33m请输入ID: \033[0m");
			while(1){
					if(scanf("%d",&id)==1){ cl_stdin(); break;}
					else{ cl_stdin(); printf("\n\t\t\t\033[31m您的输入有误！请重新输入: \033[0m"); }
			}
			printf("\n\t\t\t\033[31m确认删除[y/n]: \033[0m");
			ch=l_getc();
			if(ch=='Y' || ch== 'y')
			{
			  if (Studio_UI_Delete(id)) {	//从新载入数据
				paging.totalRecords = Studio_Srv_FetchAll(head);
				List_Paging(head, paging, studio_node_t);
			}
			}
			break;
			}
		case 'u':
		case 'U':
			printf("\n\t\t\t\033[33m请输入ID: \033[0m");
			while(1){
				if(scanf("%d",&id)==1){ cl_stdin(); break;}
				else{ cl_stdin(); printf("\n\t\t\t\033[31m您的输入有误！请重新输入: \033[0m"); }
			}
			if (Studio_UI_Modify(id)) {	//从新载入数据
				paging.totalRecords = Studio_Srv_FetchAll(head);
				List_Paging(head, paging, studio_node_t);
			}
			break;
		case 's':
		case 'S':
			printf("\n\t\t\t\033[33m请输入ID: \033[0m");
			while(1){
					if(scanf("%d",&id)==1){ cl_stdin(); break;}
					else{ cl_stdin(); printf("\n\t\t\t\033[31m您的输入有误！请重新输入: \033[0m"); }
			}
			Seat_UI_MgtEntry(id);
			paging.totalRecords = Studio_Srv_FetchAll(head);
			List_Paging(head, paging, studio_node_t);
			break;
		case 'p':
		case 'P':
			if (!Pageing_IsFirstPage(paging)) {
				Paging_Locate_OffsetPage(head, paging, -1, studio_node_t);
			}
			break;
		case 'n':
		case 'N':
			if (!Pageing_IsLastPage(paging)) {
				Paging_Locate_OffsetPage(head, paging, 1, studio_node_t);
			}
			break;
		}
	} while (choice != 'r' && choice != 'R');
	//释放链表空间
	List_Destroy(head, studio_node_t);
}

int Studio_UI_Add(void) {
	studio_t rec;
	int newRecCount = 0,er;
	char choice;

	do {
		system("clear");
		printf("\t\t\t\033[32m======================================================================================\n\n");
		printf("\t\t\t*******************************\033[0m\033[36m 演出厅 添加 界面 \033[0m\033[32m*************************************\033[0m\n\n");
		printf("\t\t\t\033[32m--------------------------------------------------------------------------------------\033[0m\n\n");
		printf("\t\t\t\033[33m请输入演出厅名称: \033[0m");
		//cl_stdin();
		scanf("%s",rec.name);
		
		printf("\n\t\t\t\033[36m请输入座位行数: \033[0m");
		while(1){
			if(scanf("%d", &(rec.rowsCount))==1){ cl_stdin(); break;}
			else {cl_stdin(); printf("\n\t\t\t\033[31m您的输入有误！请重新输入: \033[0m");}
		}
		
		printf("\n\t\t\t\033[34m请输入座位列数: \033[0m");
		while(1){
			if(scanf("%d", &(rec.colsCount))==1){ cl_stdin(); break;}
			else {cl_stdin(); printf("\n\t\t\t\033[31m您的输入有误！请重新输入: \033[0m");}
			}
		rec.seatsCount = rec.rowsCount * rec.colsCount;
		printf("\t\t\t================================================================================\n");

		//获取主键
		rec.id = EntKey_Srv_CompNewKey("Studio");

		if (Studio_Srv_Add(&rec)) {
			seat_list_t list=NULL;
			Seat_Srv_RoomInit(list,rec.id,rec.rowsCount,rec.colsCount);
			newRecCount += 1;
			printf("\n\t\t\t\033[33m演出厅添加成功!\033[0m\n");
		} else { printf("\n\t\t\t\033[31m演出厅添加失败\033[0m\n"); }

		printf("\n\t\t\t\033[32m---- [A]继续添加 ------ [R]返回----\033[0m\n\n");
		printf("\t\t\t\033[33m请选择: \033[0m");
		
		scanf("%c", &choice);
	} while ('a' == choice || 'A' == choice);
	return newRecCount;
}

int Studio_UI_Modify(int id) {
	studio_t rec;
	int rtn = 0;
	int newrow, newcolumn;
	seat_list_t list;
	int seatcount;
	char choice;
	int t;

	/*Load record*/
	if (!Studio_Srv_FetchByID(id, &rec)) {
		printf("\n\t\t\t\033[31m演出厅不存在!\033[0m\n\t\t\t\033[33m按任意键退出....\033[0m\n");
		getchar();
		return 0;
	}
	system("clear");
	printf("\t\t\t\033[32m======================================================================================\n\n");
	printf("\t\t\t*******************************\033[0m\033[36m 演出厅 修改 界面 \033[0m\033[32m*************************************\033[0m\n\n");
	printf("\t\t\t\t\033[34m%3s\t%s\033[0m\033[33m\t%s\033[0m\t\033[36m%s\033[0m\033[34m\t%s\033[0m\n", "演出厅ID", "演出厅名称", "座位行数",
			"座位列数", "座位总数");
	printf("\t\t\t\033[32m--------------------------------------------------------------------------------------\033[0m\n\n");
	printf("\t\t\t\t\033[34m%5d\t\t%s\033[0m\033[33m\t%5d\033[0m\t\t\033[36m%5d\033[0m\033[34m\t\t%5d\033[0m\n", rec.id,rec.name, rec.rowsCount, rec.colsCount,rec.seatsCount);

	printf("\t\t\t\033[32m======================================================================================\033[0m\n\n");
	printf("\t\t\t\033[33m------- [1]修改演出厅名称 ---------- [2]修改座位行数 ---------- [3]修改座位列数 --------\033[0m\n\n");
	printf("\t\t\t\033[32m======================================================================================\033[0m\n\n");
	printf("\t\t\t\033[33m请选择: \033[0m");

	//printf("\t\t\t演出厅ID:%d\n", rec.id);
	choice=l_getc();
	switch(choice)
	{
	case '1':
		printf("\n\t\t\t\033[33m演出厅名称: \033[0m");
		fflush(stdin);
		scanf("%s",rec.name);
		break;
	case '2':
	   t=2;
	   break;
	case '3':
	   t=3;
	  break;
	 }
	List_Init(list, seat_node_t);
	seatcount = Seat_Srv_FetchByRoomID(list, rec.id);
	if (seatcount) {
		//do {	
				//如果座位文件中已有座位信息，则更新的行列必须比以前大，否则不允许更改
		while(t==2)
		{
			printf("\n\t\t\t\033[34m座位的行数必须大于[%d]: \033[0m", rec.rowsCount);
			//scanf("%d", &(newrow));
			while(1){
				if(scanf("%d", &(newrow))==1){ cl_stdin(); break;}
				else {cl_stdin(); printf("\n\t\t\t\033[34m座位的行数必须大于[%d]: \033[0m", rec.rowsCount);}
			}
			if(newrow > rec.rowsCount){
				rec.rowsCount = newrow;
				break;
			}
		}
		while(t==3)
		{
			printf("\n\t\t\t\033[36m座位的列数必须大于[%d]: \033[0m", rec.colsCount);
			//scanf("%d", &(newcolumn));
			while(1){
			if(scanf("%d", &(newcolumn))==1){ cl_stdin(); break;}
			else {cl_stdin(); printf("\n\t\t\t\033[36m座位的列数必须大于[%d]: \033[0m", rec.colsCount);}
			}
			if(newcolumn > rec.colsCount)
			{
				rec.colsCount = newcolumn;
				break;
			}
		}
		//} while (newrow < rec.rowsCount || newcolumn < rec.colsCount);
		
		rec.seatsCount = rec.rowsCount * rec.colsCount;
	} else {
		if(t==2){

			printf("\t\t\t请输入新的座位行数:");
			//scanf("%d", &rec.rowsCount);
			while(1){
				if(scanf("%d", &(rec.rowsCount))==1){ cl_stdin(); break;}
				else {cl_stdin(); printf("\n\t\t\t\033[31m您的输入有误,请重新输入: \033[0m"); }
			}
		}
		if(t==3)
		{
			printf("\n\t\t\t\033[33m请输入新的座位列数: \033[0m");
			//scanf("%d", &rec.colsCount);
			while(1){
				if(scanf("%d", &(rec.colsCount))==1){ cl_stdin(); break;}
				else {cl_stdin(); printf("\n\t\t\t\033[31m您的输入有误,请重新输入: \033[0m"); }
			}
		}
		rec.seatsCount = 0;
	}

	printf("\t\t\t-------------------------------------------------------------------------------\n");

	if (Studio_Srv_Modify(&rec)) {
		rtn = 1;
		printf("\n\t\t\t\033[33m演出厅修改成功!\033[0m\n\t\t\t\033[34m按任意键返回....\033[0m\n");
	} else
		printf("\n\t\t\t\033[31m演出厅修改失败!\033[0m\n\t\t\t\033[34m按任意键返回....\033[0m\n");

	getchar();
	return rtn;
}

int Studio_UI_Delete(int id) {

	int rtn = 0;

	if (Studio_Srv_DeleteByID(id)) {
		//在删除放映厅时，同时根据放映厅id删除座位文件中的座位
		if (Seat_Srv_DeleteAllByRoomID(id))
			printf("\n\t\t\t\033[33m演出厅座位删除成功!\033[0m\n");
		printf("\t\t\t\033[34m演出厅删除成功\033[0m\n\t\t\t\033[33m按任意键退出....\033[0m\n");
		rtn = 1;
	} else { printf("\n\t\t\t\033[31m演出厅不存在!\033[0m\n\t\t\t\033[33m按任意键退出....\033[0m\n"); }

	getchar();
	return rtn;
}

