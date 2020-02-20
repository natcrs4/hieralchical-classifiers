package com.mfl.sem.classifier.text;

import java.util.Iterator;

import com.crs4.sem.model.Documentable;
import com.mfl.sem.text.model.Doc;

import lombok.Data;

@Data
public abstract class Documents {

	private int size;

	public abstract Documentable get(int index);

	public abstract void set(int index, Documentable value);

	public abstract int size();

	public Iterator<DocItem> iterator() {
		return new DocIterator();
	}

	private class DocIterator implements Iterator<DocItem> {

		private int index;

		private final DocEntry entry = new DocEntry();

		public boolean hasNext() {
			return index < size();
		}

		public DocItem next() {
			entry.update(index);

			index++;

			return entry;
		}

	}

	/**
	 * Vector entry backed by the vector. May be reused for higher performance
	 */
	private class DocEntry implements DocItem {

		private int index;
		private Doc doc;

		/**
		 * Updates the entry
		 */
		public void update(int index) {
			this.index = index;
		}

		public int index() {
			return index;
		}

		public Documentable get() {
			return Documents.this.get(index);
		}

		public void set(Documentable value) {
			Documents.this.set(this.index, value);

		}

	}

	public abstract Documents[] split(double d) ;

	public  abstract void shuffle() ;

}
