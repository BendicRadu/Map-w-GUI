package guiUtils;

import controller.Ctrl;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.StackPane;

public class ChoiceBoxView implements GuiView{
	
	StackPane root;
	ChoiceBox<Ctrl> prg;
	ObservableList<Ctrl> exampleList; 
	
	public ChoiceBoxView(
			
			StackPane root,
			ChoiceBox<Ctrl> prg,
			ObservableList<Ctrl> exampleList 
		){
		
		this.root = root;
		this.prg = prg;
		this.exampleList = exampleList;
		
		
		
		prg.setItems(exampleList);
		prg.getSelectionModel().select(0);
		
		
		prg.setMaxWidth(1000);
		prg.setMaxHeight(100);
		root.getChildren().add(prg);
	}

	@Override
	public void update() {}
	
}
