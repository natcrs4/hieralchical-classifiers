package com.mfl.sem.classifier.performance;

import java.util.Set;

import com.mfl.sem.classifier.model.Category;



/**
 * The Class to compute micro precision.
 */
public class MicroPrecision extends PMeasure{

	/**
	 * Instantiates a new precision micro.
	 *
	 * @param limit the limit
	 * @param set the set
	 */
	public MicroPrecision(int limit,Set<Category> set) {
		super(limit,set);
	}
	public MicroPrecision(PMeasure pmeasure){
		super(pmeasure);
	}

	/**
	 * P_{micro}=\frac{\sum^{|C|}_{i=1}TP_{i}}{\sum^{|C|}_{i=1}TP_{i}+FP_{i}}, R_{micro}=\frac{\sum^{|C|}_{i=1}TP_{i}}{\sum^{|C|}_{i=1}TP_{i}+FN_{i}}
	 */
	@Override
	public double measure() {
		double sumTp=0,sumDen=0;
		
		for( Category cat:this.getCategories()){
		  sumTp+=(double)this.tp(cat);
		  sumDen+= (this.tp(cat) + this.fp(cat));
		}
		return sumTp/sumDen;
    }


	@Override
	public double measure(Category cat) {
		return (this.tp(cat) + this.fp(cat))==0?0:(double) this.tp(cat)/(this.tp(cat) + this.fp(cat));
		
	}
	
	

}
