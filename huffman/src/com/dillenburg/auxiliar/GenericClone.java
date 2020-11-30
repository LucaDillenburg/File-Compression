package com.dillenburg.auxiliar;

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
            Class<?> classe = obj.getClass();
            Class<?>[] tipoDoParametroFormal = null; // pq clone tem 0 parametros
            Method metodo = classe.getMethod ("clone", tipoDoParametroFormal);
            Object[] parametroReal = null;// pq clone tem 0 parametros
            ret = metodo.invoke (obj, parametroReal);
        }
        catch (Exception e)
        {}

        return ret;
	}
}