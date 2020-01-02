import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SettingDB {
	static Connection con;
	static Statement stmt;
	
	public static void main(String[] args) {
		SettingDB db=new SettingDB();
		db.createDB();
		db.createTable("menu", "m_no int primary key not null auto_increment,"
				+ "m_group varchar(10), "
				+ "m_name varchar(30), "
				+ "m_price int");
		db.createTable("user", "u_no int primary key not null auto_increment, "
				+ "u_id varchar(20), "
				+ "u_pw varchar(4), "
				+ "u_name varchar(5), "
				+ "u_bd varchar(14), "
				+ "u_point int, "
				+ "u_grade varchar(10)");
		db.createTable("orderlist", "o_no int primary key not null auto_increment, o_date date, u_no int,"
				+ "m_no int, o_group varchar(10), o_size varchar(1), o_price int, o_count int, o_amount int, "
				+ "foreign key(u_no) references user(u_no), "
				+ "foreign key(m_no) references menu(m_no) on delete cascade on update cascade");
		db.createTable("shopping", "s_no int primary key not null auto_increment, "
				+ "m_no int, "
				+ "s_price int, "
				+ "s_count int, "
				+ "s_size varchar(1), "
				+ "s_amount int, "
				+ "foreign key(m_no) references menu(m_no) on delete cascade on update cascade");
	}
	
	void createTable(String table, String column) {
		execute("create table "+table+"("+column+")");
		if(!table.contentEquals("shopping"))
			execute("load data local infile './DataFiles/"+table+".txt' into table "+table+" ignore 1 lines");
	}
	
	void createDB() {
		execute("drop database if exists coffee");
		execute("create database coffee default character set utf8");
		execute("drop user if exists user@localhost");
		execute("create user user@localhost identified by '1234'");
		execute("grant select, delete, insert, update on coffee.* to user@localhost");
		execute("set global local_infile=1");
		execute("use coffee");
	}
	
	static {
		try {
			con=DriverManager.getConnection("jdbc:mysql://localhost?serverTimezone=UTC&allowLoadLocalInfile=true&"
					+ "allowPublicKeyRetrieval=true", "root", "1234");
			stmt=con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	static void execute(String sql) {
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
