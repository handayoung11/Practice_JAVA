import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.imageio.ImageIO;

public class AddMenu extends BaseFrame{

	public AddMenu() {
		super("�޴��߰�", 360, 250);
		MenuPanel m=new MenuPanel(this) {
			@Override
			void event() {
				// TODO Auto-generated method stub
				if(txt[0].getText().contentEquals("") || txt[1].getText().contentEquals("")
						|| file==null) {
					err_msg("��ĭ�� �����մϴ�.");
					return;
				}
				if(!txt[1].getText().matches("[0-9]*")) {
					err_msg("������ ���ڷ� �Է����ּ���.");
					return;
				}
				try {
					var rs=stmt.executeQuery("select * from menu where m_name='"+
							txt[0].getText()+"'");
					if(rs.next()) {
						err_msg("�̹� �����ϴ� �޴����Դϴ�.");
						return;
					}
				} catch (SQLException e) {
					e.printStackTrace();
					return;
				}
				try {
					ImageIO.write(ImageIO.read(file), "jpg", 
							new File("./DataFiles/�̹���/"+txt[0].getText()+".jpg"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				execute("insert into menu values(0, '"+combo.getSelectedItem()+
						"', '"+txt[0].getText()+"', "+txt[1].getText()+")");

				msg("�޴��� ��ϵǾ����ϴ�.");
			}
		};
		add(m);
		setVisible(true);
	}

	public static void main(String[] args) {
		new AddMenu();
	}
}
