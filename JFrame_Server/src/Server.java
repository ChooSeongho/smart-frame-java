import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Server {
	DataList frame_images = new DataList();
	DataList remove_data = new DataList();
	DataList add_data = new DataList();
	final static int serverPortNum = 2017;
	Server() throws IOException{
//		DataBase dataBase = new DataBase();
		ServerSocket serverSocket = null;
		Socket socket = null;
		System.out.println("서버 실행");
		
		try{
			serverSocket = new ServerSocket(serverPortNum);
			
			boolean done = true;
			while(done){
				socket = serverSocket.accept();
				System.out.println("서버 연결 완료");
//				ServerThread thread = new ServerThread(socket, dataBase);
				ServerThread thread = new ServerThread(socket, this);
				thread.start();
				thread.send("Connect");
			}
			
		} catch(Exception e){
			e.printStackTrace();
		} finally{
			if(socket != null){
				socket.close();
			}
			if(serverSocket != null){
				serverSocket.close();
			}
//			dataBase.close();
		}
	}
	public static void main(String[] args) throws IOException, SQLException {
		new Server();
		
	}
	
	public void saveFrameImage(DataFormat data){
		if(!checkDuple(data)){
			frame_images.addList(data);
//			System.out.println("save data = "+data.getName());
		}
	}
	
	public void saveAddImage(DataFormat data){
		add_data.addList(data);
	}
	
	public void saveRemoveImage(DataFormat data){
		remove_data.addList(data);
	}
	
	public void clearRemove(){
		remove_data = new DataList();
	}
	
	public void clearAdd(){
		add_data = new DataList();
	}
	
	public void clearFrameImage(){
		frame_images = new DataList();
	}
	
	public DataList loadRemoveImage(){
		return remove_data;
	}
	
	public DataList loadAddImage(){
		return add_data;
	}
	
	public DataList loadFrameImage(){
		return frame_images;
	}
	
	public boolean checkDuple(DataFormat data){
		for(int i=0; i<frame_images.getList().size(); i++){
			if(frame_images.getList().get(i).getImageFile().equals(data.getImageFile())){
//				System.out.println("duple image = "+frame_images.getList().get(i).getName()+" ==>"+data.getName());
				return true;
			}
//			System.out.println(frame_images.getList().get(i));
		}
		return false;
	}
	

}
