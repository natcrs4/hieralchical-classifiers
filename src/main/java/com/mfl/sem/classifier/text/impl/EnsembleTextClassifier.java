package com.mfl.sem.classifier.text.impl;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;

import com.crs4.sem.model.Documentable;
import com.mfl.sem.classifier.exception.ClassifierException;
import com.mfl.sem.classifier.impl.EnsembleClassifier;
import com.mfl.sem.classifier.model.CategoryDictionary;
import com.mfl.sem.classifier.model.Dictionary;
import com.mfl.sem.classifier.text.Documents;
import com.mfl.sem.classifier.text.TextClassifier;
import com.mfl.sem.model.Dataset;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
//@Builder(builderMethodName="ebuilder")
@AllArgsConstructor
public class EnsembleTextClassifier extends TextClassifierImpl{
	
	
    private int size;
	
	public TextClassifier train(Documents documents) throws IOException, ClassifierException {
		
		
		Dataset problems[] = makeProblems(documents);	
		this.getClassifier().train(problems);
		return this;
	}

	private Dataset[] makeProblems(Documents documents) throws IOException {
		Dataset [] datasets= new Dataset[this.size];
		
		for(int i=0;i<size;i++)
			{
			  documents.shuffle();
			  Documents splitted[]=documents.split(0.9d);
			  
				datasets[i]= this.makeProblem(splitted[0]);
			
			}
		return datasets;
	}
	public EnsembleTextClassifier(int size, EnsembleClassifier ensemble, Analyzer analyzer) throws IOException {
		
		this.setCategoryDictionary(new CategoryDictionary());
		this.setDictionary(new Dictionary());
		this.setClassifier(ensemble);
		this.setAnalyzer(analyzer);
		this.size=size;
		
	}
}
