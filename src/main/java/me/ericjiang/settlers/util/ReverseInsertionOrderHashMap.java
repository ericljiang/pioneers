package me.ericjiang.settlers.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

public class ReverseInsertionOrderHashMap<K, V> extends HashMap<K, V> {

    private static final long serialVersionUID = 1L;

    private LinkedList<K> orderedKeys;

    public ReverseInsertionOrderHashMap() {
        orderedKeys = new LinkedList<K>();
    }

    @Override
    public Collection<V> values() {
        return orderedKeys.stream()
                .map(k -> get(k))
                .collect(Collectors.toList());
    }

    @Override
    public V put(K key, V value) {
        orderedKeys.addFirst(key);
        return super.put(key, value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        orderedKeys.addAll(m.keySet());
        super.putAll(m);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        orderedKeys.addFirst(key);
        return super.putIfAbsent(key, value);
    }

    @Override
    public V remove(Object key) {
        orderedKeys.remove(key);
        return super.remove(key);
    }
}
