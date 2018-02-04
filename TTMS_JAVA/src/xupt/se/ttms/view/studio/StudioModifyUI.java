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

import xupt.se.ttms.model.Seat;

//import view.studioUI.ImageJPanel;



import xupt.se.ttms.model.Studio;
import xupt.se.ttms.service.SeatSrv;
import xupt.se.ttms.service.StudioSrv;
import xupt.se.ttms.view.admin.AdminUI;
import xupt.se.ttms.view.admin.SeatUI;

public class StudioModifyUI extends JFrame{
	/**
	 * 
	 */
	private JTextField textadmin1;
	private JTextField textadmin2;
	private JTextField textadmin3;
	private JTextField textadmin4;
	private JTextArea textadmin5;
	
	public StudioModifyUI(JTable table1) {
		do_StudioUpdate_actionPerformed(table1);
	}

	protected void do_StudioUpdate_actionPerformed(JTable table1) {
		Object idObject = table1.getValueAt(table1.getSelectedRow(), 0);//读取ID 
        final int idInt = Integer.parseInt(String.valueOf(idObject));
        Studio studio = new Studio();
        new StudioSrv().Fetch(idInt,studio);
        System.out.println(idInt+"\t"+studio.getName()+"\tfash "+studio.getRowCount());
        
		JPanel panel = new JPanel();
		panel.setLayout(null);
		
		JLabel label1 = new JLabel("名称：");
        label1.setBounds(45, 35, 45, 15);
        panel.add(label1);
        textadmin1 = new JTextField(studio.getName());
        textadmin1.setBounds(100, 35, 194, 21);
        panel.add(textadmin1);
        textadmin1.setColumns(10);
        
        JLabel label21 = new JLabel("地点：");
        label21.setBounds(45, 75, 45, 15);
        panel.add(label21);
        textadmin2 = new JTextField(studio.getPosition());
        textadmin2.setBounds(100, 75, 194, 21);
        panel.add(textadmin2);
        textadmin2.setColumns(10);
        
        JLabel label31 = new JLabel("座位行数：");
        label31.setBounds(30, 115, 90, 15);
        panel.add(label31);
        textadmin3 = new JTextField(String.valueOf(studio.getRowCount()));
        textadmin3.setBounds(100, 115, 194, 21);
        panel.add(textadmin3);
        textadmin3.setColumns(10);
        
        JLabel label41 = new JLabel("座位列数：");
        label41.setBounds(30, 155, 90, 15);
        panel.add(label41); 
        textadmin4 = new JTextField(String.valueOf(studio.getColCount()));
        textadmin4.setBounds(100, 155, 194, 21);
        panel.add(textadmin4);
        textadmin4.setColumns(10);
        
        JLabel label51 = new JLabel("介绍：");
        label51.setBounds(45, 195, 45, 15);
        panel.add(label51);
        textadmin5 = new JTextArea(studio.getIntroduction());
        textadmin5.setBounds(100, 195, 194, 69);
        panel.add(textadmin5);
        
        JButton buttonadminAlter = new JButton("确定");
        buttonadminAlter.setBounds(100,290,65,35);
        panel.add(buttonadminAlter);
        buttonadminAlter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                do_buttonadminAlter_actionPerformed(e,idInt);
            }
        });
        JButton buttonCancle = new JButton("取消");
        buttonCancle.setBounds(200,290,65,35);
        panel.add(buttonCancle);
        buttonCancle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	do_StudioUpdate_actionPerformed();
            }
        });
        
		this.add(panel);
		this.setTitle("修改演出厅信息");
		this.setSize(390,450);
		this.setLocationRelativeTo(null);
		this.setResizable(false);	//禁止改变窗体大小
		this.setVisible(true);



	}
	protected void do_StudioUpdate_actionPerformed() {
		this.dispose();
	}
	protected void do_buttonadminAlter_actionPerformed(ActionEvent e,int idInt) {
//		String id = textID.getText();
		String name= textadmin1.getText();
		String place = textadmin2.getText();
		String row = textadmin3.getText();
		String col = textadmin4.getText();
		String introduce = textadmin5.getText();
		int rowInt = Integer.parseInt(row);
		int colInt = Integer.parseInt(col);
//		int idInt = Integer.parseInt(id);
		Studio stu = new Studio();
		stu.setName(name);
		stu.setPosition(place);
		stu.setRowCount(rowInt);
		stu.setColCount(colInt);
		stu.setIntroduction(introduce);
		stu.setID(idInt);
		System.out.println(idInt);
		new SeatSrv().delete(idInt);
		new StudioSrv().modify(stu);
		
		new SeatUI(rowInt, colInt, idInt);
//		System.out.println(stu.getId()+stu.getName()+stu.getPosition()+stu.getRowCount()+stu.getColCount()+stu.getIntroduction());
		this.dispose();
		new AdminUI();
		}
	}
