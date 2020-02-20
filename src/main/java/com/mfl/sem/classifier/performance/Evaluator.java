package com.mfl.sem.classifier.performance;

import org.apache.lucene.analysis.Analyzer;

import com.mfl.sem.classifier.Classifier;
import com.mfl.sem.classifier.model.Category;
import com.mfl.sem.classifier.text.Documents;
import com.mfl.sem.classifier.text.TextClassifier;



// TODO: Auto-generated Javadoc
/**
 * Class for evaluating the performance over a classifier. Decorates a Performance Measure .
 */
public interface Evaluator {
	
	/**
	 * Adds the.
	 *
	 * @param documents the documents
	 * @param classifier the classifier
	 * @param analyzer the analyzer
	 */
	public void addDataset(Documents documents,TextClassifier classifier);

	/**
	 * Evaluates performance measure over a given category.
	 *
	 * @param cat the cat
	 * @return the double
	 */
	public double evaluate(Category cat);
	
	/**
	 * Evaluates the performance measure over all categories.
	 *
	 * @return the double
	 */
	public double evaluate();
	
	
	public int total(Category cat);
}
