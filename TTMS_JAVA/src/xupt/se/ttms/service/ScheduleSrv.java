package xupt.se.ttms.service;

import java.util.List;
import java.util.Vector;

import xupt.se.ttms.idao.DAOFactory;
import xupt.se.ttms.idao.iScheduleDAO;
import xupt.se.ttms.model.Schedule;

public class ScheduleSrv {
	private iScheduleDAO schedDAO=DAOFactory.creatScheduleDAO();
	
	public int add(Schedule stu){
		return schedDAO.insert(stu); 		
	}
	
	public int modify(Schedule stu){
		return schedDAO.update(stu); 		
	}
	
	public int delete(int ID){
		return schedDAO.delete(ID); 		
	}
	
	public List<Schedule> Fetch(String condt){
		return schedDAO.select(condt);		
	}
	
	public Vector<Vector> FetchSched(String condt){
		return schedDAO.selectSched(condt);		
	}
	
	public List<Schedule> FetchAll(){
		return schedDAO.select("");		
	}

	public List<Schedule> FetchPlay(String string) {
		return schedDAO.selectPlay(string);	
	}

	public Vector listing() {
		return schedDAO.listing();
	}

	public Vector<Vector> Showinfo() {

		return schedDAO.showStuinfo();	
	}
	

	public int delete(Schedule schedule) {
		return schedDAO.delete(schedule); 
	}
}
