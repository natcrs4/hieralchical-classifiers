package com.mfl.sem.classifier.impl;

import java.util.List;
import java.util.Map;

import com.mfl.sem.classifier.Classifier;
import com.mfl.sem.classifier.exception.ClassifierException;
import com.mfl.sem.model.Dataset;
import com.mfl.sem.model.ScoredItem;

import lombok.Builder;
import lombok.Data;
import no.uib.cipr.matrix.sparse.SparseVector;


@Data
@Builder
public class MultiLabelClassifier implements Classifier{
    private int numlabel;
    private Classifier classifiers[];
	@Override
	public Classifier train(Dataset... problems) throws ClassifierException {
		for( Classifier cl:this.getClassifiers())
			cl.train(problems);
		return this;
	}

	@Override
	public Classifier train(Map<Integer, Integer> coverage, Dataset... problems) throws ClassifierException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ScoredItem> classify(SparseVector item) {
		// TODO Auto-generated method stub
		return null;
	}

}
