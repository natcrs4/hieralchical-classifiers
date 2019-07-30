package com.mfl.sem.classifier.policy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mfl.sem.model.ScoredItem;

public class MajoritySelection implements SelectionPolicy{
	
    Map<Integer,Integer> map= new HashMap<Integer,Integer>();
    Map<Integer,ScoredItem> rs= new HashMap<Integer,ScoredItem>();
	@Override
	public List<ScoredItem> select(List<ScoredItem>[] classifications) {
		
		for( List<ScoredItem> list :classifications) {
			ScoredItem first = list.get(0);
			 Integer s = map.get(first.getIndex());
			 if(s==null) {
				 map.put(first.getIndex(), 1);
				 ScoredItem item = ScoredItem.builder().index(first.getIndex()).label(first.getLabel()).score(1d).build();
				 rs.put(first.getIndex(), item);
			 }
			 else
				 map.put(first.getIndex(), s+1);
			     ScoredItem item = rs.get(first.getIndex());
			     item.setScore(s+1d);
		}
		Collection<ScoredItem> values = rs.values();
		List<ScoredItem>result= new ArrayList<ScoredItem>(values);
		Collections.sort(result);
		return result;
	}

}
