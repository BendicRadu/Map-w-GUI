package guiUtils;

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

public class HeapView implements GuiView{
	
	StackPane root;
	TableView<TableColumnsView<Integer, Integer>> heap;
	ObservableList<Ctrl> exampleList;
	ChoiceBox<Ctrl> prg;
	Integer curPrgIndex;
	
	@SuppressWarnings("unchecked")
	public HeapView(
			StackPane root,
			TableView<TableColumnsView<Integer, Integer>> heap,
			ObservableList<Ctrl> exampleList,
			ChoiceBox<Ctrl> prg,
			Integer curPrgIndex
			) throws CtrlException
	{
		
		this.root = root;
		this.heap = heap;
		this.exampleList = exampleList;
		this.prg = prg;
		this.curPrgIndex = curPrgIndex;
		
		@SuppressWarnings("rawtypes")
		TableColumn idCol = new TableColumn("Address");
		idCol.setMinWidth(100);
        idCol.setCellValueFactory(
                new PropertyValueFactory<TableColumnsView<Integer, Integer>, String>("name"));
 
        
        @SuppressWarnings("rawtypes")
		TableColumn valCol = new TableColumn("Value");
        valCol.setMinWidth(100);
        valCol.setCellValueFactory(
                new PropertyValueFactory<TableColumnsView<Integer, Integer>, String>("value"));
 
		
		Map<Integer, Integer> map = null;
		
		prg.getItems().get(curPrgIndex);
		map = exampleList.get(curPrgIndex).getHeap(0);
		
		
		ObservableList<TableColumnsView<Integer, Integer>> data = FXCollections.observableArrayList();
		
		for ( Entry<Integer, Integer> entry : map.entrySet() ) {
			TableColumnsView<Integer, Integer> aux = new TableColumnsView<Integer, Integer>(entry.getKey(), entry.getValue());
		    data.add(aux);
		}
		
		heap.setItems(data);
		
		heap.getColumns().addAll(idCol, valCol);
		
		Label l = new Label("Heap");
		l.setTranslateX(412);
		l.setTranslateY(-170);

		root.getChildren().add(l);
		
		heap.setTranslateX(412);
		heap.setTranslateY(100);
		heap.setMaxSize(200, 500);
		//heap.setTranslateY(-50);
		
		root.getChildren().add(heap);
	}


	@Override
	public void update() throws CtrlException {
		
		Map<Integer, Integer> sym = null;
					
		this.curPrgIndex = this.prg.getSelectionModel().getSelectedIndex();
		sym = this.prg.getItems().get(this.curPrgIndex).getHeap(0);
			
		ObservableList<TableColumnsView<Integer, Integer>> data = FXCollections.observableArrayList();
	
		for ( Entry<Integer, Integer> entry : sym.entrySet() ) {
			TableColumnsView<Integer, Integer> aux = new TableColumnsView<Integer, Integer>(entry.getKey(), entry.getValue());
		    data.add(aux);
		}
		
		this.heap.setItems(data);
	}
	
}
