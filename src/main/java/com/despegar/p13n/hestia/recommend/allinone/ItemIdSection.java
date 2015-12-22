package com.despegar.p13n.hestia.recommend.allinone;

import com.despegar.p13n.hestia.recommend.allinone.rules.SectionsEnum;
import com.google.common.base.Preconditions;

/** 
 * Represents an item id and the section that has it.
 */
public class ItemIdSection {

    private SectionsEnum section;
    private String itemId;

    public ItemIdSection(SectionsEnum section, String itemId) {
        Preconditions.checkNotNull(section);
        Preconditions.checkNotNull(itemId);
        this.section = section;
        this.itemId = itemId;
    }

    public SectionsEnum getSection() {
        return this.section;
    }

    public void setSection(SectionsEnum section) {
        this.section = section;
    }

    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.itemId == null) ? 0 : this.itemId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        ItemIdSection other = (ItemIdSection) obj;
        if (this.itemId == null) {
            if (other.itemId != null) {
                return false;
            }
        } else if (!this.itemId.equals(other.itemId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ItemIdSection [section=" + this.section + ", itemId=" + this.itemId + "]";
    }
}
