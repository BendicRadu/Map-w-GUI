package guiUtils;

import java.util.Collections;
import java.util.Stack;
import java.util.stream.Collectors;

import controller.Ctrl;
import exceptions.CtrlException;
import exceptions.StackException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import model.IprgState;
import statements.IStatement;

public class StackListView implements GuiView {

	
	ListView<IStatement> stackView;
	StackPane root;
	ObservableList<Ctrl> exampleList;
	ChoiceBox<Ctrl> prg;
	
	ListView<IprgState> prgList;
	
	Integer curPrgIndex;
	
	
	public StackListView(
			StackPane root,
			ListView<IStatement> stackView,
			ObservableList<Ctrl> exampleList, 
			ChoiceBox<Ctrl> prg,
			
			ListView<IprgState> prgList,
			
			Integer curPrgIndex
			) throws CtrlException, StackException
		
		{
			this.root = root;
			this.exampleList = exampleList; 
			this.stackView = stackView;
			this.prg = prg;
			this.curPrgIndex = curPrgIndex;
			this.prgList = prgList;
			
			Stack<IStatement> stk = null;
		
			prg.getItems().get(curPrgIndex);
			stk = exampleList.get(curPrgIndex).getStk(0);
	
			
		
			ObservableList<IStatement> stkList = FXCollections.observableArrayList(
					stk.stream().collect(Collectors.toList())
					);
			
			
			stackView.setItems(stkList);
			

			Label l = new Label("Exe Stack");
			l.setTranslateX(-347);
			l.setTranslateY(-170);

			root.getChildren().add(l);

			
			stackView.setTranslateX(-347);
			stackView.setTranslateY(100);
			stackView.setMaxSize(500, 500);
			
			root.getChildren().add(stackView);
			

			
		}
	
	public void update() throws CtrlException, StackException
	{	
		Stack<IStatement> stk = null;
		
	
		this.curPrgIndex = this.prg.getSelectionModel().getSelectedIndex();
		
		if (prgList.getSelectionModel().getSelectedIndex() == -1){
			prgList.getSelectionModel().select(0);
		}
		
		stk = this.prg.getItems().get(this.curPrgIndex).getStk(prgList.getSelectionModel().getSelectedIndex());
	
		
		ObservableList<IStatement> stkList = FXCollections.observableArrayList(
			 	stk.stream().collect(Collectors.toList())
				);
		
		Collections.reverse(stkList);
		
		this.stackView.setItems(stkList);
	}	
}
