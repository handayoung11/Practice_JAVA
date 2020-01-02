import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Login extends BaseFrame{
	JTextField txt[]= {
		new JTextField(18),
		new JPasswordField(18)
	};
	String cap[]= {"회원가입", "종료"}, lcap[]= {"ID", "PW"};
	
	public Login() {
		super("로그인", 350, 200);
		var c=new JPanel(new BorderLayout());
		var s=new JPanel();
		var c_c=new JPanel(new GridLayout(0, 1));
		
		c.setBorder(new EmptyBorder(0, 0, 10, 20));
		c_c.setBorder(new EmptyBorder(10, 0, 0, 0));
		
		add(c);
		add(s, "South");
		c.add(c_c);
		c.add(button("로그인", e->{
			if(txt[0].getText().contentEquals("") || txt[1].getText().contentEquals("")) {
				err_msg("빈칸이 존재합니다.");
				return;
			}
			if(txt[0].getText().contentEquals("admin") 
					&& txt[1].getText().contentEquals("1234")) {
				setVisible(false);
				txt[0].setText("");
				txt[1].setText("");
				new Admin().addWindowListener(new Before(this));
				return;
			}
			try {
				var rs=stmt.executeQuery("select * from user where "
						+ "u_id='"+txt[0].getText()+"' and u_pw='"+txt[1].getText()+"'");
				rs.next();
				NAME=rs.getString(4);
				ID=rs.getString(2);
				NO=rs.getInt(1);
				query="select * from user where u_id='"+ID+"'";
				execute("delete from shopping");
				setVisible(false);
				txt[0].setText("");
				txt[1].setText("");
				new StarBox().addWindowListener(new Before(this));
			} catch (SQLException e1) {
				e1.printStackTrace();
				err_msg("회원정보가 틀립니다.다시 입력해주세요.");
			}
				
		}), "East");
		
		JLabel lbl=new JLabel("STARBOX", 0);
		lbl.setFont(new Font("arial black", Font.BOLD, 25));
		add(lbl, "North");
		
		for(int i=0; i<2; i++) {
			var c_c_p=new JPanel(new FlowLayout(2)); 
			c_c.add(c_c_p);
			c_c_p.add(label(lcap[i]+" :"));
			c_c_p.add(txt[i]);
			
			s.add(button(cap[i], e->{
				if(e.getActionCommand().contentEquals(cap[0])) {
					new Register().addWindowListener(new Before(this));
					setVisible(false);
				} else {
					dispose();
				}
			}));
		}
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new Login();
	}
}
