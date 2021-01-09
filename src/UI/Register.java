package UI;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;

import com.mysql.cj.util.StringUtils;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
public class Register extends JFrame{
	String n[] = {"Drinks","Food","Product"};
	JComboBox combo = new JComboBox(n);
	JTextField name = new JTextField(10);
	JTextField price= new JTextField(10);
	JButton photo = new JButton("Register photo");
	JButton register = new JButton("Register");
	JButton cancel = new JButton("cancel");
	JPanel pic = new Right();
	String fname="";
	String pathname = "";
	public Register() {
		setTitle("Register");
		JPanel left = new JPanel (new GridLayout(3,1));
		JPanel one = new JPanel();
		JLabel a = new JLabel("Sort Out");
		a.setHorizontalAlignment(SwingConstants.CENTER);
		one.add(a);one.add(combo);left.add(one);
		JPanel two = new JPanel();
		JLabel b = new JLabel("Name");
		b.setHorizontalAlignment(SwingConstants.CENTER);
		two.add(b); two.add(name);left.add(two);
		JPanel three = new JPanel();
		JLabel c = new JLabel("Price");
		c.setHorizontalAlignment(SwingConstants.CENTER);
		three.add(c);three.add(price);left.add(three);
		add(left , BorderLayout.CENTER);
		JPanel East = new JPanel(new BorderLayout());
		JPanel inEast = new JPanel();
		inEast.add(pic);
		East.add(inEast,BorderLayout.CENTER);
		JPanel bot = new JPanel();
		
		photo.addActionListener(new MyActionListener());
		register.addActionListener(new MyActionListener());
		cancel.addActionListener(new MyActionListener());

		bot.add(photo);
		East.add(bot,BorderLayout.SOUTH);
		add(East,BorderLayout.EAST);
		JPanel bottom = new JPanel();
		bottom.add(register); bottom.add(cancel);
		add(bottom, BorderLayout.SOUTH);
		setSize(400,270);
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				new Main();
			}
		});
	}
	class Right extends JPanel{
		public Right() {
			this.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
			this.setPreferredSize(new Dimension(150,150));
		}
	}
	public static boolean isNumeric(String str) {
		try {
			Integer.parseInt(str);
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	class MyActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("cancel")) {
				dispose();
				new Main();
			}else if(e.getActionCommand().equals("Register")) {
				if((name.getText().equals((""))|| (price.getText().equals("")))){
					JOptionPane.showMessageDialog(null, "Fill all the fields","Message",JOptionPane.INFORMATION_MESSAGE);
				}else if(!isNumeric(price.getText())) {
					JOptionPane.showMessageDialog(null, "Price has to be numeric","Message",JOptionPane.INFORMATION_MESSAGE);
				}else if(fname.equals("")){
					JOptionPane.showMessageDialog(null, "Please select a picture","Message",JOptionPane.INFORMATION_MESSAGE);
				}else {
					Connection conn = DB.Connect.makeConnection("coffee");
					try {
						Statement st = conn.createStatement();
						ResultSet re = st.executeQuery("select count(m_name) as total from menu where m_name = '"+name.getText()+"'");
						re.next();
						if(re.getLong("total")>=1) {
							JOptionPane.showMessageDialog(null, "Name already exists","Message",JOptionPane.ERROR_MESSAGE);
						}else {
						
							PreparedStatement pmst = conn.prepareStatement("insert into menu(m_group,m_name,m_price) values(?,?,?)");
							Statement sta = conn.createStatement();
							
							String m_group = (String) combo.getSelectedItem();
							if(m_group.equals("Drinks")) {
								m_group = "음료";
							}else if(m_group.equals("Food")) {
								m_group = "푸드";
							}else{
								m_group = "상품";
							}
							String m_name = name.getText();
							String m_price = price.getText();
							pmst.setString(1, m_group);pmst.setString(2, name.getText());pmst.setString(3, m_price);
							ImageIcon origin = new ImageIcon(pathname);
							Image sizing = origin.getImage();
							Image changed = sizing.getScaledInstance(150,150, Image.SCALE_SMOOTH);
							BufferedImage current = new BufferedImage(origin.getIconWidth(),origin.getIconHeight(),BufferedImage.TYPE_INT_RGB);
							Graphics g2d = current.createGraphics();
							
							g2d.drawImage(sizing, 0, 0, null);
							File outputfile = new File("/Users/jaehyeokchoi/Desktop/JavaProjects/Coffee/2019 커피매장/DataFiles/이미지/"+name.getText()+".jpg");
							try {
								ImageIO.write(current, "jpg", outputfile);
								pmst.executeUpdate();
								sta.executeUpdate("set @count = 0");
								sta.executeUpdate("update menu set m_no = @count:=@count+1");
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							JOptionPane.showMessageDialog(null, "Registration complete","Message",JOptionPane.INFORMATION_MESSAGE);
							dispose();
							new Admin();
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}else {
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files","jpg");
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				jfc.setFileFilter(filter);
				int indicator = jfc.showOpenDialog(null);
				if(indicator == JFileChooser.APPROVE_OPTION) {
					pic.removeAll();
					File selectedFile = jfc.getSelectedFile();
					String name = selectedFile.getName();
					fname = name.replace(".jpg","");
					String fullpath = selectedFile.getAbsolutePath();
					pathname = selectedFile.getAbsolutePath();
					ImageIcon origin = new ImageIcon(fullpath);
					Image sizing = origin.getImage();
					Image changed = sizing.getScaledInstance(150,150, Image.SCALE_SMOOTH);
					ImageIcon origin2 = new ImageIcon(changed);
					JLabel photo = new JLabel(origin2);
					pic.add(photo);
					revalidate();
				}
			}
			
		}
		
	}

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		new Register();
//	}

}
