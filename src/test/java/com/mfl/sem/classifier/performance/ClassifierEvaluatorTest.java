package com.mfl.sem.classifier.performance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.WordlistLoader;
 
import org.junit.Test;

import com.mfl.sem.classifier.exception.ClassifierException;
import com.mfl.sem.classifier.impl.EnsembleClassifier;
import com.mfl.sem.classifier.impl.SVMClassifier;
import com.mfl.sem.classifier.model.Category;
import com.mfl.sem.classifier.model.CategoryDictionary;
import com.mfl.sem.classifier.model.Dictionary;
import com.mfl.sem.classifier.policy.MajoritySelection;
import com.mfl.sem.classifier.text.Documents;
import com.mfl.sem.classifier.text.impl.EnsembleTextClassifier;
import com.mfl.sem.classifier.text.impl.TextClassifierImpl;
import com.mfl.sem.dataset.reader.News20Reader;


public class ClassifierEvaluatorTest {
	
	@Test
	public void testEvaluation() throws FileNotFoundException, IOException, ClassifierException{
		long tot=6000L;
		News20Reader documents = new News20Reader("/Users/mariolocci/Downloads/20news-18828/");
		  Analyzer analyzer = new StandardAnalyzer();
		  SVMClassifier svm = new SVMClassifier();
		  TextClassifierImpl textClassifier= new TextClassifierImpl(analyzer, svm);
		 
		textClassifier.train(documents);
		 
		  //TextPrecision precision = TextPrecision.builder().documents(documents).textClassifier(textClassifier).build();
		  
		Set<Category> set= new HashSet<Category>(documents.getCategoryDictionary().getCategories().values());
		PMeasure macroPrecision = new MacroPrecision(1, set);
		Evaluator evaluator= new ClassifierEvaluator( macroPrecision,tot);
		evaluator.addDataset(documents, textClassifier);
		System.out.println("Macro Precision"+evaluator.evaluate());
		for(Category x:set)
			if(evaluator.total(x)>0) 
			System.out.println(x+ " measure "+ evaluator.evaluate(x)+ " totals "+ evaluator.total(x));
		
		MacroRecall macroRecall = new MacroRecall(macroPrecision);
		evaluator= new ClassifierEvaluator( macroRecall,tot);
		System.out.println("Macro Recall"+ evaluator.evaluate());
		for(Category x:set)
			if(evaluator.total(x)>0) 
				System.out.println(x+ " measure "+ evaluator.evaluate(x)+ " totals "+ evaluator.total(x));
		
		MicroPrecision microPrecision = new MicroPrecision(macroPrecision);
		evaluator= new ClassifierEvaluator( microPrecision,tot);
		System.out.println("Micro Precision"+ evaluator.evaluate());
		for(Category x:set)
			if(evaluator.total(x)>0) 
			System.out.println(x+ " measure "+ evaluator.evaluate(x)+ " totals "+ evaluator.total(x));
		
		MicroRecall microRecall = new MicroRecall(macroPrecision);
		evaluator= new ClassifierEvaluator( microRecall,tot);
		System.out.println("Micro Recall"+ evaluator.evaluate());
		for(Category x:set)
			if(evaluator.total(x)>0) 
			System.out.println(x+ " measure "+ evaluator.evaluate(x)+ " totals "+ evaluator.total(x));
		
	}
	
//	@Test
//	public void testPrecisionWithPosTagger() throws FileNotFoundException, IOException{
//		long tot=1L;
//		ServerConfig serverConfig = ConfigFactory.create(ServerConfig.class);
//		DataSetWikipedia wikidoc= new DataSetWikipedia(serverConfig);
//		Set<Category> set= wikidoc.getCategories();
//		File file = new File("src/test/resources/stopwords_en.txt");
//	
//		String[] stopwords = Analyzers.readStopword(file);
//		
//		PosTagger posTagger = new TreeTagger(serverConfig.treetaggerCommand(),serverConfig.treetaggerModelEN());
//		Analyzer analyzer = new PosTaggerAnalyzer(posTagger, stopwords);
//		
//		PMeasure macroPrecision = new MacroPrecision(1, set);
//		Evaluator evaluator= new ClassifierEvaluator( macroPrecision,tot);
//		Classifier classifier= DensityClassifier
//				.builder()
//				.scoreFunction(new DensityFunction())
//				.semanticNet(new WordNetAdapter(serverConfig)).
//				wordnetCl(new WordNetDomain(serverConfig)).
//				build();
//		
//		
//		evaluator.addDataset(wikidoc, classifier, analyzer);
//		System.out.println("Macro Precision"+evaluator.evaluate());
//		for(Category x:set)
//			if(evaluator.total(x)>0) 
//			System.out.println(x+ " measure "+ evaluator.evaluate(x)+ " totals "+ evaluator.total(x));
//		MacroRecall macroRecall = new MacroRecall(macroPrecision);
//		evaluator= new ClassifierEvaluator( macroRecall,tot);
//		System.out.println("Macro Recall"+ evaluator.evaluate());
//		for(Category x:set)
//			if(evaluator.total(x)>0) 
//				System.out.println(x+ " measure "+ evaluator.evaluate(x)+ " totals "+ evaluator.total(x));
//		
//		MicroPrecision microPrecision = new MicroPrecision(macroPrecision);
//		evaluator= new ClassifierEvaluator( microPrecision,tot);
//		System.out.println("Micro Precision"+ evaluator.evaluate());
//		for(Category x:set)
//			if(evaluator.total(x)>0) 
//			System.out.println(x+ " measure "+ evaluator.evaluate(x)+ " totals "+ evaluator.total(x));
//		
//		MicroRecall microRecall = new MicroRecall(macroPrecision);
//		evaluator= new ClassifierEvaluator( microRecall,tot);
//		System.out.println("Micro Recall"+ evaluator.evaluate());
//		for(Category x:set)
//			if(evaluator.total(x)>0) 
//			System.out.println(x+ " measure "+ evaluator.evaluate(x)+ " totals "+ evaluator.total(x));
//	}

}
