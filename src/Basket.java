import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Basket extends BaseFrame{
	DefaultTableModel model=model(new String[] {"메뉴명", "가격", "수량", "사이즈", "금액", ""});
	JButton button[]=new JButton[3];

	public Basket() {
		super("장바구니", 600, 350);
		
		var s=new JPanel();
		String cap[]= {"구매", "삭제", "닫기"};
		JTable table=table(model);
		
		add(label(NAME+"회원님 장바구니", JLabel.CENTER, 25), BorderLayout.NORTH);
		add(new JScrollPane(table));
		add(s, BorderLayout.SOUTH);
		
		int i=0;
		for(var str:cap) {
			s.add(button[i]=size(button(str, e->{
				if(e.getActionCommand()==cap[2]) {
					dispose();
				}
				else if(e.getActionCommand()==cap[1]) {
					int row=table.getSelectedRow();
					if(row==-1) {
						err_msg("삭제할 메뉴를 선택해주세요.");
						return;
					}
					execute("delete from shopping where s_no="+table.getValueAt(row, 5));
					addRow();
				}
				else {
					for(int j=0; j<model.getRowCount(); j++) {
						int point=toint(getInfo()[5]+"");
						int price=toint(model.getValueAt(j, 4)+"");
						Object row[]=new Object[6];
						Object info[]=getInfo("select m_no, m_group from menu"
								+ " where m_name='"+model.getValueAt(j, 0)+"'", 2);
						row[0]=info[0];
						row[1]=info[1];
						row[2]=model.getValueAt(j, 3);
						row[3]=model.getValueAt(j, 1);
						row[4]=model.getValueAt(j, 2);
						row[5]=price;
						if(price>point) {
							StarBox.pay(row, price);
						} else {
							StarBox.pay_point(row, price, point);
						}
					}
					execute("delete from shopping");
					StarBox.setInfo();
					dispose();
				}
			}), 120, 30));
			i++;
		}
		
		addRow();
		if(model.getRowCount()==0) {
			button[0].setEnabled(false);
			button[1].setEnabled(false);
		}
		table.getColumnModel().getColumn(5).setMinWidth(0);
		table.getColumnModel().getColumn(5).setMaxWidth(0);
		table.getColumnModel().getColumn(0).setMinWidth(200);
		setVisible(true);
	}
	
	void addRow() {
		addRow(model, "select m_name, s_price, s_count, s_size, s_amount, s_no "
				+ "from shopping s inner join menu m on s.m_no=m.m_no");
		if(model.getRowCount()==0) {
			button[0].setEnabled(false);
			button[1].setEnabled(false);
		}
	}
	
	public static void main(String[] args) {
		setLogin(2);
		new StarBox();
	}
}
