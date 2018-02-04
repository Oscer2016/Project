package xupt.se.ttms.service;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import xupt.se.ttms.idao.DAOFactory;
import xupt.se.ttms.idao.iStudioDAO;
import xupt.se.ttms.model.Studio;
import xupt.se.util.DBUtil;

public class StudioSrv {
	private iStudioDAO stuDAO=DAOFactory.creatStudioDAO();
	
	public int add(Studio stu){
		return stuDAO.insert(stu); 		
	}
	
	public int modify(Studio stu){
		return stuDAO.update(stu); 		
	}
	
	public int delete(int ID){
		return stuDAO.delete(ID); 		
	}
	
	public boolean makeSeat(int ID) {
		return stuDAO.makeSeat(ID);
	}
	
	public boolean changeSeat(int studio_id, int row, int col, int status) {
		return stuDAO.changeSeat(studio_id, row, col, status);
	}
	
	public List<Studio> Fetch(String condt){
		return stuDAO.select(condt);		
	}
	
	public Vector<Vector> FetchAll(String condt) {
		return stuDAO.selectStudio(condt);
	}
	public List<Studio> FetchAll(){
		return stuDAO.select("");		
	}
	
	public List<Studio> Fetch(int ID,Studio studio ){
		return stuDAO.selectID(ID,studio);		
	}
	
	public Vector<Vector> FetchSelect(String condt){
		return stuDAO.selectFetch(condt);		
	}
	
	public Vector<Vector> FetchSelectPosition(String condt){
		return stuDAO.selectFetchPosition(condt);		
	}
	
	public List<Studio> FetchStudio(String condt){
		return stuDAO.selectStudioList(condt);		
	}
	
	public int createSeats(Studio stu){
		if(stu.getStudioFlag()==0){
			return stuDAO.createSeats(stu);
        }
		return -1;
	}

	public int insertSeat(Studio studio){
		return stuDAO.insertSeat(studio);
	}

	public List<Studio> selectID(int condt) {
		System.out.println("-->>000");
		List<Studio> stuList = new LinkedList<Studio>();
		
		try {
			String sql = "select studio_id from studio ";
			
			sql+= "where studio_id = " + condt;
			DBUtil db = new DBUtil();
			db.openConnection();
			System.out.println("---" + sql);
			ResultSet rst = db.execQuery(sql);
			System.out.println("-->>000");
			if (rst!=null) {
				
				while(rst.next()){
					Studio stu = new Studio();
					stu.setID(rst.getInt("studio_id"));
					stuList.add(stu);
				}
			}
			db.close(rst);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stuList;
	}

	public Vector<Vector> Fetch() {
		return stuDAO.selectSched();
	}

}
