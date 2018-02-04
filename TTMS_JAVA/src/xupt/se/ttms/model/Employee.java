package xupt.se.ttms.model;

import java.io.Serializable;

public class Employee implements Serializable {
	private int id;
	private String  employee_workid; 	//员工工号
	private String  employee_name;
	private String  employee_passwd;
	private String  employee_sex;
	private String  employee_position;
	private int		employee_salary;
	private String  employee_tel;
	private String  employee_address;
	private String  employee_email;
	private int     employeeStatus;
	private int     flag;
   
	/*
     * EmployeeStatus :   1:成功登录   0:登录失败
     *
     * */
    /*
     * flag :   4:信息正确 0:用户名或密码为空   1:用户不存在  2:用户名和密码不匹配  3:职位信息有误
     *
     * */
	
	public Employee() {	
	}
 
	public Employee(String employee_workid,String employee_name,String employee_sex,
			String employee_passwd,String employee_position,int employee_salary,
			String employee_tel,String employee_address,String employee_email) {
		
		this.employee_name = employee_name;
		this.employee_tel = employee_tel;
		this.employee_address = employee_address;
		this.employee_email = employee_email;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getEmployee_workid() {
		return employee_workid;
	}
	public void setEmployee_workid(String employee_workid) {
		this.employee_workid = employee_workid;
	}
	
	public String getEmployee_name() {
		return employee_name;
	}
	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}
	
	public String getEmployee_passwd() {
		return employee_passwd;
	}
	public void setEmployee_passwd(String employee_passwd) {
		this.employee_passwd = employee_passwd;
	}
	
	public void setEmployee_sex(String employee_sex) {
		this.employee_sex = employee_sex;
	}
	public String getEmployee_sex() {
		return employee_sex;
	}
	
	public void setEmployee_position(String employee_position) {
		this.employee_position = employee_position;
	}
	public String getEmployee_position() {
		return employee_position;
	}
	
	public void setEmployee_salary(int employee_salary) {
		this.employee_salary = employee_salary;
	}
	public int getEmployee_salary() {
		return employee_salary;
	}
	
	public String getEmployee_tel() {
		return employee_tel;
	}
	public void setEmployee_tel(String employee_tel) {
		this.employee_tel = employee_tel;
	}
	
	public String getEmployee_address() {
		return employee_address;
	}
	public void setEmployee_address(String employee_address) {
		this.employee_address = employee_address;
	}
	
	public String getEmployee_email() {
		return employee_email;
	}
	public void setEmployee_email(String employee_email) {
		this.employee_email = employee_email;
	}
	
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	public int getEmployeeStatus() {
		return employeeStatus;
	}
	public void setEmployeeStatus(int employeeStatus) {
		this.employeeStatus = employeeStatus;
	}
}
