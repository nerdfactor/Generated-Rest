package eu.nerdfactor.springutil.generatedrest.data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A simplistic entity merger that will try to merge PersistentEntities
 * or use reflection of getter and setters.
 *
 * @author Daniel Klug
 */
public class GeneratedEntityMerger implements DataMerger {

	@Override
	public <T> T merge(T obj, T updated) {
		if (obj instanceof PersistentEntity) {
			return (T) ((PersistentEntity<?>) obj).mergeWithEntity(updated);
		}
		return this.reflectMerge(obj, updated);
	}

	/**
	 * Merges two objects of the same type by accessing getters and setters.
	 *
	 * @param original The original object.
	 * @param updated The object with updated values.
	 * @param <T> Type of the merged objects.
	 * @return The original object with the merged values.
	 */
	protected <T> T reflectMerge(T original, T updated) {
		Method[] methods = original.getClass().getDeclaredMethods();
		for (Method m : methods) {
			String name = m.getName();
			try {
				String setter = name;
				Class<?> type = m.getReturnType();
				Object value = null;
				if (name.startsWith("get") && m.getParameterCount() == 0) {
					value = m.invoke(updated);
					setter = name.replace("get", "set");
				}
				if (name.startsWith("is") && m.getParameterCount() == 0) {
					// getters for booleans may start with "is", per java code conventions.
					value = m.invoke(updated);
					setter = name.replace("is", "set");
				}
				if (value != null && (type == String.class && !value.equals(""))) {
					// only merge non empty values.
					try {
						Method method = original.getClass().getMethod(setter, type);
						method.invoke(original, value);
					} catch (NoSuchMethodException | SecurityException | IllegalAccessError | IllegalAccessException |
					         InvocationTargetException e) {
						// setter may not exist or can't be accessed.
					}
				}
			} catch (Exception e) {
				// merge may fail.
				// todo: throw Exception to be handled upstream or fail silently?
			}
		}
		return original;
	}
}
