package com.mfl.sem.dataset.reader;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.Test;

import com.mfl.sem.classifier.text.DocItem;



public class News20ReaderTest {
	@Test
	public void testIteratore() {
		 News20Reader resource = new News20Reader("/Users/mariolocci/Downloads/20news-18828/");
		  Iterator<DocItem> it=resource.iterator();
		  int k=0;
		  while( it.hasNext()&&k<10)
		  {
			  DocItem current = it.next();
			  System.out.println(current.get());
			  k++;
		  }
			  
		 assertEquals(resource.size(),18828);
		
	}

}
