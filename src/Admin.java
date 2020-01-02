import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Admin extends BaseFrame{

	public Admin() {
		super("관리자 메뉴", 270, 180);
		((JPanel)getContentPane()).setBorder(new EmptyBorder(10, 0, 10, 0));
		setLayout(new GridLayout(0, 1));
		String str[]= {"메뉴 등록", "메뉴 관리", "로그아웃"};
		for(String cap:str) {
			add(button(cap, it->{
				if(it.getActionCommand()==str[2]) {
					dispose();
				}
				else if(it.getActionCommand()==str[1]) {
					new ModifyMenu().addWindowListener(new Before(this));
				}
				else {
					new AddMenu().addWindowListener(new Before(this));
				}
				setVisible(false);
			}));
		}
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new Admin();
	}
}
