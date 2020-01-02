import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Register extends BaseFrame {
	int size=20;
	JTextField txt[]= {
			new JTextField(size),
			new JTextField(size),
			new JTextField(size)
	};
	String cap[]= {"이름", "아이디", "비밀번호", "생년월일"}, bcap[]= {"가입 완료", "취소"};
	JComboBox combo[]= {
			new JComboBox<Integer>(),
			new JComboBox<Integer>(),
			new JComboBox<Integer>()
	};
	
	
	public Register() {
		super("회원가입", 300, 250);
		var s=new JPanel();
		var c=new JPanel(new GridLayout(0, 1, 0, 10));
		
		add(s, "South");
		add(c);
		c.setBorder(new EmptyBorder(10, 0, 0, 5));
		
		for(int i=1900; i<=LocalDate.now().getYear(); i++) {
			combo[0].addItem(i);
		}
		for(int i=1; i<=12; i++) {
			combo[1].addItem(i);
		}
		for(int i=0; i<2; i++) {
			combo[i].setSelectedIndex(-1);
			combo[i].addItemListener(e->chkMonth(e));
		}
		
		for(var str:bcap) {
			s.add(button(str, e->{
				if(e.getActionCommand().contentEquals(bcap[0])) {
					for(int i=0; i<3; i++) {
						if(txt[i].getText().contentEquals("") || 
								combo[i].getSelectedIndex()==-1) {
							err_msg("누락된 항목이 있습니다.");
							return;
						}
					}
					try {
						var rs=stmt.executeQuery("select * from user where u_id='"+
								txt[1].getText()+"'");
						if(rs.next()) {
							err_msg("아이디가 중복되었습니다.");
							txt[1].setText("");
							return;
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					String birth=combo[0].getSelectedItem()+"-"+combo[1].getSelectedItem()
							+"-"+combo[2].getSelectedItem();
					DateTimeFormatter format=DateTimeFormatter.ofPattern("yyyy-M-d");
					LocalDate date=LocalDate.parse(birth, format);
					execute("insert into user values(0, '"+txt[1].getText()+"', "
							+ "'"+txt[2].getText()+"', "
							+ "'"+txt[0].getText()+"', "
							+ "'"+date+"', 0, '일반')");
					msg("가입완료 되었습니다.");
					dispose();
				} else dispose();
			}));
		}
		
		for(int i=0; i<cap.length; i++) {
			var c_row=new JPanel(new FlowLayout());
			c_row.add(size(label(cap[i], JLabel.RIGHT), 60, 20));
			if(i==3) {
				String ccap[]= {"년", "월", "일"};
				for(int j=0; j<3; j++) {
					c_row.add(combo[j]);
					c_row.add(label(ccap[j]));
				}
				c.add(c_row);
				continue;
			}
			c_row.add(txt[i]);
			c.add(c_row);
		}
		
		setVisible(true);
	}
	
	void chkMonth(ItemEvent e) {
		if(combo[0].getSelectedIndex()==-1 || combo[1].getSelectedIndex()==-1) {
			return;
		}
		LocalDate date=LocalDate.of((int)combo[0].getSelectedItem(), 
				(int)combo[1].getSelectedItem(), 1);
		combo[2].removeAllItems();
		int last=date.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
		
		for(int i=1; i<=last; i++) combo[2].addItem(i);
	}

	public static void main(String[] args) {
		new Register();
	}
}
