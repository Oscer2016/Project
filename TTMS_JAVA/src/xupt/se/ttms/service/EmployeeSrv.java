package xupt.se.ttms.service;

import java.util.List;
import java.util.Vector;

import xupt.se.ttms.idao.DAOFactory;
import xupt.se.ttms.idao.IEmployeeDAO;
import xupt.se.ttms.idao.iStudioDAO;
import xupt.se.ttms.model.Employee;
import xupt.se.ttms.model.Studio;

public class EmployeeSrv {
    private IEmployeeDAO employeeDAO=DAOFactory.creatEmployeeDAO();

    public int add(Employee emp){
        return employeeDAO.insert(emp);
    }

    public int modify(Employee emp){
        return employeeDAO.update(emp);
    }

    public int delete(int ID){
        return employeeDAO.delete(ID);
    }

    public List<Employee> Fetch(String condt){
        return employeeDAO.select(condt);
    }
    
    public Employee Fetchone(String condt){
        return employeeDAO.selectone(condt);
    }

    public List<Employee> FetchAll(){
        return employeeDAO.select("");
    }
    
    public Vector<Vector> FetchEmp() {
    	return employeeDAO.selectEmployee();
    }
    
    public List<Employee> FetchEm(int ID,Employee studio ){
		return employeeDAO.selectID(ID,studio);		
	}
    

	public Vector<Vector> FetchEmPloyee(String string) {
		return employeeDAO.selectAllEm(string);	
	}


}
