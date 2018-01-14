package guiUtils;

import java.util.List;
import java.util.stream.Collectors;

import controller.Ctrl;
import exceptions.CtrlException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;

public class OutView implements GuiView {

	
	ListView<Integer> outView;
	StackPane root;
	ObservableList<Ctrl> exampleList;
	ChoiceBox<Ctrl> prg;
	Integer curPrgIndex;
	
	public OutView(
			StackPane root,
			ListView<Integer> outView,
			ObservableList<Ctrl> exampleList, 
			ChoiceBox<Ctrl> prg,
			Integer curPrgIndex
			) throws CtrlException
		
		{
			this.root = root;
			this.exampleList = exampleList; 
			this.outView = outView;
			this.prg = prg;
			this.curPrgIndex = curPrgIndex;
			
			List<Integer> lst = null;
			
			prg.getItems().get(curPrgIndex);
			lst = exampleList.get(curPrgIndex).getOut(0);
		
			
		
			ObservableList<Integer> oLst = FXCollections.observableArrayList(
					lst.stream().collect(Collectors.toList())
					);
			
			outView.setItems(oLst);
			
			Label l = new Label("Output");
			l.setTranslateX(615);
			l.setTranslateY(-170);

			root.getChildren().add(l);
			
			outView.setTranslateX(615);
			outView.setTranslateY(100);
			outView.setMaxSize(200, 500);
			
			root.getChildren().add(outView);
		}
	
	public void update() throws CtrlException
	{	
		List<Integer> lst = null;
		
	
		this.curPrgIndex = this.prg.getSelectionModel().getSelectedIndex();
		
		lst = this.prg.getItems().get(this.curPrgIndex).getOut(0);
	
		ObservableList<Integer> oLst = FXCollections.observableArrayList(
			 	lst.stream().collect(Collectors.toList())
				);
	
		
		this.outView.setItems(oLst);
	}	
}
