package com.despegar.p13n.hestia.api.data.model;
import org.apache.commons.lang.math.NumberUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import com.google.common.base.Preconditions;

public class HotelItem extends ItemHome {

    private String hid;

    @Deprecated
    public HotelItem() {
        super(ItemType.HOTEL);
        // constructor for serializacion
    }

    /**
     * @param hid
     */
    public HotelItem(String hid) {
        super(ItemType.HOTEL);
        Preconditions.checkNotNull(hid);
        Preconditions.checkArgument(!hid.equalsIgnoreCase("null"));
        Preconditions.checkArgument(NumberUtils.isNumber(hid), "Hotel id is not a number %s", hid);
        this.hid = hid;
    }

    public String getHid() {
        return this.hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    @Override
    @JsonIgnore
    public String getId() {
        return this.hid;
    }

    @Override
    public String toString() {
        return "HotelItem [hid=" + this.hid + "]";
    }
}
