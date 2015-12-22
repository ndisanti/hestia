package com.despegar.p13n.hestia.api.data.model;
import org.codehaus.jackson.annotate.JsonIgnore;

public class ClosedPackagesItem extends ItemHome{

	
	 private String cluid; // cluster id

	    @Deprecated
	    public ClosedPackagesItem() {
	        super(ItemType.CLOSED_PACKAGES);
	        // constructor for serializacion
	    }

	    /**
	     * @param hid
	     */
	    public ClosedPackagesItem(String cluid) {
	        super(ItemType.CLOSED_PACKAGES);
	        this.cluid = cluid;
	    }

	    public String getCluid() {
	        return this.cluid;
	    }

	    public void setCluid(String cluid) {
	        this.cluid = cluid;
	    }

	    @Override
	    @JsonIgnore
	    public String getId() {
	        return this.cluid;
	    }

	    @Override
	    public String toString() {
	        return "ClosedPackagesItem [cluid=" + this.cluid + "]";
	    }
}
