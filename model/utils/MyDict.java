package utils;
import java.io.BufferedReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import exceptions.DictException;


public class MyDict<TKey, T> implements ImyDict<TKey, T>{
	protected Map<TKey, T> dict;
	
	public MyDict(){
		dict = new HashMap<TKey, T>();
	}
	
	@Override
	public MyDict<TKey, T> clone(){
		MyDict<TKey, T> clone;
	
		clone = new MyDict<TKey, T>();
		clone.dict = new HashMap<TKey, T>(this.dict);
		return clone;
		
	}
	
	public T get(TKey key) throws DictException{
		T elem = dict.get(key);
		if(elem == null){
			throw new DictException("No such key");
		}
		return elem;
	}
	
	public void setContent(Map<TKey, T> newMap){
		this.dict = newMap;
	}
	
	public Map<TKey, T> getContent(){
		return this.dict;
	}
	
	public Collection<T> values(){
		return dict.values();
	}
	
	public Boolean containsValue(T value){
		return dict.containsValue(value);
	}
	
	public Boolean containsKey(TKey key){
		return dict.containsKey(key);
	}
	
	public Boolean isEmpty(){
		return dict.isEmpty();
	}
	
	public T putIfAbsent(TKey key, T elem) throws DictException{
		
		T elemAux = dict.get(key);
		if(elemAux != null){
			throw new DictException("Key not available");
		}
		
		return dict.putIfAbsent(key, elem);
	}
	
	public T put(TKey key, T elem){
		return dict.put(key, elem);
	}
	
	public TKey putKey(T elem) throws DictException{
		throw new DictException("Method not suported for instance of MyDict");
	}
	
	public void remove(TKey key) throws DictException{
		this.get(key);
		dict.remove(key);
	}
	
	public Iterator<HashMap.Entry<TKey, T>> getIterator(){
		return dict.entrySet().iterator();
	}
	
	public Boolean isInFirst(String elem) throws DictException{
		throw new DictException("Method not suported for instance of MyDict");
	}
	public Boolean isInSecond(BufferedReader elem) throws DictException{
		throw new DictException("Method not suported for instance of MyDict");
	}

	
	public Set<Map.Entry<TKey, T>> entrySet(){
		return this.dict.entrySet();
	}
	
	
	public int generateId(){
		return (int) Math.random();
	}
	
	public Map<TKey, T> getItems(){
		return this.dict;
	}

	
	@Override
	public String toString(){
		
		if(dict.isEmpty()){
			return "SymTable is empty\n\n";
		}
		
		String str = "SymTable:\n";
		for ( Entry<TKey, T> entry : dict.entrySet() ) {
		    TKey key = entry.getKey();
		    T value = entry.getValue();
		    str += key + "-->" + value + "\n";
		}
		str += "\n";
		return str;
	}

}
