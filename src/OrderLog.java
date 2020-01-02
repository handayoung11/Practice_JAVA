import java.awt.BorderLayout;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class OrderLog extends BaseFrame{
	JTextField txt=new JTextField(25);
	DefaultTableModel model=model(new String[] {"구매일자", "메뉴명", "가격", "사이즈", "수량", "총금액"});
	JTable table=new JTable(model);
	
	public OrderLog() {
		super("구매내역", 700, 350);
		var s=new JPanel();
		
		add(label(NAME+"회원님 구매내역", JLabel.CENTER, 25), BorderLayout.NORTH);
		add(new JScrollPane(table));
		add(s, BorderLayout.SOUTH);
		
		addRow(model, "select o_date, m_name, format(o_price, 0), o_size, o_count, "
				+ "format(o_amount, 0) from orderlist o inner join menu m on m.m_no=o.m_no"
				+ " where u_no="+NO);
		s.add(label("총 결제 금액", JLabel.CENTER));
		s.add(txt);
		s.add(button("닫기", e->{
			dispose();
		}));
		
		try {
			var rs=stmt.executeQuery("select format(sum(o_amount), 0) from orderlist where"
					+ " u_no="+NO);
			rs.next();
			txt.setText(rs.getString(1));
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(int i=0; i<table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(DTCR);
		}
		table.getColumnModel().getColumn(1).setMinWidth(200);
		txt.setEditable(false);
		txt.setHorizontalAlignment(JTextField.RIGHT);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		setLogin(1);
		new OrderLog();
	}
}
