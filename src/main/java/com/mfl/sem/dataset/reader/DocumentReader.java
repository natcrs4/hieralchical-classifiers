package com.mfl.sem.dataset.reader;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.ListUtils;

import com.crs4.sem.model.Documentable;
import com.mfl.sem.classifier.text.Documents;
import com.mfl.sem.text.model.Doc;

import lombok.Data;

@Data
public class DocumentReader extends Documents{
   private List<Documentable> docs;
	@Override
	public void set(int index, Documentable value) {
		this.docs.set(index, value);
		
	}

	@Override
	public Documentable get(int index) {
		
		return this.docs.get(index);
	}

	@Override
	public int size() {
		
		return this.docs.size();
	}

	public DocumentReader(List<Documentable> docs) {
		this.setDocs(docs);
	}

	@Override
	public Documents[] split(double d) {
	     List<Documentable> part1 = this.docs.subList(0, (int)Math.round(d*this.size()));
	     Documents [] dd = new Documents[2];
	     dd[0]= new DocumentReader(part1);
	     List<Documentable> part2 = this.docs.subList((int)Math.round(d*this.size()),this.size());
	     dd[1]= new DocumentReader(part2);
	     return dd;
	}

	@Override
	public void shuffle() {
		Collections.shuffle(this.getDocs());
		
	}

}
