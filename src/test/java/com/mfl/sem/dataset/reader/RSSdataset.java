package com.mfl.sem.dataset.reader;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.crs4.sem.model.Documentable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mfl.sem.classifier.text.Documents;
import com.mfl.sem.text.model.Doc;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class RSSdataset {
	
	
@Test
public void buildDataset() throws IllegalArgumentException, FeedException, IOException {
	
    String urls[][]= {{"http://www.repubblica.it/rss/scienze/rss2.0.xml","scienze"},{"http://www.repubblica.it/rss/tecnologia/rss2.0.xml","tecnologia"},
    		{"http://www.repubblica.it/rss/persone/rss2.0.xml","persone"},{"http://www.repubblica.it/rss/salute/rss2.0.xml","salute"}};
	List<Documentable> docs= new ArrayList<Documentable>();
    for ( int k=0;k<urls.length;k++) {
	URL feedSource = new URL(urls[k][0]);
	String label=urls[k][1];
    

	SyndFeedInput input = new SyndFeedInput();
	SyndFeed feed = input.build(new XmlReader(feedSource));
	String desc=feed.getDescription();
	List<SyndEntry> entries = (List<SyndEntry>)feed.getEntries();
    for (SyndEntry entry: entries){
    	  System.out.println("Title: " + entry.getTitle());
    System.out.println("Link: " + entry.getLink());
    System.out.println("Author: " + entry.getAuthor());
    System.out.println("Publish Date: " + entry.getPublishedDate());
    System.out.println("Description: " + entry.getDescription().getValue());
    String text=entry.getAuthor() + " "+ entry.getTitle()+ " "+ entry.getDescription().getValue();
    String categories[]= new String[1];
    categories[0]= label;
   String [] links= new String[1];
   links[0]=entry.getLink();
     Doc document=  Doc.builder().description(text)
    		 .authors(entry.getAuthor())
    		 .title(entry.getTitle())
    		 .links(links)
    		 .categories(categories).build();
     docs.add(document);
    }
    }
    // Documents docreader= new DocumentReader(docs);
     //Gson gson = new Gson();
    // gson.toJson(docs, new FileWriter("src/test/resources/docs.json"));
     ObjectMapper objectMapper = new ObjectMapper();
     objectMapper.writeValue(new File("src/test/resources/docs.json"), docs);
     assertTrue(docs.size()>10);
}
     
     
}
