package com.mfl.sem.classifier;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.SerializationUtils;

import com.mfl.sem.classifier.exception.ClassifierException;
import com.mfl.sem.classifier.exception.ZeroInstanceException;
import com.mfl.sem.classifier.model.Category;
import com.mfl.sem.classifier.model.CategoryDictionary;
import com.mfl.sem.model.Dataset;
import com.mfl.sem.model.ScoredItem;

import lombok.Data;
import no.uib.cipr.matrix.sparse.SparseVector;

@Data
public class HClassifier<T extends Classifier> implements Classifier {

	private Map<Integer, Integer> coverage;
	private List<HClassifier<?>> children;
	private Classifier classifier;
	private int category;
	private CategoryDictionary categoryDictionary;

	public Classifier train(Dataset train) throws ClassifierException {
		if (this.getCoverage().size() > 1) {
			// System.out.println(Arrays.toString(this.coverage));
			Map<Integer,Integer> newcov= this.add(this.getCoverage(),this.getCategory());
			classifier.train(train, newcov);
			for (int j = 0; j < children.size(); j++)
				try {
					children.get(j).train(train);
				} catch (ZeroInstanceException e) {

				}

		}
		return this;
	}

	public Map<Integer, Integer> add(Map<Integer, Integer> coverage, int category) {
		Map<Integer,Integer> newmap= new HashMap<Integer,Integer>();
		for(Entry<Integer, Integer> e:coverage.entrySet())
			newmap.put(e.getKey(), e.getValue());
		newmap.put(category, category);
		return newmap;
	}

	public Classifier train(Dataset problem, Map<Integer, Integer> coverage) throws ClassifierException {

		return this.getClassifier().train(problem, coverage);
	}

	public List<ScoredItem> classify(SparseVector item) {
		List<ScoredItem> rs = this.classifier.classify(item);
		if (this.getChildren() == null)
			return rs;
		for (HClassifier<?> child : this.getChildren()) {
			if (child.getCategory() == rs.get(0).getIndex()) {
				if (child.getCoverage().size() > 1)
					rs = child.classify(item);

				else {
					int current_cat = this.getCategory();
					rs.get(0).setIndex(current_cat);
					break;
				}
			}
		}
		return rs;
	}

	public String toString() {
		if (this.getCategoryDictionary() != null) {
			String result = this.getCategoryDictionary().getCategory(this.getCategory()).getLabel();
			for (HClassifier<?> node : this.getChildren())
				result += "" + node;
			if (this.getChildren() != null) {
				String aux = "";
				Collection<Integer> v = this.getCoverage().values();
				for (Integer i : v) {
					aux +=(  i==(v.size()-1))?this.getCategoryDictionary().getCategory(i).getLabel():(this.getCategoryDictionary().getCategory(i).getLabel()+",");

				}
				result += " <" + aux + ">";
			}

			return " [ " + result + " ]";
		} else
			return super.toString();

	}

}
