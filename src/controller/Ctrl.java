package controller;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import exceptions.CtrlException;
import exceptions.RepoException;
import exceptions.StackException;
import model.IprgState;
import repository.IRepo;
import statements.IStatement;
import utils.ImyTuple;

public class Ctrl {
	
	public IRepo r;
	ExecutorService executor;
	
	public Ctrl(IRepo r){
		this.r = r;
	}
	
	public IRepo getRepo(){
		return this.r;
	}
	
	public List<IprgState> getPrgList() throws CtrlException{
		try {
			return r.getPrgList();
		} catch (RepoException e) {
			throw new CtrlException("Controller recieved a repo exception: " + e.getMessage());
		}
	}
	
	Map<Integer,Integer> conservativeGarbageCollector(Collection<Integer> symTableValues, Map<Integer,Integer> heap){
			return heap.entrySet().stream()
			 .filter(e->symTableValues.contains(e.getKey()))
			 .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}
	
	public List<IprgState> removeCompletedPrg(List<IprgState> inPrgList){
		return inPrgList.stream()
				.filter(p -> ! p.isNotCompleted())
				.collect(Collectors.toList());
	}

	
	public void oneStepForAllPrg(List<IprgState> prgList) throws CtrlException, InterruptedException{
		
		
		for(IprgState prg: prgList){
			try {
				r.logPrgStateExec(prg);
			} catch (RepoException e) {
				throw new CtrlException("Controller encountered a repo exception: " + e.getMessage());
			}
		}
		
		List<Callable<IprgState>> callList = prgList.stream()
				.map((IprgState p) -> (Callable<IprgState>)(() -> {
					return p.oneStep();
					}
				))
				.collect(Collectors.toList());
		
		
		
		/*
		List<IprgState> newPrgList = executor.invokeAll(callList).stream()
				 . map(future -> { 
					 	try {
							return future.get();
						} catch (Exception e){
							
							try {
								r.logException(e);
							} catch (RepoException e1) {
								e1.printStackTrace();
							}
							
							return null;
						}
					} 
				 )
				 .filter(p -> p != null)
				 .collect(Collectors.toList());
		*/
		
		List<IprgState> newPrgList = new ArrayList<IprgState>();
		List<Future<IprgState>> execList = executor.invokeAll(callList);
		
		for(Future<IprgState> f: execList){
			try {
				if( f.get() != null ){
					newPrgList.add(f.get());
				}
			} catch (ExecutionException e) {
				throw new CtrlException("Controller recieved an execution exception: " + e.getMessage());
			}
		}
		
		prgList.addAll(newPrgList);
		
		prgList.forEach(prg ->{
			try {
				r.logPrgStateExec(prg);
			} catch (RepoException e) {
				try {
					r.logException(e);
				} catch (RepoException e1) {}
			}
		});
		r.setPrgList(prgList);
		r.setPrgListAux(prgList);
		
	}
	
	public void completeEval() throws CtrlException{
		
		executor = Executors.newFixedThreadPool(2);
	
		List<IprgState> prgList;
		try {
			prgList = removeCompletedPrg( r.getPrgList() );
		} catch (RepoException e) {
			throw new CtrlException("Controller encountered a repo exception: " + e.getMessage());
		}
		while(prgList.size() > 0){
			try {
				this.oneStepForAllPrg(prgList);
			} catch (InterruptedException e) {
				throw new CtrlException("Controller encountered an Interrupted exception: " + e.getMessage());
			}
			try {
				prgList = removeCompletedPrg( r.getPrgList() );
			} catch (RepoException e) {
				throw new CtrlException("Controller encountered a repo exception: " + e.getMessage());
			}
		}
		executor.shutdownNow();
		r.setPrgList(prgList);
		}
	
	public void oneStep() throws CtrlException{
		

		List<IprgState> prgList;
		
		executor = Executors.newFixedThreadPool(2);
		
		this.setLogPath("file");
		
		
		try {
			prgList = r.getPrgList();

		} catch (RepoException e) {
			throw new CtrlException("Controller encountered a repo exception: " + e.getMessage());
		}
		
		
		if(prgList.size() > 0){
			try {
				
				this.oneStepForAllPrg(prgList);
								
			} catch (InterruptedException e) {
				throw new CtrlException("Controller encountered an Interrupted exception: " + e.getMessage());
			}
			
			if (prgList.size() > 1){
				try {
					prgList = removeCompletedPrg( r.getPrgList() );
				} catch (RepoException e) {
					throw new CtrlException("Controller encountered a repo exception: " + e.getMessage());
				}
			}
		}
				
		executor.shutdownNow();
		r.setPrgList(prgList);
	}
	
	public void setPrgList(List<IprgState> l){
		r.setPrgList(l);
	}
	
	public void setLogPath(String path){
		this.r.setLogPath(path);
	}
	
	public String getLogPath(){
		return this.r.getPath();
	}
	
	public void logPrgStateExec(IprgState state) throws CtrlException{
		try {
			this.r.logPrgStateExec(state);
		} catch (RepoException r) {
			throw new CtrlException("Cotroller received a repo exeption: " + r.getMessage());
		}
	} 
	
	public List<IprgState> getPrg() throws CtrlException{
		try {
			return r.getPrgList();
		} catch (RepoException e) {
			throw new CtrlException("Controller recieved a repo exception: " + e.getMessage());
		}
	}
	
	
	public Stack<IStatement> getStk(int i) throws CtrlException, StackException{
		return this.getPrg().get(i).getExeStack().getItems();
	}
	
	public Map<String, Integer> getSym(int i) throws CtrlException{
		return this.getPrg().get(i).getSymTable().getItems();
	}
	
	public Map<Integer, ImyTuple<String, BufferedReader>> getFile(int i) throws CtrlException{
		return this.getPrg().get(i).getFileTable().getItems();
	}
	
	public Map<Integer, Integer> getHeap(int i) throws CtrlException{
		return this.getPrg().get(i).getHeap().getItems();
	}
	
	public List<Integer> getOut(int i) throws CtrlException{
		return this.getPrg().get(i).getOutput().getItems();
	}
	
	@Override
	public String toString(){
		return this.r.toString();
	}
	
	
}
