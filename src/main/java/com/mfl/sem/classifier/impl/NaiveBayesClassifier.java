package com.mfl.sem.classifier.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mfl.sem.classifier.Classifier;
import com.mfl.sem.classifier.model.Category;
import com.mfl.sem.classifier.model.CategoryDictionary;
import com.mfl.sem.model.Dataset;
import com.mfl.sem.model.ScoredItem;

import lombok.Data;
import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.VectorEntry;
import no.uib.cipr.matrix.sparse.SparseVector;

@Data
public class NaiveBayesClassifier implements Classifier {
	
	private Dataset problem;
	private Vector wordcategorycount[];
	private int documentperword[];
	private int documentlength[];
	private int complementarydocumentlength[];
	private Vector worddocument[];
	private double alphai=0.0001d;
	private double alpha0=0.0d;
	

	@Override
	public Classifier train(Dataset problem) {
		this.setProblem(problem);
		this.worddocument=new SparseVector[problem.getData().length];
		this.wordcategorycount= new SparseVector[problem.getCategorysize()];
		this.documentperword=new int[problem.getFeaturesize()];
		this.documentlength=new int[problem.getData().length];
		this.complementarydocumentlength=new int[problem.getData().length];
		for(int j=0;j<problem.getCategorysize();j++)
			this.wordcategorycount[j]= new SparseVector(problem.getFeaturesize());
		
		for(int k=0;k<problem.getFeaturesize();k++)
			this.documentperword[k]= 0;
		
		
		for(int i=0;i<problem.getData().length;i++) {
			   worddocument[i]= new SparseVector(problem.getFeaturesize());
		       Vector doc = problem.getData()[i];
		       Iterator<VectorEntry> iterator = doc.iterator();
		       int len=0;
		       while(iterator.hasNext()) {
		    	   VectorEntry dij = iterator.next();		    	  
		    	   len+=dij.get();
		    	  worddocument[i].set(dij.index(),Math.log(dij.get()+1)); //log(dij+1)
		    	
		       }
		       
		       Iterator<VectorEntry> itr = doc.iterator();
		       while(itr.hasNext()) {
		    	   VectorEntry cell = itr.next();
		    	   if(cell.get()>0) documentperword[cell.index()]++;
		       }
		    	 documentlength[i]=len; 
		}  
		
		for(int i=0;i<problem.getData().length;i++) {
			Vector doc = worddocument[i];
		       Iterator<VectorEntry> iterator = doc.iterator();
		       while(iterator.hasNext()) {
		    	   VectorEntry dij = iterator.next();
		    	   dij.set(dij.get()*Math.log(1.0d/documentperword[dij.index()])); //dij=dij*IDF
		    	   dij.set(dij.get()/(documentlength[i])); //
		       }
		}
	   for(int j=0;j<problem.getCategorysize();j++) {  
		for(int i=0;i<problem.getData().length;i++) {
			  Vector doc = worddocument[i];
	       int cat=problem.getCategories()[i];
	        if(j !=cat ) {
	        	wordcategorycount[j].add(doc);
	         this.complementarydocumentlength[j]+=this.documentlength[j];
	       }
	    	   
	    
		}
	   }
		 for(int j=0;j<problem.getCategorysize();j++) { 
	    		
    		 Iterator<VectorEntry> iterator = wordcategorycount[j].iterator();
		       while(iterator.hasNext()) {
		    	   VectorEntry dij = iterator.next();
		    	   dij.set((dij.get()+this.alphai)/(complementarydocumentlength[j])+this.alpha0);
		    	   
		       }
     }
		return this;
	}

	

	@Override
	public List<ScoredItem> classify(SparseVector document) {
		double probmin=Double.MAX_VALUE;
		List<ScoredItem> list= new ArrayList<ScoredItem>();
		int catmin;
		for(int j=0;j<problem.getCategorysize();j++) { 
			  double probability=0;
			  
			 Iterator<VectorEntry> iterator = document.iterator();
		       while(iterator.hasNext()) {
		    	   VectorEntry word = iterator.next();
		    	  double coeff=wordcategorycount[j].get(word.index());
		    	  probability+=coeff*word.get();
		       }
		       if(probmin>probability) {
		    	   probmin=probability;
		    	   catmin=j;
		       }
		       ScoredItem scored = new ScoredItem();
		       scored.setScore(probability);
		       scored.setIndex(j);
		       list.add(scored);
		}
		Collections.sort(list);
		return list;
	}



	@Override
	public Classifier train(Dataset problem, Map<Integer, Integer> coverage) {
		// TODO Auto-generated method stub
		return null;
	}

}
