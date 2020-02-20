package com.mfl.sem.classifier.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mfl.sem.classifier.Classifier;
import com.mfl.sem.classifier.exception.ClassifierException;
import com.mfl.sem.classifier.exception.ZeroInstanceException;
import com.mfl.sem.classifier.model.Category;
import com.mfl.sem.model.Dataset;
import com.mfl.sem.model.ScoredItem;
import com.mfl.sem.text.model.Doc;

import de.bwaldvogel.liblinear.Feature;
import de.bwaldvogel.liblinear.FeatureNode;
import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import de.bwaldvogel.liblinear.Parameter;
import de.bwaldvogel.liblinear.Problem;
import de.bwaldvogel.liblinear.SolverType;
import lombok.Data;
import no.uib.cipr.matrix.VectorEntry;
import no.uib.cipr.matrix.sparse.SparseVector;

@Data
public class SVMClassifier implements Classifier {
	//private Parameter parameter; 
	private Model model;

	@Override
	public Classifier train(Dataset ... datasets) throws ClassifierException {
		Dataset dataset= datasets[0];
		int newsize=computeNewSize(dataset.getCategories());
		if(newsize==0) throw new ZeroInstanceException();
		//this.setParameter( new Parameter(SolverType.L2R_LR_DUAL, 0.1, 0.1));
		Problem problem = new Problem();
		problem.bias = -1;
		problem.x = new FeatureNode[newsize][];
		problem.y = new double[newsize];
		problem.l = newsize;
		problem.n = dataset.getFeaturesize() + 1;
		int i = 0;
		int k=0;
		for (SparseVector f : dataset.getData()) {
            if(dataset.getCategories()[i]>=0) {
			problem.x[k] = toFeatureNode(f);
			
			problem.y[k] = dataset.getCategories()[i];
			k++;
            }
            i++;
		}
		Linear.disableDebugOutput();
		this.setModel(Linear.train(problem, new Parameter(SolverType.L2R_LR_DUAL,100, 1, 0.01)));
		return this;
	}

	private int computeNewSize(int categories[] ) {
		int newsize=0;
	    for(int i=0;i<categories.length;i++)
	    	if(categories[i]>=0) newsize++;
	    return newsize;
	}

	private Feature[] toFeatureNode(SparseVector data) {

		int sz = data.getUsed();
		FeatureNode[] features = new FeatureNode[sz];
		Iterator<VectorEntry> iter = data.iterator();
		int i = 0;
		while (iter.hasNext()) {
			VectorEntry doc = iter.next();
			features[i] = new FeatureNode(doc.index()+1, doc.get());
			i++;
		}
        Arrays.sort(features, new Comparator<FeatureNode>() {
	        @Override
	        public int compare(FeatureNode  a, FeatureNode  b)
	        {

	            return  a.getIndex()-b.getIndex();
	        }});
		return features;

	}

	
	

	@Override
	public List<ScoredItem> classify(SparseVector item) {
		Feature[] feature;
		ArrayList<ScoredItem> result = new ArrayList<ScoredItem>();

		feature = toFeatureNode(item);
		double prediction = Linear.predict(model, feature);
		double[] prob_estimates = new double[model.getNrClass()];
		Linear.predictProbability(model, feature, prob_estimates);
		for(int k=0;k<model.getNrClass();k++) {
		ScoredItem scoredItem = new ScoredItem(model.getLabels()[k], prob_estimates[k]);
		result.add(scoredItem);
		
		}
		Collections.sort(result);
		return result;
	}

	@Override
	public Classifier train( Map<Integer,Integer>  coverage,Dataset ... problems) throws ClassifierException {
		Dataset problem= problems[0];
		Dataset dataset = reconfigureDataset(problem, coverage);
		dataset.balance();
		this.train(dataset);
		return this;
	}

	private Dataset reconfigureDataset(Dataset problem, Map<Integer, Integer> coverage) {
		Dataset dataset = new Dataset();
		dataset.setData(problem.getData());
		dataset.setCategories(new int[problem.getData().length]);
		dataset.setFeaturesize(problem.getFeaturesize());
		dataset.setCategorysize(problem.getCategorysize());
		Integer cat;
		int newsize=0;
		for(int i=0;i<problem.getData().length;i++) {
			cat=coverage.get(problem.getCategories()[i]);
		    if(cat==null) { 
		    	//System.out.println(cat + " "+problem.getCategories()[i]);
		    	dataset.getCategories()[i]=-1;
		    }
		    else dataset.getCategories()[i]=cat;
		}
		return dataset;
	}

}
