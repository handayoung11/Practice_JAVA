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
	DefaultTableModel model=model(new String[] {"��������", "�޴���", "����", "������", "����", "�ѱݾ�"});
	JTable table=new JTable(model);
	
	public OrderLog() {
		super("���ų���", 700, 350);
		var s=new JPanel();
		
		add(label(NAME+"ȸ���� ���ų���", JLabel.CENTER, 25), BorderLayout.NORTH);
		add(new JScrollPane(table));
		add(s, BorderLayout.SOUTH);
		
		addRow(model, "select o_date, m_name, format(o_price, 0), o_size, o_count, "
				+ "format(o_amount, 0) from orderlist o inner join menu m on m.m_no=o.m_no"
				+ " where u_no="+NO);
		s.add(label("�� ���� �ݾ�", JLabel.CENTER));
		s.add(txt);
		s.add(button("�ݱ�", e->{
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
