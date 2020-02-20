package com.mfl.sem.classifier.performance;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.crs4.sem.model.Documentable;
import com.crs4.sem.neo4j.service.TaxonomyService;
import com.mfl.sem.classifier.exception.ClassifierException;
import com.mfl.sem.classifier.text.Documents;
import com.mfl.sem.classifier.text.TextClassifier;
import com.mfl.sem.model.ScoredItem;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TextPrecision {
	
	private Documents documents;
	private TextClassifier  textClassifier;
	private Documents testset;
	private Documents trainset;

	
	public void split(double perc) {
		Documents[] splitted = this.getDocuments().split(perc);
    	
    	this.setTrainset(splitted[0]);
    	this.setTestset(splitted[1]);
	}
	
	public double calculate() throws ClassifierException, IOException {
    	double result=0d;
    	this.getTextClassifier().train(this.getTrainset());
    	
    	for(int k=0;k<this.getTestset().size();k++) {
    		Documentable item = this.getTestset().get(k);
    		List<ScoredItem> res = this.textClassifier.classify(item);
    		HashSet<String> set = new HashSet<String>(Arrays.asList(this.getTestset().get(k).getCategories()));
    		if(!res.isEmpty()&&set.contains(res.get(0).getLabel()))
    	
    			result++;
    		else 
    			if(res.size()>1&&set.contains(res.get(1).getLabel()))
    	    	 result++;
    	}
    	result=result/this.getTestset().size();
    	return result;
    }

}
