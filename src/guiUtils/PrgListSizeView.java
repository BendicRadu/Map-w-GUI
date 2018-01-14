package guiUtils;

import controller.Ctrl;
import exceptions.CtrlException;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class PrgListSizeView implements GuiView{

	StackPane root;
	TextField prgListSizeView;
	ObservableList<Ctrl> exampleList;
	ChoiceBox<Ctrl> prg;
	Integer curPrgIndex;
	
	public PrgListSizeView(
			StackPane root,
			TextField prgListSizeView,
			ObservableList<Ctrl> exampleList,
			ChoiceBox<Ctrl> prg,
			Integer curPrgIndex
			) throws CtrlException{
		
		this.root = root;
		this.prgListSizeView = prgListSizeView;
		this.exampleList = exampleList;
		this.prg = prg;
		this.curPrgIndex = curPrgIndex;
		
		
		prgListSizeView.setText(String.valueOf(prg.getItems().get(this.curPrgIndex).getPrgList().size()));
		
		
		Label l = new Label("Nr. of active threads: ");
		
		l.setTranslateX(130);
		l.setTranslateY(-240);
		
		root.getChildren().add(l);
		
		prgListSizeView.setTranslateX(240);
		prgListSizeView.setTranslateY(-240);
		prgListSizeView.setMaxWidth(50);
		
		root.getChildren().add(prgListSizeView);
		
	}

	@Override
	public void update() throws CtrlException {
		
		int index = this.prg.getSelectionModel().getSelectedIndex();
		prgListSizeView.setText(String.valueOf(prg.getItems().get(index).getPrgList().size()));
		
	}
	
	
	
}
