package xupt.se.ttms.view.studio;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import com.sun.org.apache.bcel.internal.generic.Select;

import xupt.se.ttms.model.Studio;
import xupt.se.ttms.service.StudioSrv;
import xupt.se.ttms.view.admin.AdminUI;
import xupt.se.ttms.view.admin.SeatUI;
import xupt.se.ttms.view.tmpl.ImagePanel;

public class StudioAddUI extends JFrame implements MouseListener, FocusListener {

    private static final long serialVersionUID = 1L;

    // 获得屏幕的大小
    final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;
    // 定义全局组件
    JTextField name, row, col, introduction;

    ImagePanel backgroundPanel = null;
    JButton button_ok, button_cancel;

    private JTextField textadmin1;
	private JTextField textadmin2;
	private JTextField textadmin3;
	private JTextField textadmin4;
	private JTextArea textadmin5;
	private JCheckBox checkBoxTrue;
	private int flag;
	JFrame frame;
    public StudioAddUI(AdminUI frame)   {
    	this.frame = frame;
        do_StudioAdd_actionPerformed();
        
        
    }
    protected void do_StudioAdd_actionPerformed()  {
		
        JPanel panel= new JPanel();
        panel.setLayout(null); 
        
        
        JLabel label1 = new JLabel("名称：");
        label1.setBounds(45, 25, 45, 15);
        panel.add(label1);
        textadmin1 = new JTextField();
        textadmin1.setBounds(100, 23, 194, 21);
        panel.add(textadmin1);
        textadmin1.setColumns(10);
        
        JLabel label21 = new JLabel("地点：");
        label21.setBounds(45, 65, 45, 15);
        panel.add(label21);
        textadmin2 = new JTextField();
        textadmin2.setBounds(100, 63, 194, 21);
        panel.add(textadmin2);
        textadmin2.setColumns(10);
        
        JLabel label31 = new JLabel("座位行数：");
        label31.setBounds(30, 105, 90, 15);
        panel.add(label31);
        textadmin3 = new JTextField();
        textadmin3.setBounds(100, 103, 194, 21);
        panel.add(textadmin3);
        textadmin3.setColumns(10);
        
        JLabel label41 = new JLabel("座位列数：");
        label41.setBounds(30, 145, 90, 15);
        panel.add(label41);
        textadmin4 = new JTextField();
        textadmin4.setBounds(100, 143, 194, 21);
        panel.add(textadmin4);
        textadmin4.setColumns(10);
        
        JLabel label51 = new JLabel("备注：");
        label51.setBounds(45, 185, 45, 15);
        panel.add(label51);
        textadmin5 = new JTextArea();
        textadmin5.setBounds(100, 183, 194, 69);
        panel.add(textadmin5);
        
        checkBoxTrue = new JCheckBox("自动生成座位");
        checkBoxTrue.setBounds(55,270,160,30);
        panel.add(checkBoxTrue);
        checkBoxTrue.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                do_checkBox1_actionPerformed(e);
            }
        });
  
        JButton buttonadmin = new JButton("确定");
        buttonadmin.setBounds(100,320,65,35);
        panel.add(buttonadmin);
        buttonadmin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                do_buttonadmin_actionPerformed(e);	
            }
        });
        
        JButton buttonCancle = new JButton("取消");
        buttonCancle.setBounds(200,320,65,35);
        panel.add(buttonCancle);
        buttonCancle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e1) {
            	Cancle(e1);
            }
        });
        this.add(panel);
		this.setTitle("添加演出厅信息");
		this.setSize(390,450);
		this.setLocationRelativeTo(null);
		this.setResizable(false);	//禁止改变窗体大小
		this.setVisible(true);
    }
    
	protected void do_checkBox1_actionPerformed(ActionEvent e) {
		if(checkBoxTrue.isSelected()){
			flag=1;
		}
		else{
			flag=0;
		}
	}
	
	protected void Cancle(ActionEvent e) {
		this.dispose();
	}
	
	protected void do_buttonadmin_actionPerformed(ActionEvent e) {
		String name= textadmin1.getText();
		String position = textadmin2.getText();
		String row = textadmin3.getText();
		String col = textadmin4.getText();
		String introduce = textadmin5.getText();
		int rowInt = Integer.parseInt(row);
		int colInt = Integer.parseInt(col);
		Studio stu = new Studio();
		stu.setName(name);
		stu.setPosition(position);
		stu.setRowCount(rowInt);
		stu.setColCount(colInt);
		stu.setIntroduction(introduce);
		stu.setFlag(flag);
		new StudioSrv().add(stu);
		System.out.println("flag->>>>>"+flag);
		if(flag==1){	
			getSeatID();
		}
		//关闭窗口
		this.frame.dispose();
		this.dispose();
		new AdminUI();
	}

    protected void getSeatID(){
    	Studio studio = new Studio();
		int id = 0;
		String row = textadmin3.getText();
		String col = textadmin4.getText();
		String introduce = textadmin5.getText();
		int rowInt = Integer.parseInt(row);
		int colInt = Integer.parseInt(col);
		List<Studio> list = new StudioSrv().FetchStudio(textadmin1.getText());
		id = list.get(list.size()-1).getId();
		System.out.println("id-->>>>"+id);
		new SeatUI(rowInt,colInt,id);
    }

     
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

     
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

     
    public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

     
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
   
    
//    public static void main(String[] args) {
//    	new StudioAddUI();
//    }

}
