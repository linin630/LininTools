package com.linin.utils;

import java.lang.reflect.Method;

/**
 * ���乤��
 * 
 * @author linin
 */
public class ReflectUtil {

	/**
	 * ���ݶ��󣬷���һ��class�������ڻ�ȡ����
	 */
	public static Class<?> getClass(Object obj) {
		try {
			return Class.forName(obj.getClass().getName());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ���ݶ��󣬻�ȡĳ������
	 * 
	 * @param obj
	 *            ����
	 * @param methodName
	 *            ������
	 * @param parameterTypes
	 *            �÷����贫�Ĳ������ͣ�������贫�Σ��򲻴�
	 */
	public static Method getMethod(Object obj, String methodName,
			Class<?>... parameterTypes) {
		try {
			Method method = getClass(obj).getDeclaredMethod(methodName,
					parameterTypes);
			method.setAccessible(true);
			return method;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Method getMethod(Class<?> cls, String methodName,
			Class<?>... parameterTypes) {
		try {
			Method method = cls.getDeclaredMethod(methodName, parameterTypes);
			method.setAccessible(true);
			return method;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ֱ�Ӵ�����󡢷�����������������ʹ�øö�������ط���
	 * 
	 * @param obj
	 * @param methodName
	 * @param parameter
	 */
	public static Object invoke(Object obj, String methodName,
			Object... parameter) {
		Class<?>[] parameterTypes = new Class<?>[parameter.length];
		for (int i = 0; i < parameterTypes.length; i++) {
			parameterTypes[i] = parameter[i].getClass();
		}
		try {
			return getMethod(obj, methodName, parameterTypes).invoke(obj,
					parameter);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ֱ�Ӵ���������������������������ʹ�øö�������ؾ�̬����
	 * 
	 * @param cls
	 * @param methodName
	 * @param parameter
	 */
	public static Object invoke(Class<?> cls, String methodName,
			Object... parameter) {
		Class<?>[] parameterTypes = new Class<?>[parameter.length];
		for (int i = 0; i < parameterTypes.length; i++) {
			parameterTypes[i] = parameter[i].getClass();
		}
		try {
			return getMethod(cls, methodName, parameterTypes).invoke(null,
					parameter);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
