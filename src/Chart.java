import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.sql.SQLException;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Chart extends BaseFrame{
	JComboBox<String> group=new JComboBox<String>();
	JPanel chart;
	
	public Chart() {
		super("인기상품 Top5", 400, 450);
		chart=new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				super.paintComponent(g);
				int gap=55, bar_h=25, max=0;
				double bar_max=300;
				Graphics2D g2=(Graphics2D)g;
				
				Color color[]= {Color.red, Color.orange, Color.yellow, Color.green, 
						Color.cyan};
				g2.setStroke(new BasicStroke(2));
//				g.setFont(label("").getFont());
				try {
					var rs=stmt.executeQuery("select sum(o_amount), m_name, sum(o_count)"
							+ " as s from orderlist o inner join menu m on m.m_no=o.m_no"
							+ " where o_group='"+group.getSelectedItem()
							+"' group by m.m_no order by s desc limit 5");
					int i=0;
					while(rs.next()) {
						if(i==0) {
							max=rs.getInt(3);
						}
						g.setColor(color[i]);
						g.fillRect(0, gap*i+30, (int)(bar_max*rs.getInt(3)/max), bar_h);
						g.setColor(Color.black);
						g2.drawRect(0, gap*i+30, (int)(bar_max*rs.getInt(3)/max), bar_h);
						g.drawString(rs.getString(2)+"-"+rs.getString(3)+"개", 10, gap*i+70);
						i++;
					}
					g2.setStroke(new BasicStroke(3));
					g.setColor(Color.black);
					g2.drawLine(0, 0, 0, 300);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
		var n=new JPanel();
		var c=new JPanel(new GridLayout(0, 1));
		add(n, "North");
		add(c);
		
		n.setBackground(Color.LIGHT_GRAY);
		n.add(group);
		n.add(label("인기상품 Top5", JLabel.CENTER, 20));
		c.add(chart);
		c.setBorder(new EmptyBorder(30, 40, 0, 0));
		
		group.addItem("음료");
		group.addItem("푸드");
		group.addItem("상품");
		
		group.addItemListener(e->{
			chart.repaint();
			chart.revalidate();
		});
		
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new Chart();
	}
}
