package xupt.se.ttms.idao;

import java.util.List;
import java.util.Vector;

import xupt.se.ttms.model.User;

public interface IUserDAO {
    public int insert(User usr);
    public User select(String condt);
}
