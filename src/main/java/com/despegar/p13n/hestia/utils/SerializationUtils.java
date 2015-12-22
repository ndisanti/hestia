package com.despegar.p13n.hestia.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.type.TypeReference;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

public class SerializationUtils {

    private static final String SEPARATOR = ",";
    private static final String COLLECTION_PREFIX = "C[";
    private static final String COLLECTION_TERMINATOR = "]";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private SerializationUtils() {
    }

    public static String collectionToString(Collection<String> list) {
        return COLLECTION_PREFIX + StringUtils.join(list, SEPARATOR) + COLLECTION_TERMINATOR;
    }

    public static boolean isCollectionString(String collectionString) {
        return collectionString != null && collectionString.startsWith(COLLECTION_PREFIX)
            && collectionString.endsWith(COLLECTION_TERMINATOR);
    }

    public static Collection<String> stringToCollection(String collectionString) {
        if (!isCollectionString(collectionString)) {
            throw new IllegalArgumentException("String " + collectionString + " is not a valid collection string");
        } else {
            List<String> result = new ArrayList<String>();
            String csv = collectionString.substring(COLLECTION_PREFIX.length(),
                collectionString.lastIndexOf(COLLECTION_TERMINATOR));
            for (String value : csv.split(SEPARATOR)) {
                result.add(value);
            }
            return result;
        }
    }

    public static byte[] serializeHessian(final Object object) {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();

        final Hessian2Output out = new Hessian2Output(bos);

        try {
            out.startMessage();
            out.writeObject(object);
            out.completeMessage();
            out.close();
        } catch (final Exception e) {
            throw new RuntimeException("exception in serialize to hessian", e);
        }

        return bos.toByteArray();
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserializeHessian(final byte[] data) {
        final ByteArrayInputStream bin = new ByteArrayInputStream(data);

        final Hessian2Input in = new Hessian2Input(bin);

        Object obj = null;
        try {

            in.startMessage();
            obj = in.readObject();
            in.completeMessage();

            in.close();
            bin.close();

        } catch (final Exception e) {
            throw new RuntimeException("exception in deserialize from hessian", e);
        }

        return (T) obj;
    }

    public static byte[] serializeJava(final Object object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.flush();
        } catch (IOException ex) {
            throw new RuntimeException("exception in serialize to java", ex);
        }
        return baos.toByteArray();
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserializeJava(byte[] bytes) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return (T) ois.readObject();
        } catch (Exception ex) {
            throw new RuntimeException("exception in deserialize from java", ex);
        }
    }

    public static byte[] serializeJson(final Object object) {
        try {
            final byte[] recordData = MAPPER.writeValueAsBytes(object);
            return recordData;
        } catch (final Exception ex) {
            throw new RuntimeException("exception in serialize to json", ex);
        }
    }

    public static <T> byte[] serializeJson(final List<T> list, final Class<T> elementsClass) {
        try {
            ObjectWriter typedWriter = MAPPER.writerWithType(MAPPER.getTypeFactory().constructCollectionType(
                list.getClass(), elementsClass));
            final byte[] recordData = typedWriter.writeValueAsBytes(list);
            return recordData;
        } catch (final Exception ex) {
            throw new RuntimeException("exception in serialize to json", ex);
        }
    }

    public static String serializeJsonStr(final Object object) {
        try {
            MAPPER.configure(Feature.INDENT_OUTPUT, true);
            final String recordData = MAPPER.writeValueAsString(object);
            return recordData;
        } catch (final Exception ex) {
            throw new RuntimeException("exception in serialize to json", ex);
        }
    }

    public static <T> T deserializeJson(final byte[] bytes, final Class<T> valueType) {
        try {
            return MAPPER.readValue(bytes, valueType);
        } catch (final Exception ex) {
            throw new RuntimeException("exception in deserialize to json", ex);
        }
    }

    public static <T> T deserializeJson(byte[] dataBytes, TypeReference<T> typeReference) {
        try {
            return MAPPER.readValue(dataBytes, typeReference);
        } catch (final Exception ex) {
            throw new RuntimeException("exception in deserialize to json", ex);
        }
    }
}
