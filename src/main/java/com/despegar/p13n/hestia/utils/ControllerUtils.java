package com.despegar.p13n.hestia.utils;

import com.despegar.p13n.euler.commons.client.model.CountryCode;
import com.despegar.p13n.euler.commons.client.model.Language;
import com.despegar.p13n.hestia.exception.ApiIllegalArgumentException;

public class ControllerUtils {

	
	 public static Language checkLanguage(String lan) {
	        Language c = Language.fromString(lan);
	        if (c == null) {
	            String msg = String.format("Language not supported [%s]", lan);
	            throw new ApiIllegalArgumentException(msg);
	        }
	        return c;
	    }

	    public static CountryCode checkCountry(String country) {
	        CountryCode c = CountryCode.fromString(country);
	        if (c == null) {
	            String msg = String.format("Country code not supported [%s]", country);
	            throw new ApiIllegalArgumentException(msg);
	        }
	        return c;
	    }

}
