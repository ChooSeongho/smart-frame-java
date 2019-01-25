import java.io.*;
import java.net.Socket;
import java.util.Base64;

import com.google.gson.Gson;

public class ConnectServer extends Thread {
	private String server = "192.168.219.117";
	// private String server = "172.0.0.1";
	private int port = 2017;
	private Socket socket;
	private PrintWriter output;
	private BufferedReader br = null;
	private String data = new String();
	String dir = "/home/pi/Pictures";
	String imageJSON;
	public final static int fileSize = 52428800; // 50MB
	DataList dataList;
	JSON json = new JSON();
	boolean done = true;
	String BLUMAC;

	public void setStream() {
		try {
			OutputStream outStream = socket.getOutputStream();
			output = new PrintWriter(new OutputStreamWriter(outStream), true);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println("set on streams");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void setImageList(String data, String blueaddress){
		imageJSON = data;
		BLUMAC = blueaddress;
	}

	public void run() {
		String str = null, mac = null, name = null, type = null;
		try {
			socket = new Socket(server, port);
			setStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		dataList = new DataList();
		while (done) {
			try {
				data = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(data!= null && data.trim().equals("Connect")){
				System.out.println("connected");
				send(imageJSON);
				send(json.makeJSONArray(1,"Frame",BLUMAC,"get","null").toString());
				
			}else if(data != null){
//				DataList dataList = json.parseJSONFromGSON(data);
				DataList dataList = new Gson().fromJson(data, DataList.class);
				System.out.println("datalist count = " + dataList.arrayCount());
				for (int i = 0; i < dataList.arrayCount(); i++) {
					mac = dataList.getList().get(i).getBLUMAC().trim();
					name = dataList.getList().get(i).getName().trim();
					type = dataList.getList().get(i).getType().trim();
					String image = dataList.getList().get(i).getImageFile().trim();
					if (dataList.getList().get(i).getImageFile().equals("remove")) {
						File f = new File(dir + "/" + name);
						if (f.delete()) {
							System.out.println("파일 또는 디렉토리를 성공적으로 지웠습니다: " + name);
						} else {
							System.err.println("파일 또는 디렉토리 지우기 실패: " + name);
						}
					} else {
						Base64EnDe ende = new Base64EnDe();
						int bytesRead;
						int current = 0;
						FileOutputStream fileData = null;
						BufferedOutputStream buffer = null;
						
//						for(int j=0; j<data.length(); j+=200){
//							if(j+200 >= data.length()){
//								System.out.println(data.substring(j, data.length()));
//							} else {
//								System.out.println(data.substring(j, j+199));
//							}
//						}
//						for(int j=0; j<image.length(); j+=200){
//							if(j+200 >= image.length()){
//								System.out.println(image.substring(j, image.length()));
//							} else {
//								System.out.println(image.substring(j, j+199));
//							}
//						}
						 
						try {
							InputStream byteBuffer = new ByteArrayInputStream(ende.stringToBytes(image));
//							InputStream byteBuffer = new ByteArrayInputStream(ende.stringToBytes(dataList.getList().get(i).getImageFile()));
							// 파일 받기
							byte[] fileBuffer = new byte[fileSize];
							System.out.println("dir :" + dir);
							fileData = new FileOutputStream(dir+"/"+name);
							buffer = new BufferedOutputStream(fileData);
							bytesRead = byteBuffer.read(fileBuffer, 0, fileBuffer.length);
							current = bytesRead; // 현재 위치 이어서 전송

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
						} finally {
							try {
								buffer.close();
								fileData.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}

	public void send(String message) {
		output.println(message);
		output.flush();
	}

	public void stopConnect() {
		try {
			send("close");
			done = false;
			br.close();
			output.close();
			socket.close();
			System.out.println("close connect");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
