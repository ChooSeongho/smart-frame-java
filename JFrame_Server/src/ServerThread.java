import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Base64;

import org.json.simple.parser.JSONParser;


public class ServerThread extends Thread{
	private Socket socket;
	private BufferedReader br = null;
	private PrintWriter pw = null;
	private JSON json = new JSON();
	Server server;
	boolean done = true;
	public final static int fileSize = 52428800; // 50MB
//	private DataBase dataBase;
 
	ServerThread(Socket s, DataBase dataBase) throws IOException{
		this.socket = s;
//		this.dataBase = dataBase;
		setStreams();
	}
	ServerThread(Socket s, Server server) throws IOException {
		this.server = server;
		this.socket = s;
		setStreams();
	}
	
	// ���� ����� ����
	private void setStreams() throws IOException{
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		pw = new PrintWriter(socket.getOutputStream(), true);
	}
	
	//�������̵��� ��� throw �Ұ�. 
	public void run(){
		try{
			service();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
 
	private void service()throws IOException{
		String str = null, mac = null, name = null, type = null;
		System.out.println("���� �غ� ����");
		while(done){
			str = br.readLine();
			if(str == null){
				System.out.println("data - null");
				break;
			}
			
			if(str.trim().equals("close")){
				done = false;
				closeAll();
				break;
			}
//			System.out.println("receive = "+str);
			DataList dataList = json.parseJSONFromGSON(str);
			System.out.println("datalist count = "+dataList.arrayCount());
			for(int i = 0; i < dataList.arrayCount(); i++){
//				mac = dataList.getList().get(i).getBLUMAC().trim(); 
//				name = dataList.getList().get(i).getName().trim(); 
//				type = dataList.getList().get(i).getType().trim();
				String image = dataList.getList().get(i).getImageFile();
				if(dataList.getList().get(i).getType().trim().equals("And")){
					if(dataList.getList().get(i).getName().trim().equals("get")){
//						File file = new File("C:/Users/Choo/gametitle_09.jpg");
						System.out.println("get ����");
						for(int count=0; count<server.loadFrameImage().getList().size(); count++){
							json.addJSONArray("Server", server.loadFrameImage().getList().get(count).getBLUMAC(), server.loadFrameImage().getList().get(count).getName(), server.loadFrameImage().getList().get(count).getImageFile());
						}
						send(json.getJSONArray().toString());
//						send(json.makeJSONArray(10, "Server", "12:23:43:dv:53", "gametitle_09", Base64EnDe.fileToString(file)));
						// �ȵ���̵�� ������ ������ �ִ� ���� �̹��� ����Ʈ ����
					} else if(dataList.getList().get(i).getImageFile().equals("remove")){
						// �ȵ���̵忡�� �������� ���� ���� ���� ��û
						server.saveRemoveImage(dataList.getList().get(i));
						System.out.println("remove at " + dataList.getList().get(i).getImageFile());
					} else {
						// �ȵ���̵忡�� ���� �̹��� ���ϵ� ������ ���̽� 'Data' ���̺� ����
//						for(int kkk=0; kkk<str.length(); kkk+=50){
//							int last = kkk+50;
//							if(kkk+50 > str.length()){
//								last = str.length();
//							}
//							System.out.println(str.substring(kkk, last));
//						}
//						isimagesaved(dataList.getList().get(i).getImageFile(), "c://Network/" + dataList.getList().get(i).getName());
						server.saveAddImage(dataList.getList().get(i));
						System.out.println("mac = "+dataList.getList().get(i).getBLUMAC().trim());
						System.out.println("name = "+dataList.getList().get(i).getName().trim());
//						System.out.println("image = "+dataList.getList().get(i).getImageFile().toString());
						System.out.println("type = "+dataList.getList().get(i).getType().trim());
//						dataBase.insert(mac, name, image, type);
						for(int j=0; j<str.length(); j+=200){
							if(j+200 >= str.length()){
								System.out.println(str.substring(j, str.length()));
							} else {
								System.out.println(str.substring(j, j+199));
							}
						}
					}
				} else if(dataList.getList().get(i).getType().trim().equals("Frame")){
					if(dataList.getList().get(i).getName().trim().equals("get")){
						if(server.loadAddImage().arrayCount()>0){
							for(int count=0; count<server.loadAddImage().getList().size(); count++){
								json.addJSONArray("Server", server.loadAddImage().getList().get(count).getBLUMAC(), server.loadAddImage().getList().get(count).getName(), server.loadAddImage().getList().get(count).getImageFile());
								System.out.println("send addimage = "+server.loadAddImage().getList().get(count).getName());
							}
							send(json.getJSONArray().toString());
							server.clearAdd();
						}
						if(server.loadRemoveImage().arrayCount()>0){
							for(int count=0; count<server.loadRemoveImage().getList().size(); count++){
								json.addJSONArray("Server", server.loadRemoveImage().getList().get(count).getBLUMAC(), server.loadRemoveImage().getList().get(count).getName(), server.loadRemoveImage().getList().get(count).getImageFile());
								System.out.println("send removeimage = "+server.loadRemoveImage().getList().get(count).getName());
							}
							send(json.getJSONArray().toString());
							server.clearRemove();
						}
						// ������ ������ �ִ� ���ڷ� �������� �̹��� ���ϵ� ���ڷ� ����
					} else {
						// ���ڿ��� ������ �̹��� ����Ʈ�� ������ ���̽� ''���̺� ����
						server.saveFrameImage(dataList.getList().get(i));
//						System.out.println("mac = "+dataList.getList().get(i).getBLUMAC().trim());
//						System.out.println("name = "+dataList.getList().get(i).getName().trim());
//						System.out.println("image = "+dataList.getList().get(i).getImageFile().toString());
//						System.out.println("type = "+dataList.getList().get(i).getType().trim());
//						dataBase.insert(mac, name, image, type);
					}
				}
			}
		}
//		str = br.readLine();
//		System.out.println("receive = "+str);
//		DataList dataList = json.parseJSONFromGSON(str);
//		System.out.println("datalist count = "+dataList.arrayCount());
//		for(int i = 0; i < dataList.arrayCount(); i++){
//			mac = dataList.getList().get(i).getBLUMAC().trim(); 
//			name = dataList.getList().get(i).getName().trim(); 
//			type = dataList.getList().get(i).getType().trim();
//			String image = dataList.getList().get(i).getImageFile();
//			if(dataList.getList().get(i).getType().trim().equals("And")){
//				if(dataList.getList().get(i).getName().trim().equals("get")){
//					File file = new File("C:/Users/Choo/gametitle_09.jpg");
//					System.out.println("get ����");
//					send(json.makeJSONArray(10, "Server", "12:23:43:dv:53", "gametitle_09", Base64EnDe.fileToString(file)));
//					// �ȵ���̵�� ������ ������ �ִ� ���� �̹��� ����Ʈ ����
//				} else if(dataList.getList().get(i).getImageFile().equals("remove")){
//					// �ȵ���̵忡�� �������� ���� ���� ���� ��û
//				} else {
//					// �ȵ���̵忡�� ���� �̹��� ���ϵ� ������ ���̽� 'Data' ���̺� ���� 
//					System.out.println("mac = "+dataList.getList().get(i).getBLUMAC().trim());
//					System.out.println("name = "+dataList.getList().get(i).getName().trim());
//					System.out.println("image = "+dataList.getList().get(i).getImageFile().toString());
//					System.out.println("type = "+dataList.getList().get(i).getType().trim());
////					dataBase.insert(mac, name, image, type);
//				}
//			} else if(dataList.getList().get(i).getType().trim().equals("Frame")){
//				if(dataList.getList().get(i).getName().trim().equals("get")){
//					// ������ ������ �ִ� ���ڷ� �������� �̹��� ���ϵ� ���ڷ� ����
//				} else {
//					// ���ڿ��� ������ �̹��� ����Ʈ�� ������ ���̽� ''���̺� ����
//				}
//			}
//		}
////		System.out.println("���̺� �߰� �Ϸ�.");
////		System.out.println("search");
////		DataList searchResult = new DataList();
////		searchResult = dataBase.searchDB(mac);
////		for(int i=0; i<searchResult.arrayCount(); i++){
////			System.out.println("mac = "+searchResult.getList().get(i).getBLUMAC());
////			System.out.println("name = "+searchResult.getList().get(i).getName());
////			System.out.println("image = "+searchResult.getList().get(i).getImageFile());
////		}
////		pw.println(str);
////		pw.flush();
	}
	
	public void isimagesaved(String image, String dir){
//		byte[] byteImage = Base64.decode(dataList.get(pos).getImageFile(),0);
		Base64EnDe ende = new Base64EnDe();
		int bytesRead;
		int current = 0;
		FileOutputStream fileData = null;
		BufferedOutputStream buffer = null;
		try {
			InputStream byteBuffer = new ByteArrayInputStream(Base64.getDecoder().decode(image));
			// ���� �ޱ�
			byte[] fileBuffer = new byte[fileSize];
			System.out.println("dir :" + dir);
			fileData = new FileOutputStream(dir);
			buffer = new BufferedOutputStream(fileData);
			bytesRead = byteBuffer.read(fileBuffer, 0, fileBuffer.length);
			current = bytesRead; // ���� ��ġ �̾ ����

			while (bytesRead > -1) {
				bytesRead = byteBuffer.read(fileBuffer, current, (fileBuffer.length - current));
				if (bytesRead >= 0)
					current += bytesRead;
			}
			buffer.write(fileBuffer, 0, current);
			buffer.flush();
			System.out.println("File " + dir + " downloaded (" + current + " bytes read)");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(String message){
		pw.println(message);
		pw.flush();
	}
	
	public void closeAll()throws IOException{
		done = false;
		if (pw != null)
			pw.close();
		if (br != null)
			br.close();
		if (socket != null)
			socket.close();
		System.out.println("���� ����");
	}
}