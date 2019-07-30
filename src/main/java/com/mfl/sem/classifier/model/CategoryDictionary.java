package com.mfl.sem.classifier.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class CategoryDictionary {
	private  Integer counter;
	private  Map<String,Category> categories;
	private Map<Integer,Category> indexes;
	
	public CategoryDictionary(){
		 this.setCategories(new HashMap<String,Category>());
		 this.setIndexes(new HashMap<Integer,Category>());
		 this.setCounter(0);
}
	 public  Category newCategory(String label){
		   String  	_label=label.trim().toLowerCase();
		    	Category aux = this.getCategories().get(_label);
		    	if(aux==null){
		    	aux= new Category(counter,_label);
		    	this.getCategories().put(_label, aux);
		    	this.getIndexes().put(counter, aux);
		    	counter++;
		    	}
		    return aux;
		}
	 public int size(){
			return counter;
		}
	public Category getCategory(int i) {
		return this.indexes.get(i);
		
	}
	
	public List<String> labels(){
		return new ArrayList<String>(this.categories.keySet());
	}
		
	

}
