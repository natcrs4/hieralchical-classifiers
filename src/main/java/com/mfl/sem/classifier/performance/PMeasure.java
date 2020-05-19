package com.mfl.sem.classifier.performance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mfl.sem.classifier.model.Category;
import com.mfl.sem.model.ScoredItem;

import lombok.Data;

// TODO: Auto-generated Javadoc
/**
 * Instantiates a new p measure.
 */

/*
 * (non-Javadoc)
 * 
 * @see java.lang.Object#toString()
 */
@Data
public abstract class PMeasure {

	/** The map of true positive related to category */
	public Map<String, Integer> tp;

	/** Map of false positive related to category */
	public Map<String, Integer> fp;

	/** Map of false negative related to category */
	public Map<String, Integer> fn;

	/** Total expected result related to a category */
	public Map<String, Integer> total;

	public Set<Category> categories;

	/**
	 * Instantiates a new p measure.
	 *
	 * @param limit
	 *            the limit
	 */
	public PMeasure(int limit, Set<Category> categories) {
		this.setLimit(limit);
		this.setTotal(new HashMap<String, Integer>());
		this.setTp(new HashMap<String, Integer>());
		this.setFp(new HashMap<String, Integer>());
		this.setFn(new HashMap<String, Integer>());
		this.setCategories(categories);
	}
	public PMeasure(PMeasure pmeasure){
		this.setLimit(pmeasure.getLimit());
		this.setTotal(pmeasure.getTotal());
		this.setTp(pmeasure.getTp());
		this.setFp(pmeasure.getFp());
		this.setFn(pmeasure.getFn());
		this.setCategories(pmeasure.getCategories());
	}

	/** The limit. */
	public int limit;

	/**
	 * Adds the.
	 *
	 * @param actual
	 *            the actual
	 * @param expected
	 *            the expected
	 */
	public void add(List<ScoredItem> actual, String  [] expected) {
		int min = Math.min(this.getLimit(), expected.length);
		Set<String> set_actual = this.convertToSet(actual,this.getLimit());
		Set<String> set_expected = this.convertToSet(expected,this.getLimit());
		for (int i = 0; i < min; i++) {
			String exp = expected[i];
			this.increment(exp, this.getTotal());
			if (set_actual.contains(exp))
				this.increment(exp, this.getTp());
			else
				this.increment(exp, this.getFn());
		}
		min = Math.min(this.getLimit(), actual.size());
		for (int i = 0; i < min; i++) {
			ScoredItem act = actual.get(i);

			if (!set_expected.contains(act.getLabel()))
				this.increment(act.getLabel(), this.getFp());

		}
	}

	

	private Set<String> convertToSet(List<ScoredItem> actual, int limit) {
		Set<String> result = new HashSet<String>();
		for( int i=0;i<Math.min(actual.size(),this.getLimit());i++){
		    ScoredItem x = actual.get(i);
			String cat = x.getLabel();
			result.add(cat);
		}
		return result;
	}
	
	private Set<String> convertToSet(String [] actual, int limit) {
		Set<String> result = new HashSet<String>();
		for( int i=0;i<Math.min(actual.length,this.getLimit());i++){
		   String x = actual[i];
			result.add(x);
		}
		return result;
	}

	private void increment(String label, Map<String, Integer> map) {
		Integer aux = map.get(label);
		if (aux == null)
			map.put(label, 1);
		else
			map.put(label, aux + 1);

	}

	public int tp(Category category) {
		Integer aux = this.getTp().get(category.getLabel());
		if(aux==null) return 0;
		else return aux;
	}

	public int fp(Category category) {
		Integer aux= this.getFp().get(category.getLabel());
		if(aux==null) return 0;
		else return aux;
	}

	public int fn(Category category) {
		Integer aux=this.getFn().get(category.getLabel());
		if(aux==null) return 0;
		else return aux;
		
	}

	public int total(Category category) {
		Integer aux= this.getTotal().get(category.getLabel());
		if(aux==null) return 0;
		else return aux;
	}

	/**
	 * Measure.
	 *
	 * @return the double
	 */
	public abstract double measure();
	public abstract double measure(Category cat);

}
