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
public class TopFive {
	JFrame frame = new JFrame();
	Vector<Vector<String>>menu = new Vector<Vector<String>>();
	Vector<Vector<String>>orderlist = new Vector<Vector<String>>();
	Vector<Vector<String>>drink = new Vector<Vector<String>>();
	Vector<Vector<String>>food = new Vector<Vector<String>>();
	Vector<Vector<String>>product = new Vector<Vector<String>>();
	JComboBox<String>change;
	JPanel center;
	String id;

	public TopFive(String a) {
		frame.setTitle("Top5");
		id = a;
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				new Starbox(id);
			}
		});
		setVector();
		JPanel top = new JPanel();
		top.setBackground(Color.DARK_GRAY);
		top.add(change);top.add(new JLabel("'s Top 5 item"));
		frame.add(top,BorderLayout.NORTH);
		center = new MyPanel();
		frame.add(center,BorderLayout.CENTER);
		change.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				System.out.println("all");
				frame.remove(center);
				center.removeAll();
				center = new MyPanel();
				frame.add(center,BorderLayout.CENTER);
				frame.revalidate();
				
				frame.repaint();
			}

		
			
		});
		
		frame.setSize(400,600);
		frame.setVisible(true);
		
	}
	public void setVector() {
		String n[] = {"Food","Drink","Product"};
		change = new JComboBox<String>(n);
		Connection conn = DB.Connect.makeConnection("coffee");
		try {
			Statement st = conn.createStatement();
			ResultSet re = st.executeQuery("select * from menu");
			while(re.next()){
				Vector<String>a = new Vector<String>();
				a.add(re.getString("m_no"));a.add(re.getString("m_group"));a.add(re.getString("m_name"));a.add(re.getString("m_price"));
				menu.add(a);
			}
			re = st.executeQuery("select * from orderlist");
			while(re.next()) {
				Vector<String>a = new Vector<String>();
				a.add(re.getString("o_no"));a.add(re.getString("o_date"));a.add(re.getString("u_no"));a.add(re.getString("m_no"));a.add(re.getString("o_group"));a.add(re.getString("o_size"));a.add(re.getString("o_price"));a.add(re.getString("o_count"));a.add(re.getString("o_amount"));
				orderlist.add(a);
			}
			re = st.executeQuery("select o_group ,m_no, sum(o_count) as total from orderlist where o_group = '음료' group by m_no  order by total desc");
			while(re.next()) {
				Vector<String>a = new Vector<String>();
				String menuName = "";
				for(int i=0;i<menu.size();i++) {
					if(re.getString("m_no").equals(menu.elementAt(i).elementAt(0))) {
						menuName = menu.elementAt(i).elementAt(2);
					}
				}
				a.add(menuName);a.add(re.getString("total"));
				drink.add(a);
				
			}
			re = st.executeQuery("select o_group ,m_no, sum(o_count) as total from orderlist where o_group = '푸드' group by m_no  order by total desc");
			while(re.next()) {
				Vector<String>a = new Vector<String>();
				String menuName = "";
				for(int i=0;i<menu.size();i++) {
					if(re.getString("m_no").equals(menu.elementAt(i).elementAt(0))) {
						menuName = menu.elementAt(i).elementAt(2);
					}
				}
				a.add(menuName);a.add(re.getString("total"));
				food.add(a);
				
			}
			re = st.executeQuery("select o_group ,m_no, sum(o_count) as total from orderlist where o_group = '상품' group by m_no  order by total desc");
			while(re.next()) {
				Vector<String>a = new Vector<String>();
				String menuName = "";
				for(int i=0;i<menu.size();i++) {
					if(re.getString("m_no").equals(menu.elementAt(i).elementAt(0))) {
						menuName = menu.elementAt(i).elementAt(2);
					}
				}
				a.add(menuName);a.add(re.getString("total"));
				product.add(a);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class MyPanel extends JPanel{
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			String which = (String) change.getSelectedItem();
			String top1;
			int amount1;
			String top2;
			int amount2;
			String top3;
			int amount3;
			String top4;
			int amount4;
			String top5;
			int amount5;
			
			if(which.equals("Drink")) {
				top1 = drink.elementAt(0).elementAt(0);
				amount1 = Integer.parseInt(drink.elementAt(0).elementAt(1));
				top2 = drink.elementAt(1).elementAt(0);
				amount2 = Integer.parseInt(drink.elementAt(1).elementAt(1));
				top3 = drink.elementAt(2).elementAt(0);
				amount3 = Integer.parseInt(drink.elementAt(2).elementAt(1));
				top4 = drink.elementAt(3).elementAt(0);
				amount4 = Integer.parseInt(drink.elementAt(3).elementAt(1));
				top5 = drink.elementAt(4).elementAt(0);
				amount5 = Integer.parseInt(drink.elementAt(4).elementAt(1));
			}else if(which.equals("Food")) {
				top1 = food.elementAt(0).elementAt(0);
				amount1 = Integer.parseInt(food.elementAt(0).elementAt(1));
				top2 = food.elementAt(1).elementAt(0);
				amount2 = Integer.parseInt(food.elementAt(1).elementAt(1));
				top3 = food.elementAt(2).elementAt(0);
				amount3 = Integer.parseInt(food.elementAt(2).elementAt(1));
				top4 = food.elementAt(3).elementAt(0);
				amount4 = Integer.parseInt(food.elementAt(3).elementAt(1));
				top5 = food.elementAt(4).elementAt(0);
				amount5 = Integer.parseInt(food.elementAt(4).elementAt(1));
			}else {
				top1 = product.elementAt(0).elementAt(0);
				amount1 = Integer.parseInt(product.elementAt(0).elementAt(1));
				top2 = product.elementAt(1).elementAt(0);
				amount2 = Integer.parseInt(product.elementAt(1).elementAt(1));
				top3 = product.elementAt(2).elementAt(0);
				amount3 = Integer.parseInt(product.elementAt(2).elementAt(1));
				top4 = product.elementAt(3).elementAt(0);
				amount4 = Integer.parseInt(product.elementAt(3).elementAt(1));
				top5 = product.elementAt(4).elementAt(0);
				amount5 = Integer.parseInt(product.elementAt(4).elementAt(1));
			}
			int width;
			g.setColor(Color.BLACK);
			g.fillRect(5, 20, 2, 450);
			g.setFont(new Font("Ariel",Font.BOLD,15));
			g.setColor(Color.RED);
			g.fillRect(7, 60, 300, 30);
			g.setColor(Color.BLACK);
			g.drawString(top1 +"-"+ amount1,10,120);
			
			g.setColor(Color.ORANGE);
			width = (int)Math.ceil(((float)amount2/amount1)*300);
			g.fillRect(7, 140, width, 30);
			g.setColor(Color.BLACK);
			g.drawString(top2 +"-"+ amount2,10,200);
			
			g.setColor(Color.YELLOW);
			width = (int)Math.ceil(((float)amount3/amount1)*300);
			g.fillRect(7, 220, width, 30);
			g.setColor(Color.BLACK);
			g.drawString(top3 +"-"+ amount3,10,280);
			
			g.setColor(Color.green);
			width = (int)Math.ceil(((float)amount4/amount1)*300);
			g.fillRect(7, 300, width, 30);
			g.setColor(Color.BLACK);
			g.drawString(top4 +"-"+ amount4,10,360);
			
			g.setColor(Color.BLUE);
			width = (int)Math.ceil(((float)amount5/amount1)*300);
			g.fillRect(7, 380, width, 30);
			g.setColor(Color.BLACK);
			g.drawString(top5 +"-"+ amount5,10,440);
			
		}
	}
//	public static void main(String[] args) {
//		new TopFive("coffee16");
//	}
}
