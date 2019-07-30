package com.mfl.sem.classifier;

import com.mfl.sem.model.Dataset;

import no.uib.cipr.matrix.sparse.SparseVector;

public interface  OneVersusAllClassifier {
	 public Classifier train(Dataset problem, String [] coverage) ;
	 public  Double classify(SparseVector item) ;
}
