package xupt.se.ttms.idao;
import xupt.se.ttms.model.Studio;

import java.util.List;
import java.util.Vector;


public interface iStudioDAO {
	public int insert(Studio stu);
	public int update(Studio stu);
	public int delete(int ID);
	public List<Studio> select(String condt); 
	public int createSeats(Studio stu);
	public Vector<Vector> selectStudio(String condt);
	public boolean makeSeat(int iD);
	public boolean changeSeat(int studio_id, int row, int col, int status);
	public List<Studio> selectID(int iD, Studio studio);
	public List<Studio> selectStudioList(String condt);
	public Vector<Vector> selectFetch(String condt);
	public Vector<Vector> selectFetchPosition(String condt);
	public int insertSeat(Studio studio);
	public Vector<Vector> selectSched();
}
