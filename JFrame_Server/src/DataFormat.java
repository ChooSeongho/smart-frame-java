import java.sql.Blob;

// JSON으로 받아올 데이터 포맷 클래스
public class DataFormat {
	private String BLUMAC; // 액자의 Blutooth MAC 주소
//	private Blob imageFile; // 이미지 데이터
	private String imageFile;
//	private byte[] imageFile;
	private String name;   // 사진 파일 이름(전송할 파일 or 삭제하고 싶은 파일 or 액자가 서버에 있던 자료들 요청 or 안드로이드 화면에 뿌려줄 이미지 리스트 요청)
	private String type; // 보낸 곳이 안드로이드인지 액자인지
	
	
	public void setBLUMAC(String MAC){ 
		BLUMAC = MAC;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
//	public void setimageFile(Blob data){
//		imageFile = data;
//	}
	public void setimageFile(String data){
		imageFile = data;
	}
//	public void setimageFile(byte[] data){
//		imageFile = data;
//	}
//	
	public void setType(String type){
		this.type = type;
	}
	
	public String getBLUMAC(){
		return BLUMAC;
	}
	
	public String getName(){
		return name;
	}
	
//	public Blob getImageFile(){
//		return imageFile;
//	}
	public String getImageFile(){
		return imageFile;
	}
//	public byte[] getImageFile(){
//		return imageFile;
//	}
	
	public String getType(){
		return type;
	}
}
