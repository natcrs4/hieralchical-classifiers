package com.mfl.sem.classifier.model;

import java.util.HashMap;


import lombok.Data;

@Data
public class Dictionary {
	private HashMap<String, Term> dict = new HashMap<String, Term>();
	private int size;
	

	public Dictionary(){
		this.setDict(new HashMap<String, Term>());
		this.setSize(0);
	}
	private void addToDictionary(Term term){
		Term t=this.getDict().get(term.getForma());
		if(t==null) this.getDict().put(term.getForma(), term);
		
	}
	
	public  Term newTerm(String forma){
		Term t=this.getDict().get(forma);
		if(t==null) {
			t=new Term(forma,size++);
			this.getDict().put(forma,t );
		}
		
		return t;
	}

	public  Term get(String forma){
		Term t=this.getDict().get(forma);
		return t;
	}

	
	public int size(){
		return size;
	}
}
