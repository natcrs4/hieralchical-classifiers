package com.mfl.sem.classifier.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.junit.Test;

import com.mfl.sem.classifier.exception.ClassifierException;
import com.mfl.sem.classifier.impl.NaiveBayesClassifier;
import com.mfl.sem.classifier.impl.SVMClassifier;
import com.mfl.sem.classifier.performance.Precision;
import com.mfl.sem.classifier.text.Documents;
import com.mfl.sem.classifier.text.impl.TextClassifierImpl;
import com.mfl.sem.dataset.reader.News20Reader;
import com.mfl.sem.model.Dataset;

public class PrecisionTest {
	
  @Test
  public void testNewsReaderBayes() throws IOException, ClassifierException {
	  Documents documents = new News20Reader("/Users/mariolocci/Downloads/20news-18828/");
	  Analyzer analyzer = new StandardAnalyzer();
	  TextClassifierImpl textClassifier= new TextClassifierImpl(analyzer, new NaiveBayesClassifier());
	  Dataset problem = textClassifier.makeProblem(documents);
	
	 
	  Precision precision = new Precision(problem,new NaiveBayesClassifier());
	  double result=precision.calculate();
	  assertEquals(result,0.832,0.01);
  }

  @Test
  public void testNewsReaderSVM() throws IOException, ClassifierException {
	  Documents documents = new News20Reader("/Users/mariolocci/Downloads/20news-18828/");
	  Analyzer analyzer = new StandardAnalyzer();
	  SVMClassifier svm = new SVMClassifier();
	  TextClassifierImpl textClassifier= new TextClassifierImpl(analyzer, svm);
	  Dataset problem = textClassifier.makeProblem(documents);
	  textClassifier.setProblem(problem);
	
	 
	  Precision precision = new Precision(problem,svm);
	  double result=precision.calculate();
	  assertEquals(result,0.891,0.01);
  }
  

  
}
