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
public class Orderlist extends JFrame{
	String u_no;
	Vector<Vector<String>>user = new Vector<Vector<String>>();
	Vector<Vector<String>>orderlist = new Vector<Vector<String>>();
	Vector<Vector<String>>menu = new Vector<Vector<String>>();

	Vector<String>col = new Vector<String>();
	DefaultTableModel model;
	JTable jt;
	JScrollPane jsp;
	String id = "";
	String name;
	int total =0;
	
	public Orderlist(String a,String b) {
		setTitle("OrderHistory");
		u_no = a;
		id = b;
		makeVecor();
		model = new DefaultTableModel(orderlist,col) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		jt = new JTable(model);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		for(int i=0;i<6;i++) {
			jt.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}
		jt.getColumnModel().getColumn(1).setPreferredWidth(210);
		jsp = new JScrollPane(jt);
		JLabel topLabel = new JLabel(name + "'s Order History");
		topLabel.setFont(new Font("Ariel",Font.BOLD,40));
		JPanel top = new JPanel();
		top.add(topLabel);
		JLabel bot1 = new JLabel("Total Amount : ");
		DecimalFormat f = new DecimalFormat("#,###,###,###");
		JTextField bot2 = new JTextField(10);
		bot2.setHorizontalAlignment(SwingConstants.RIGHT);
		bot2.setText(f.format(total));
		bot2.setEnabled(false);
		JButton exit = new JButton("exit");
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dispose();
				new Starbox(id);
			}
			
		});
		JPanel bot = new JPanel();
		
		
		bot.add(bot1);bot.add(bot2);bot.add(exit);
		
		
		add(top,BorderLayout.NORTH);
		add(jsp,BorderLayout.CENTER);
		add(bot,BorderLayout.SOUTH);
		
		setSize(800,500);
		setVisible(true);
		
	}
	public void makeVecor() {
		col.add("purchase date");col.add("menu name");col.add("price");col.add("size");col.add("amount");col.add("total price");
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
			}
			re = st.executeQuery("select * from orderlist where u_no = "+u_no);
			while(re.next()) {
				String name ="";
				for(int i=0;i<menu.size();i++) {
					if(re.getString("m_no").equals(menu.elementAt(i).elementAt(0))) {
						name = menu.elementAt(i).elementAt(2);
					}
				}
				Vector<String>a = new Vector<String>();
				a.add(re.getString("o_date"));a.add(name);a.add(re.getString("o_size"));a.add(re.getString("o_price"));a.add(re.getString("o_count"));a.add(re.getString("o_amount"));
				orderlist.add(a);
			}
			re = st.executeQuery("select sum(o_amount) as suma from orderlist where u_no = "+u_no);
			re.next();
			total = (int) re.getLong("suma");
			for(int i=0;i<user.size();i++) {
				if(user.elementAt(i).elementAt(0).equals(u_no)) {
					name = user.elementAt(i).elementAt(3);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
////		new Orderlist("16","coffee16");
//	}

}
