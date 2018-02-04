package xupt.se.ttms.view.schedule;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.sun.org.apache.xpath.internal.operations.Mod;

import xupt.se.ttms.model.Schedule;
import xupt.se.ttms.service.PlaySrv;
import xupt.se.ttms.service.ScheduleSrv;
import xupt.se.util.Item;
import xupt.se.util.MyFont;

public class ModifyScheduleUI extends JFrame implements MouseListener, ActionListener {

	private JLabel label, label_stuname, label_playname, label_time, label_price;
	private JTextField textField_stuname, textField_playname, textField_price;
	private JButton modifybtn, quitebtn, timebtn;
	private JPanel contentPane;
	private JComboBox<String> play_comboBox, studio_comboBox;
	private int item = 0;

	private Schedule schedule = new Schedule();
	/**
	 * Launch the application.
	 */
	// public static void main(String[] args) {
	// new ModifyScheduleUI(schedule);
	// }

	/**
	 * Create the frame.
	 */
	public ModifyScheduleUI(Schedule schedule, int studio_id, int play_id) {
		this.schedule = schedule;
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(700, 100, 600, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		initPanel(studio_id, play_id);
		this.setVisible(true);
	}

	private void initPanel(int studio_id, int play_id) {

		JPanel panel = new JPanel();
		panel.setLayout(null);
		contentPane.add(panel, BorderLayout.CENTER);

		label = new JLabel("修改演出计划");
		label.setFont(new Font("微软雅黑", Font.PLAIN, 22));
		label.setBounds(225, 50, 150, 30);
		panel.add(label);

		label_stuname = new JLabel("演出厅名称");
		label_stuname.setFont(MyFont.PLAIN);
		label_stuname.setBounds(100, 160, 100, 30);
		panel.add(label_stuname);
		studio_comboBox = new JComboBox<>();
		studio_comboBox.setFont(MyFont.PLAIN);
		studio_comboBox.setMaximumRowCount(8);
		// studio_comboBox.setSelectedIndex(studio_id);

		studio_comboBox.setBounds(210, 160, 250, 30);
		panel.add(studio_comboBox);
		initstudio_comboBox(item);

		label_playname = new JLabel("演出剧目");
		label_playname.setFont(MyFont.PLAIN);
		label_playname.setBounds(100, 240, 100, 30);
		panel.add(label_playname);
		play_comboBox = new JComboBox<>();
		play_comboBox.setFont(MyFont.PLAIN);
		play_comboBox.setMaximumRowCount(8);
		play_comboBox.setBounds(210, 240, 250, 30);
		panel.add(play_comboBox);
		// play_comboBox.setSelectedIndex(play_id);
		initplay_comboBox();

		label_time = new JLabel("演出时间");
		label_time.setFont(MyFont.PLAIN);
		label_time.setBounds(100, 320, 100, 30);
		panel.add(label_time);
		timebtn = new DateChooserJButton();
		timebtn.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		timebtn.setBounds(210, 320, 250, 30);
		timebtn.setText("" + schedule.getSched_time());
		panel.add(timebtn);
		// System.out.println("---->>>>"+timebtn.getText());

		label_price = new JLabel("票价");
		label_price.setFont(MyFont.PLAIN);
		label_price.setBounds(100, 400, 100, 30);
		panel.add(label_price);
		textField_price = new JTextField();
		textField_price.setText("" + schedule.getSched_ticket_price());
		textField_price.setFont(MyFont.PLAIN);
		textField_price.setBounds(210, 400, 250, 30);
		panel.add(textField_price);

		modifybtn = new JButton("确认修改");
		modifybtn.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		modifybtn.setBounds(150, 550, 100, 25);
		panel.add(modifybtn);
		modifybtn.addMouseListener(this);

		quitebtn = new JButton("放弃编辑");
		quitebtn.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		quitebtn.setBounds(350, 550, 100, 25);
		panel.add(quitebtn);
		quitebtn.addMouseListener(this);
	}

	private void initplay_comboBox() {
		Vector playname = new Vector<>();
		playname = new ScheduleSrv().listing();
		for (int i = 0; i < playname.size(); i++) {
			play_comboBox.addItem((String) playname.elementAt(i));
		}
	}

	private void initstudio_comboBox(int ID) {
		Vector studioname = new Vector<>();
		studioname = new PlaySrv().listingStudio(item);
		for (int i = 0; i < studioname.size(); i++) {
			studio_comboBox.addItem((String) studioname.elementAt(i));

		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == quitebtn) {
			this.dispose();
		} else if (e.getSource() == modifybtn) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				schedule.setPlay_name((play_comboBox.getSelectedItem()).toString());
				schedule.setSched_ticket_price(Double.parseDouble(textField_price.getText()));
				schedule.setStudio_name(studio_comboBox.getSelectedItem().toString());
				schedule.setPlay_id(play_comboBox.getSelectedIndex() + 1);
				schedule.setStudio_id(studio_comboBox.getSelectedIndex());
				try {
					schedule.setSched_time(sdf.parse(timebtn.getText() + ":ss"));
				} catch (ParseException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}

				new ScheduleSrv().modify(schedule);
				this.dispose();

			} catch (Exception e2) {
				// TODO: handle exception
			}
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

}
