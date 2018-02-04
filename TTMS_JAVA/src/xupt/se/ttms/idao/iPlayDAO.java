
package xupt.se.ttms.idao;
import xupt.se.ttms.model.Play;

import java.util.List;
import java.util.Vector;


public interface iPlayDAO {
	public int insert(Play stu);
	public int update(Play stu);
	public int delete(int ID);
	public List<Play> select(String condt); 
	public List<Play> selectScheduledPlay(String condt);
	public Vector listingtype();
	public Vector listing();
	public Vector<Vector> Fetch();
	public int delete(Play play);
	public Vector listingStudio(int iD);
	Vector<String> ShowPlayName(Play play);
}
