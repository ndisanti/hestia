package com.despegar.p13n.hestia.utils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtils {

	  public static DateTimeFormatter FORMAT_yyyyMMdd = DateTimeFormat.forPattern("yyyyMMdd");

	    public static DateTimeFormatter FORMAT_yyyy_MM_dd = DateTimeFormat.forPattern("yyyy-MM-dd");

	    public static DateTimeFormatter FORMAT_yyyy_MM_dd_hh_mm = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");

	    public static DateTimeFormatter FORMAT_yyyyMMddHHmm = DateTimeFormat.forPattern("yyyyMMddHHmm");

	    public static String currentDayAsyyyyMMdd() {
	        return FORMAT_yyyyMMdd.print(System.currentTimeMillis());
	    }

	    public static String yesterdayAsyyyyMMdd() {
	        return FORMAT_yyyyMMdd.print(new DateTime().minusDays(1).getMillis());
	    }

	    public static DateTime yyyyMMddAsDate(String yyyyMMdd) {
	        return FORMAT_yyyyMMdd.parseDateTime(yyyyMMdd);
	    }
}
