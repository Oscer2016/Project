/*
 * SalesAnalysis_UI.c
 *
 *  Created on: 2015年5月8日
 *      Author: Administrator
 */
#include "SalesAnalysis_UI.h"
#include "../Common/ComFunction.h"
#include "../Service/SalesAnalysis.h"
#include "../Service/Schedule.h"
#include "../Service/Play.h"
#include "../Common/List.h"
#include "../Common/Common.h"
#include <stdio.h>
#include <unistd.h>
#include <assert.h>
#include <string.h>

extern account_t gl_CurUser;

static const int SALESANALYSIS_PAGE_SIZE = 5;

/*//销售分析模块入口函数,显示分析好的销售数据
void SalesAanalysis_UI_BoxOffice() {
	// 请补充完整
	
}


void SalesAanalysis_UI_StatSale(int usrID, user_date_t stDate, user_date_t endDate){
	// 请补充完整
}
*/
void SalesAanalysis_UI_MgtEntry() {

	int i;
	char choice;
	
	schedule_list_t head;
	salesanalysis_node_t *p;
	salesanalysis_list_t list;
	
	Pagination_t paging;
	
	List_Init(head, schedule_node_t);
	List_Init(list,salesanalysis_node_t);
	
	paging.offset = 0;
	paging.pageSize = SALESANALYSIS_PAGE_SIZE;
	
	paging.totalRecords = Schedule_Perst_SelectAll(head);
	//printf("%d",SalesAnalysis_Srv_FetchAll(list));
	SalesAnalysis_Srv_FetchAll(list);
	
	Paging_Locate_FirstPage(list, paging);
	
	do {
		system("clear");
		//SalesAnalysis_Srv_FetchAll(list);
	
		//Paging_Locate_FirstPage(list, paging);
	
		system("clear");
		printf("\t\t\033[32m===========================================================================================\n\n");
		printf("\t\t*************************************\033[0m\033[36m 销售数据 分析 \033[0m\033[32m***************************************\n\n");
		printf("\t\t===========================================================================================\033[0m\n\n");

		printf("\t\t\033[35m%4s\033[0m\t\033[33m%-15s%-8s\t%s %-10s\033[0m\033[32m%s\t\033[0m\033[36m%s\033[0m\033[31m%10s\033[0m\033[34m%15s\033[0m\n", "剧目ID", "剧目名称", "剧目地区",
				"剧目时长", "上座数","上映日期","下线日期","票价","票房收入");

		printf("\t\t\033[32m--------------------------------------------------------------------------------------------\033[0m\n");
		Paging_ViewPage_ForEach(list, paging,salesanalysis_node_t, p, i){
		printf("\t\t\033[35m%4d\033[0m\t\033[33m%-15s%-8s\t%d%8ld\033[0m", p->data.play_id, p->data.name,p->data.area, p->data.duration,p->data.totaltickets);
		printf("\t\033[32m%4d-%02d-%02d\033[0m", p->data.start_date.year, p->data.start_date.month, p->data.start_date.day);
		printf("\t\033[36m%4d-%02d-%02d\033[0m",p->data.end_date.year, p->data.end_date.month, p->data.end_date.day);
		printf("\033[31m%6d\033[0m\033[34m%8ld\033[0m\n",p->data.price,p->data.sales);
		}
		
		printf("\n\t\t\033[32m----------------- 共 \033[0m\033[33m%d\033[0m\033[32m 条数据 --------------------------- 页码: %d\033[0m/\033[33m%d\033[0m\033[32m -----------------------\033[0m\n\n",
				paging.totalRecords, Pageing_CurPage(paging),
				Pageing_TotalPages(paging));
		printf("\t\t\033[32m===========================================================================================\033[0m\n\n");
		printf("\t\t\033[32m----------------\033[0m[\033[33mP\033[0m]\033[32m上页-------------\033[0m[\033[33mR\033[0m]\033[32m返回-------------\033[0m[\033[33mN\033[0m]\033[32m下页----------------------------\033[0m\n\n");
		printf("\t\t\033[32m===========================================================================================\033[0m\n\n");
		printf("\t\t\033[33m请选择: \033[0m");

		choice=l_getc();

		switch (choice) {
		
				case 'p':
				case 'P':
					if (!Pageing_IsFirstPage(paging)) {
						Paging_Locate_OffsetPage(list, paging, -1, salesanalysis_node_t);
					}
					break;
				case 'n':
				case 'N':
					if (!Pageing_IsLastPage(paging)) {
						Paging_Locate_OffsetPage(list, paging, 1, salesanalysis_node_t);
					}
					break;
				}
	} while (choice != 'r' && choice != 'R');
}



