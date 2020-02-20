package com.mfl.sem.classifier.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mfl.sem.classifier.Classifier;
import com.mfl.sem.classifier.exception.ClassifierException;
import com.mfl.sem.classifier.policy.SelectionPolicy;
import com.mfl.sem.model.Dataset;
import com.mfl.sem.model.ScoredItem;

import lombok.Builder;
import lombok.Data;
import no.uib.cipr.matrix.sparse.SparseVector;

@Data
@Builder
public  class EnsembleClassifier implements Classifier{
	
	
	private Classifier[] group;
	
	private int size;
	
	private SelectionPolicy selectionPolicy;
	
	@Data
	public class TrainThread extends Thread {
		private Classifier classifier;
	    private Dataset problem;
		public TrainThread(Classifier c, Dataset problem) {
			this.classifier=c;
			this.problem=problem;
		}
	
	    public void run() {
	    	try {
				this.classifier.train(this.problem);
			} catch (ClassifierException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	@Data
	public class ClassifyThread extends Thread {
		private Classifier classifier;
	    private SparseVector item;
		private List<ScoredItem> result;
		public ClassifyThread(Classifier c, SparseVector item) {
			this.classifier=c;
			this.item=item;
		}
	
	    public void run() {
	    
				result=this.classifier.classify(this.item);
			
	    }
	    
	}
	
	
    
	@Override
	public Classifier train(Dataset ... problems) throws ClassifierException {
		int i=0;
		TrainThread tr[]= new TrainThread[this.size];
		for( Classifier c:group) {
			
			//c.train(problems[i]);
			 tr[i] = new TrainThread(c,problems[i]);
			 tr[i].start();
			i++;
		}
		for(int j =0;j<this.size;j++) {
		 try {
			
			tr[j].join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		}
		return this;
	}

	@Override
	public Classifier train(Map<Integer, Integer> coverage,Dataset ...problems  ) throws ClassifierException {
		int i=0;
		for( Classifier c:group) {
			c.train(coverage,problems[i]);
			i++;
		}
		return this;
	}

	@Override
	public List<ScoredItem> classify(SparseVector item) {
		@SuppressWarnings("unchecked")
		List<ScoredItem> [] results= (ArrayList<ScoredItem> []) new ArrayList [this.getSize()]; 
		int i=0;
		ClassifyThread tr[]= new ClassifyThread[this.size];
		for( Classifier c:group) {
			tr[i] = new ClassifyThread(c,item);
			//results[i]=c.classify(item);
			tr[i].start();
			i++;
		}
		for(int j =0;j<this.size;j++) {
			 try {
				
				tr[j].join();
				results[j]=tr[j].getResult();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			}
		return this.getSelectionPolicy().select(results);
	}

}
