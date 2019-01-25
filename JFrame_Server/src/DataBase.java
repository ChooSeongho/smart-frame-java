import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.Base64.Decoder;

public class DataBase {
	// JDBC Driver ��Ű�� ��ġ
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver"; 
	/* SQL URL ? SQL�� ���� ������ �� �ڵ� ���� �� �� ������ true/false & SSL(Secure Socket Layer)�� ����� ������ true/false
	          �� SSL ��� �Ұ����� ���� ������ ���� �����ָ� �۵����� ���� ������ ���� �޽��� ����.*/
	static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/jframe?autoReconnect=true&useSSL=false"; 
		
	// SQL ����� ID
	static final String USERNAME = "root";
	// SQL ����� Password
	static final String PASSWORD = "root";
	
	Connection con = null; // MYSQL�� java ���� ���� Ŭ����
	// Statement - SQL���� �����ϰ� ����� ��ȯ�ϴ� ��ɵ��� ĸ��ȭ�� �������̽�
	Statement st = null;
	// PreparedStatement - Statement�� ��� �޴µ�, Statement���� �� ���� ����
	PreparedStatement pst = null;
	/* 
	 *  Statement�� ���� :
	 *  1. �������� SQL���� ���� �� �ִ�.
	 *  2. SQL���� ���������� �ۼ��ϱ� ������ ���� �м��� �� �ִ�.
	 *  Statement�� ���� :
	 *  1. sql���� ���ڿ��� �ۼ��Ǳ� ������ ���ڿ��� ���� ���� �ֱ� ���ؼ� ��������ǥ ���� ó���� ���� �ؾ��Ѵ�.
	 *  2. �߸� �ۼ��� ��� ��Ÿ��(������)���� Ȯ���� �� �ִ�.
	 *  3. �ۼ��� SQL���� �����ϱ� ��ƴ�.
	 *  PreparedStatement ���ȵ� ���� :
	 *  1. sql������ �̸� �������� �� �ֵ��� �����Ǿ���.
	 *  2. ��������ǥ���� ���ڿ� ���� �ڵ������� ó���Ѵ�.
	 *  3. �����ϱ� ���ϴ�.
	 */
	// ResultSet - ����� �ִ� SQL������ ������ �� ����� ���� ������ ���.
	ResultSet rs = null;
	
	public DataBase(){
		try {
			// jar����(mysql-connector-java-5.1.41-bin.jar)�ȿ� �ִ� com.mysql.jdbc��Ű���� DriverŬ���� �ε�
			Class.forName(JDBC_DRIVER); 

			// jdbc�� ���� MYSQL�� java ����
			con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD); 
			
/*
 * 			st= con.createStatement();
 *			rs = st.executeQuery("Show Databases");
 *			
 *			if(st.execute("Show Databases")){
 *				rs = st.getResultSet();
 *			}
 *			
 *			while(rs.next()){
 *				String str = rs.getNString(1);
 *				System.out.println(str);
 *			}
 */
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void insert(String blueMac, String imageName, String imageFile, String type){
		if(type.trim().equals("And")){
			String query = "insert into data (BLUMAC, name, imageFile) values(?, ?, ?)";
			try {
				pst = con.prepareStatement(query);
				pst.setString(1, blueMac);
				pst.setString(2, imageName);
//				pst.setBlob(3, imageFile);
//				pst.setBlob(3, imageFile);
				pst.setBytes(3, Base64EnDe.stringToBytes(imageFile));
				System.out.println(pst.executeUpdate()+"���� 'data' table�� �߰��Ǿ����ϴ�.");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(type.trim().equals("Frame")){
			String query = "insert into information values(?, ?, ?)";
			try {
				pst = con.prepareStatement(query);
				pst.setString(1, blueMac);
				pst.setString(2, imageName);
//				pst.setBlob(3, imageFile);
				pst.setBytes(3, Base64EnDe.stringToBytes(imageFile));
				System.out.println(pst.executeUpdate()+"���� 'data' table�� �߰��Ǿ����ϴ�.");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public DataList searchDB(String BLUMAC){
		String query = "select * from data where BLUMAC = '"+BLUMAC+"'";
		try {
			DataList dataList = new DataList();
			ResultSet rs = pst.executeQuery(query);
			while(rs.next()){
				DataFormat data = new DataFormat();
				data.setBLUMAC(rs.getString("BLUMAC"));
				data.setName(rs.getString("name"));
				data.setimageFile(Base64EnDe.bytesToString(rs.getBytes("imageFile")));
				dataList.addList(data);
			}
			return dataList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public void delete(){
		
	}
	
	public void getData(){
		
	}
	
	public void close() throws SQLException{
		rs.close();
		st.close();
		con.close();
	}

}
