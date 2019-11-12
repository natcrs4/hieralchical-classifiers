package com.mfl.sem.text.model;

import com.crs4.sem.model.Documentable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Doc implements Documentable{
	private Long id;
	private String description;
	private String authors;
	private String title;
	private String [] categories;
	private String [] links;
	private String url;
	//private int index;
	 
	
	

	

	


	

	@Override
	public String text() {
		
		return (title==null?"":title)+ " "+ (description==null?"":description) + " "+ (authors==null?"":authors);
	}











	

	

	

	
	

}
