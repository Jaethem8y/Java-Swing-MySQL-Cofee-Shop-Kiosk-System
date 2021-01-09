package UI;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
public class Cart extends JFrame{
	String name;
	Vector<String>col = new Vector<String>();
	Vector<Vector<String>>data = new Vector<Vector<String>>();
	Vector<Vector<String>>menu = new Vector<Vector<String>>();
	Vector<Vector<String>>user = new Vector<Vector<String>>();
	DefaultTableModel model;
	JTable jt;
	String no;
	String point;
	String user_name;
	JScrollPane jsp;
	String id;
	JButton buy = new JButton("Buy");
	JButton remove = new JButton("Remove");
	JButton exit = new JButton("Exit");
	
	public Cart(String a,String b) {
		name = a;
		id = b;
		setVector();
		
		JLabel top = new JLabel(a+"'s Cart");
		top.setFont(new Font("Ariel",Font.BOLD,40));
		setTitle("Cart");
		model = new DefaultTableModel(data,col) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		jt = new JTable(model);
		jsp = new JScrollPane(jt);
		JPanel to = new JPanel();
		to.add(top);
		add(to,BorderLayout.NORTH);
		add(jsp,BorderLayout.CENTER);
		
		JPanel bot = new JPanel();
		bot.add(buy);bot.add(remove);bot.add(exit);
		if(data.size()==0) {
			buy.setEnabled(false);
			remove.setEnabled(false);
		}
		buy.addActionListener(new MyActionListener());
		remove.addActionListener(new MyActionListener());
		exit.addActionListener(new MyActionListener());

		add(bot,BorderLayout.SOUTH);
		setSize(800,800);
		setVisible(true);

		
	}
	public void setVector() {
		col.add("Menu Name");col.add("Price");col.add("Amount");col.add("Size");col.add("Price");
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
					point = re.getString("u_point");
				}
			}
			re = st.executeQuery("select * from shopping");
			while(re.next()) {		
				Vector<String>a = new Vector<String>();
				String mname="";
				for(int i=0;i<menu.size();i++) {
					if(re.getString("m_no").equals(menu.elementAt(i).elementAt(0))) {
						mname = menu.elementAt(i).elementAt(2);
					}
				}
				a.add(mname);a.add(re.getString("s_price"));a.add(re.getString("s_count"));a.add(re.getString("s_size"));a.add(re.getString("s_amount"));
				data.add(a);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	class MyActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(e.getActionCommand().equals("Exit")) {
				dispose();
				new Starbox(id);
			}else if(e.getActionCommand().equals("Remove")) {
				int sel = jt.getSelectedRow()+1;
				if(sel == 0) {
					JOptionPane.showMessageDialog(null, "choose something to delete","Message",JOptionPane.INFORMATION_MESSAGE);
				}
				
				Connection conn = DB.Connect.makeConnection("coffee");
				try {
					Statement st = conn.createStatement();
					st.executeUpdate("delete from shopping where s_no = "+sel);
					st.executeUpdate("set @count = 0");
					st.executeUpdate("update shopping set s_no = @count:=@count+1");
					data.clear();
					setVector();
					jt.revalidate();
					revalidate();
					if(data.size()==0) {
						buy.setEnabled(false);remove.setEnabled(false);
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
					
				
			}else {
				Connection conn = DB.Connect.makeConnection("coffee");
				Statement st;
				int before=0;
				int index = -1;
				try {
					st = conn.createStatement();
					ResultSet re = st.executeQuery("select sum(o_amount) as sum from orderlist where u_no = "+no);
					re.next();
					before = (int) re.getLong("sum");
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}				
				int total_price = 0;
				for(int i=0;i<data.size();i++) {
					total_price += Integer.parseInt(data.elementAt(i).elementAt(4));
				}
				int d_points = Integer.parseInt(point);
				int tot_point = d_points + (int)Math.ceil((float)total_price*0.05);
				int extra = (int)Math.ceil((float)total_price*0.05);
				int after =  before+total_price;
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date dat = new Date();
				String date = formatter.format(dat);
				if(Integer.parseInt(point)>total_price) {
					JPanel k = new JPanel(new GridLayout(3,1));
					k.add(new JLabel("Your current Point: "+point));
					k.add(new JLabel("Would you like to pay with point?"));
					k.add(new JLabel("(if no payment will be processed normally)"));
					int result = JOptionPane.showConfirmDialog(null, k,"Message",JOptionPane.YES_NO_OPTION);
					if(result == JOptionPane.YES_OPTION) {
						point = Integer.toString(Integer.parseInt(point)-total_price);
						index =1;
						try {
							st = conn.createStatement();
							st.executeUpdate("update user set u_point = "+point+" where u_no = "+no);
							JPanel information = new JPanel(new GridLayout(2,1));
							information.add(new JLabel("Payment done with points"));
							information.add(new JLabel("leftover points: "+point));
							dispose();
							new Orderlist(no,id);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}else {
						JOptionPane.showMessageDialog(null, "Payment Processed","Message",JOptionPane.INFORMATION_MESSAGE);
						
						try {
							PreparedStatement pmst = conn.prepareStatement("insert into orderlist(o_date,u_no,m_no,o_group,o_size,o_price,o_count,o_amount) values(?,?,?,?,?,?,?,?)");
							for(int i=0;i<data.size();i++) {
								String group="";
								String m_no="";
								for(int j=0;j<menu.size();j++) {
									if(data.elementAt(i).elementAt(1).equals(menu.elementAt(j).elementAt(0))) {
										group = menu.elementAt(j).elementAt(1);
										m_no = menu.elementAt(j).elementAt(0);
									}
								}
								pmst.setString(1, date);pmst.setString(2,no);pmst.setString(3, m_no);;pmst.setString(4,group);pmst.setString(5, data.elementAt(i).elementAt(3));pmst.setString(6, data.elementAt(i).elementAt(1));pmst.setString(7, data.elementAt(i).elementAt(2));pmst.setString(8, data.elementAt(i).elementAt(4)); 
								pmst.executeUpdate();
								
							}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}else {
					if(index == -1) {
						JOptionPane.showMessageDialog(null, "Payment Processed","Message",JOptionPane.INFORMATION_MESSAGE);
						try {
							PreparedStatement pmst = conn.prepareStatement("insert into orderlist(o_date,u_no,m_no,o_group,o_size,o_price,o_count,o_amount) values(?,?,?,?,?,?,?,?)");
							for(int i=0;i<data.size();i++) {
								String group="";
								String m_no="";
								for(int j=0;j<menu.size();j++) {
									if(data.elementAt(i).elementAt(0).equals(menu.elementAt(j).elementAt(2))) {
										group = menu.elementAt(j).elementAt(1);
										m_no = menu.elementAt(j).elementAt(0);
									}
								}
								pmst.setString(1, date);pmst.setString(2,no);pmst.setString(3, m_no);;pmst.setString(4,group);pmst.setString(5, data.elementAt(i).elementAt(3));pmst.setString(6, data.elementAt(i).elementAt(1));pmst.setString(7, data.elementAt(i).elementAt(2));pmst.setString(8, data.elementAt(i).elementAt(4)); 
								pmst.executeUpdate();
							}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					try {
						st = conn.createStatement();
						st.executeUpdate("update user set u_point = "+tot_point);
					} catch (SQLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					
					if((before<300000 && after>=300000)&&after<500000) {
						JOptionPane.showMessageDialog(null, "You are now Bronze!","Message",JOptionPane.INFORMATION_MESSAGE);
						
						try {
							st = conn.createStatement();
							st.executeUpdate("update user set u_grade = 'Bronze' where u_no = "+no);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}else if((before<500000 && after >= 500000)&& after < 800000) {
						JOptionPane.showMessageDialog(null, "You are now Silver!","Message",JOptionPane.INFORMATION_MESSAGE);
						try {
							st = conn.createStatement();
							st.executeUpdate("update user set u_grade = 'Silver' where u_no = "+no);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}else if((before<800000&&after>=800000)) {
						JOptionPane.showMessageDialog(null, "You are now Gold!","Message",JOptionPane.INFORMATION_MESSAGE);
						try {
							st = conn.createStatement();
							st.executeUpdate("update user set u_grade = 'Gold' where u_no = "+no);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				
				try {
					Statement sto = conn.createStatement();
					sto.executeUpdate("delete from shopping");
					sto.executeUpdate("set @count = 0");
					sto.executeUpdate("update shopping set s_no = @count:=@count+1");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				dispose();
				new Orderlist(no,id);
			}
				
			
		}
		
	}
}
