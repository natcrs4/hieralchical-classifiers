package com.mfl.sem.classifier.performance;

import java.util.List;

import com.mfl.sem.classifier.Classifier;
import com.mfl.sem.classifier.exception.ClassifierException;
import com.mfl.sem.model.Dataset;
import com.mfl.sem.model.ScoredItem;

import lombok.Data;
import no.uib.cipr.matrix.sparse.SparseVector;

@Data
public class Precision {
	private Dataset problem;
	private Classifier classifier;
	
	public Precision(Dataset problem, Classifier classifier) {
		this.setClassifier(classifier);
		this.setProblem(problem);
	}
    public double calculate() throws ClassifierException {
    	double result=0d;
    	Dataset[] splitted = this.getProblem().split(0.6d);
    	this.getClassifier().train(splitted[0]);
    	int size=splitted[1].getData().length;
    	for(int k=0;k<size;k++) {
    		SparseVector item = splitted[1].getData()[k];
    		List<ScoredItem> res = this.classifier.classify(item);
    		if(res.get(0).getIndex()==splitted[1].getCategories()[k])
    			result++;
    	}
    	result=result/size;
    	return result;
    }
}
