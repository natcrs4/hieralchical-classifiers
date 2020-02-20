package com.mfl.sem.dataset.reader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import com.crs4.sem.model.Documentable;
import com.mfl.sem.classifier.model.CategoryDictionary;
import com.mfl.sem.classifier.text.Documents;
import com.mfl.sem.text.model.Doc;

import lombok.Data;


@Data
public class News20Reader extends Documents{
//public static final Logger logger = Logger.getLogger(News20Reader.class);
public Map<String,String> classAssociation;
public CategoryDictionary categoryDictionary;
private List<Documentable> documents;



public News20Reader() {
	classAssociation=new HashMap<String,String>();
	this.setDocuments(new ArrayList<Documentable>());
}
public News20Reader(String directory)
{
	classAssociation=new HashMap<String,String>();
	this.setCategoryDictionary(new CategoryDictionary());
	this.setDocuments(new ArrayList<Documentable>());
	this.read(directory, null);
}
	public List<Documentable> read(String directory, String charset) {
		File dir=new File(directory);
		List<Documentable> list = new ArrayList<Documentable>();
		File [] f = dir.listFiles();
		int i=0;
		
		for( File file:f){
			List<Doc> aux = this.readAll(file, classLabel(i));//assign numeric class
			String classlabel = classAssociation.get(file.getName());
			if(classlabel==null)
				classAssociation.put(classLabel(i), file.getName());
			else
				classAssociation.put(classlabel, file.getName());
			list.addAll(aux);
			i++;
		}
	    Collections.shuffle(list);
	    Collections.shuffle(list);
	    System.out.println(" dataset size "+list.size());
	    this.setDocuments(list);
		return list;
	}

	
	
	
	
	public String classLabel(int i){
		if(i<10) return "00"+i;
		else return "0"+i;
	}
	
	
	public List<Doc> readAll(File directory,String cl){
		
		List<Doc> list = new ArrayList<Doc>();
		this.getCategoryDictionary().newCategory(cl);
		File [] f = directory.listFiles();
		for( File file:f){
			Doc item = new Doc();
			String classes[] =new String[1];
			classes[0]=cl;
			item.setCategories(classes);
			String abs;
			try {
				abs = FileUtils.readFileToString(file,"windows-1252");
				item.setDescription(abs);
				list.add(item);
			} catch (IOException e) {
				//logger.error(""+e);
			}
			
		}
		
		return list;
		
	}
	
	public static void main(String args[]){
		 News20Reader resource = new News20Reader();
		resource.read("/Users/mariolocci/Downloads/20news-18828/", null);
	
		
	}


	

	public static int contract( String dew,int[] map) {
		Integer cl = Integer.valueOf (dew);
		int newcl = map[cl];
		return newcl;
	}
	
	
	@Override
	public void set(int index, Documentable value) {
		this.documents.set(index, value);
		
	}

	@Override
	public Documentable get(int index) {
		
		return this.documents.get(index);
	}

	@Override
	public int size() {
		
		return this.documents.size();
	}

	@Override
	public Documents[] split(double d) {
	     List<Documentable> part1 = this.documents.subList(0, (int)Math.round(d*this.size()));
	     Documents [] dd = new Documents[2];
	     dd[0]= new DocumentReader(part1);
	     List<Documentable> part2 = this.documents.subList((int)Math.round(d*this.size()),this.size());
	     dd[1]= new DocumentReader(part2);
	     return dd;
	}
	@Override
	public void shuffle() {
		Collections.shuffle(this.getDocuments());
		
	}

	
}
