package com.mfl.sem.classifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.Node;

import com.crs4.sem.neo4j.service.TaxonomyService;
import com.mfl.sem.classifier.model.Category;
import com.mfl.sem.classifier.model.CategoryDictionary;

public class HClassifierBuilder<T extends Classifier> {

	private int [][] coverage;
	
	private CategoryDictionary categoryBuilder;
	private Classifier classifier;
	private int label;
	private Node root;
	private TaxonomyService taxonomyService;
	private Class<T> species;

	public static HClassifierBuilder builder() {
		return new HClassifierBuilder();
	}

	public HClassifierBuilder<T> root(Node root) {
		this.root = root;
		return this;
	}

	public HClassifierBuilder<T> species(Class<T> species) {
		this.species = species;
		return this;
	}

	public HClassifierBuilder<T> taxonomyService(TaxonomyService taxonomyService) {
		this.taxonomyService = taxonomyService;
		return this;
	}

	public HClassifier<T> build() throws InstantiationException, IllegalAccessException {
		
		List<Node> children = taxonomyService.getChildren(root);
		Category category = categoryBuilder.newCategory(taxonomyService.getCategory(root));
		
		if (children.size() > 0) {
			HClassifier<T> hclassifier = new HClassifier<T>();
			hclassifier.setCategory(category.getCode());
			hclassifier.setCategoryDictionary(categoryBuilder);
			Map<Integer,Integer> coverage = buildcoverage( children, taxonomyService,categoryBuilder);
			
			hclassifier.setCoverage(coverage);
			List<HClassifier<?>> list_child= new ArrayList<HClassifier<?>>();
			for(Node child:children) {
				HClassifier<T> current = HClassifierBuilder.builder().categoryBuilder(categoryBuilder).root(child)
						.taxonomyService(taxonomyService).species(species).build();
				if( current!=null)
					list_child.add(current);
			}
			hclassifier.setChildren(list_child);
			if(children.size()>1)
				hclassifier.setClassifier(this.species.newInstance());
			return hclassifier;
		
		}
		else {
			return null;
		}
		
		
		}

	public static Map<Integer, Integer> buildcoverage( List<Node> children, TaxonomyService taxonomyService, CategoryDictionary categoryBuilder) {
		Map<Integer,Integer> coverage= new HashMap<Integer,Integer>();
		
		
		for (Node child : children) {

			String label_parent = taxonomyService.getCategory(child);
			String[] string_labels = taxonomyService.branchLabels(child, true);
			
			Category cat_parent = categoryBuilder.newCategory(label_parent);
			for (int j = 0; j < string_labels.length; j++) {
				Category cat = categoryBuilder.newCategory(string_labels[j]);
				
				coverage.put(cat.getCode(), cat_parent.getCode());
				//System.out.println(cat.getCode() +" "+cat_parent.getCode());
			}
			
			
		}
		return coverage;
	}

		
	

	public HClassifierBuilder categoryBuilder(CategoryDictionary categoryBuilder) {
		this.categoryBuilder = categoryBuilder;
		return this;
	}

}
