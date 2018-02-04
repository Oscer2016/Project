package xupt.se.ttms.idao;
import xupt.se.ttms.model.Seat;

import java.util.List;


public interface iSeatDAO {
	public int insert(Seat stu);
	public int update(Seat stu);
	public int update(int studio_id, int row, int col);
	public int delete(int ID);
	public List<Seat> select(String condt);
	public int getSeatStatus(Seat seat);
	public int lockSeat(int id, String date);
	public int unlockSeat(int iD);

}
