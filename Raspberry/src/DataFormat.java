import java.sql.Blob;

// JSON���� �޾ƿ� ������ ���� Ŭ����
public class DataFormat {
	private String BLUMAC; // ������ Blutooth MAC �ּ�
//	private Blob imageFile; // �̹��� ������
	private String imageFile;
//	private byte[] imageFile;
	private String name;   // ���� ���� �̸�(������ ���� or �����ϰ� ���� ���� or ���ڰ� ������ �ִ� �ڷ�� ��û or �ȵ���̵� ȭ�鿡 �ѷ��� �̹��� ����Ʈ ��û)
	private String type; // ���� ���� �ȵ���̵����� ��������
	
	
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
