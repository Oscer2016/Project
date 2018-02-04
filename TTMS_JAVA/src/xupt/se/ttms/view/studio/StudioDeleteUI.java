package xupt.se.ttms.view.studio;


import javax.swing.JDialog;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import xupt.se.ttms.model.Studio;
import xupt.se.ttms.service.SeatSrv;
import xupt.se.ttms.service.StudioSrv;
import xupt.se.ttms.view.admin.AdminUI;


public class StudioDeleteUI {
	public StudioDeleteUI(JTable table1){
		do_buttonDeleteStudio_actionPerformed(table1);
	}
	protected void do_buttonDeleteStudio_actionPerformed(JTable table1) {
		
//		System.out.println(10);
        Object idObject = table1.getValueAt(table1.getSelectedRow(), 0);//读取ID
        
        int idInt = Integer.parseInt(String.valueOf(idObject));

        new SeatSrv().delete(idInt);
        new StudioSrv().delete(idInt);
        
        System.out.println(idInt);
        new AdminUI();
        if(!new StudioSrv().selectID(idInt).isEmpty()){
        	JOptionPane.showMessageDialog(null, "正在使用，无法删除", "【出错啦】", JOptionPane.ERROR_MESSAGE);
        }
       
    }
}
