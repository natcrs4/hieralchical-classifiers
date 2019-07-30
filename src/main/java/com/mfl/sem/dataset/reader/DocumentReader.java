package com.mfl.sem.dataset.reader;

import java.util.List;

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

}
