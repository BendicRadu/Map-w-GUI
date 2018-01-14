package guiUtils;

import java.util.Map;
import java.util.Map.Entry;

import controller.Ctrl;
import exceptions.CtrlException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import model.IprgState;

public class SymTableView implements GuiView{
	
	StackPane root;
	TableView<TableColumnsView<String, Integer>> symTable;
	ObservableList<Ctrl> exampleList;
	ChoiceBox<Ctrl> prg;
	
	ListView<IprgState> prgList;
	
	Integer curPrgIndex;
	
	@SuppressWarnings("unchecked")
	public SymTableView(
			StackPane root,
			TableView<TableColumnsView<String, Integer>> symTable,
			ObservableList<Ctrl> exampleList,
			ChoiceBox<Ctrl> prg,
			ListView<IprgState> prgList,
			Integer curPrgIndex
			) throws CtrlException
	{
		
		this.root = root;
		this.symTable = symTable;
		this.exampleList = exampleList;
		this.prg = prg;
		this.curPrgIndex = curPrgIndex;
		this.prgList = prgList;
		
		@SuppressWarnings("rawtypes")
		TableColumn idCol = new TableColumn("Id");
		idCol.setMinWidth(100);
        idCol.setCellValueFactory(
                new PropertyValueFactory<TableColumnsView<String, Integer>, String>("name"));
 
        @SuppressWarnings("rawtypes")
		TableColumn valCol = new TableColumn("value");
        valCol.setMinWidth(100);
        valCol.setCellValueFactory(
                new PropertyValueFactory<TableColumnsView<String, Integer>, String>("value"));
 
		
		Map<String, Integer> map = null;
	
		prg.getItems().get(curPrgIndex);
		map = exampleList.get(curPrgIndex).getSym(0);

		ObservableList<TableColumnsView<String, Integer>> data = FXCollections.observableArrayList();
		
		for ( Entry<String, Integer> entry : map.entrySet() ) {
			TableColumnsView<String, Integer> aux = new TableColumnsView<String, Integer>(entry.getKey(), entry.getValue());
		    data.add(aux);
		}
		
		symTable.setItems(data);
		symTable.getColumns().addAll(idCol, valCol);
		

		Label l = new Label("Sym Table");
		l.setTranslateX(6);
		l.setTranslateY(-170);

		root.getChildren().add(l);
		
		symTable.setTranslateX(6);
		symTable.setTranslateY(100);
		symTable.setMaxSize(200, 500);
		
		root.getChildren().add(symTable);
	}


	@Override
	public void update() throws CtrlException {
		
		Map<String, Integer> sym = null;
			
		this.curPrgIndex = this.prg.getSelectionModel().getSelectedIndex();
		sym = this.prg.getItems().get(this.curPrgIndex).getSym(prgList.getSelectionModel().getSelectedIndex());
		

		ObservableList<TableColumnsView<String, Integer>> data = FXCollections.observableArrayList();
	
		for ( Entry<String, Integer> entry : sym.entrySet() ) {
			TableColumnsView<String, Integer> aux = new TableColumnsView<String, Integer>(entry.getKey(), entry.getValue());
		    data.add(aux);
		}
		
		this.symTable.setItems(data);
	}
	
}
