package com.mfl.sem.classifier.performance;

import java.util.Set;

import com.mfl.sem.classifier.model.Category;



public class MacroRecall extends PMeasure {

	

	public MacroRecall(int limit, Set<Category> categories) {
		super(limit, categories);
		// TODO Auto-generated constructor stub
	}

	public MacroRecall(PMeasure pmeasure){
		super(pmeasure);
	}

	
		@Override
		public double measure() {
	         double sum=0;
			 double tot=0;
			for( Category cat:this.getCategories()){
			  sum+= measure(cat);
			  if(this.total(cat)>0) tot++;
			}
			
			return sum/tot;
	    }




		public double measure(Category cat) {
			return (this.tp(cat)+this.fn(cat))==0?0:((double)this.tp(cat))/(this.tp(cat) + this.fn(cat));
		}
		    
	

}
