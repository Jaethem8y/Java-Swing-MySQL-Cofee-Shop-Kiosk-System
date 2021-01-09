package DB;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Connect {

	public static Connection makeConnection(String db) {
		String url = "jdbc:mysql://localhost/"+db;
		String id = "root";
		String pass = "1234";
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver on");
			conn = DriverManager.getConnection(url,id,pass);
			System.out.println("DB on");
		}catch(ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}
	
	public static Connection createDB() {
		Connection conn = makeConnection("");
		try {
			Statement st = conn.createStatement();
			st.executeUpdate("create database if not exists coffee");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	
	public static Connection createTable() {
		Connection conn = createDB();
		try {
			Statement st = conn.createStatement();
			st.executeUpdate("use coffee");
			st.executeUpdate("create table if not exists menu(m_no int primary key not null auto_increment,m_group varchar(10),m_name varchar(30), m_price int)");
			System.out.println("A");
			st.executeUpdate("create table if not exists user(u_no int primary key not null auto_increment, u_id varchar(20), u_pw varchar(4), u_name varchar(5), u_bd varchar(14), u_point int, u_grade varchar(10))");
			System.out.println("A");
			st.executeUpdate("create table if not exists shopping(s_no int primary key not null auto_increment,m_no int,s_price int, s_count int, s_size varchar(1), s_amount int, foreign key(m_no) references menu(m_no))");
			System.out.println("A");
			st.executeUpdate("create table if not exists orderlist(o_no int primary key not null auto_increment, o_date Date, u_no int, m_no int, o_group varchar(10),o_size varchar(1), o_price int, o_count int, o_amount int, foreign key(u_no) references user(u_no), foreign key (m_no) references menu(m_no))");
			System.out.println("A");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
		
	}
	
	public static void insertTable() {
		String filename[] = {"menu.txt","user.txt"};
		String sql[] = {"insert into menu values(?,?,?,?)","insert into user values(?,?,?,?,?,?,?)",};
		String orderlist = "insert into orderlist values(?,?,?,?,?,?,?,?,?)";
		String orderlistFile = "orderlist.txt";
		Connection conn = createTable();
		for(int i=0;i<filename.length;i++) {
			try {
				Scanner s = new Scanner(new FileReader("/Users/jaehyeokchoi/Desktop/JavaProjects/Coffee/2019 커피매장/DataFiles/"+filename[i]));
				PreparedStatement psmt = conn.prepareStatement(sql[i]);
				s.nextLine();
				while(s.hasNext()) {
					String a = s.nextLine();
					StringTokenizer st = new StringTokenizer(a,"\t");
					int k = st.countTokens();
					for(int j=0;j<k;j++) {
						String b = st.nextToken();
						psmt.setString(j+1, b);
					}
					psmt.executeUpdate();
				}
				s.close();

			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
			} catch(SQLException e) {
				System.out.println(e.getMessage());
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		}
		try {
			PreparedStatement psmt = conn.prepareStatement(orderlist);
			Scanner s = new Scanner(new FileReader("/Users/jaehyeokchoi/Desktop/JavaProjects/Coffee/2019 커피매장/DataFiles/"+orderlistFile));
			System.out.println(s.nextLine());
			while(s.hasNext()) {
				String a = s.nextLine();
				StringTokenizer st = new StringTokenizer(a,"\t");
				int k = st.countTokens();
//				for(int i=0;i<k;i++) {
//					if(i==6) {
//						System.out.print(k+"\t");
//						System.out.println(st.nextToken());
//					}else {
//						st.nextToken();
//					}
//				}
//				System.out.println();
				if(k==8) {
					psmt.setString(1, st.nextToken());
					String b = st.nextToken();
					if(b.equals("2019-02-30")||b.equals("2019-02-29")) {
						b = "2019-03-01";
					}
					psmt.setString(2, b);
					psmt.setString(3, st.nextToken());
					psmt.setString(4, st.nextToken());
					psmt.setString(5, st.nextToken());
					psmt.setString(6, " "
							+ "");
					psmt.setString(7, st.nextToken());
					psmt.setString(8, st.nextToken());
					psmt.setString(9, st.nextToken());


				}else {
					for(int j=0;j<k;j++) {
						String b = st.nextToken();
						if(b.equals("2019-02-30")||b.equals("2019-02-29")) {
							b = "2019-03-01";
						}
						psmt.setString(j+1, b);
					}
				}
				psmt.executeUpdate();
				
			}
			s.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		} catch(Exception e) {
			System.out.println(e.getMessage());
			
		}
		
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		insertTable();
	}

}
