package com.mfl.sem.classifier;

import java.util.List;
import java.util.Map;

import com.mfl.sem.classifier.exception.ClassifierException;
import com.mfl.sem.classifier.model.Category;
import com.mfl.sem.model.Dataset;
import com.mfl.sem.model.ScoredItem;

import no.uib.cipr.matrix.sparse.SparseVector;




public interface Classifier {
	 public Classifier train(Dataset ... problems) throws ClassifierException ;
	 public Classifier train(Map<Integer,Integer>  coverage,Dataset ... problems) throws ClassifierException ;
	 public  List<ScoredItem> classify(SparseVector item) ;
	
}
