package xupt.se.ttms.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;

import xupt.se.ttms.idao.IEmployeeDAO;
import xupt.se.ttms.model.Employee;
import xupt.se.util.ConnectionManager;


public class EmployeeDAO implements IEmployeeDAO {

    public int insert(Employee employee) {
        int rtn = 0;
        if(employee == null) {
            return rtn;
        }

        // 获取Connection
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        try {
            String sql = "insert into employee(emp_no,emp_name,emp_passwd,emp_sex,emp_position,emp_salary,emp_tel_num,emp_addr,emp_email) values(?,?,?,?,?,?,?,?,?)";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, employee.getEmployee_workid());
            pstmt.setString(2, employee.getEmployee_name());
            pstmt.setString(3, employee.getEmployee_passwd());
            pstmt.setString(4, employee.getEmployee_sex());
            pstmt.setString(5, employee.getEmployee_position());
            pstmt.setInt(6, employee.getEmployee_salary());
            pstmt.setString(7, employee.getEmployee_tel());
            pstmt.setString(8, employee.getEmployee_address());
            pstmt.setString(9, employee.getEmployee_email());
            
            pstmt.executeUpdate();
            rtn = 1;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接
            ConnectionManager.close(null, pstmt, con);
            return rtn;
        }
    }

    public int update(Employee employee) {
        int rtn = 0;
        if (employee == null) {
        	return rtn;
        }
        
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        try {
        	
            String sql = "update employee set emp_no=?, emp_name=?, emp_passwd=?, emp_sex=?, emp_position=?,"
            		+ "emp_salary=?, emp_tel_num=?, emp_addr=?, emp_email=? where emp_id=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, employee.getEmployee_workid());
            pstmt.setString(2, employee.getEmployee_name());
            pstmt.setString(3, employee.getEmployee_passwd());
            pstmt.setString(4, employee.getEmployee_sex());
            pstmt.setString(5, employee.getEmployee_position());
            pstmt.setInt(6, employee.getEmployee_salary());
            pstmt.setString(7, employee.getEmployee_tel());
            pstmt.setString(8, employee.getEmployee_address());
            pstmt.setString(9, employee.getEmployee_email());
            pstmt.setInt(10, employee.getId());
            
            pstmt.executeUpdate();
            rtn = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	ConnectionManager.close(null, pstmt, con);
            return rtn;
        }
    }

    public int delete(int ID) {
        int rtn = 0;
        
        // 获取Connection
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        try {
			String sql = "delete from  employee where emp_id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, ID);
			pstmt.executeUpdate();
			rtn = 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接
            ConnectionManager.close(null, pstmt, con);
            return rtn;
		}	
    }


    public List<Employee> select(String condt) {
    	System.out.println(condt);
        List<Employee> empList = null;
        empList = new LinkedList<Employee>();
        
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        try {
            String sql = "select * from employee ";
            condt.trim();
            if(!condt.isEmpty()) {
                sql += "where emp_name=?";
            }
            
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, condt);
			rst = pstmt.executeQuery();
            
            while(rst.next()){
                Employee employee = new Employee();
                employee.setId(rst.getInt("emp_id"));
                employee.setEmployee_workid(rst.getString("emp_no"));
                employee.setEmployee_name(rst.getString("emp_name"));
                employee.setEmployee_sex(rst.getString("emp_sex"));
                employee.setEmployee_passwd(rst.getString("emp_passwd"));
                employee.setEmployee_position(rst.getString("emp_position"));
                employee.setEmployee_salary(rst.getInt("emp_salary"));
                employee.setEmployee_tel(rst.getString("emp_tel_num"));
                employee.setEmployee_address(rst.getString("emp_addr"));
                employee.setEmployee_email(rst.getString("emp_email"));

                empList.add(employee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	ConnectionManager.close(rst, pstmt, con);
        	return empList;
        }
    }
    
    public Employee selectone(String condt) {
    	
      	Employee emp = new Employee();
      	
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        
        try {
        	String sql = "select * from employee ";
            condt.trim();
            if(!condt.isEmpty()) {
                sql += "where " + condt + ";";
            }
            
  			pstmt = con.prepareStatement(sql);
  			rst = pstmt.executeQuery();
              while(rst.next()){
                  emp.setId(rst.getInt("emp_id"));
                  emp.setEmployee_workid(rst.getString("emp_no"));
                  emp.setEmployee_name(rst.getString("emp_name"));
                  emp.setEmployee_sex(rst.getString("emp_sex"));
                  emp.setEmployee_passwd(rst.getString("emp_passwd"));
                  emp.setEmployee_position(rst.getString("emp_position"));
                  emp.setEmployee_salary(rst.getInt("emp_salary"));
                  emp.setEmployee_tel(rst.getString("emp_tel_num"));
                  emp.setEmployee_address(rst.getString("emp_addr"));
                  emp.setEmployee_email(rst.getString("emp_email"));
              }
          } catch (Exception e) {
              e.printStackTrace();
          } finally {
        	  ConnectionManager.close(rst, pstmt, con);
        	  return emp;
          }
    }
      
    @Override
  	public Vector<Vector> selectEmployee() {
  		
  		Vector<Vector> vector = new Vector<Vector>();
  		Vector temp = new Vector<String>();
  	
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
  		try {
  			String sql = "select emp_id,emp_name,emp_sex,emp_position,emp_salary,emp_tel_num as 'tel' from employee";
			pstmt = con.prepareStatement(sql);
			rst = pstmt.executeQuery();

			while (rst.next()) {
				temp.add(rst.getInt("emp_id"));
				temp.add(rst.getString("emp_name"));
				temp.add(rst.getString("emp_sex"));
				temp.add(rst.getString("emp_position"));
				temp.add(rst.getInt("emp_salary"));
				temp.add(rst.getString("tel"));

				vector.add(temp);
				Vector t = new Vector<String>();
				temp = t;
			}
  		} catch (Exception e) {
  			e.printStackTrace();
  		} finally {
  			ConnectionManager.close(rst, pstmt, con);
  			return vector;
  		}
  	}

	@Override
	public List<Employee> selectID(int ID, Employee stu) {
		List<Employee> stuList = null;
  		stuList = new LinkedList<Employee>();
  		
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
  		try {
  			String sql = "select emp_name, emp_passwd, emp_position, emp_salary,emp_tel_num from employee where emp_id=?";
  			pstmt = con.prepareStatement(sql);
  			pstmt.setInt(1, ID);
  			rst = pstmt.executeQuery();
			while (rst.next()) {
				stu.setEmployee_name(rst.getString("emp_name"));
				stu.setEmployee_passwd(rst.getString("emp_passwd"));
				stu.setEmployee_position(rst.getString("emp_position"));
				stu.setEmployee_salary(Integer.parseInt(rst.getString("emp_salary")));
				stu.setEmployee_tel(rst.getString("emp_tel_num"));
				//System.out.println(Integer.parseInt(rst.getString("studio_row_count"))+ "  "+rst.getInt("studio_row_count")+ "  "+ stu.getColCount());
				stuList.add(stu);
			}
  		} catch (Exception e) {
  			e.printStackTrace();
  		} finally {
  			ConnectionManager.close(rst, pstmt, con);
  	  		return stuList;
  		}
	}

	@Override
    public Vector<Vector> selectAllEm(String condt){
    	Vector<Vector> vector = new Vector<Vector>();
  		Vector temp = new Vector<String>();
  		
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
  		try {
  			String sql = "select emp_id, emp_name, emp_sex, emp_position ,emp_salary, emp_tel_num from employee";
  			condt.trim();
  			if (!condt.isEmpty())
  				sql += " where emp_name=? or emp_position=?";
  			pstmt = con.prepareStatement(sql);
  			pstmt.setString(1, condt);
  			pstmt.setString(2, condt);
  			rst = pstmt.executeQuery();
			while(rst.next()){
				temp.add(rst.getInt("emp_id"));
				temp.add(rst.getString("emp_name"));
				temp.add(rst.getString("emp_sex"));
				temp.add(rst.getString("emp_position"));
				temp.add(rst.getInt("emp_salary"));
				temp.add(rst.getString("emp_tel_num"));
				vector.add(temp);
				Vector t = new Vector<String>();
				temp = t;
			}
  			
  		} catch (Exception e) {
  			e.printStackTrace();
  		} finally {
  			ConnectionManager.close(rst, pstmt, con);
  	  		return vector;
  		}  
	}

//    public static void main(String[] args) {
//    	
//        EmployeeDAO d = new EmployeeDAO();
//        Employee employee = new Employee();
//        
//		employee.setEmployee_workid("c3");
//		employee.setEmployee_name("何攀");
//		employee.setEmployee_passwd("123456");
//		employee.setEmployee_sex("male");
//		employee.setEmployee_position("clerk");
//		employee.setEmployee_salary(5000);
//		employee.setEmployee_tel("15803214789");
//		employee.setEmployee_address("place");
//		employee.setEmployee_email("test@126.com");
//		System.out.println(employee.getEmployee_name());
//		System.out.println(employee.getEmployee_salary());
//		System.out.println(employee.getEmployee_email());
//		d.insert(employee);
//        List<Employee> v = d.select("test");
//        System.out.print(v);
//        for(int i=0;i<v.size();i++){
//            System.out.println(v.get(i).getEmployee_name());
//            System.out.println(v.get(i).getId());
//        }
//    }

}
