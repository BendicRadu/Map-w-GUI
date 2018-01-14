package guiUtils;

import java.io.BufferedReader;
import java.util.Map;
import java.util.Map.Entry;

import controller.Ctrl;
import exceptions.CtrlException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;

import utils.ImyTuple;

public class FileView implements GuiView{
	
	StackPane root;
	TableView<TableColumnsView<Integer, String>> fileTable;
	ObservableList<Ctrl> exampleList;
	ChoiceBox<Ctrl> prg;
	Integer curPrgIndex;
	
	@SuppressWarnings("unchecked")
	public FileView(
			StackPane root,
			TableView<TableColumnsView<Integer, String>> fileTable,
			ObservableList<Ctrl> exampleList,
			ChoiceBox<Ctrl> prg,
			Integer curPrgIndex
			)  throws CtrlException
	{
		
		this.root = root;
		this.fileTable = fileTable;
		this.exampleList = exampleList;
		this.prg = prg;
		this.curPrgIndex = curPrgIndex;
		
		@SuppressWarnings("rawtypes")
		TableColumn idCol = new TableColumn("Id");
		idCol.setMinWidth(100);
        idCol.setCellValueFactory(
                new PropertyValueFactory<TableColumnsView<Integer, String>, String>("name"));
 
        @SuppressWarnings("rawtypes")
		TableColumn valCol = new TableColumn("File Name");
        valCol.setMinWidth(100);
        valCol.setCellValueFactory(
                new PropertyValueFactory<TableColumnsView<Integer, String>, String>("value"));
 
		
		Map<Integer, ImyTuple<String, BufferedReader>> map = null;
		try {
			prg.getItems().get(curPrgIndex);
			map = exampleList.get(curPrgIndex).getFile(0);
		} catch (CtrlException e) {
			e.printStackTrace();
		}
		
		ObservableList<TableColumnsView<Integer, String>> data = FXCollections.observableArrayList();
		
		for ( Entry<Integer, ImyTuple<String, BufferedReader>> entry : map.entrySet() ) {
			TableColumnsView<Integer, String> aux = new TableColumnsView<Integer, String>(entry.getKey(), entry.getValue().getFirst());
		    data.add(aux);
		}
		
		fileTable.setItems(data);
		fileTable.getColumns().addAll(idCol, valCol);
		
		Label l = new Label("Output");
		l.setTranslateX(209);
		l.setTranslateY(-170);

		root.getChildren().add(l);
		
		fileTable.setTranslateX(209);
		fileTable.setTranslateY(100);
		fileTable.setMaxSize(200, 500);
		
		
		root.getChildren().add(fileTable);
	}


	@Override
	public void update() throws CtrlException
	
		{

		Map<Integer, ImyTuple<String, BufferedReader>>map = null;
			
		this.curPrgIndex = this.prg.getSelectionModel().getSelectedIndex();
		map = this.prg.getItems().get(this.curPrgIndex).getFile(0);
			
		ObservableList<TableColumnsView<Integer, String>> data = FXCollections.observableArrayList();
		
		for ( Entry<Integer, ImyTuple<String, BufferedReader>> entry : map.entrySet() ) {
			TableColumnsView<Integer, String> aux = new TableColumnsView<Integer, String>(entry.getKey(), entry.getValue().getFirst());
		    data.add(aux);
		}
		
		this.fileTable.setItems(data);
	}
	
}
