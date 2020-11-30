package com.dillenburg.huffmancompression.auxiliar;

import java.lang.reflect.Method;

public class GenericClone
{
	public static final Object clone(Object obj)
	{
		if(!(obj instanceof Cloneable))
			return obj;

        Object ret = null;

        try
        {
            Class<?> _class = obj.getClass();
            Class<?>[] parameterTypes = null;
            Method method = _class.getMethod ("clone", parameterTypes);
            Object[] params = null;
            ret = method.invoke (obj, params);
        }
        catch (Exception e)
        {}

        return ret;
	}
}