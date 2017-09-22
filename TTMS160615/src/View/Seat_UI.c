/*
 *  Seat_UI.c
 *
 *  Created on: 2015年5月23日
 *  Author: lc
 */
#include "Seat_UI.h"
#include "../Service/Seat.h"
#include "../Service/Studio.h"
#include "../Service/EntityKey.h"
#include "../Common/List.h"
#include "../Common/ComFunction.h"
#include <stdio.h>
#include <ctype.h>
//根据状态返回对应表示状态符号
char Seat_UI_Status2Char(seat_status_t status) {

	char tmp;

	if(status == SEAT_GOOD)
		tmp = 'E';
	if(status == SEAT_BROKEN)
		tmp = 'F';
	if(status == SEAT_NONE)
		tmp = 'G';

	return tmp;
}

//根据状态符号返回桌位状态
seat_status_t Seat_UI_Char2Status(char statusChar) {

	seat_status_t tmp;
	if(statusChar == 'E')
	{
		tmp = SEAT_GOOD;
	}
	if(statusChar == 'F')
	{
		tmp = SEAT_BROKEN;
	}
	if(statusChar == 'G')
	{
		tmp = SEAT_NONE;
	}
	return tmp;
}

int  Seat_UI_MgtEntry(int roomID) {

	// 请补充完整
	int i,j;
	
	studio_t buf;
	seat_list_t list;
	List_Init(list, seat_node_t);
	
	seat_node_t *q;
	char choice;
	if(!(Studio_Srv_FetchByID(roomID,&buf)))
	{
		printf("\n\t\t\033[31m演出厅不存在!\033[0m\n\t\t\033[31m按任意键退出....\033[0m\n");
		getchar();
		return 0;
	}
	if(!(Seat_Srv_FetchByRoomID(list,roomID)))
	{
	  Seat_Srv_RoomInit(list,roomID,buf.rowsCount,buf.colsCount);
	}
	Seat_Srv_FetchByRoomID(list,roomID);
	do {
		system("clear");
		printf("\t\t\t\033[32m=====================================================================================\n\n");
		printf("\t\t\t************************************\033[0m\033[36m 座位 管理 \033[0m\033[32m**************************************\033[0m\n\n\n");
		for(i=1;i<=buf.rowsCount;i++)
		{
			printf("\t\t\t");
			for(j=1;j<=buf.colsCount;j++)
			{
				q=Seat_Srv_FindByRowCol(list,i,j);
				if(q->data.status == SEAT_NONE)
				{
					printf("\033[33m☺\033[0m\t");
				}
				if(q->data.status == SEAT_GOOD)
				{
					printf("\033[36m☻\033[0m\t");
				}
				if(q->data.status == SEAT_BROKEN)
				{
					printf("\033[31mⓍ\033[0m\t");
				}
			}
			printf("\n");
		}
	      
		printf("\n\t\t\t\033[32m=====================================================================================\033[0m\n\n");
		printf("\t\t\t\033[33m☺: 空位[G/g]\033[0m\t\t\t\t\033[36m☻: 有位[E/e]\033[0m\t\t\t\033[31mⓍ: 损坏[F/f]\033[0m\n\n");
		printf("\t\t\t\033[33m-------- [A]添加 ----------- [D]删除 ----------- [U]修改 ------------ [R]返回 -------\033[0m\n\n");
		printf("\t\t\t\033[32m=====================================================================================\033[0m\n\n");
	    printf("\n\t\t\t\033[33m请选择: \033[0m");

		choice=l_getc();

		switch (choice) {
		case 'a':
		case 'A':
			{
				int row,column;
				printf("\n\t\t\t\033[33m请输入座位的行号: \033[0m");
				//scanf("%d",&row);
				while(1){
					if(scanf("%d", &(row))==1){ cl_stdin(); break;}
					else {cl_stdin(); printf("\n\t\t\t\033[31m输入有误,请重新输入: \033[0m");}
				}
				printf("\n\t\t\t\033[33m请输入座位的列号: \033[0m");
				//scanf("%d",&column);
				while(1){
					if(scanf("%d", &(column))==1){ cl_stdin(); break;}
					else {cl_stdin(); printf("\t\t\t\033[31m输入有误,请重新输入: \033[0m");}
				}
				Seat_UI_Add(list,roomID,row,column);
				break;
			}
		case 'd':
		case 'D':
			{
			int row,column;
			printf("\t\t\t\033[32m=====================================================================================\n\n");
			printf("\t\t\t************************************\033[0m\033[36m 座位 删除 \033[0m\033[32m**************************************\033[0m\n\n\n");
			printf("\t\t\t\033[32m--------------------------------------------------------------------------------\033[0m\n");
			printf("\n\t\t\t\033[33m请输入座位的行号: \033[0m");
			//scanf("%d",&row);
			while(1){
				if(scanf("%d", &(row))==1){ cl_stdin(); break;}
				else {cl_stdin(); printf("\n\t\t\t\033[31m输入有误,请重新输入: \033[0m");}
			}
			printf("\n\t\t\t\033[33m请输入座位的列号: \033[0m");
			//scanf("%d",&column);
			while(1){
				if(scanf("%d", &(column))==1){ cl_stdin(); break;}
				else {cl_stdin(); printf("\n\t\t\t\033[31m输入有误,请重新输入: \033[0m");}
			}
			Seat_UI_Delete(list,row,column);
			break;
			}
		case 'U':
		case 'u':
			{
			int row,column;
			char ch;
			printf("\t\t\t\033[32m=====================================================================================\n\n");
			printf("\t\t\t************************************\033[0m\033[36m 座位 修改 \033[0m\033[32m**************************************\033[0m\n\n\n");
			printf("\t\t\t\033[33m----- [1]按行修改 -------------------[2]按列修改 -------------------[3]单座修改 -----\033[0m\n\n");
			printf("\t\t\t\033[32m-------------------------------------------------------------------------------------\033[0m\n");
			printf("\n\t\t\t\033[33m请选择: \033[0m");

			ch=l_getc();
			switch(ch)
			{
			case '3':
			printf("\n\t\t\t\033[33m请输入座位的行号: \033[0m");
			//scanf("%d",&row);
			while(1){
				if(scanf("%d", &(row))==1){ cl_stdin(); break;}
				else {cl_stdin(); printf("\n\t\t\t\033[31m输入有误,请重新输入: \033[0m");}
				}
			printf("\n\t\t\t\033[33m请输入座位的列号: \033[0m");
			//scanf("%d",&column);
			while(1){
				if(scanf("%d", &(column))==1){ cl_stdin(); break;}
				else {cl_stdin(); printf("\n\t\t\t\033[31m输入有误,请重新输入: \033[0m");}
				}
			Seat_UI_Modify(list,row,column);
			break;
			case '2':
				printf("\n\t\t\t\033[33m请输入座位的列号: \033[0m");
				while(1){
				if(scanf("%d", &(column))==1){ cl_stdin(); break;}
				else {cl_stdin(); printf("\n\t\t\t\033[31m输入有误,请重新输入: \033[0m");}
				}
				Seat_UI_Modify(list,0,column);
				break;
			case '1':
				printf("\n\t\t\t\033[33m请输入座位的行号: \033[0m");
			//scanf("%d",&row);
			while(1){
				if(scanf("%d", &(row))==1){ cl_stdin(); break;}
				else {cl_stdin(); printf("\n\t\t\t\033[31m输入有误,请重新输入: \033[0m");}
				}
				Seat_UI_Modify(list,row,0);
				break;
			}
			break;
			}
		}	
	}while (choice != 'r' && choice != 'R');
					
}

int Seat_UI_Add(seat_list_t list, int roomID, int row, int column) {
	//输入一个座位
	seat_t rec;
	studio_t buf;
	seat_node_t *p;
	//char ch;
	char choice;
	if (!Studio_Srv_FetchByID(roomID, &buf)) {
		printf("\n\t\t\t\033[31m演出厅不存在!\033[0m\n\t\t\t\033[33m按任意键退出....\033[0m\n");
		getchar();
		return 0;
	}
	Seat_Srv_FetchByRoomID(list,roomID);
	p=Seat_Srv_FindByRowCol(list,row,column);
	if(!p)
	{
		printf("\033[31m\t\t\t座位不存在!\033[0m\n");
		return 0;
	}
	system("clear");
	while(1){

		printf("\t\t\t\033[32m=====================================================================================\n\n");
		printf("\t\t\t************************************\033[0m\033[36m 座位 添加 \033[0m\033[32m**************************************\033[0m\n\n\n");
		printf("\t\t\t\033[32m-------------------------------------------------------------------------------------\033[0m\n");
		if(p->data.status==SEAT_GOOD)
		{
			printf("\n\t\t\t\033[34m座位已存在!\033[0m\n");
			return 0;
		}
		p->data.status=SEAT_GOOD;
		rec=p->data;
		if(Seat_Srv_Modify(&rec)){
			printf("\n\t\t\t\033[33m座位添加成功!\033[0m\n");
		}else{ printf("\t\t\t座位添加失败!\n"); }

		printf("\n\t\t\t\033[32m---- [A]继续添加 ------ [R]返回----\033[0m\n\n");
		printf("\t\t\t\033[33m请选择: \033[0m");
		//fflush(stdin);

		choice=l_getc();
		if(choice =='a' || choice=='A')
		{
			printf("\n\t\t\t\033[33m请输入座位的行号: \033[0m");
			//scanf("%d",&row);
			while(1){
			if(scanf("%d", &(row))==1){ cl_stdin(); break;}
			else {cl_stdin(); printf("\n\t\t\t\033[31m输入有误,请重新输入: \033[0m");}
			}
			printf("\n\t\t\t\033[33m请输入座位的列号: \033[0m");
			//scanf("%d",&column);
			while(1){
			if(scanf("%d", &(column))==1){ cl_stdin(); break;}
			else {cl_stdin(); printf("\n\t\t\t\033[31m输入有误,请重新输入: \033[0m");}
			}
		}else{break;}
	}
	return 1;
}

int Seat_UI_Modify(seat_list_t list, int row, int column) {

	seat_t rec;
	seat_node_t *p;
	seat_status_t tmp;
	studio_t buf;
	int rtn=0,i;
	char ch;
	
	if(row==0){

		p=Seat_Srv_FindByRowCol(list,1,column);
		Studio_Srv_FetchByID(p->data.roomID,&buf);
		if(!p){
			printf("\n\t\t\t\033[31m座位不存在!\033[0m");
			return 0;
		}
		printf("\n\033[33m\t\t\t请输入座位符号状态: \033[0m");
		ch=l_getc();

		printf("%d",p->data.row);
		for(i=1;i<=buf.rowsCount;i++){

			p=Seat_Srv_FindByRowCol(list,i,column);
			tmp=Seat_UI_Char2Status(toupper(ch));
			p->data.status=tmp;
			rec=p->data;
			Seat_Srv_Modify(&rec);
		}
		return 1;
	}
	if(column==0){

		p=Seat_Srv_FindByRowCol(list,row,1);
		Studio_Srv_FetchByID(p->data.roomID,&buf);
		if(!p){
			printf("\033[31m\n\t\t\t座位不存在!\033[0m");
			return 0;
		}
		printf("\n\t\t\t\033[33m请输入座位符号状态: \033[0m");
		ch=l_getc();
		for(i=1;i<=buf.colsCount;i++){
			p=Seat_Srv_FindByRowCol(list,row,i);
			tmp=Seat_UI_Char2Status(toupper(ch));
			p->data.status=tmp;
			rec=p->data;
			Seat_Srv_Modify(&rec);
		}
		return 1;
	}
	//system("clear");
	p=Seat_Srv_FindByRowCol(list,row,column);
	
	printf("\n\033[33m\t\t\t请输入座位符号状态: \033[0m");
	ch=l_getc();
	tmp=Seat_UI_Char2Status(toupper(ch));
	p->data.status=tmp;
	rec=p->data;
	if(Seat_Srv_Modify(&rec)){
		rtn++;
		printf("\n\t\t\t\033[34m座位修改成功!\033[0m\n\033[33m按任意键退出....\033[0m\n");
	}
	else{
		printf("\n\t\t\t\033[31m座位修改失败!\033[0m\n");
	}
	return rtn;
}

int Seat_UI_Delete(seat_list_t list, int row, int column) {
	
	seat_node_t *p;
	seat_t rec;
	p=Seat_Srv_FindByRowCol(list,row,column);
	if(!p){
		printf("\n\033[31m\t\t\t座位不存在!\033[0m");
		return 0;
	}
	//system("clear");
	//Seat_Srv_DeleteByID(p->data.id);

	p->data.status=SEAT_NONE;
	rec=p->data;
	Seat_Srv_Modify(&rec);
	return 1;
}


