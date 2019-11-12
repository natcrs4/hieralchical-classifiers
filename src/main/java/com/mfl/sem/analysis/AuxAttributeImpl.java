package com.mfl.sem.analysis;

import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeImpl;
import org.apache.lucene.util.AttributeReflector;

public class AuxAttributeImpl extends AttributeImpl implements AuxAttribute, Cloneable {

	 private String type;
	  
	  /** Initialize this attribute with {@link TypeAttribute#DEFAULT_TYPE} */
	  public AuxAttributeImpl() {
	    this(DEFAULT_TYPE); 
	  }
	  
	  /** Initialize this attribute with <code>type</code> */
	  public AuxAttributeImpl(String type) {
	    this.type = type;
	  }
	  
	  @Override
	  public String type() {
	    return type;
	  }

	  @Override
	  public void setType(String type) {
	    this.type = type;
	  }

	  @Override
	  public void clear() {
	    type = DEFAULT_TYPE;    
	  }

	  @Override
	  public boolean equals(Object other) {
	    if (other == this) {
	      return true;
	    }
	    
	    if (other instanceof AuxAttributeImpl) {
	      final AuxAttributeImpl o = (AuxAttributeImpl) other;
	      return (this.type == null ? o.type == null : this.type.equals(o.type));
	    }
	    
	    return false;
	  }

	  @Override
	  public int hashCode() {
	    return (type == null) ? 0 : type.hashCode();
	  }
	  
	  @Override
	  public void copyTo(AttributeImpl target) {
	    AuxAttribute t = (AuxAttribute) target;
	    t.setType(type);
	  }

	  @Override
	  public void reflectWith(AttributeReflector reflector) {
	    reflector.reflect(AuxAttribute.class, "type", type);
	  }

}
