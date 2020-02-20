package com.mfl.sem.classifier.performance;

import java.util.Set;

import com.mfl.sem.classifier.model.Category;

import lombok.Data;

@Data
public class MicroF1 extends PMeasure {
    private double beta;
	public MicroF1(int limit,Set<Category> categories) {
	   super(limit,categories);
	   this.setBeta(1);
	}

	public MicroF1(PMeasure pmeasure){
		super(pmeasure);
	}

	@Override
	public double measure() {
		MicroPrecision P = new MicroPrecision(this.getLimit(),this.categories);
		MicroRecall R = new MicroRecall(this.getLimit(),this.categories);
		double p=P.measure();
		double r=R.measure();
		
		double result= ((this.getBeta()*this.getBeta()+1)*p*r)/(this.getBeta()*this.getBeta()*p+r);
		return result;
	}



	@Override
	public double measure(Category cat) {
		// TODO Auto-generated method stub
		return 0;
	}

}
