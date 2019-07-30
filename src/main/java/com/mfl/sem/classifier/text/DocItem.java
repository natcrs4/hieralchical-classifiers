package com.mfl.sem.classifier.text;

import com.crs4.sem.model.Documentable;

public interface DocItem {

	    /**
	     * Returns the current index
	     */
	    int index();

	    /**
	     * Returns the value at the current index
	     */
	    Documentable get();

	    /**
	     * Sets the value at the current index
	     */
	    void set(Documentable value);
	    

	
}
