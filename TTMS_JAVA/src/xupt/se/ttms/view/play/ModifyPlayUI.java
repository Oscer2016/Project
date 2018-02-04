package xupt.se.ttms.view.play;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import xupt.se.ttms.model.Play;
import xupt.se.ttms.service.PlaySrv;
import xupt.se.ttms.service.ScheduleSrv;
import xupt.se.ttms.view.tmpl.WindowOpacity;
import xupt.se.util.MyFont;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class ModifyPlayUI extends JFrame implements ActionListener, MouseListener {

	// 获得屏幕的大小
	final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;

	private JTextField name_textField, typeId_textField, time_textField, langId_textField, price_textField;
	JComboBox<String> lang_comboBox, type_comboBox;
	private JButton button_reset, button_add;
	private JPanel contentPane;
	private JTextArea textArea;

	private Play play = new Play();

	/**
	 * 创建框架
	 */
	
	JFrame jframe = null; 
	
	public ModifyPlayUI(PlayUI jframe, Play play) {

		this.jframe = jframe;
		this.play = play;
		new WindowOpacity(this);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel label = new JLabel("修改剧目");
		label.setFont(MyFont.Title);
		label.setBounds(150, 20, 90, 30);
		contentPane.add(label);

		name_textField = new JTextField();
		name_textField.setFont(MyFont.Item);
		name_textField.setBounds(90, 70, 220, 30);
		name_textField.setText(play.getName());
		contentPane.add(name_textField);
		name_textField.setColumns(10);
		JLabel label1 = new JLabel("剧目名称");
		label1.setFont(MyFont.Item);
		label1.setBounds(20, 70, 80, 20);
		contentPane.add(label1);

		type_comboBox = new JComboBox<>();
		type_comboBox.setBounds(90, 110, 220, 30);
		type_comboBox.setFont(MyFont.Item);
//		type_comboBox.setSelectedIndex(play.getType()+1);
		contentPane.add(type_comboBox);
		JLabel label2 = new JLabel("剧目类型");
		label2.setFont(MyFont.Item);
		label2.setBounds(20, 110, 80, 30);
		contentPane.add(label2);

		inittype_comboBox();

		lang_comboBox = new JComboBox<>();
		lang_comboBox.setFont(MyFont.Item);
		lang_comboBox.setBounds(90, 150, 220, 30);
//		lang_comboBox.setSelsectedIndex(play.getLangId()+1);
		contentPane.add(lang_comboBox);
		JLabel label4 = new JLabel("剧目语言");
		label4.setFont(MyFont.Item);
		label4.setBounds(20, 150, 80, 30);
		contentPane.add(label4);

		initlang_comboBox();

		time_textField = new JTextField();
		time_textField.setFont(MyFont.Item);
		time_textField.setBounds(90, 200, 220, 30);
		time_textField.setText("" + play.getLength());
		contentPane.add(time_textField);
		time_textField.setColumns(10);
		JLabel label3 = new JLabel("剧目时长");
		label3.setFont(MyFont.Item);
		label3.setBounds(20, 200, 80, 30);
		contentPane.add(label3);

//		price_textField = new JTextField();
//		price_textField.setFont(MyFont.Item);
//		price_textField.setBounds(90, 250, 220, 30);
//		price_textField.setText(""+play.getTicketPrice());
//		contentPane.add(price_textField);
//		price_textField.setColumns(10);
//		JLabel label5 = new JLabel("剧目票价");
//		label5.setFont(MyFont.Item);
//		label5.setBounds(20, 250, 80, 30);
//		contentPane.add(label5);

		textArea = new JTextArea();
		textArea.setFont(MyFont.Item);
		textArea.setBounds(90, 290, 220, 160);
		textArea.setText(play.getIntroduction());;
		contentPane.add(textArea);
		JLabel label6 = new JLabel("剧目简介");
		label6.setFont(MyFont.Item);
		label6.setBounds(20, 296, 80, 30);
		contentPane.add(label6);

		// 添加按钮
		button_add = new JButton("修改");
		button_add.setBounds(50, 462, 70, 30);
		button_add.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		button_add.setForeground(Color.white);
		button_add.setFont(MyFont.Static);
		button_add.addMouseListener(this);
		contentPane.add(button_add);

		button_reset = new JButton("取消");
		button_reset.setBounds(180, 462, 70, 30);
		button_reset.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
		button_reset.setForeground(Color.white);
		button_reset.setFont(MyFont.Static);
		button_reset.addMouseListener(this);

		contentPane.add(button_reset);


		this.setSize((int)(width * 0.28f), (int)(height * 0.75f));
		this.setResizable(false);
		this.requestFocus();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	private void inittype_comboBox() {
		Vector<String> playtype = new Vector<String>();
		playtype = new PlaySrv().listingtype();
		for (int i = 0; i < playtype.size(); i++) {
			type_comboBox.addItem(playtype.elementAt(i));
		}
	}

	private void initlang_comboBox() {
		Vector<String> playlang = new Vector<String>();
		playlang = new PlaySrv().listing();
		for (int i = 0; i < playlang.size(); i++) {
			lang_comboBox.addItem(playlang.elementAt(i));
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == button_add) {
			play.setTypeId(type_comboBox.getSelectedIndex() + 1);
			play.setLangId(lang_comboBox.getSelectedIndex() + 1);
			play.setName(name_textField.getText().toString());
			play.setIntroduction(textArea.getText().toString());
			play.setLength(Integer.parseInt(time_textField.getText().toString()));
//			play.setTicketPrice(Integer.parseInt(price_textField.getText().toString()));
			try {
				new PlaySrv().modify(play);
				JOptionPane.showMessageDialog(null, "修改成功！！！");
				this.dispose();
				this.jframe.dispose();
				new PlayUI();
			} catch (Exception e2) {
				JOptionPane.showMessageDialog(null, "修改失败！！！");
				this.dispose();
			}
		}
		if (e.getSource() == button_reset) {
			name_textField.setText(null);
			// typeId_textField.setText(null);
			time_textField.setText(null);
			// langId_textField.setText(null);
			price_textField.setText(null);
			textArea.setText(null);
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自动生成的方法存根

	}
	
//	public static void main(String[] args) {
//		new PlayaddUI();
//	}

}
