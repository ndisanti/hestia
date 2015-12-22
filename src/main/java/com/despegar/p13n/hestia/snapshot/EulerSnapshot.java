package com.despegar.p13n.hestia.snapshot;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.despegar.library.snapshot.index.query.Query;
import com.despegar.library.rest.RestConnector;
import com.despegar.library.snapshot.index.IndexDefinition;

public class EulerSnapshot<T>
    extends UpdateControlQuerableSnapshot<T> {

    private static <T> IndexDefinition<T> createDefaultIndexDefinition(Class<T> clazz, String... aliases) {
        return new IndexDefinition<T>(clazz, "id", aliases);
    }

    public EulerSnapshot(Class<T> clazz, String authority, String path, boolean update, String... aliases) {
        super(createDefaultIndexDefinition(clazz), authority, path, update, aliases);
    }

    public EulerSnapshot(Class<T> clazz, String protocol, String authority, String path, boolean update, String... aliases) {
        super(createDefaultIndexDefinition(clazz), protocol, authority, path, update, aliases);
    }

    public EulerSnapshot(RestConnector restConnector, Class<T> clazz, String path, boolean update, String... aliases) {
        super(restConnector, createDefaultIndexDefinition(clazz), path, update, aliases);
    }

    public EulerSnapshot(RestConnector restConnector, String snapshotDir, Class<T> clazz, String path, boolean update,
        String... aliases) {
        super(restConnector, snapshotDir, createDefaultIndexDefinition(clazz), path, update, aliases);
    }

    public T get(String id) {
        return super.get(Query.get("id", id));
    }

    public Map<String, T> get(Collection<String> ids) {
        Map<String, T> ret = new HashMap<String, T>(ids.size());
        for (String id : ids) {
            ret.put(id, this.index.getItems().get(id));
        }
        return ret;
    }
}
