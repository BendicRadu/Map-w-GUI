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
import model.IprgState;

public class PrgListView implements GuiView{
	
	
	StackPane root;
	ListView<IprgState> iPrg;
	
	ObservableList<Ctrl> exampleList;
	ChoiceBox<Ctrl> prg;
	Integer curPrgIndex; 
	
	public PrgListView(
			
			StackPane root,
			ListView<IprgState> iPrg,
			
			ChoiceBox<Ctrl> prg,
			ObservableList<Ctrl> exampleList,
			
			Integer curPrgIndex
		) throws CtrlException{
		
		this.root = root;
		this.iPrg = iPrg;
		this.prg = prg;
		this.exampleList = exampleList;
		this.curPrgIndex = curPrgIndex;


		List<IprgState> lst = null;
	
		prg.getItems().get(curPrgIndex);
		lst = exampleList.get(curPrgIndex).getPrgList();
	
		
	
		ObservableList<IprgState> oLst = FXCollections.observableArrayList(
				lst.stream().collect(Collectors.toList())
				);
		
		iPrg.setItems(oLst);
		

		Label l = new Label("Program list");
		l.setTranslateX(-347);
		l.setTranslateY(-310);

		root.getChildren().add(l);
		
		iPrg.setTranslateX(-347);
		iPrg.setTranslateY(-240);
		
		iPrg.setMaxWidth(500);
		iPrg.setMaxHeight(100);
		
		root.getChildren().add(iPrg);
	}

	@Override
	public void update() throws CtrlException {
		
		List<IprgState> lst = null;
		
		
		
		this.curPrgIndex = this.prg.getSelectionModel().getSelectedIndex();
		lst = this.prg.getItems().get(this.curPrgIndex).getPrgList();
	
		
		ObservableList<IprgState> oLst = FXCollections.observableArrayList(
			 	lst.stream().collect(Collectors.toList())
				);
	
		
		this.iPrg.setItems(oLst);
	}
	
}
