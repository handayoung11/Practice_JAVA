import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.sql.SQLException;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class StarBox extends BaseFrame{
	String bcap1[]= {"구매내역", "장바구니", "인기상품 Top5", "Logout"}, bcap2[]= {"음료", "푸드", "상품"},
			group;
	JScrollPane scr=new JScrollPane();
	JPanel c=new JPanel(new BorderLayout(20, 0)), 
			c_e=size(new JPanel(new BorderLayout(20, 0)), 420, 0);
	JLabel img=new JLabel();
	static JLabel info=label("", JLabel.LEFT, 15);
	JTextField txt[]= {
			size(new JTextField(), 150, 25),
			size(new JTextField(), 150, 25),
			size(new JTextField(), 150, 25)
	};
	JComboBox combo[]= {
			new JComboBox<Integer>(),
			new JComboBox<String>()
	};
	int m_no;

	public StarBox() {
		super("STARBOX", 750, 600);
		var n=new JPanel(new GridLayout(0, 1));
		var w=new JPanel(new GridLayout(0, 1, 0, 5));
		add(n, "North");
		add(w, "West");
		add(c);
		c.add(scr);

		((JPanel)getContentPane()).setBorder(new EmptyBorder(0, 0, 15, 0));
		n.add(info);
		var n_row=new JPanel(new FlowLayout(FlowLayout.LEFT));
		n.add(n_row);
		for(var str:bcap1) {
			n_row.add(button(str, e->event(e)));
		}

		w.setBorder(new EmptyBorder(0, 0, 350, 0));
		for(var str:bcap2) {
			w.add(button(str, e->setList(e.getActionCommand())));
		}

		setInfo();
		setCom();
		setList("음료");
		setVisible(true);
	}

	static void setInfo() {
		Object result[]=getInfo();
			info.setText("회원명 : "+result[3]+" / 회원등급 : "+
					result[6]+" / 총 누적 포인트 : "+result[5]);
	}
	
	static void pay(Object[] row, int price) {
		execute("update user set u_point=u_point+"
				+(int)(price*0.05)+" where u_id='"+ID+"'");
		
		
		execute("insert into orderlist values("
				+ "0, curdate(), "+NO+", "+row[0]+", '"+row[1]+"', "
				+ "'"+row[2]+"', "
				+ ""+row[3]+", "
				+ ""+row[4]+", "
				+ ""+row[5]+")");
		msg("구매되었습니다.");
	}

	void setCom() {
		String bcap[]= {"장바구니에 담기", "구매하기"}, 
				cap[]= {"주문메뉴", "가격", "수량", "사이즈", "총금액"};
		var s=new JPanel();
		var c=new JPanel(new GridLayout(0, 1));
		var w=new JPanel(new GridLayout(0, 1));
		c_e.add(c);
		c_e.add(s, BorderLayout.SOUTH);
		c_e.add(w, BorderLayout.WEST);

		img.setBorder(new LineBorder(Color.black));
		w.add(size(img, 150, 150));
		for(int i=0; i<cap.length; i++) {
			var tmp=new JPanel(new FlowLayout(FlowLayout.LEFT));
			tmp.add(size(label(cap[i]+" :", JLabel.RIGHT), 60, 20));
			if(i<=1) {
				tmp.add(txt[i]);
			}
			else if(i<=3) {
				tmp.add(size(combo[i-2], 150, 25));
			}
			else {
				tmp.add(txt[2]);
			}
			c.add(tmp);
		}
		for(var str:bcap) {
			s.add(button(str, a->{
				if(a.getActionCommand()==bcap[0]) {
					msg("장바구니에 담았습니다.");
					fold();
					execute("insert into shopping values(0, "+m_no+", "+
							txt[1].getText()+", "+combo[0].getSelectedItem()+", '"+
							(group.contentEquals("상품")?"":combo[1].getSelectedItem())+"', "
							+txt[2].getText()+")");
				}
				else {
					int price=toint(txt[2].getText());
					Object row[]=new Object[6];
					int point=toint(getInfo()[5]+"");
					row[0]=m_no;
					row[1]=group;
					row[2]=(group.contentEquals("상품")?"":combo[1].getSelectedItem());
					row[3]=txt[1].getText();
					row[4]=combo[0].getSelectedItem();
					row[5]=txt[2].getText();
					if(price>point) {
						pay(row, price);
					}
					else {
						pay_point(row, price, point);
					}
					setGrade(getInfo()[6]+"");
					setInfo();
				}
			}));
		}
		
		for(int i=0; i<3; i++) {
			txt[i].setEditable(false);
		}

		for(int i=1; i<=10; i++) {
			combo[0].addItem(i);
		}
		combo[1].addItem("M");
		combo[1].addItem("L");

		for(var com:combo) {
			com.addItemListener(e ->{
				double minus=1;
				try {
					var rs=stmt.executeQuery(query);
					rs.next();
					if(rs.getString(7).equals("Bronze")) {
						minus=0.97;
					}
					if(rs.getString(7).equals("Silver")) {
						minus=0.95;
					}
					if(rs.getString(7).equals("Gold")) {
						minus=0.9;
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				int price=(int)((toint(txt[1].getText())
						+(combo[1].getSelectedIndex()==1?1000:0))
						*(int)combo[0].getSelectedItem()*minus);
				txt[2].setText(price+"");
			});
		}

		w.setBorder(new EmptyBorder(0, 0, 70, 0));
		c_e.setBorder(new EmptyBorder(100, 10, 120, 0));
	}
	
	static void pay_point(Object row[], int price, int point) {
		int yes=JOptionPane.showConfirmDialog(null, 
				"회원님의 총 포인트 : "+point+"\n"
						+ "포인트로 결제하시겠습니까?\n"
						+ "(아니오를 클릭 시 현금결제가 됩니다)", 
						"결제수단", JOptionPane.YES_NO_OPTION);
		if(yes==JOptionPane.YES_OPTION) {
			point-=toint(row[5]+"");
			execute("update user set u_point="+point+" where u_id='"+ID+"'");
			msg(" 포인트로 결제 완료되었습니다.\n남은 포인트 : "+point);
		}
		else {
			pay(row, price);
		}
	}
	
	static void setGrade(String grade) {
		try {
			var rs=stmt.executeQuery("select sum(o_amount) from orderlist"
					+ " where u_no="+NO);
			rs.next();
			String grdChk=grade;
			if(rs.getInt(1)>=300000) {
				grdChk="Bronze";
			}
			if(rs.getInt(1)>=500000) {
				grdChk="Silver";
			}
			if(rs.getInt(1)>=800000) {
				grdChk="Gold";
			}
			if(!grdChk.contentEquals(grade)) {
				msg("축하합니다!\n회원님 등급이 "+grdChk+"로 승급하셨습니다.");
			}
			execute("update user set u_grade='"+grdChk+"' where u_no="+NO);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void setList(String group) {
		this.group=group;
		var scr_c=new JPanel(new GridLayout(0, 3, 10, 15));
		scr.setViewportView(scr_c);

		try {
			var rs=stmt.executeQuery("select * from menu where m_group='"+group+"'");
			combo[1].setEnabled(!group.contentEquals("상품"));
			while(rs.next()) {
				var name=rs.getString("m_name");
				var price=rs.getInt("m_price");
				var mno=rs.getInt(1);
				var path="./DataFiles/이미지/"+name+".jpg";
				var item=new JPanel(new BorderLayout());
				var scr_img=new JLabel(img(path, 200, 200));
				item.add(scr_img);
				item.add(label(name, JLabel.CENTER), BorderLayout.SOUTH);
				scr_c.add(item);
				scr_img.setBorder(new LineBorder(Color.black));

				scr_img.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						img.setIcon(img(path, 150, 140));
						txt[0].setText(name);
						txt[1].setText(price+"");
						m_no=mno;
						combo[0].setSelectedIndex(1);
						combo[0].setSelectedIndex(0);
						combo[1].setSelectedIndex(0);
						c.add(c_e, BorderLayout.EAST);
						setSize(1170, 600);
					}
				});

				fold();
				repaint();
				revalidate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	void fold() {
		c.remove(c_e);
		setSize(750, 600);
	}

	private void event(ActionEvent e) {
		// TODO Auto-generated method stub
		setVisible(false);
		if(e.getActionCommand()==bcap1[0]) {
			new OrderLog().addWindowListener(new Before(this));
		}
		if(e.getActionCommand()==bcap1[1]) {
			new Basket().addWindowListener(new Before(this));
		}
		if(e.getActionCommand()==bcap1[2]) {
			new Chart().addWindowListener(new Before(this));
		}
		if(e.getActionCommand()==bcap1[3]) {
			dispose();
		}
	}

	public static void main(String[] args) {
		setLogin(3);
		execute("delete from shopping");
		new StarBox();
	}
}
