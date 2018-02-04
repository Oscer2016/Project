package xupt.se.ttms.service;

import xupt.se.ttms.dao.LogsDAO;

import java.text.SimpleDateFormat;
import java.util.Date;


public class logs {

    public logs() {}
    public logs(String str) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(new Date());
        System.out.println(date+"   " +str);
        new LogsDAO().insert(date,str);
    }

}
