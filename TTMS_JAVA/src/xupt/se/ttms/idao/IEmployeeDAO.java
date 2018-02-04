package xupt.se.ttms.idao;

import java.util.List;
import java.util.Vector;

import xupt.se.ttms.model.Employee;

public interface IEmployeeDAO {
    public int insert(Employee stu);
    public int update(Employee stu);
    public int delete(int ID);
    public List<Employee> select(String condt);
    public Employee selectone(String condt);
	public Vector<Vector> selectEmployee();
	public List<Employee> selectID(int iD, Employee studio);
	public Vector<Vector> selectAllEm(String string);
}