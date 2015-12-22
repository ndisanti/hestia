package com.despegar.p13n.hestia.utils;

import java.util.Arrays;

import com.despegar.p13n.hestia.recommend.allinone.activity.ActivityType;
import com.google.common.base.Preconditions;

public class HestiaUtils {

	   public static void checkActivityType(ActivityType toCheck, ActivityType... supported) {
	        boolean supports = false;
	        for (ActivityType type : supported) {
	            if (toCheck == type) {
	                supports = true;
	                break;
	            }
	        }
	        Preconditions.checkArgument(supports, "Only (%s) are supported. Received: (%s)", //
	            Arrays.toString(supported), toCheck);
	    }

}
