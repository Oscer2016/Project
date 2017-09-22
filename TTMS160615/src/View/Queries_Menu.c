#include <stdio.h>
#include <stdlib.h>

#include "../Common/ComFunction.h"
#include "Queries_Menu.h"
#include "Schedule_UI.h"
#include "Studio_UI.h"
#include "Play_UI.h"

void Queries_Menu(void){

	char choice;
     while(1){
		system("clear");
		printf("\t\t\033[32m=======================================================================================\033[0m\n\n");
		printf("\t\t\033[32m------------------------------------- \033[0m\033[33m剧目 查询\033[0m \033[32m---------------------------------------\033[0m\n\n");
		printf("\t\t\033[32m=======================================================================================\033[0m\n\n");
		printf("\t\t\033[33m------ [A]剧目查询 ------------------ [B]演出计划查询 ------------------ [R]返回 ------\033[0m\n\n");
		printf("\t\t\033[32m=======================================================================================\033[0m\n\n");
		printf("\n\t\t\033[33m请选择: \033[0m");
			
		choice=l_getc();
		if('r'==choice || 'R'==choice) break;	
		switch(choice){
					
			case 'a':
			case 'A':
				DisplayQueryPlay();
				break;

			case 'b':
			case 'B':
				Schedule_UI_Query(-1);
				break;
		}
	}
}
