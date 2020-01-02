import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class ModifyMenu extends BaseFrame{
	JComboBox<String> combo=new JComboBox<String>();
	JTextField txt=new JTextField(20);
	JButton jB=new JButton("찾기");
	DefaultTableModel model=model(new String[] {"분류", "메뉴명", "가격"});
	JTable table=table(model);
	MenuPanel e;
	int row=-1;
	String path;
	
	public ModifyMenu() {
		super("메뉴 수정", 750, 300);
		var c=new JPanel(new BorderLayout());
		var n=new JPanel(new FlowLayout(FlowLayout.LEFT));
		e=new MenuPanel(this) {
			
			@Override
			void event() {
				// TODO Auto-generated method stub
				if(row==-1) {
					err_msg("삭제할 메뉴를 선택해주세요.");
					return;
				}
				
				new File(path).delete();
				execute("delete from menu where m_name='"+table.getValueAt(row, 1)+"'");
				msg("삭제되었습니다.");
				model.removeRow(row);
				setDef();
			}
			
			@Override
			void modify() {
				if(row==-1) {
					err_msg("수정할 메뉴를 선택해주세요.");
					return;
				}
				if(txt[0].getText().contentEquals("") || txt[1].getText().contentEquals("")
						|| img.getIcon()==null) {
					err_msg("빈칸이 존재합니다.");
					return;
				}
				if(!txt[1].getText().matches("[0-9]*")) {
					err_msg("가격을 다시 입력해주세요.");
					return;
				}
				try {
					var rs=stmt.executeQuery("select * from menu where m_name='"
							+txt[0].getText()+"'");
					if(rs.next()) {
						if(!rs.getString("m_name").contentEquals(table.getValueAt(row, 1)+"")) {
							err_msg("이미 존재하는 메뉴명입니다.");
							return;
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
				execute("update menu set m_group='"+combo.getSelectedItem()+"', "
						+ "m_name='"+txt[0].getText()+"', m_price="+txt[1].getText()+""
						+ " where m_name='"+table.getValueAt(row, 1)+"'");
				try {
					var im=ImageIO.read(file);
					ImageIO.write(im, "jpg", new File("./DataFiles/이미지/"+txt[0].getText()+".jpg"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				msg("수정되었습니다.");
				table.setValueAt(combo.getSelectedItem(), row, 0);
				table.setValueAt(txt[0].getText(), row, 1);
				table.setValueAt(txt[1].getText(), row, 2);
			}
		};
		
		add(c);
		add(n, "North");
		add(e, "East");
		
		n.add(label("검색"));
		n.add(combo);
		n.add(txt);
		n.add(button("찾기", it->{
			addRow(combo.getSelectedItem()+"");
		}));

		combo.addItem("전체");
		combo.addItem("음료");
		combo.addItem("푸드");
		combo.addItem("상품");
		
		e.combo.setSelectedIndex(-1);
		e.jB[0].setText("사진선택");
		e.jB[1].setText("삭제");
		((JPanel)getContentPane()).setBorder(new EmptyBorder(0, 0, 10, 10));
		c.add(new JScrollPane(table));
		
		table.getColumnModel().getColumn(1).setMinWidth(200);
		table.getColumnModel().getColumn(0).setCellRenderer(DTCR);
		table.getColumnModel().getColumn(2).setCellRenderer(DTCR);
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				setInfo();
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				setInfo();
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				setInfo();
			}
		});
		
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				setInfo();
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				setInfo();
			}
		});
				
		addRow("전체");
		setVisible(true);
	}
	
	void setInfo() {
		row=table.getSelectedRow();
		e.combo.setSelectedItem(table.getValueAt(row, 0));
		e.txt[0].setText(table.getValueAt(row, 1)+"");
		e.txt[1].setText(table.getValueAt(row, 2)+"");
		path="./DataFiles/이미지/"+table.getValueAt(row, 1)+".jpg";
		e.img.setIcon(img(path, 130, 140));
		e.file=new File(path);
	}
	
	void setDef() {
		row=-1;
		e.txt[0].setText("");
		e.combo.setSelectedIndex(-1);
		e.txt[1].setText("");
		e.img.setIcon(null);
		e.file=null;
	}
	
	void addRow(String str) {
		String sql="select m_group, m_name, m_price from menu where m_name like '%"+
				txt.getText()+"%'";
		if(!str.contentEquals("전체")) sql+=" and m_group ='"+str+"'";
		addRow(model, sql);
	}
	
	public static void main(String[] args) {
		new ModifyMenu();
	}
}
