package com.mfl.sem.classifier.performance;

import java.util.Set;

import com.mfl.sem.classifier.model.Category;



// TODO: Auto-generated Javadoc
/**
 * Calculates macro precision.
 */
public class MacroPrecision extends PMeasure {

	/**
	 * Instantiates a new precision macro.
	 *
	 * @param limit the limit
	 * @param categories the categories
	 */
	public MacroPrecision(int limit, Set<Category> categories) {
		super(limit, categories);
		
	}
	public MacroPrecision(PMeasure pmeasure){
		super(pmeasure);
	}

	/*P_{macro}=\frac{1}{|C|}\sum^{|C|}_{i=1}\frac{TP_{i}}{TP_{i}+FP_{i}}
	 */
	@Override
	public double measure() {
         double sum=0;
		 double tot=0;
		for( Category cat:this.getCategories()){
		  sum+= this.measure(cat);
		  if(this.total(cat)>0) tot++;
		}
			return sum/tot;
    }
	
	public double measure( Category cat){
		return (this.tp(cat)+this.fp(cat))==0?0:((double)this.tp(cat))/(this.tp(cat) + this.fp(cat));
	}
	

}
