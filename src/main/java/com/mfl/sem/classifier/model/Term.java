package com.mfl.sem.classifier.model;

import lombok.Data;

@Data
public class Term {
	public Term(String forma, int index) {
		this.setForma(forma);
		this.setIndex(index);
	}
	private String forma;
	private int index;

	
	
}
