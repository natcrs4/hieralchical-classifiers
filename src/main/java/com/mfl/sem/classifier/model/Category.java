package com.mfl.sem.classifier.model;

import lombok.Data;

@Data
public class Category {
    
	public Category(Integer code, String label) {
		this.setLabel(label);
		this.setCode(code);
	}
	private String label;
	private Integer code;
	private String description;
	@Override 
	public boolean equals(Object o) {
		if(o instanceof Category)
			return ((Category) o).getLabel().equals(this.getLabel());
			else return false;
	}
	
	@Override 
	public int hashCode() {
		return this.getLabel().hashCode();
	}

	
	
	
}
