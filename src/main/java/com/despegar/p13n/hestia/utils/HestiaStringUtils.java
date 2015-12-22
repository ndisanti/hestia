package com.despegar.p13n.hestia.utils;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.despegar.framework.utils.string.StringUtils;
import com.google.common.net.InetAddresses;
public class HestiaStringUtils  extends StringUtils {

    private static final String DEFAULT_SEPARATOR = ",";

    private static final String UUID_REGEX = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    private static final Pattern UUID_PATTERN = Pattern.compile(UUID_REGEX);

    private HestiaStringUtils() {

    }

    public static String quote(String inner) {
        return "\"" + inner + "\"";
    }


    public static String toCsvQuoted(List<String> fields) {
        StringBuilder builder = new StringBuilder();
        for (String field : fields) {
            builder.append(quote(field));
            builder.append(DEFAULT_SEPARATOR);
        }
        builder.delete(builder.length() - DEFAULT_SEPARATOR.length(), builder.length());
        return builder.toString();
    }

    public static String toCsv(Object... objects) {
        return toCsv(Arrays.asList(objects));
    }

    public static <T> String toCsv(List<T> objects) {
        StringBuilder builder = new StringBuilder();
        for (Object obj : objects) {
            builder.append(obj.toString());
            builder.append(DEFAULT_SEPARATOR);
        }
        builder.delete(builder.length() - DEFAULT_SEPARATOR.length(), builder.length());
        return builder.toString();
    }

    public static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }

    public static boolean isValidIPAddress(String ipAddr) {
        if (ipAddr == null) {
            return false;
        }
        return InetAddresses.isInetAddress(ipAddr);
    }


    public static boolean isValidEmailAddress(String aEmailAddress) {
        if (org.apache.commons.lang.StringUtils.isBlank(aEmailAddress)) {
            return false;
        }
        try {
            InternetAddress emailValid = new InternetAddress(aEmailAddress);
            emailValid.validate();
            return true;
        } catch (AddressException e) {
            return false;
        }
    }

    public static boolean isValidUUID(String uuid) {
        Matcher mtch = UUID_PATTERN.matcher(uuid);
        return mtch.find();
    }

    public static boolean isEmpty(String s) {
        return org.apache.commons.lang.StringUtils.isEmpty(s);
    }

    public static boolean isNotEmpty(String s) {
        return org.apache.commons.lang.StringUtils.isNotEmpty(s);
    }

    public static boolean isNotBlank(String s) {
        return org.apache.commons.lang.StringUtils.isNotBlank(s) && !HestiaStringUtils.NULL_STRING_VALUE.equals(s);
    }

    public static String replaceNonAlphanumericCharacters(String value, String replacement) {
        return value.replaceAll("[^A-Za-z0-9 ]", replacement);
    }

    public static String removeNonAlphanumericCharacters(String value) {
        return replaceNonAlphanumericCharacters(value, "");
    }

    public static String defaultString(Object obj) {
        return obj == null ? HestiaStringUtils.EMTPY_STRING : obj.toString();
    }

    public static String defaultString(Object obj, String defaultValue) {
        return obj == null ? defaultValue : obj.toString();
    }

}
