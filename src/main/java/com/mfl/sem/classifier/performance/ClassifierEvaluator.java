package com.mfl.sem.classifier.performance;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;

import com.crs4.sem.model.Documentable;
import com.mfl.sem.classifier.model.Category;
import com.mfl.sem.classifier.text.DocItem;
import com.mfl.sem.classifier.text.Documents;
import com.mfl.sem.classifier.text.TextClassifier;
import com.mfl.sem.model.ScoredItem;

import lombok.Data;
import lombok.extern.log4j.Log4j;

@Data
//@Log4j
public class ClassifierEvaluator  implements Evaluator{
	
	public ClassifierEvaluator(PMeasure pmeasure){
		this.setPmeasure(pmeasure);
		this.setMaxdoc(Long.MAX_VALUE);
	}
	
	public ClassifierEvaluator(PMeasure pmeasure,Long maxdoc){
		this.setPmeasure(pmeasure);
		this.setMaxdoc(maxdoc);
	}
	
	private PMeasure pmeasure;
	private Long maxdoc;

	public void addDataset(Documents documents, TextClassifier classifier) {
		Evaluation evaluation=null;
	    int i=0;
	    Iterator<DocItem> iterator = documents.iterator();
		while(iterator.hasNext()&&i<this.getMaxdoc()){
			Documentable doc = iterator.next().get();
			List<ScoredItem> categories;
			try {
				categories = classifier.classify(doc);
				this.getPmeasure().add(categories,doc.getCategories());
				i++;
				//log.debug("added the "+i+"-th realization");
			} catch (IOException e) {
				//log.error(""+e);
			}
			
			}	
	
	}

	
	public double evaluate(Category cat) {
		
		return this.getPmeasure().measure(cat);
	}

	public double evaluate() {
		return this.getPmeasure().measure();
	}
   
	public int total(Category cat){
		return this.getPmeasure().total(cat);
	}

	



	

}
