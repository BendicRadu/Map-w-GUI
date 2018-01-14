package guiUtils;

import javafx.beans.property.SimpleStringProperty;

public class TableColumnsView<T1, T2>{

	private final SimpleStringProperty name;
	private final SimpleStringProperty value;
	
	TableColumnsView(T1 name, T2 value){
		this.name = new SimpleStringProperty(name.toString());
		this.value = new SimpleStringProperty(value.toString());
	}
	
	public String getName(){
		return name.get();
	}
	
	public String getValue(){
		return value.get();
	}
	
}
