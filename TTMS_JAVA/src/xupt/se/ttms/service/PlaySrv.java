package xupt.se.ttms.service;

import java.util.List;

import xupt.se.ttms.idao.DAOFactory;
import xupt.se.ttms.idao.iPlayDAO;
import xupt.se.ttms.model.Play;
import java.util.Vector;

public class PlaySrv {
	private iPlayDAO playDAO=DAOFactory.creatPlayDAO();
	
	public int add(Play stu){
		return playDAO.insert(stu); 		
	}
	
	public int modify(Play stu){
		return playDAO.update(stu); 		
	}
	
	public int delete(int ID){
		return playDAO.delete(ID); 		
	}
	
	public int delete(Play play){
		return playDAO.delete(play); 		
	}
	
	public List<Play> Fetch(String condt){
		return playDAO.select(condt);		
	}
	
	public List<Play> FetchAll(){
		return playDAO.select("");		
	}
	
	public List<Play> selectScheduledPlay(String condt){
		return playDAO.selectScheduledPlay("");
	}
	
	public Vector<Vector> Fecth(){
		return playDAO.Fetch();
	}
	
	
	public Vector listing() {
		return playDAO.listing();
	}
	
	public Vector listingtype() {
		return playDAO.listingtype();
	}
	

	public Vector listingStudio(int item) {
		return playDAO.listingStudio(item);
	}
}
