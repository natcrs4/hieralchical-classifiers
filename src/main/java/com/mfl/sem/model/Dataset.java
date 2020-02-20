package com.mfl.sem.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import lombok.Data;
import no.uib.cipr.matrix.sparse.SparseVector;
@Data
public class Dataset {
	
	private SparseVector [] data;
    int [] categories;
    int categorysize;
    int featuresize;

    
    public Dataset(SparseVector [] data,int [] categories) {
    	this.setData(data);
    	this.setCategories(categories);
    }

//    public Dataset shuffle() {
//    	List<Integer> indexes= new ArrayList<Integer>(this.data.length);
//    	
//    	for ( int i=0;i<data.length;i++) {
//    		indexes.set(i, i);
//    	}
//    	Collections.shuffle(indexes);
//    	SparseVector [] data= new SparseVector[this.data.length];
//    	int [] categories= new int[this.data.length];
//    }
    public Dataset() {
    	
    }
	public Dataset(SparseVector[] data, int[] categories,  int featuresize, int categorysize) {
		this.setData(data);
    	this.setCategories(categories);
    	this.setFeaturesize(featuresize);
    	this.setCategorysize(categorysize);
	}
	
	public Dataset [] split(double percentuale) {
		
		int dim1=Math.round(this.data.length*0.6f);
		int dim2=this.data.length-dim1;
		SparseVector[] ve1=new SparseVector[dim1];
		
		int cat1[]= new int[dim1];
		SparseVector[] ve2=new SparseVector[dim2];
		int cat2[]= new int[dim2];
		
		for(int k=0;k<this.data.length;k++) {
			if(k<dim1) {
				ve1[k]=this.data[k];
				cat1[k]=this.categories[k];
			}
			else {
				ve2[k-dim1]=this.data[k];
				cat2[k-dim1]=this.categories[k];
			}
			
		}
		Dataset problem1= new Dataset(ve1,cat1,this.featuresize,this.categorysize);
		Dataset problem2= new Dataset(ve2,cat2,this.featuresize,this.categorysize);
		Dataset result[]= new Dataset[2];
		result[0]=problem1;
		result[1]=problem2;
		return result;
	}
	
	public Dataset balance() {
		int max=0;
	    
	    SparseVector [][] reversetable=this.buildReverseTable(this.data,this.categories,this.categorysize);
		Random rnd= new Random();
         List<Pair<SparseVector,Integer>> pairs= new ArrayList<Pair<SparseVector,Integer>>();
		for( int i=0; i<reversetable.length;i++)
		{
			int len=reversetable[i]!=null?reversetable[i].length:0;
			if(len>max) max=len;
		}
		for( int i=0; i< this.categorysize;i++) {
			int min=reversetable[i]!=null?reversetable[i].length:0;
		if(min>0)while(min<max) {
			SparseVector sample =this.extractRandomSample(i, reversetable, rnd);
			min++;
			pairs.add( new MutablePair<SparseVector,Integer>(sample,i));
		}
		
		
	}
		int ns=this.data.length+pairs.size();
		Dataset dataset=new Dataset(new SparseVector[ns],new int[ns],this.featuresize,this.categorysize);
		for(int i=0;i<this.data.length;i++) {
			dataset.data[i]=this.data[i];
			dataset.categories[i]=this.categories[i];
			
		}
		
		for(int i=0;i<pairs.size();i++) {
			dataset.data[i+this.data.length]=pairs.get(i).getKey();
			dataset.categories[i+this.data.length]=pairs.get(i).getValue();
			
		}
		return dataset;
	}

	private SparseVector[][] buildReverseTable(SparseVector[] data,int categories[],int categorysize) {
		SparseVector [][] result= new SparseVector[categorysize][];
		Map<Integer,List<SparseVector>> aux= new HashMap<Integer,List<SparseVector>> ();
		for( int i=0;i<categories.length;i++)
			{
			List<SparseVector> list = aux.get(categories[i]);
			if(list==null)  {
				list= new ArrayList<SparseVector>();
				aux.put(categories[i], list);
			}
			list.add(data[i]);
			}
		for( int i=0;i<categories.length;i++) {
			List<SparseVector> current = aux.get(i);
			if(current!=null)
				result[i]=current.toArray(new SparseVector[current.size()]);
		}
		return result;
	}


	private SparseVector  extractRandomSample(int i,   SparseVector [][] reversetable, Random rnd) {
		if(reversetable[i]==null) return null;
		int index= rnd.nextInt(reversetable[i].length);
		return reversetable[i][index];
	}
}
