import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

/**
 Get number of accessible public fields for a given class.
 */
class FieldGetter {

    public int getNumberOfAccessibleFields(Class<?> clazz) {
        // Add implementation here
        return clazz.getFields().length;
    }

}