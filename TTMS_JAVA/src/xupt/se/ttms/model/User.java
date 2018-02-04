package xupt.se.ttms.model;

public class User {
	
    private int    		userId;
	private String 		userName;
	private String 		userSex;
	private String    	userBirthday;
	private String    	userTelnum;
	private String    	userRank;
	private String    	userEmail;
	private double    	userCumulative;	// 用户累计消费金额
	private int    		userStatus;

	public User () {}
	
	public User(String userName,String userSex,String userBirthday,String userTelnum,String userEmail) {
		this.userName = userName;
		this.userSex = userSex;
		this.userBirthday = userBirthday;
        this.userTelnum = userTelnum;
        this.userEmail = userEmail;
	}
	
    public String getUsername() {
        return userName;
    }
    public void setUsername(String userName) {
        this.userName = userName;
    }
    
    
    
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public double getUserCumulative() {
        return userCumulative;
    }
    public void setUserCumulative(double userCumulative) {
        this.userCumulative = userCumulative;
    }
    
    
    
    public String getUserRank() {
    	return userRank;
    }
    public void setUserRank(String userRank) {
        this.userRank = userRank;
    }

    public String getUserSex() {
    	return userSex;
    }
    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }


	public String getUserBirthday() {
		return userBirthday;
	}
    public void setUserBirthday(String userBirthday) {
        this.userBirthday = userBirthday;
    }
    
	public String getUserTelnum() {
		return userTelnum;
	}
    public void setUserTelnum(String userTelnum) {
        this.userTelnum = userTelnum;
    }

	public String getUserEmail() {
		return userEmail;
	}
    public void setuserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    
}
