package reghzy.witt.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Recursively maps a class to a list of values (taking into account interfaces)
 * @param <T> Value type
 */
public class ClassMultiMap<T> {
    // We could use another Map<Class, List<IDataProvider>>, and use it to map a top-level
    // TileEntity to a list of providers in all of its superclasses, however, if a provider
    // is registered after that tile has been "baked", then we'd have to scan the map for
    // tiles that inherit from the class that was registered, and remove every entry so it
    // can be re-baked on demand. May be worth the extra perf at some point.

    // We also need to use a cached map of interfaces so that we
    // don't have to recursively scan all interfaces, as it's pricey.

    private final HashMap<Class<?>, ArrayList<T>> map;
    private ArrayList<T> currList;

    public ClassMultiMap() {
        this.map = new HashMap<Class<?>, ArrayList<T>>();
    }

    /**
     * Associates the value with the class. Meaning, {@link ClassMultiMap#getValues(Class)} called with
     * the key, a derived type or a class that implements the key interface, will contain the value.
     * @param key The key to map the value to
     * @param value The value to add
     * @return True when added, False when already added ({@link Object#equals(Object)} comparison)
     */
    public boolean put(Class<?> key, T value) {
        ArrayList<T> list = this.map.get(key);
        if (list == null)
            this.map.put(key, list = new ArrayList<T>());
        else if (list.contains(value))
            return false;
        list.add(value);
        return true;
    }

    /**
     * Gets all of the values possibly associated with the class type and its hierarchy
     * @param clazz The target class type
     * @return A list of values. Returns null if no values found, and is never empty when non-null
     */
    public ArrayList<T> getValues(Class<?> clazz) {
        this.currList = null;

        HashSet<Class<?>> itfDejaVu = new HashSet<Class<?>>();
        for (Class<?> klass = clazz; klass != null; klass = klass.getSuperclass()) {
            ArrayList<T> list = this.map.get(klass);
            if (list != null && !list.isEmpty()) {
                if (this.currList == null)
                    this.currList = new ArrayList<T>();
                this.currList.addAll(list);
            }

            this.runInterfaces(klass, itfDejaVu);
        }

        ArrayList<T> theList = this.currList;
        this.currList = null;
        return theList;
    }

    private void runInterfaces(Class<?> klass, HashSet<Class<?>> dejaVu) {
        Class<?>[] interfaces = klass.getInterfaces();

        // First we accumulate tips from the interfaces directly on the class
        for (Class<?> itf : interfaces) {
            if (dejaVu.contains(itf)) { // already processed to skip
                continue;
            }

            ArrayList<T> list = this.map.get(itf);
            if (list != null && !list.isEmpty()) {
                if (this.currList == null)
                    this.currList = new ArrayList<T>();
                this.currList.addAll(list);
            }
        }

        // Secondly we process the hierarchy of the interfaces,
        // ensuring we add the itf to the dejaVu set before running it
        for (Class<?> itf : interfaces) {
            if (dejaVu.add(itf)) { // not already processed so run it
                runInterfaces(itf, dejaVu);
            }
        }
    }
}
