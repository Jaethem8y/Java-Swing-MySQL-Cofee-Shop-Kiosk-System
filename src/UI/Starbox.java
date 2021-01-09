package UI;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class Starbox{
	JFrame frame = new JFrame();
	String id;
	String pick;
	Vector<Vector<String>>user = new Vector<Vector<String>>();
	Vector<Vector<String>>menu = new Vector<Vector<String>>();
	Vector<Vector<String>>orderlist = new Vector<Vector<String>>();
	String size;
	String user_name="";
	JScrollPane jsp;
	JTextField mPrice = new JTextField(10);
	JTextField mtotal = new JTextField(10);
	JPanel alll = new JPanel();
	JComboBox<String>mSize;
	String no="";
	int originPrice;
	int price;
	String grade;
	int counter = 0;
	public Starbox(String a) {
		id = a;
		frame.setTitle("StarBox");
		setVector();
		setSubTitle();
		setLeftTitle();
		setCenterTitle();
		frame.setSize(800,600);
		frame.setVisible(true);
	}
	public void setVector() {
		
		Connection conn = DB.Connect.makeConnection("coffee");
		try {
			Statement st = conn.createStatement();
			ResultSet re = st.executeQuery("select * from menu");
			while(re.next()){
				Vector<String>a = new Vector<String>();
				a.add(re.getString("m_no"));a.add(re.getString("m_group"));a.add(re.getString("m_name"));a.add(re.getString("m_price"));
				menu.add(a);
			}
			re = st.executeQuery("select * from user");
			while(re.next()) {
				Vector<String>a = new Vector<String>();
				a.add(re.getString("u_no"));a.add(re.getString("u_id"));a.add(re.getString("u_pw"));a.add(re.getString("u_name"));a.add(re.getString("u_bd"));a.add(re.getString("u_point"));a.add(re.getString("u_grade"));
				user.add(a);
				if(re.getString("u_id").equals(id)) {
					no = re.getString("u_no");
					user_name = re.getString("u_name");
				}
			}
			re = st.executeQuery("select * from orderlist");
			while(re.next()) {
				Vector<String>a = new Vector<String>();
				a.add(re.getString("o_no"));a.add(re.getString("o_date"));a.add(re.getString("u_no"));a.add(re.getString("m_no"));a.add(re.getString("o_group"));a.add(re.getString("o_size"));a.add(re.getString("o_price"));a.add(re.getString("o_count"));a.add(re.getString("o_amount"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setSubTitle() {
		String name="";
		String grade="";
		String point="";
		for(int i=0;i<user.size();i++) {
			if(id.equals(user.elementAt(i).elementAt(1))) {
				name = user.elementAt(i).elementAt(3);
				grade = user.elementAt(i).elementAt(6);
				point = user.elementAt(i).elementAt(5);
			}
		}
		JLabel label = new JLabel("CustomerName: "+name+"/CustomerGrade: "+grade+"/Points: "+point);
		JButton btn[] = new JButton[4];
		String names[] = {"OrderList","Cart","Top 5","LogOut"};
		JPanel b = new JPanel(new FlowLayout(FlowLayout.LEFT));
		for(int i=0;i<names.length;i++) {
			btn[i] = new JButton(names[i]);
			btn[i].addActionListener(new SettingListener());
			btn[i].setPreferredSize(new Dimension(100,40));
			btn[i].setActionCommand(names[i]);
			b.add(btn[i]);
		}
		JPanel a = new JPanel();
		a.setLayout(new GridLayout(2,1));
		a.add(label);
		a.add(b);
		frame.add(a,BorderLayout.NORTH);

	}
	public void setLeftTitle() {
		JButton btn[] = new JButton[3];
		String name[] = {"Drinks","Food","Product"};
		JPanel a = new JPanel(new GridLayout(3,1,3,3));
		JPanel b = new JPanel();
		for(int i=0;i<name.length;i++) {
			btn[i] = new JButton(name[i]);
			btn[i].addActionListener(new SwitchListener());
			btn[i].setPreferredSize(new Dimension(80,70));
			a.add(btn[i]);
		}
		b.add(a);
		frame.add(b,BorderLayout.WEST);
	}
	public void setCenterTitle() {
		if(counter==0) {
			pick = "음료";
		}
		Vector<JPanel>picked = new Vector<JPanel>();
		for(int i=0;i<menu.size();i++) {
			if(pick.equals((String)menu.elementAt(i).elementAt(1))) {
				String elementa = menu.elementAt(i).elementAt(2);
				ImageIcon origin = new ImageIcon("/Users/jaehyeokchoi/Desktop/JavaProjects/Coffee/2019 커피매장/DataFiles/이미지/"+elementa+".jpg");
				Image sizing = origin.getImage();
				Image changed = sizing.getScaledInstance(200,200, Image.SCALE_SMOOTH);
				origin = new ImageIcon(changed);
				JButton a = new JButton(origin);
				a.addActionListener(new SelectListener());
				a.setActionCommand(elementa);
				JPanel k = new JPanel();
				k.setLayout(new BorderLayout());
				JPanel e = new JPanel();
				e.add(new JLabel(elementa));
				k.add(a,BorderLayout.CENTER);k.add(e,BorderLayout.SOUTH);
				picked.add(k);
			}
		}
		int height = (int)Math.ceil((double)picked.size()/3);
		JPanel center = new JPanel(new GridLayout(height,3));
		for(int i=0;i<picked.size();i++) {
			center.add(picked.elementAt(i));
		}
		jsp = new JScrollPane(center);
		frame.add(jsp,BorderLayout.CENTER);
		if(counter == 0) {
			counter++;
		}
		System.out.println(counter);
		System.out.println(pick);
	}
	public void sidePanel(String a){
		String elementa = a;
		JLabel label[] = new JLabel[5];
		String names[] = {"Menu: ","Price: ","Quan: ","Size: ","Total: "};
		JTextField mName = new JTextField(10);
		
		Integer nums[] = {1,2,3,4,5,6,7,8,9,10};
		JComboBox<Integer>mAmount = new JComboBox<Integer>(nums);
		String si[] = {"M","L"};
		mSize = new JComboBox<String>(si);
		JButton goCart  = new JButton("Cart");
		JButton toBuy = new JButton("Buy");
		

		
		mName.setEnabled(false);
		mPrice.setEnabled(false);
		mtotal.setEnabled(false);
		JPanel right = new JPanel();
		right.setLayout(new GridLayout(5,1));
		for(int i=0;i<5;i++) {
			label[i]= new JLabel(names[i]);
		}
		JPanel uno = new JPanel();
		uno.add(label[0]);uno.add(mName);
		JPanel dos = new JPanel();
		dos.add(label[1]);dos.add(mPrice);
		JPanel tres = new JPanel();
		tres.add(label[2]);tres.add(mAmount);
		JPanel cuatro = new JPanel();
		cuatro.add(label[3]);cuatro.add(mSize);
		if(pick.equals( "상품")) {
			mSize.setEnabled(false);
		}
		JPanel cinco = new JPanel();
		cinco.add(label[4]);cinco.add(mtotal);
		right.add(uno);right.add(dos);right.add(tres);right.add(cuatro);right.add(cinco);
		mName.setText(elementa);
		int s =  (int) mAmount.getSelectedItem();

		
		
		mAmount.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				System.out.println("ok");
				int mam = (int) mAmount.getSelectedItem();
				int tota = mam * price;
				for(int i=0;i<user.size();i++) {
					if(id.equals(user.elementAt(i).elementAt(1))) {
						grade = user.elementAt(i).elementAt(6);
						break;
					}
				}
				int totp=0;
				if(grade.equals("Bronze")) {
					totp = (int) (Math.floor((double)tota*0.97));
				}else if(grade.equals("Silver")) {
					totp = (int) (Math.floor((double)tota*0.95));
				}else if(grade.equals("Gold")) {
					totp = (int) (Math.floor((double)tota*0.90));
					System.out.println("Gold");
				}else {
					totp = tota;
				}
				mtotal.setText(Integer.toString(totp));
				frame.revalidate();				
			}
			
		});
		mSize.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				String si = (String) mSize.getSelectedItem();
				if(si.equals("L") && (originPrice==price)) {
					price = price + 1000;
					mPrice.setText(Integer.toString(price));
					int mam = (int) mAmount.getSelectedItem();
					int tota = mam * price;
					for(int i=0;i<user.size();i++) {
						if(id.equals(user.elementAt(i).elementAt(1))) {
							grade = user.elementAt(i).elementAt(6);
							break;
						}
					}
					int totp=0;
					if(grade.equals("Bronze")) {
						totp = (int) (Math.floor((double)tota*0.97));
					}else if(grade.equals("Silver")) {
						totp = (int) (Math.floor((double)tota*0.95));
					}else if(grade.equals("Gold")) {
						totp = (int) (Math.floor((double)tota*0.90));
						System.out.println("Gold");
					}else {
						totp = tota;
					}
					mtotal.setText(Integer.toString(totp));
					frame.revalidate();				
				}else if(si.equals("M")&&(originPrice+1000 == price)){
					price = price - 1000;
					mPrice.setText(Integer.toString(price));
					int mam = (int) mAmount.getSelectedItem();
					int tota = mam * price;
					for(int i=0;i<user.size();i++) {
						if(id.equals(user.elementAt(i).elementAt(1))) {
							grade = user.elementAt(i).elementAt(6);
							break;
						}
					}
					int totp=0;
					if(grade.equals("Bronze")) {
						totp = (int) (Math.floor((double)tota*0.97));
					}else if(grade.equals("Silver")) {
						totp = (int) (Math.floor((double)tota*0.95));
					}else if(grade.equals("Gold")) {
						totp = (int) (Math.floor((double)tota*0.90));
						System.out.println("Gold");
					}else {
						totp = tota;
					}
					mtotal.setText(Integer.toString(totp));
					frame.revalidate();	
				}
			}
			
		});
		
		JPanel extra = new JPanel();
		
		ImageIcon origin = new ImageIcon("/Users/jaehyeokchoi/Desktop/JavaProjects/Coffee/2019 커피매장/DataFiles/이미지/"+elementa+".jpg");
		Image sizing = origin.getImage();
		Image changed = sizing.getScaledInstance(150,150, Image.SCALE_SMOOTH);
		origin = new ImageIcon(changed);
		extra.add(new JLabel(origin));
		JPanel semi = new JPanel(new GridLayout(1,2));
		semi.add(extra);semi.add(right);
		JPanel w = new JPanel();
		w.add(goCart);w.add(toBuy);
		JPanel all = new JPanel(new BorderLayout());
		all.add(semi,BorderLayout.CENTER);
		all.add(w,BorderLayout.SOUTH);
		alll.add(all);
		frame.add(alll,BorderLayout.EAST);
		frame.setSize(1400,600);
		goCart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int dno=-1;
				String dmenu = mName.getText();
				int dprice = Integer.parseInt(mPrice.getText());
				int damount = (int) mAmount.getSelectedItem();
				String dsize = (String)mSize.getSelectedItem();
				int dtotal = Integer.parseInt(mtotal.getText());
				for(int i=0;i<menu.size();i++) {
					if(menu.elementAt(i).elementAt(2).equals(dmenu)) {
						dno = Integer.parseInt(menu.elementAt(i).elementAt(0));
						break;
					}
				}
				Connection conn = DB.Connect.makeConnection("coffee");
				try {
					PreparedStatement st = conn.prepareStatement("insert into shopping(m_no,s_price,s_count,s_size,s_amount) values(?,?,?,?,?)");
					st.setLong(1, dno);st.setLong(2, dprice);st.setLong(3, damount);st.setString(4,dsize);st.setLong(5, dtotal);
					st.executeUpdate();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					System.out.println(e1.getMessage());
				}
				alll.removeAll();
				frame.remove(alll);
				frame.revalidate();
				frame.setSize(800,600);
				JOptionPane.showMessageDialog(null, "Added to Cart!","MEssage",JOptionPane.INFORMATION_MESSAGE);		
			}
			
		});
		toBuy.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int index = -1;
				
				int dpoint = -1;
				String u_no = "";
				String dsize = (String)mSize.getSelectedItem();
				int damount = (int) mAmount.getSelectedItem();
				int dtotal = Integer.parseInt(mtotal.getText());
				for(int i=0;i<user.size();i++) {
					if(user.elementAt(i).elementAt(1).equals(id)) {
						dpoint = Integer.parseInt(user.elementAt(i).elementAt(5));
						u_no = (user.elementAt(i).elementAt(0));
						index =i;
					}
				}
				if(dpoint<dtotal) {
					dpoint = dpoint + (int)Math.ceil((float)dtotal*0.05);
					Connection conn = DB.Connect.makeConnection("coffee");
					try {
						Statement st = conn.createStatement();
						st.executeUpdate("update user set u_point = "+dpoint+" where u_id = '"+id+"'");
						setVector();
						conn.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(null, "Payment Processed","Message",JOptionPane.ERROR_MESSAGE);
				}else {
					JPanel k = new JPanel(new GridLayout(3,1));
					k.add(new JLabel("Your current Point: "+dpoint));
					k.add(new JLabel("Would you like to pay with point?"));
					k.add(new JLabel("(if no payment will be processed normally)"));
					int result = JOptionPane.showConfirmDialog(null, k,"Message",JOptionPane.YES_NO_OPTION);
					if(result == JOptionPane.NO_OPTION) {
						dpoint = dpoint + (int)Math.ceil((float)dtotal*0.05);
						Connection conn = DB.Connect.makeConnection("coffee");
						try {
							Statement st = conn.createStatement();
							st.executeUpdate("update user set u_point = "+dpoint+" where u_id = '"+id+"'");
							setVector();
							conn.close();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						JOptionPane.showMessageDialog(null, "Payment Processed","Message",JOptionPane.ERROR_MESSAGE);
					}else {
						index = 1;
						int sub = dpoint - dtotal;
						JPanel information = new JPanel(new GridLayout(2,1));
						information.add(new JLabel("Payment done with points"));
						information.add(new JLabel("leftover points: "+sub));
						Connection conn = DB.Connect.makeConnection("coffee");
						try {
							Statement st = conn.createStatement();
							st.executeUpdate("update user set u_point = "+sub+" where u_id = '"+id+"'");
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						JOptionPane.showMessageDialog(null, information,"Message",JOptionPane.INFORMATION_MESSAGE);
					}
					
					
				}
				if(index != 1) {
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					int dprice = Integer.parseInt(mPrice.getText());
					Date dat = new Date();
					String date = formatter.format(dat);
					String m_no="";
					String o_group="";
					int before = 0;
					int after = 0;
					for(int i=0;i<menu.size();i++) {
						if(mName.getText().equals(menu.elementAt(i).elementAt(2))) {
							m_no = menu.elementAt(i).elementAt(0);
							o_group = menu.elementAt(i).elementAt(1);
						}
					}
					
					Connection conn = DB.Connect.makeConnection("coffee");
					Statement st;
					try {
						st = conn.createStatement();
						ResultSet re = st.executeQuery("select sum(o_amount) as sum from orderlist where u_no = "+u_no);
						re.next();
						before = (int) re.getLong("sum");
					} catch (SQLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					
					try {
						Statement sta = conn.createStatement();
						sta.executeUpdate("alter table shopping auto_increment = 1");
						PreparedStatement psmt = conn.prepareStatement("insert into orderlist(o_date,u_no,m_no,o_group,o_size,o_price,o_count,o_amount) values (?,?,?,?,?,?,?,?)");
						sta.executeUpdate("alter table shopping auto_increment = 1");
						System.out.println("yesssss");
						psmt.setString(1,date);psmt.setString(2, u_no);psmt.setString(3, m_no);psmt.setString(4, o_group);
						if(o_group.equals("상품")) {
							psmt.setString(5,"");
						}else {
							psmt.setString(5,dsize);
						}
						psmt.setLong(6,dprice);psmt.setLong(7, damount);psmt.setLong(8, dtotal);                          
						psmt.executeUpdate();
						setVector();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					after = before + dtotal;
					if((before<300000 && after>=300000)&&after<500000) {
						JOptionPane.showMessageDialog(null, "You are now Bronze!","Message",JOptionPane.INFORMATION_MESSAGE);
						try {
							st = conn.createStatement();
							st.executeUpdate("update user set u_grade = 'Bronze' where u_no = "+u_no);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}else if((before<500000 && after >= 500000)&& after < 800000) {
						JOptionPane.showMessageDialog(null, "You are now Silver!","Message",JOptionPane.INFORMATION_MESSAGE);
						try {
							st = conn.createStatement();
							st.executeUpdate("update user set u_grade = 'Silver' where u_no = "+u_no);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}else if((before<800000&&after>=800000)) {
						JOptionPane.showMessageDialog(null, "You are now Gold!","Message",JOptionPane.INFORMATION_MESSAGE);
						try {
							st = conn.createStatement();
							st.executeUpdate("update user set u_grade = 'Gold' where u_no = "+u_no);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				
				
				frame.dispose();
//				new Starbox(id);
				new Orderlist(u_no,id);
			}
			
		});
	}
	
	

	class SettingListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String names[] = {"OrderList","Cart","Top 5","LogOut"};
			if(e.getActionCommand().equals(names[0])) {
				frame.dispose();
				new Orderlist(no,id);
			}else if(e.getActionCommand().equals(names[1])) {
				frame.dispose();
				new Cart(user_name,id);
			}else if(e.getActionCommand().equals(names[2])) {
				frame.dispose();
				new TopFive(id);
			}else if(e.getActionCommand().equals(names[3])) {
				frame.dispose();
				new Main();
			}

		}
		
	}
	class SwitchListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			System.out.println(e.getActionCommand());
			if(e.getActionCommand().equals("Drinks")) {
				pick = "음료";
			}else if(e.getActionCommand().equals("Food")) {
				pick = "푸드";
			}else if (e.getActionCommand().equals("Product")) {
				pick = "상품";
			}
			frame.remove(jsp);
			alll.removeAll();
			frame.remove(alll);
			setCenterTitle();
			frame.revalidate();
			frame.setSize(1200,600);
		}
		
	}
	class SelectListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			alll.removeAll();
			frame.remove(alll);
			sidePanel(e.getActionCommand());
			String elementa = e.getActionCommand();
			for(int i=0;i<menu.size();i++) {
				if(elementa.equals(menu.elementAt(i).elementAt(2))) {
					price = Integer.parseInt(menu.elementAt(i).elementAt(3));
					break;
				}
			}
			grade="";
			for(int i=0;i<user.size();i++) {
				if(id.equals(user.elementAt(i).elementAt(1))) {
					grade = user.elementAt(i).elementAt(6);
					break;
				}
			}

			int totp=0;
			mPrice.setText(Integer.toString(price));
			if(grade.equals("Bronze")) {
				totp = (int) (Math.floor((double)price*0.97));
			}else if(grade.equals("Silver")) {
				totp = (int) (Math.floor((double)price*0.95));
			}else if(grade.equals("Gold")) {
				totp = (int) (Math.floor((double)price*0.90));
				System.out.println("Gold");
			}else {
				totp = price;
			}
			originPrice = price;
			mtotal.setText(Integer.toString(totp));
			frame.revalidate();
		}
		
	}
	
}
