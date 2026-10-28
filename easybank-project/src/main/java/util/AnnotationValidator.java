package util;
import model.annotation.MaxLength;
import model.annotation.Positive;
import java.lang.reflect.Field;
import java.util.ArrayList;
public final class AnnotationValidator {
    private AnnotationValidator() {}
    public static String[] validate(Object obj) {
        if(obj==null) return new String[]{"Object cannot be null"};
        ArrayList<String> errors=new ArrayList<String>();
        for(Class<?> type=obj.getClass(); type!=null && type!=Object.class; type=type.getSuperclass()) {
            for(Field field:type.getDeclaredFields()) {
                try { field.setAccessible(true); Object value=field.get(obj);
                    if(field.isAnnotationPresent(Positive.class) && value instanceof Number) { Number n = (Number)value; if (n.longValue()<0) errors.add(field.getName()+" "+field.getAnnotation(Positive.class).message()); }
                    if(field.isAnnotationPresent(MaxLength.class) && value instanceof String) { String s = (String)value; if (s.length()>field.getAnnotation(MaxLength.class).value()) errors.add(field.getName()+" length must be <= "+field.getAnnotation(MaxLength.class).value()); }
                } catch(ReflectiveOperationException | RuntimeException e) { errors.add("Could not validate field "+field.getName()); }
            }
        }
        return errors.toArray(new String[0]);
    }
    public static String readId(Object obj) {
        for(Class<?> type=obj.getClass();type!=null&&type!=Object.class;type=type.getSuperclass()) for(Field field:type.getDeclaredFields()) if(field.isAnnotationPresent(model.annotation.Id.class)) {
            try { field.setAccessible(true); Object value=field.get(obj); if(value==null) throw new IllegalStateException("@Id field is null"); return value.toString(); }
            catch(ReflectiveOperationException e){throw new IllegalStateException("Cannot read @Id field",e);}
        }
        throw new IllegalArgumentException("No @Id field found on "+obj.getClass().getName());
    }
}