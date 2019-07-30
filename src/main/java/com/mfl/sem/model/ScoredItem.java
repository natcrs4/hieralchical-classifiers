package com.mfl.sem.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoredItem implements Comparable<ScoredItem> {

	private int index;
	private Double score;
	private String label;

	public int compareTo(ScoredItem o) {

		return o.getScore().compareTo(this.getScore());
	}

	public ScoredItem(int index , double score) {
		this.setIndex(index);
		this.setScore(score);
	}

	
}
