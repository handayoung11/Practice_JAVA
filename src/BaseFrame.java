import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class BaseFrame extends JFrame{
	static Statement stmt;
	static Connection con;
	static String NAME, ID, query;
	static int NO;
	static DefaultTableCellRenderer DTCR=new DefaultTableCellRenderer(); 
	
	public BaseFrame(String title, int w, int h) {
		// TODO Auto-generated constructor stub
		super(title);
		setSize(w, h);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	static {
		try {
			con=DriverManager.getConnection("jdbc:mysql://localhost?"
					+ "serverTimezone=UTC&allowLoadLocalInfile=true&"
					+ "allowPublicKeyRetrieval=true", "user", "1234");
			stmt=con.createStatement();
			DTCR.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
			execute("use coffee");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	static void execute(String sql) {
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	static ImageIcon img(String path, int w, int h) {
		try {
			return new ImageIcon(ImageIO.read(new File(path)).getScaledInstance(
					w, h, Image.SCALE_SMOOTH));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	JLabel label(ImageIcon img) {
		JLabel label=new JLabel(img);
		return label;
	}
	
	static void setLogin(int no) {
		try {
			var rs=stmt.executeQuery("select * from user where u_no="+no);
			rs.next();
			ID=rs.getString(2);
			NAME=rs.getString(4);
			NO=rs.getInt(1);
			query="select * from user where u_id='"+ID+"'";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	JTable table(DefaultTableModel model) {
		JTable table=new JTable(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		return table;
	}
	
	void addRow(DefaultTableModel model, String sql) {
		model.setRowCount(0);
		try {
			var rs=stmt.executeQuery(sql);
			while(rs.next()) {
				Object row[]=new Object[model.getColumnCount()];
				for(int i=0; i<row.length; i++) {
					row[i]=rs.getString(i+1);
				}
				model.addRow(row);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	DefaultTableModel model(String title[]) {
		DefaultTableModel model=new DefaultTableModel(null, title) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		return model;
	}
	
	static <T extends JComponent> T size(T c, int w, int h) {
		c.setPreferredSize(new Dimension(w, h));
		return c;
	}
	
	static Object[] getInfo(String query, int size) {
		try {
			var rs=stmt.executeQuery(query);
			rs.next();
			Object row[]=new Object[size];
			for(int i=0; i<row.length; i++) {
				row[i]=rs.getString(i+1);
			}
			return row;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	static Object[] getInfo() {
		try {
			var rs=stmt.executeQuery(query);
			rs.next();
			Object row[]=new Object[7];
			for(int i=0; i<row.length; i++) {
				row[i]=rs.getString(i+1);
			}
			return row;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	static int toint(String str) {
		return Integer.parseInt(str);
	}
	
	void err_msg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "메시지", JOptionPane.ERROR_MESSAGE);
	}
	
	static void msg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "메시지", JOptionPane.INFORMATION_MESSAGE);
	}
	
	static JButton button(String cap, ActionListener a) {
		JButton button=new JButton(cap);
		button.addActionListener(a);
		return button;
	}
	
	static JLabel label(String cap) {
		JLabel label=new JLabel(cap);
		return label;
	}
	
	static JLabel label(String cap, int alig) {
		JLabel label=new JLabel(cap, alig);
		return label;
	}
	
	static JLabel label(String cap, int alig, int size) {
		JLabel label=new JLabel(cap, alig);
		label.setFont(new Font("", Font.BOLD, size));
		return label;
	}
	
	class Before extends WindowAdapter{
		BaseFrame b;
		
		public Before(BaseFrame bef) {
			b=bef;
		}
		
		@Override
		public void windowClosed(WindowEvent e) {
			b.setVisible(true);
		}
	}
}
