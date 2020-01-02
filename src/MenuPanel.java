import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public abstract class MenuPanel extends JPanel{
	JTextField txt[]= {
			new JTextField(13),
			new JTextField(13)
	};
	JComboBox<String> combo=new JComboBox<String>();
	JLabel img=new JLabel();
	JButton jB[]=new JButton[4];
	File file;
	
	public MenuPanel(BaseFrame base) {
		// TODO Auto-generated constructor stub
		setLayout(new BorderLayout());
		var c=new JPanel(new GridLayout(0, 1));
		var e=new JPanel(new BorderLayout());
		var s=new JPanel();
		add(c);
		add(e, "East");
		add(s, "South");
		setBorder(new EmptyBorder(10, 0, 0, 0));
		
		String str[]= {"분류", "메뉴명", "가격"};
		img.setBorder(new LineBorder(Color.black));
		for(int i=0; i<3; i++) {
			JPanel temp=new JPanel(new FlowLayout(FlowLayout.LEFT));
			c.add(temp);
			temp.add(BaseFrame.size(BaseFrame.label(str[i]), 60, 25));
			if(i==0) {
				temp.add(combo);
			}
			else {
				temp.add(txt[i-1]);
			}
		}
		
		combo.addItem("음료");
		combo.addItem("푸드");
		combo.addItem("상품");
		e.add(BaseFrame.size(img, 130, 200));
		e.add(jB[0]=BaseFrame.button("사진등록", it->{
			JFileChooser file=new JFileChooser();
			file.setFileFilter(new FileNameExtensionFilter("JPG Image", "jpg"));
			if(file.APPROVE_OPTION==file.showOpenDialog(null)) {
				img.setIcon(
						BaseFrame.img(file.getSelectedFile().getAbsolutePath(), 130, 133));
				this.file=file.getSelectedFile();
			}
		}), "South");
		
		s.add(jB[1]=BaseFrame.button("등록", it->{
			event();
		}));
		if(base instanceof ModifyMenu) {
			s.add(jB[2]=BaseFrame.button("수정", it->{
				modify();
			}));
		}
		s.add(jB[3]=BaseFrame.button("취소", it->{
			base.dispose();
		}));
	}
	
	void modify() {
		
	}
	abstract void event();
	
	public static void main(String[] args) {
		new ModifyMenu();
	}
}

