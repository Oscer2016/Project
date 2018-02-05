package xupt.se.ttms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import xupt.se.ttms.idao.IDataDictDAO;
import xupt.se.ttms.model.DataDict;
import xupt.se.ttms.model.Studio;
import xupt.se.util.ConnectionManager;

public class DataDictDAO implements IDataDictDAO {
	
	@Override
	public int insert(DataDict ddict) {
		// TODO Auto-generated method stub
		int rtn = 0;
		if (ddict == null) {
			return rtn;
		}
		
        // 获取Connection
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
		try {
			String sql = "insert into data_dict(dict_parent_id, dict_index, dict_name, dict_value) values(?,?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, ddict.getSuperId());
			pstmt.setInt(2, ddict.getIndex());
			pstmt.setString(3, ddict.getName());
			pstmt.setString(4, ddict.getValue());
			
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

	@Override
	public int update(DataDict ddict) {
		// TODO Auto-generated method stub
		int rtn = 0;
		if (ddict == null) {
			return rtn;
		}
		
        // 获取Connection
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
		try {
			String sql = "update data_dict set dict_parent_id=?, dict_index=?, dict_name=?, dict_value=? where dict_id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, ddict.getSuperId());
			pstmt.setInt(2, ddict.getIndex());
			pstmt.setString(3, ddict.getName());
			pstmt.setString(4, ddict.getValue());
			pstmt.setInt(5, ddict.getId());
			
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

	@Override
	public int delete(int ID) {
		// TODO Auto-generated method stub
		int rtn = 0;	
		
        // 获取Connection
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
		try {
			String sql = "delete from  data_dict where dict_id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, ID);
			pstmt.executeUpdate();
			rtn  = 1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
            // 关闭连接
            ConnectionManager.close(null, pstmt, con);
            return rtn;
		}	
	}

	@Override
	public List<DataDict> select(String condt) {
		// TODO Auto-generated method stub
		List<DataDict> ddictList = null;
		ddictList = new LinkedList<DataDict>();
		
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
		try {
			String sql = "select dict_id, dict_parent_id,  dict_index , dict_name , dict_value from data_dict ";
			condt.trim();
			if (!condt.isEmpty()) 
				sql += " where " + condt;
			pstmt = con.prepareStatement(sql);
			rst = pstmt.executeQuery();

			while(rst.next()){
				DataDict ddict=new DataDict();
				ddict.setId(rst.getInt("dict_id"));
				ddict.setSuperId(rst.getInt("dict_parent_id"));
				ddict.setIndex(rst.getInt("dict_index"));
				ddict.setName(rst.getString("dict_name"));
				ddict.setValue(rst.getString("dict_value"));
				ddictList.add(ddict);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
            ConnectionManager.close(rst, pstmt, con);
            return ddictList;
		}
	}
		 
	public List<DataDict> findByID( int id) {
		return select(" dict_parent_id =" + id);
	}
	
	public List<DataDict> findSelfByID(int id) {
//		System.out.print(select(" dict_parent_id =" +id).get(3).getName());
		return select(" dict_id =" + id);
	}
	
	public void findAllSonByID(List<DataDict> list, int id) {
		List<DataDict> childList = findByID(id);
		for (int i=0; i<childList.size(); i++) {
			if(!hasChildren(childList.get(i).getId()))
				list.add(childList.get(i));
			else {
				 findAllSonByID(list, childList.get(i).getId());
			}
		}
	}
	
	// 给定的节点是否有孩子节点
	public boolean hasChildren(int id) {
		List<DataDict> list = select(" dict_parent_id =" +id);
		return list.size()>0 ? true : false;
	}
	
	public DataDict findSelfByName(String condt){
		return select(" dict_name = '"+ condt +"'").get(0);
	}
	
	public static void main(String[] args){
		DataDictDAO dictDAO = new DataDictDAO();
		List<DataDict> list = dictDAO.findByID(1);

		//for(int i=0;i<list.size();i++) System.out.println(list.get(i).getName());
		System.out.println(dictDAO.hasChildren(2));

		List<DataDict> list1 = new ArrayList<DataDict>();

		dictDAO.findAllSonByID(list1,0);
		for(int i=0;i<list1.size();i++) System.out.println("tt" +list1.get(i).getId());
	}
	
}
