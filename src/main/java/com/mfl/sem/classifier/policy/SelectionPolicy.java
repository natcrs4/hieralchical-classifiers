package com.mfl.sem.classifier.policy;

import java.util.List;

import com.mfl.sem.model.ScoredItem;

public interface SelectionPolicy {
	
	public List<ScoredItem> select(List<ScoredItem> [] classifications);

}
