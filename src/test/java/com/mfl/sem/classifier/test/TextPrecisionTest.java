package com.mfl.sem.classifier.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.junit.Test;

import com.mfl.sem.classifier.exception.ClassifierException;
import com.mfl.sem.classifier.impl.SVMClassifier;
import com.mfl.sem.classifier.performance.Precision;
import com.mfl.sem.classifier.performance.TextPrecision;
import com.mfl.sem.classifier.text.Documents;
import com.mfl.sem.classifier.text.impl.TextClassifierImpl;
import com.mfl.sem.dataset.reader.News20Reader;
import com.mfl.sem.model.Dataset;

public class TextPrecisionTest {
	
	@Test
	  public void testNewsReaderSVM() throws IOException, ClassifierException {
		  Documents documents = new News20Reader("/Users/mariolocci/Downloads/20news-18828/");
		  Analyzer analyzer = new StandardAnalyzer();
		  SVMClassifier svm = new SVMClassifier();
		  TextClassifierImpl textClassifier= new TextClassifierImpl(analyzer, svm);
		 
		
		 
		  TextPrecision precision = TextPrecision.builder().documents(documents).textClassifier(textClassifier).build();
		  precision.split(0.6d);
		  double result=precision.calculate();
		  assertEquals(result,0.891,0.01);
	  }
	
	public Analyzer produces(){
		Analyzer analyzer;
		Map<String, Analyzer> mapanalyzer=new HashMap<String,Analyzer>();
    	// { "id", "url", "title", "description", "authors", "type", "source_id", "internal_id",
			//	"publishDate", "links", "movies", "gallery", "attachments", "podcasts", "score", "neoid", "entities",
			//	"categories", "trainable" };
    	mapanalyzer.put("url", new StopAnalyzer());
    	mapanalyzer.put("title", new StandardAnalyzer());
    	mapanalyzer.put("description", new StandardAnalyzer());
    	
		analyzer= new PerFieldAnalyzerWrapper(new EnglishAnalyzer(),mapanalyzer);
		
		return analyzer;
	}

}
