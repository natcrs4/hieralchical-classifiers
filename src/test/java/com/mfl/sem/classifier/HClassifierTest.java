package com.mfl.sem.classifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.junit.Test;
import org.neo4j.graphdb.Node;
import org.neo4j.io.fs.FileUtils;

import com.crs4.sem.model.Documentable;
import com.crs4.sem.neo4j.service.TaxonomyCSVReader;
import com.crs4.sem.neo4j.service.TaxonomyService;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mfl.sem.classifier.exception.ClassifierException;
import com.mfl.sem.classifier.impl.SVMClassifier;
import com.mfl.sem.classifier.model.Category;
import com.mfl.sem.classifier.model.CategoryDictionary;
import com.mfl.sem.classifier.text.Documents;
import com.mfl.sem.classifier.text.TextClassifier;
import com.mfl.sem.classifier.text.impl.TextClassifierImpl;
import com.mfl.sem.dataset.reader.DocumentReader;
import com.mfl.sem.model.ScoredItem;
import com.mfl.sem.text.model.Doc;

public class HClassifierTest {
	
  @Test
  public void testHClassifierSVM() throws InstantiationException, IllegalAccessException, IOException, ClassifierException {
	 // TaxonomyCSVReader taxoreader = new TaxonomyCSVReader();
	   File neodirectory = new File("/tmp/test");
	   if(neodirectory.exists())
		   FileUtils.deleteRecursively(neodirectory);;
	   TaxonomyService taxoservice = new TaxonomyService(new File("/tmp/test"));
	   TaxonomyCSVReader.read(new File("src/main/resources/taxo.csv"), taxoservice);
	   Node root = taxoservice.searchCategory("root");
	   CategoryDictionary categoryDictionary = new CategoryDictionary();
	HClassifier<SVMClassifier> hclassifier=HClassifierBuilder.builder()
			.species(SVMClassifier.class)
			.root(root)
			.taxonomyService(taxoservice)
			.categoryBuilder(categoryDictionary).build();
	//Gson gson = new Gson();
	ObjectMapper objectMapper = new ObjectMapper();
	//List<Doc> docs= gson.fromJson(new FileReader("src/test/resources/docs.json"), List.class);
	List<Documentable> docs=objectMapper.readValue(new File("src/test/resources/docs.json"), new TypeReference<List<Doc>>() { });
	Documents docreader= new DocumentReader(docs);
    TextClassifier textClassifier= new TextClassifierImpl(new StandardAnalyzer(), hclassifier,categoryDictionary);
    
	textClassifier.train(docreader);
	Documentable doc=docreader.get(0);
	System.out.println(doc.getCategories()[0]);
	List<ScoredItem> result = textClassifier.classify(doc);
	assertEquals(result.get(0).getLabel(),doc.getCategories()[0]);
	
	 doc=docreader.get(100);
		System.out.println(doc.getCategories()[0]);
	 result = textClassifier.classify(doc);
	assertEquals(result.get(0).getLabel(),doc.getCategories()[0]);
	
	doc=docreader.get(50);
	System.out.println(doc.getCategories()[0]);
	 result = textClassifier.classify(doc);
	assertEquals(result.get(0).getLabel(),doc.getCategories()[0]);
  }
  
 @Test
 public void testBuildCoverage() throws IOException {
	// TaxonomyCSVReader taxoreader = new TaxonomyCSVReader();
	   File neodirectory = new File("/tmp/test");
	   if(neodirectory.exists())
		   FileUtils.deleteRecursively(neodirectory);;
	   TaxonomyService taxoservice = new TaxonomyService(new File("/tmp/test"));
	   TaxonomyCSVReader.read(new File("src/main/resources/taxo.csv"), taxoservice);
	   Node root = taxoservice.searchCategory("root");
	   CategoryDictionary categoryDictionary = new CategoryDictionary();
	   
	   List<Node> children = taxoservice.getChildren(root);
	   Map<Integer, Integer> map = HClassifierBuilder.buildcoverage(children, taxoservice, categoryDictionary);
	    assertEquals(map.size(),32);
	   
	   Node arte = taxoservice.searchCategory("arte");
	   List<Node> children_arte = taxoservice.getChildren(arte);
	   map = HClassifierBuilder.buildcoverage(children_arte, taxoservice, categoryDictionary);
	   Category artecat=categoryDictionary.newCategory("root");
	   assertNull(map.get(artecat.getCode()));
	   
	   Node scienza = taxoservice.searchCategory("scienza e tecnologia");
	   List<Node> children_scienza = taxoservice.getChildren(scienza);
	   map = HClassifierBuilder.buildcoverage(children_scienza, taxoservice, categoryDictionary);
	   Category scienzacat=categoryDictionary.newCategory("scienza e tecnologia");
	   assertNull(map.get(scienzacat.getCode()));
	   
 }

 
@Test
public void serializationDeser() throws InstantiationException, IllegalAccessException, IOException, ClassifierException {
	  //TaxonomyCSVReader taxoreader = new TaxonomyCSVReader();
	   File neodirectory = new File("/tmp/test");
	   if(neodirectory.exists())
		   FileUtils.deleteRecursively(neodirectory);;
	   TaxonomyService taxoservice = new TaxonomyService(new File("/tmp/test"));
	   TaxonomyCSVReader .read(new File("src/main/resources/taxo.csv"), taxoservice);
	   Node root = taxoservice.searchCategory("root");
	   CategoryDictionary categoryDictionary = new CategoryDictionary();
	HClassifier<SVMClassifier> hclassifier=HClassifierBuilder.builder()
			.species(SVMClassifier.class)
			.root(root)
			.taxonomyService(taxoservice)
			.categoryBuilder(categoryDictionary).build();
	 ObjectMapper objectMapper = new ObjectMapper();
    
 	//List<Doc> docs= gson.fromJson(new FileReader("src/test/resources/docs.json"), List.class);
 	List<Documentable> docs=objectMapper.readValue(new File("src/test/resources/docs.json"), new TypeReference<List<Doc>>() { });
 	Documents docreader= new DocumentReader(docs);
     TextClassifier textClassifier= new TextClassifierImpl(new StandardAnalyzer(), hclassifier,categoryDictionary);
     textClassifier.train(docreader);
     
     
     ObjectMapper objectMapper2 = new ObjectMapper();
     objectMapper2.setSerializationInclusion(Include.NON_NULL);
     objectMapper2.writeValue(new File("/tmp/hclassifier.json"), hclassifier);
     
    
     HClassifier<SVMClassifier> revive=objectMapper2.readValue(new File("/tmp/hclassifier.json"), new TypeReference<HClassifier<SVMClassifier>>()
     { });
    assertTrue(revive.getCoverage().equals(hclassifier.getCoverage()));
}
}
