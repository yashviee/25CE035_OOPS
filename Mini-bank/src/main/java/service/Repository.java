package service;
import util.AnnotationValidator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class Repository<T> {
    private final Map<String,T> items=new HashMap<>();
    public synchronized T save(T entity) { if(entity==null) throw new IllegalArgumentException("Entity cannot be null"); items.put(AnnotationValidator.readId(entity),entity); return entity; }
    @SafeVarargs public final synchronized void saveAll(T... entities) { for(T entity:entities) save(entity); }
    public synchronized T findById(String id) { return items.get(id); }
    public synchronized ArrayList<T> findAll() { return new ArrayList<>(items.values()); }
    public synchronized boolean delete(String id) { return items.remove(id)!=null; }
    public static <N extends Number> double average(N[] values) { if(values==null||values.length==0) return 0; double sum=0; for(N n:values) sum+=n.doubleValue(); return sum/values.length; }
}



