import java.util.ArrayList;
// DataFormat 배열 관리 클래스
public class DataList {
	private ArrayList<DataFormat> list = new ArrayList<DataFormat>();
	
	public void setList(ArrayList<DataFormat> list){
		this.list = list;
	}
	
	public ArrayList<DataFormat> getList(){
		return list;
	}
	
	public int arrayCount(){
		return list.size();
	}
	
	public void addList(DataFormat data){
		list.add(data);
	}
}
