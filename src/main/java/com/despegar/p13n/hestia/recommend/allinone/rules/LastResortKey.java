/**
 * 
 */
package com.despegar.p13n.hestia.recommend.allinone.rules;

import org.apache.commons.lang.builder.HashCodeBuilder;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Language;
import com.despegar.p13n.euler.commons.client.model.Product;
import com.google.common.base.Preconditions;

class LastResortKey {
    private final Product home;
    private final CountryCode cc;
    private final Language language;

    LastResortKey(Product home, CountryCode cc, Language language) {
        Preconditions.checkNotNull(home);
        Preconditions.checkNotNull(cc);
        Preconditions.checkNotNull(language);
        this.home = home;
        this.cc = cc;
        this.language = language;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.cc).append(this.home).append(this.language).toHashCode();
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
        LastResortKey other = (LastResortKey) obj;
        if (this.cc != other.cc) {
            return false;
        }
        if (this.home != other.home) {
            return false;
        }
        if (this.language != other.language) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "LastResortKey [home=" + this.home + ", cc=" + this.cc + ", language=" + this.language + "]";
    }


}
