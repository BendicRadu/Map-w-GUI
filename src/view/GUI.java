package view;



import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import controller.Ctrl;
import exceptions.CtrlException;
import exceptions.RepoException;
import exceptions.StackException;
import expressions.ConstExp;
import expressions.ReadHeap;
import expressions.VarExp;
import guiUtils.ChoiceBoxView;
import guiUtils.FileView;
import guiUtils.GuiView;
import guiUtils.HeapView;
import guiUtils.OutView;
import guiUtils.PrgListSizeView;
import guiUtils.StackListView;
import guiUtils.SymTableView;
import guiUtils.PrgListView;

import guiUtils.TableColumnsView;
import javafx.application.Application;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.IprgState;
import model.PrgState;
import repository.IRepo;
import repository.Repo;
import statements.AssignmentStmt;
import statements.CompoundStmt;
import statements.IStatement;
import statements.New;
import statements.PrintStmt;
import statements.WriteHeap;
import utils.ImyDict;
import utils.ImyList;
import utils.ImyStack;
import utils.ImyTuple;
import utils.MyDict;
import utils.MyHDict;
import utils.MyList;
import utils.MyStack;
import utils.MyTDict;

public class GUI extends Application {
    
	Examples ex;
	
	ObservableList<Ctrl> exampleList;
	ObservableList<IStatement> stkList;
	
	boolean updateFlag = false;
	
	Integer curPrgListIndex = 0;
	
	Integer curPrgIndex = 0;
	Ctrl curPrg;
	
	StackPane root;
	StackPane rootSelect;
	
	ChoiceBox<Ctrl> prg = new ChoiceBox<Ctrl>();
	
	ListView<IStatement> stackView = new ListView<IStatement>();
	ListView<Integer> outView = new ListView<Integer>();
	ListView<IprgState> prgListView = new ListView<IprgState>();
	
	TableView<TableColumnsView<String, Integer>> symView = new TableView<TableColumnsView<String, Integer>>();
	TableView<TableColumnsView<Integer, String>> fileView = new TableView<TableColumnsView<Integer, String>>();
	TableView<TableColumnsView<Integer, Integer>> heapView = new TableView<TableColumnsView<Integer, Integer>>();
	
	TextField prgListSize = new TextField();
	
	
	//V - view
	GuiView stkV;
	GuiView choiceV;
	GuiView symV;
	GuiView fileV;
	GuiView heapV;
	GuiView outV;
	GuiView prgListV;
	GuiView prgListSizeV;
	
	public static void main(String[] args){
		
        launch(args);
    }
	
	
	public void prgEnd(){
		try {
			
			Ctrl c = prg.getItems().get(this.curPrgIndex);
			
			List<IprgState> tmpList = c.getPrgList();
		
			tmpList.stream().
			forEach(p -> p.getFileTable().entrySet().stream()
				.forEach(s->{
					try {
						s.getValue().getSecond().close();
					} catch (IOException e) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Done!");
						alert.setHeaderText("The selected program has terminared it's execution");
						alert.setContentText(e.getMessage());
						alert.showAndWait();
					}
				})
				);
			
		} catch (CtrlException e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Done!");
			alert.setHeaderText("The selected program has terminared it's execution");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}
	
	
	public void createPrgChoice(){

		prg.getSelectionModel().selectedIndexProperty().addListener(
				
				e->{		
					this.curPrgIndex = prg.getSelectionModel().getSelectedIndex();
					this.curPrg = prg.getSelectionModel().getSelectedItem();
					this.curPrgListIndex = 0;
					this.update(true);
				}
		);
	}
	
	public void createPrgListChoice(){
		
		prgListView.getSelectionModel().selectedIndexProperty().addListener(			
				e->{		
					
					
					this.curPrgListIndex = prgListView.getSelectionModel().getSelectedIndex();
					this.update(false);
				
				}
		);
	}
	
	
	public void createLabels(){}
	
	public void createButtons(){
		
		Button b = new Button("One step");
		
		b.setOnAction(e -> {
		            		
							
							try {
								this.exampleList.get(this.curPrgIndex).oneStep();
								this.update(true);
							
							}catch (CtrlException e1) {
								Alert alert = new Alert(AlertType.INFORMATION);
								alert.setTitle("Program error");
								alert.setHeaderText("The selected program has encountered an error during it's execution");
								alert.setContentText(e1.getMessage());
								alert.showAndWait();
									
							}catch (IndexOutOfBoundsException e2){
								Alert alert = new Alert(AlertType.INFORMATION);
								alert.setTitle("Selection Error");
								alert.setHeaderText("No program or no thread has been selected");
								alert.setContentText(e2.getMessage());
								alert.showAndWait();
							} finally{
								this.prgEnd();
							}
				
							
		            }
		    );
		
		b.setTranslateX(0);
		b.setTranslateY(-240);
		
		root.getChildren().add(b);
		
	}
	
	public void update(boolean updateFlag){
		
		try {
			
			stkV.update();
			symV.update();
			fileV.update();
			heapV.update();
			outV.update();

			if(updateFlag){
				prgListV.update();
				prgListSizeV.update();
			}
				
		}catch (CtrlException e1) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Done!");
			alert.setHeaderText("The selected program has terminared it's execution");
			alert.setContentText(e1.getMessage());
			alert.showAndWait();
				
		}catch (IndexOutOfBoundsException e2){
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Selection Error");
			alert.setHeaderText("No program or no thread has been selected");
			alert.setContentText(e2.getMessage());
			alert.showAndWait();
		
		} catch (StackException e3) {
		
			
		}finally{
			this.prgEnd();
		}
	}
	
	
    
    @Override
    public void start(Stage primaryStage) {
        
    	primaryStage.setTitle("Interpreter");
    	

        Stage secondStage = new Stage();
        secondStage.setTitle("Program Select");
    	
        root = new StackPane();
    	rootSelect = new StackPane();
        
        
    	this.ex = new Examples();
    	this.exampleList = ex.get();
    	
    	try{
	        
    		choiceV = new ChoiceBoxView(rootSelect, prg, exampleList); 		
    		prgListV = new PrgListView(root, prgListView, prg, exampleList, curPrgIndex);
	        
	        stkV = new StackListView(root, stackView, exampleList, prg, prgListView, curPrgIndex);
	        symV = new SymTableView(root, symView, exampleList, prg, prgListView, curPrgIndex);
	        fileV = new FileView(root, fileView, exampleList, prg, curPrgIndex);
	        heapV = new HeapView(root, heapView, exampleList, prg, curPrgIndex);
	        outV = new OutView(root, outView, exampleList, prg, curPrgIndex);
	        prgListSizeV = new PrgListSizeView(root, prgListSize, exampleList, prg, curPrgIndex);
    	
	        
    	}catch (CtrlException e){
    		Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Program error");
			alert.setHeaderText("The selected program has encountered an error during it's execution");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
			
    	} catch (StackException e1) {
    		Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Program Completed");
			alert.setHeaderText(e1.getMessage());
			alert.showAndWait();
		}
    	
    	
        this.createPrgChoice();
        this.createPrgListChoice();
        this.createLabels();
        this.createButtons();
        

        primaryStage.setScene(new Scene(root, 1500, 800));
        secondStage.setScene(new Scene(rootSelect, 1000, 100));
        
        primaryStage.show();
        secondStage.show();
    }
}