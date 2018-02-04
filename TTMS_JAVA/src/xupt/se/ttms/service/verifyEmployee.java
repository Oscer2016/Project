package xupt.se.ttms.service;

import java.util.List;

import xupt.se.ttms.dao.EmployeeDAO;
import xupt.se.ttms.model.Employee;


public class verifyEmployee {
	String workid;
    String name;
    String sex;
    String passwd;
    String position;
    int    salary;
    String tel;
    String address;
    String email;

    public verifyEmployee() {}

    public verifyEmployee(String name,String passwd,String position) {
    	this.name = name;
    	this.passwd = passwd;
    	this.position = position;
    }
    
    public verifyEmployee(String workid,String name,String sex,String passwd, 
    		String position,int salary,String tel,String address,String email) {
    	
    	this.workid = workid;
    	this.name = name;
    	this.sex = sex;
    	this.passwd = passwd;
    	this.position = position;
    	this.salary = salary;
    	this.tel = tel;
    	this.address = address;
    	this.email = email;
    }

    public Employee verifyPasswd (){

    	Employee emp = new EmployeeDAO().selectone("emp_name = '" + name + "'");

//        System.out.println(user.getUserPasswd());
        
        if (emp.getEmployee_name() == null) {
        	emp.setFlag(0);
        	return emp;
        } else if (!emp.getEmployee_passwd().equals(passwd)) {
        	emp.setFlag(1);
        	return emp;
        } else if (!emp.getEmployee_position().equals(position)) { 
        	emp.setFlag(2);
        	return emp;
        } else if(emp.getEmployee_passwd().equals(passwd) && emp.getEmployee_position().equals(position)) {
        	emp.setEmployeeStatus(1);                //设置用户的登录状态为1
        	emp.setFlag(3);
//            System.out.println(user.getPosition());
//            System.out.println(position);
            new EmployeeDAO().update(emp);
//            new logs("用户 " + name + " 登录成功!");
        }
        return emp;
    }
    
    public Employee isExistUser () {
        System.out.println(name + " " + passwd);
        Employee emp = new EmployeeDAO().selectone(name);

        if(emp.getEmployee_name() == null) {

        	Employee emp1 = new Employee(workid,name,sex,passwd,position,salary,tel,address,email);
            new EmployeeDAO().insert(emp1);
 
//            new logs("用户 " + name + " 注册成功!");
            
            emp1.setEmployeeStatus(2);           //Status设置为2表示注册成功
            return emp1;
        } else {
            emp.setEmployeeStatus(3);            //Status设置为3表示用户已经存在
            return emp;
        }
    }
}
