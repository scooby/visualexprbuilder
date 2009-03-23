/*
    Copyright 2009 Benjamin Samuel. All rights reserved.
    This file is part of VisualExpressionBuilder, a component for the
    Swing UI for Java. Java is a trademark of Sun.

    VisualExpressionBuilder is free software: you can redistribute it and/or
    modify it under the terms of the GNU General Public License as published
    by the Free Software Foundation, either version 3 of the License, or (at
    your option) any later version.

    VisualExpressionBuilder is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
    Public License for more details.
    
    You should have received a copy of the GNU General Public License along
    with VisualExpressionBuilder, in the file COPYING in the root directory of
    the distribution. If not, see <http://www.gnu.org/licenses/>.
 */

package net.samuel.ben;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;

public class OperatorFromMethod implements Operator {
    private Method _m;
    private ArrayList<Object> paramValues;
    private ArrayList<Class<?>> paramTypes;
    private int paramsSet;
    private Operator init(Method __m) {
	_m = __m;
	paramTypes.clear();
	if(Modifier.isStatic(_m.getModifiers())) {
	    paramTypes.add(_m.getDeclaringClass());
	}
	paramTypes.addAll(paramTypes.size(),
	    Arrays.asList(_m.getParameterTypes()));
	reset();
	return this;
    }
    public String getName() { return _m.toString(); }
    public Class<?> returnType() { return _m.getReturnType(); }
    public Class<?> getArgType(int arg) {
	return paramTypes.get(arg);
    }
    public void reset() {
	paramValues.clear();
	for(int i = paramTypes.size(); i > 0; --i) {
	    paramValues.add(null);
	}
	paramsSet = 0;
    }
    public boolean setArg(int arg, Object val) {
	if(arg < 0 || arg >= maxArgs())
	    throw new IndexOutOfBoundsException();
	if(arg < minArgs() && ((paramValues.get(arg) == null)
	    ^ (val == null))) 
	{
	    paramsSet += val == null ? -1 : 1;
	}
	paramValues.set(arg, val);
	return paramsSet == minArgs();
    }
    public boolean evalLeftToRight() { return false; }
    public Object getReturn() throws InvocationTargetException {
	try {
	    return _m.invoke(paramValues.get(0),
		paramValues.subList(1,
		    paramValues.size()).toArray());
	} catch(IllegalAccessException e) {
	    // This shouldn't happen since paramValues
	    // always has arg 0.
	    throw new RuntimeException(e);
	}
    }
    public String toString() { return _m.toGenericString(); }
}
