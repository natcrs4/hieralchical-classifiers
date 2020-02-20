package com.mfl.sem.classifier.text.impl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.junit.Test;

import com.mfl.sem.classifier.exception.ClassifierException;
import com.mfl.sem.classifier.impl.EnsembleClassifier;
import com.mfl.sem.classifier.impl.SVMClassifier;
import com.mfl.sem.classifier.model.CategoryDictionary;
import com.mfl.sem.classifier.model.Dictionary;
import com.mfl.sem.classifier.performance.TextPrecision;
import com.mfl.sem.classifier.policy.MajoritySelection;
import com.mfl.sem.classifier.text.Documents;
import com.mfl.sem.dataset.reader.News20Reader;


public class EnsebleTextClassifierTest {

	@Test
	  public void testNewsReaderSVM() throws IOException, ClassifierException {
		  Documents documents = new News20Reader("/Users/mariolocci/Downloads/20news-18828/");
		  Analyzer analyzer = new StandardAnalyzer();
		  SVMClassifier svm [] = new SVMClassifier[10];
		  for(int i=0;i<10;i++)
			  svm[i]= new SVMClassifier();
		  EnsembleClassifier ensemble = EnsembleClassifier.builder().size(10).group(svm).selectionPolicy(new MajoritySelection()).build();
		  EnsembleTextClassifier textClassifier= new EnsembleTextClassifier(10,ensemble,analyzer);
		 
		  TextPrecision precision = TextPrecision.builder().documents(documents).textClassifier(textClassifier).build();
		  precision.split(0.6d);
		  double result=precision.calculate();
		  assertEquals(result,0.891,0.01);
	  }
}
