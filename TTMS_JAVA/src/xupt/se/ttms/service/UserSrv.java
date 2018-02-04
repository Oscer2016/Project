package xupt.se.ttms.service;

import java.util.List;
import java.util.Vector;

import xupt.se.ttms.idao.DAOFactory;
import xupt.se.ttms.idao.IEmployeeDAO;
import xupt.se.ttms.idao.IUserDAO;
import xupt.se.ttms.idao.iStudioDAO;
import xupt.se.ttms.model.Studio;
import xupt.se.ttms.model.User;

public class UserSrv {
    private IUserDAO userDAO = DAOFactory.creatUserDAO();

    public int add(User usr){
        return userDAO.insert(usr);
    }

    public User Fetch(String condt){
        return userDAO.select(condt);
    }
    
}
