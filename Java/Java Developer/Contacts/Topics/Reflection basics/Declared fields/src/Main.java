import java.util.Arrays;

/**
 Get number of fields class declares (the ones inherited should be excluded).
 */
class FieldGetter {

    public int getNumberOfFieldsClassDeclares(Class<?> clazz) {
        // Add implementation here
        return clazz.getDeclaredFields().length;
    }

}