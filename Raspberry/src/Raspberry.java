import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

public class Raspberry extends JFrame{
	JLabel pic;
	JButton wifiButton;
	Timer pictureTimer;
	Timer serverTimer;
	Socket socket;
	String BLUMAC;
	int x=0;
//	String dir = "C:/Users/choo/images";
	String dir = "/home/pi/Pictures";
	ConnectServer connectServer;
	File[] imageFiles;
	JSON imageListJSON;
	//Images Path In Array
//	String[] list = {
//			
//			
//	};
	List<String> list = new ArrayList<String>();
			
	public Raspberry() {
		super("Java SlideShow");
		try {
			Process runBluetooch = Runtime.getRuntime().exec("sudo hciconfig hci0 piscan");
			Process takeBluetoothMac = Runtime.getRuntime().exec("bluetoothctl");
			InputStreamReader inputStreamReader = new InputStreamReader(takeBluetoothMac.getInputStream());
			BufferedReader br = new BufferedReader(inputStreamReader);
			while(true){
				BLUMAC = br.readLine();
				String[] split = BLUMAC.split(" ");
				System.out.println("BLUMAC = " + BLUMAC);
				if(split.length>3 && split[1].trim().equals("Controller")){
					BLUMAC = split[2];
					System.out.println("BLUMAC = "+BLUMAC);
					break;
				}
			}
//			while((BLUMAC = br.readLine()) != null) {
//				String[] split = BLUMAC.split(" ");
//				System.out.println("BLUMAC = "+BLUMAC);
//			}
			
			System.out.println("success BLUMAC");
//			Process closeBluetoothMAC = Runtime.getRuntime().exec("quit");
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		FindImage(dir);
		pic = new JLabel();
		pic.setBounds(0, 0, 785, 363);
		wifiButton = new JButton(new ImageIcon(getClass().getClassLoader().getResource("wifi.png")));
		wifiButton.setLocation(700, 10);
		wifiButton.setSize(50, 50);
		wifiButton.setBorderPainted(false);
		wifiButton.setContentAreaFilled(false);
		wifiButton.setFocusPainted(false);
		
		wifiButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("clicked wifi button");
				try {
					Process connectWifi = Runtime.getRuntime().exec("wpa_gui");
					Process runKeyboard = Runtime.getRuntime().exec("matchbox-keyboard");
//					BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
//				    String line = null;
//				   
//				    while((line = br.readLine()) != null){
//				        System.out.println(line);
//				    }
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
//		pic.add(wifiButton);
		add(wifiButton);
		//Call The Function SetImageSize
		SetImageSize(list.size()-1);
		//set a timer
		setTimer();
		
		add(pic);
		pictureTimer.start();
		System.out.println("PictureTimer strat");
		setLayout(null);
		setSize(800,400);
		getContentPane().setBackground(Color.decode("#bdb67b"));
		setLocationRelativeTo(null);
//		setDefaultCloseOperation(Raspberry.EXIT_ON_CLOSE);
		addWindowListener(
		         new WindowAdapter() {
		            public void windowClosing( WindowEvent e ) {
		            	System.out.println("closed");
//		            	connectServer.stopConnect();
		            	System.exit( 0 );
		            }
		         }
		      );
		setVisible(true);
		System.out.println("success JFrame");
		serverTimer.start();
		System.out.println("ServerTimer Start");
	}
	
	public void setTimer(){
		serverTimer = new Timer(5000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
//				socket = new Socket(/*ServerIP*/ /*, ServerPort*/);
				connectServer = new ConnectServer();
				FindImage(dir);
				connectServer.setImageList(imageListJSON.getJSONArray().toString(), BLUMAC);
				connectServer.start();
				System.out.println("connectServer start");
//				connectServer.send(imageListJSON.getJSONArray().toString());
			}
		});
		
		pictureTimer = new Timer(3000, new ActionListener() {
			
		@Override
		public void actionPerformed(ActionEvent e) {
			SetImageSize(x);
			x += 1;
			if(x >= list.size() )
				x=0;
			
			}
		});
	}
	
	//create a function to resize the image
	public void SetImageSize(int i){
		ImageIcon icon = new ImageIcon(list.get(i));
		Image img = icon.getImage();
		Image newImg = img.getScaledInstance(pic.getWidth(), pic.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon newImc = new ImageIcon(newImg);
		pic.setIcon(newImc);
	}
	
	// find images that file name is end of .jpg.
	public void FindImage(String dir){
		imageListJSON = new JSON();
		File fileList = new File(dir);
        File[] imgFiles = fileList.listFiles(new FilenameFilter(){ // 이미지 파일 리스트 검색 (searching image file list)
			@Override
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				return name.endsWith(".jpg") || name.endsWith(".png");
			}
        });
        if(imgFiles.length>0){ // 이미지 파일 리스트 list에 추가
        	list.clear();
      	  for(int i=0; i<imgFiles.length; i++){
      		  imageListJSON.addJSONArray("Frame", BLUMAC, imgFiles[i].getName(), Base64EnDe.fileToString(imgFiles[i])); // images put in JSON Array
      		  System.out.println("img to String = " + imgFiles[i].toString());
      		  list.add(dir + "/" +imgFiles[i].getName());
      		  System.out.println("image = "+imgFiles[i].getName());
      	  }
        } else{
      	  System.out.println("폴더에 jpg파일이 없습니다.");
        }
        
	}
	public static void main(String[] args) {
		new Raspberry();
	}

}
