package xupt.se.ttms.view.admin;

import xupt.se.ttms.model.Studio;
import xupt.se.ttms.service.StudioSrv;
public class SeatUI {
	public SeatUI(int row, int col,int id){
		for(int i=1;i<=row;i++){
			for(int j=1;j<=col;j++){
				Studio studio= new Studio();
				studio.setRowCount(i);
				studio.setColCount(j);
				studio.setID(id);
				studio.setFlag(0);
				new StudioSrv().insertSeat(studio);
			}
		}
	}
}
