package com.mfl.sem.classifier.performance;

import java.util.Set;

import com.mfl.sem.classifier.model.Category;


public class MeasureFactory {
	
	
	public static PMeasure build(int type, int limit, Set<Category> categories){
		switch(type){
		case EvaluationType.MICRO_PRECISION: return makeMicroPrecision(limit,categories); 
		case EvaluationType.MACRO_PRECISION: return makeMicroPrecision(limit,categories); 
		case EvaluationType.MICRO_RECALL: return makeMicroRecall(limit,categories);
		case EvaluationType.MACRO_RECALL: return makeMacroRecall(limit,categories);
		case EvaluationType.MACRO_F1: return makeMacroF1(limit,categories);
		case EvaluationType.MICRO_F1: return makeMicroF1(limit,categories);
		}
		return makeMicroPrecision(limit,categories); 
	}
	
	public static  MicroPrecision makeMicroPrecision(int limit, Set<Category> categories){
		return new MicroPrecision(limit,categories);
	}
	public static  MacroPrecision makeMacroPrecision(int limit, Set<Category> categories){
		return new MacroPrecision(limit,categories);
	}
	
	
	public static MicroRecall makeMicroRecall(int limit,Set<Category> categories){
		return new MicroRecall(limit,categories);
	}
	public static MacroRecall makeMacroRecall(int limit,Set<Category> categories){
		return new MacroRecall(limit,categories);
	}
	
	public static MacroF1 makeMacroF1(int limit,Set<Category> categories){
		return new MacroF1(limit,categories);
	}
	
	public static MicroF1 makeMicroF1(int limit,Set<Category> categories){
		return new MicroF1(limit,categories);
	}
	

}
