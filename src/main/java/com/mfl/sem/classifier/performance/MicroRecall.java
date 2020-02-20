package com.mfl.sem.classifier.performance;

import java.util.Set;

import com.mfl.sem.classifier.model.Category;



public class MicroRecall extends PMeasure{

	public MicroRecall(int limit, Set<Category> categories) {
		super(limit, categories);
		// TODO Auto-generated constructor stub
	}
	public MicroRecall(PMeasure pmeasure){
		super(pmeasure);
	}
	@Override
	public double measure() {
		double sumTp=0,sumDen=0;
		
		for( Category cat:this.getCategories()){
		  sumTp+=(double)this.tp(cat);
		  sumDen+= (this.tp(cat) + this.fn(cat));
		}
		return sumTp/sumDen;
    }
	@Override
	public double measure(Category cat) {
		return (this.tp(cat) + this.fn(cat))==0?0:(double) this.tp(cat)/(this.tp(cat) + this.fn(cat));
	}
}
