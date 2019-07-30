package com.mfl.sem.classifier.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mfl.sem.classifier.Classifier;
import com.mfl.sem.classifier.exception.ClassifierException;
import com.mfl.sem.classifier.policy.SelectionPolicy;
import com.mfl.sem.model.Dataset;
import com.mfl.sem.model.ScoredItem;

import lombok.Data;
import no.uib.cipr.matrix.sparse.SparseVector;

@Data
public abstract class EnsembleClassifier implements Classifier{
	
	
	private Classifier[] group;
	
	private int size;
	
	private SelectionPolicy selectionPolicy;
	
	

	@Override
	public Classifier train(Dataset problem) throws ClassifierException {
		for( Classifier c:group)
			c.train(problem);
		return this;
	}

	@Override
	public Classifier train(Dataset problem, Map<Integer, Integer> coverage) throws ClassifierException {
		
		for( Classifier c:group)
			c.train(problem,coverage);
		return this;
	}

	@Override
	public List<ScoredItem> classify(SparseVector item) {
		@SuppressWarnings("unchecked")
		List<ScoredItem> [] results= (ArrayList<ScoredItem> []) new ArrayList [this.getSize()]; 
		int i=0;
		for( Classifier c:group) {
			results[i]=c.classify(item);
			i++;
		}
		return this.getSelectionPolicy().select(results);
	}

}
