package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class Manage {
	JFrame frame = new JFrame();
	Vector<Vector<String>>menu = new Vector<Vector<String>>();
	Vector<Vector<String>>data =  new Vector<Vector<String>>();
	Vector<String>col = new Vector<String>();
	
	JComboBox<String> sort;
	DefaultTableModel model;
	
	JButton search = new JButton("Search");
	JTextField find = new JTextField(20);
	
	
	JTable jt;
	JScrollPane jsp;
	
	String nameSort[] = {"","Drink","Food","Product"};
	
	JComboBox<String> rightSort = new JComboBox<String>(nameSort);
	JPanel left = new JPanel(new BorderLayout());
	JPanel right = new JPanel(new BorderLayout());
	
	JButton remove = new JButton("Remove");
	JButton update = new JButton("Update");
	JButton cancel = new JButton("Cancel");
	JButton choosePic = new JButton("Choose picture");
	
	JTextField menuName = new JTextField(10);
	JTextField menuPrice = new JTextField(10);
	
	JPanel selectPic = new JPanel();

	
	public Manage() {
		frame.setTitle("Manage");
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				new Admin();
			}
		});
		setVector();
		setTable();
		makeTop();
		makeRight();
		mJtable();
		removeAction();
		updateAction();
		
		search.addActionListener(new SearchListener());
		
		frame.add(left,BorderLayout.CENTER);
		JPanel rightPane = new JPanel();
		rightPane.add(right);
		frame.add(rightPane,BorderLayout.EAST);
		frame.setSize(900,400);
		frame.setVisible(true);
	}
	
	public void setVector(){
		Connection conn = DB.Connect.makeConnection("coffee");
		try {
			Statement st = conn.createStatement();
			ResultSet re = st.executeQuery("select * from menu");
			while(re.next()) {
				Vector<String>a = new Vector<String>();
				a.add(re.getString("m_group"));a.add(re.getString("m_name"));a.add(re.getString("m_price"));
				Vector<String>b = new Vector<String>();
				b.add(re.getString("m_no"));b.add(re.getString("m_group"));b.add(re.getString("m_name"));b.add(re.getString("m_price"));
				menu.add(b);
				data.add(a);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	public void setTable() {
		col.add("sort");col.add("menuName");col.add("price");
		model = new DefaultTableModel(data,col){
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		jt = new JTable(model);
		jsp = new JScrollPane(jt);
		left.add(jsp,BorderLayout.CENTER);
		jt.getColumnModel().getColumn(0).setPreferredWidth(60);
		jt.getColumnModel().getColumn(1).setPreferredWidth(400);
		jt.getColumnModel().getColumn(2).setPreferredWidth(60);
		
	}
	
	public void makeTop() {
		String name[] = {"All","Drinks","Food","Product"};
		sort = new JComboBox<String>(name);
		JPanel top = new JPanel();
		top.add(new JLabel("Search"));top.add(sort);top.add(find);top.add(search);
		left.add(top,BorderLayout.NORTH);
	}
	
	public void makeRight() {
		selectPic.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
		selectPic.setPreferredSize(new Dimension(150,150));
		
		JPanel rleft = new JPanel (new GridLayout(3,1));
		JPanel one = new JPanel();
		JLabel a = new JLabel("Sort Out");
		a.setHorizontalAlignment(SwingConstants.CENTER);
		one.add(a);one.add(rightSort);rleft.add(one);
		JPanel two = new JPanel();
		JLabel b = new JLabel("Name");
		b.setHorizontalAlignment(SwingConstants.CENTER);
		two.add(b); two.add(menuName);rleft.add(two);
		JPanel three = new JPanel();
		JLabel c = new JLabel("Price");
		c.setHorizontalAlignment(SwingConstants.CENTER);
		three.add(c);three.add(menuPrice);rleft.add(three);
		right.add(rleft,BorderLayout.CENTER);
		JPanel rRight = new JPanel(new BorderLayout());
		rRight.add(selectPic,BorderLayout.CENTER);
		rRight.add(choosePic,BorderLayout.SOUTH);
		right.add(rRight,BorderLayout.EAST);
		JPanel rBottom = new JPanel();
		rBottom.add(remove);rBottom.add(update);rBottom.add(cancel);
		right.add(rBottom,BorderLayout.SOUTH);
		right.add(new JLabel(""),BorderLayout.NORTH);
		
		
	}
	
	public void mJtable() {
		jt.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				selectPic.removeAll();
				int re = jt.getSelectedRow();
				if(data.elementAt(re).elementAt(0).equals("음료")) {
					rightSort.setSelectedIndex(1);
				}else if(data.elementAt(re).elementAt(0).equals("푸드")) {
					rightSort.setSelectedIndex(2);
				}else if(data.elementAt(re).elementAt(0).equals("상품")) {
					rightSort.setSelectedIndex(3);
				}
				System.out.println(rightSort.getSelectedItem());
				menuName.setText(data.elementAt(re).elementAt(1));
				menuPrice.setText(data.elementAt(re).elementAt(2));
				ImageIcon origin = new ImageIcon("/Users/jaehyeokchoi/Desktop/JavaProjects/Coffee/2019 커피매장/DataFiles/이미지/"+data.elementAt(re).elementAt(1)+".jpg");
				Image sizing = origin.getImage();
				Image changed = sizing.getScaledInstance(150,150, Image.SCALE_SMOOTH);
				ImageIcon origin2 = new ImageIcon(changed);
				JLabel photo = new JLabel(origin2);
				selectPic.add(photo);
				frame.revalidate();
			}
		});
	}
	public void removeAction() {
		remove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int re = jt.getSelectedRow();
				if(re == -1) {
					JOptionPane.showMessageDialog(null, "Please select somethign","Message",JOptionPane.ERROR_MESSAGE);
				}else {
					Connection conn = DB.Connect.makeConnection("coffee");
					try {
						Statement st = conn.createStatement();
						st.executeUpdate("delete from menu where m_name = '"+menuName.getText()+"'");
						st.executeUpdate("set @count = 0");
						st.executeUpdate("update menu set m_no = @count:=@count+1");
						menuName.setText("");
						menuPrice.setText("");
						choosePic.removeAll();
						menu.clear();
						data.clear();
						setVector();
						frame.revalidate();
						JOptionPane.showMessageDialog(null,"Complete","Message",JOptionPane.INFORMATION_MESSAGE);

					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, "Cannot delete non-new items","Message",JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					}
					
				}
			}
			
		});
	}
	public static boolean isNumeric(String str) {
		try {
			Integer.parseInt(str);
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	public void updateAction() {
		update.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int re = jt.getSelectedRow();
				if(re == -1) {
					JOptionPane.showMessageDialog(null, "Please Pick Something","Message",JOptionPane.ERROR_MESSAGE);
				}else if(rightSort.getSelectedIndex() == 0){
					JOptionPane.showMessageDialog(null, "Please pick from sort","Message",JOptionPane.ERROR_MESSAGE);
				}else if(menuName.getText().equals("") || menuPrice.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Please fill every fields","Message",JOptionPane.ERROR_MESSAGE);
				}else if(!isNumeric(menuPrice.getText())){
					JOptionPane.showMessageDialog(null, "Please use numbers for price","Message",JOptionPane.ERROR_MESSAGE);
				}else {
					Connection conn = DB.Connect.makeConnection("coffee");
					try {
						Statement st = conn.createStatement();
						ResultSet res = st.executeQuery("select count(*) as suma from menu where m_name = '"+menuName.getText()+"'");
						res.next();
						if(res.getLong("suma")>=1 && !menuName.getText().equals(data.elementAt(re).elementAt(1))) {
							JOptionPane.showMessageDialog(null, "Name already exists","Message",JOptionPane.ERROR_MESSAGE);
						}else {
//							res = st.executeQuery("select m_no from menu where m_name = '"+data.elementAt(re).elementAt(2)+"'");
//							res.next();
//							int m_no = (int) res.getLong("m_no");
							String group = "";
							if(rightSort.getSelectedIndex()==1) {
								group = "음료";
							}else if(rightSort.getSelectedIndex() == 2) {
								group = "푸드";
							}else {
								group = "상품";
							}
							String m_no="";
							String o_name = data.elementAt(re).elementAt(1);
							for(int i=0;i<menu.size();i++) {
								if(o_name.equals(menu.elementAt(i).elementAt(2))) {
									m_no = menu.elementAt(i).elementAt(0);
								}
							}
							
							PreparedStatement pmst = conn.prepareStatement("update menu set m_name = ?, m_price = ? ,m_group = ? where m_no = ?");
							String ffname = menuName.getText();
							pmst.setString(1, menuName.getText());pmst.setString(2,menuPrice.getText());pmst.setString(3,group);pmst.setString(4, m_no);
							pmst.executeUpdate();
							System.out.println("here");
							System.out.println(m_no);
							System.out.println("here");
							menuName.setText("");
							menuPrice.setText("");
							choosePic.removeAll();
							menu.clear();
							data.clear();
							
							setVector();
							frame.revalidate();
							JOptionPane.showMessageDialog(null, "Complete","Message",JOptionPane.INFORMATION_MESSAGE);
							Path source = Paths.get("/Users/jaehyeokchoi/Desktop/JavaProjects/Coffee/2019 커피매장/DataFiles/이미지/"+o_name+".jpg"); 
							try {
								Files.move(source, source.resolveSibling(ffname+".jpg"));
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
				}
			}
			
		});
	}
	class SearchListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			int grouping = sort.getSelectedIndex();
			String searchN = find.getText();
			data.clear();
			Connection conn = DB.Connect.makeConnection("coffee");
			try {
				Statement st = conn.createStatement();
				ResultSet re;
				if(grouping == 0) {
					re = st.executeQuery("select * from menu where m_name like '%"+searchN+"%'");
					while(re.next()) {
						Vector<String>a = new Vector<String>();
						a.add(re.getString("m_group"));a.add(re.getString("m_name"));a.add(re.getString("m_price"));
						data.add(a);
					}
				}else if(grouping == 1) {
					re = st.executeQuery("select * from menu where m_name like '%"+searchN+"%' having m_group = '음료'");
					while(re.next()) {
						Vector<String>a = new Vector<String>();
						a.add(re.getString("m_group"));a.add(re.getString("m_name"));a.add(re.getString("m_price"));
						data.add(a);
					}
				}else if(grouping == 2) {
					re = st.executeQuery("select * from menu where m_name like '%"+searchN+"%' having m_group = '푸드'");
					while(re.next()) {
						Vector<String>a = new Vector<String>();
						a.add(re.getString("m_group"));a.add(re.getString("m_name"));a.add(re.getString("m_price"));
						data.add(a);
					}
				}else if(grouping ==3) {
					re = st.executeQuery("select * from menu where m_name like '%"+searchN+"%' having m_group = '상품'");
					while(re.next()) {
						Vector<String>a = new Vector<String>();
						a.add(re.getString("m_group"));a.add(re.getString("m_name"));a.add(re.getString("m_price"));
						data.add(a);
					}
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			jt.revalidate();
			
			
		}
		
	}
	
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		new Manage();
//	}

}
