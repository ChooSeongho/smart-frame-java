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
	// JDBC Driver 패키지 위치
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver"; 
	/* SQL URL ? SQL과 연결 끊겼을 때 자동 연결 해 줄 것인지 true/false & SSL(Secure Socket Layer)을 사용할 것인지 true/false
	          ※ SSL 사용 할것인지 안할 것인지 설정 안해주면 작동에는 문제 없으나 오류 메시지 나옴.*/
	static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/jframe?autoReconnect=true&useSSL=false"; 
		
	// SQL 사용자 ID
	static final String USERNAME = "root";
	// SQL 사용자 Password
	static final String PASSWORD = "root";
	
	Connection con = null; // MYSQL과 java 연동 위한 클래스
	// Statement - SQL문을 실행하고 결과를 반환하는 기능들을 캡슐화한 인터페이스
	Statement st = null;
	// PreparedStatement - Statement를 상속 받는데, Statement보다 더 쓰기 편함
	PreparedStatement pst = null;
	/* 
	 *  Statement의 장점 :
	 *  1. 동적으로 SQL문을 만들 수 있다.
	 *  2. SQL문을 직접적으로 작성하기 때문에 쉽게 분석할 수 있다.
	 *  Statement의 단점 :
	 *  1. sql문이 문자열로 작성되기 때문에 문자역과 같은 값을 넣기 위해서 작은따옴표 등의 처리를 직접 해야한다.
	 *  2. 잘못 작성된 경우 런타임(실행중)에만 확인할 수 있다.
	 *  3. 작성된 SQL문을 재사용하기 어렵다.
	 *  PreparedStatement 보안된 장점 :
	 *  1. sql문장을 미리 컴파일할 수 있도록 개선되었다.
	 *  2. 작은따옴표같은 문자열 등을 자동적으로 처리한다.
	 *  3. 재사용하기 편하다.
	 */
	// ResultSet - 결과가 있는 SQL문장을 실행한 후 결과를 받을 때에만 사용.
	ResultSet rs = null;
	
	public DataBase(){
		try {
			// jar파일(mysql-connector-java-5.1.41-bin.jar)안에 있는 com.mysql.jdbc패키지의 Driver클래스 로딩
			Class.forName(JDBC_DRIVER); 

			// jdbc를 통해 MYSQL과 java 연동
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
				System.out.println(pst.executeUpdate()+"개가 'data' table에 추가되었습니다.");
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
				System.out.println(pst.executeUpdate()+"개가 'data' table에 추가되었습니다.");
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
