package com.mfl.sem.classifier;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.neo4j.graphdb.Node;
import org.neo4j.io.fs.FileUtils;

import com.crs4.sem.neo4j.service.TaxonomyCSVReader;
import com.crs4.sem.neo4j.service.TaxonomyService;
import com.mfl.sem.classifier.impl.NaiveBayesClassifier;
import com.mfl.sem.classifier.model.CategoryDictionary;


public class HClassifierBuilderTest {
	
	@Test
	public void build() throws IOException, InstantiationException, IllegalAccessException {
		
		
		   File neodirectory = new File("/tmp/test");
		   if(neodirectory.exists())
			   FileUtils.deleteRecursively(neodirectory);;
		   TaxonomyService taxoservice = new TaxonomyService(new File("/tmp/test"));
		   TaxonomyCSVReader.read(new File("src/main/resources/taxo.csv"), taxoservice);
		   Node root = taxoservice.searchCategory("root");
		HClassifier<NaiveBayesClassifier> hclassifier=HClassifierBuilder.builder()
				.species(NaiveBayesClassifier.class)
				.root(root)
				.taxonomyService(taxoservice)
				.categoryBuilder(new CategoryDictionary()).build();
		assertNotNull(hclassifier);
	}
	

}
