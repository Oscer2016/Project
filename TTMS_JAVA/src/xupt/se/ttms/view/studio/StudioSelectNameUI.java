package xupt.se.ttms.view.studio;

import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import xupt.se.ttms.service.StudioSrv;
import xupt.se.ttms.view.admin.AdminUI;
import xupt.se.ttms.view.tmpl.BaseTableModule;

public class StudioSelectNameUI extends AdminUI {
	
	public StudioSelectNameUI(String str1,String jcb) {
		do_buttonSelect_actionPerformed(str1,jcb);
	}

	protected void do_buttonSelect_actionPerformed(String str1,String jcb) {
		
		Vector<Vector> vector = new Vector<Vector>();
		if(jcb.equals("名称")){
			vector = new StudioSrv().FetchSelect(str1);
//			System.out.println(studio.getID());
			if(vector.size()>0){ 
				panelleft.remove(scrollPane);
				String[] columnNames = {"演出厅ID","演出厅名称","演出厅地点","座位行数","座位列数","备注"};
				baseTableModule = new BaseTableModule(columnNames, vector);
				tableleft = new JTable(baseTableModule);
				tableleft.setAutoCreateRowSorter(true);
			    DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();//单元格渲染器  
			    tcr.setHorizontalAlignment(JLabel.CENTER);//居中显示  
			    tableleft.setDefaultRenderer(Object.class, tcr);//设置渲染器 
				scrollPane = new JScrollPane(tableleft);
				scrollPane.setBounds(0,90,1035,400);
				panelleft.add(scrollPane);
				
				
			}else{
				JOptionPane.showConfirmDialog(null,"请输入正确名称", "【警告】", JOptionPane.YES_NO_OPTION);
			}
		}else if(jcb.equals("地点")){
//			System.out.println(str1);	//读出地点
			vector = new StudioSrv().FetchSelectPosition(str1);
//			System.out.println(studio.getID());
			System.out.println(vector.size());	//读出地点的个数
			if(vector.size()>0){
				panelleft.remove(scrollPane);
				String[] columnNames = {"演出厅ID","演出厅名称","演出厅地点","座位行数","座位列数","备注"};
				baseTableModule = new BaseTableModule(columnNames, vector);
				tableleft = new JTable(baseTableModule);
				tableleft.setAutoCreateRowSorter(true);
			    DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();//单元格渲染器  
			    tcr.setHorizontalAlignment(JLabel.CENTER);//居中显示  
			    tableleft.setDefaultRenderer(Object.class, tcr);//设置渲染器 
				scrollPane = new JScrollPane(tableleft);
				scrollPane.setBounds(0,90,1035,400);
				panelleft.add(scrollPane);
			}else{
				JOptionPane.showConfirmDialog(null,"请输入正确地点", "【警告】", JOptionPane.YES_NO_OPTION);
			}
		}else{
			JOptionPane.showConfirmDialog(null,"请选择查找方式", "【警告】", JOptionPane.YES_NO_OPTION);
		}

	}
}
