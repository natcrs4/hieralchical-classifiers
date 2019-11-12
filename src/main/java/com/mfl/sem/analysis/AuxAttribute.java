package com.mfl.sem.analysis;

import org.apache.lucene.util.Attribute;

public interface AuxAttribute extends Attribute {

	  /** the default type */
	  public static final String DEFAULT_TYPE = "word";

	  /** 
	   * Returns this Token's lexical type.  Defaults to "word". 
	   * @see #setType(String)
	   */
	  public String type();

	  /** 
	   * Set the lexical type.
	   * @see #type() 
	   */
	  public void setType(String type);
	}
