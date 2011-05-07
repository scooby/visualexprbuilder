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
import java.util.Collections;
import java.util.List;

public class OperatorFromMethod extends Operator {
	protected Method m;
	protected ArrayList<Class<?>> paramTypes;
	protected ArrayList<Node> params;
	public OperatorFromMethod(Method _m) {
		super();
		if(_m == null)
			throw new NullPointerException();
		m = _m;
		Class<?>[] ca = m.getParameterTypes();
		if(isStatic()) {
			paramTypes = new ArrayList<Class<?>>(Arrays.asList(ca));
		} else {
			paramTypes = new ArrayList<Class<?>>(ca.length + 1);
			paramTypes.add(m.getDeclaringClass());
			paramTypes.addAll(Arrays.asList(ca));
		}
		params = new ArrayList<Node>(paramTypes.size());
		for(Class<?> c : paramTypes) {
			params.add(new UnsetArg(this, c));
		}
	}
	protected boolean isStatic() { 
		return Modifier.isStatic(m.getModifiers()); 
	}
	public String getLabel() { return toString(); }
	public Class<?> getType() { return m.getReturnType(); }
	public List<? extends Node> getIns() { 
		return Collections.unmodifiableList(params);
	}
	public void setIn(int arg, Node n) {
		if(arg < 0 || arg >= maxArgs())
			throw new IndexOutOfBoundsException("arg " + arg + " of " + this.toString());
		if(n == null)
			n = new UnsetArg(this, paramTypes.get(arg));
		if(paramTypes.get(arg).isAssignableFrom(n.getType()))
			params.set(arg, n);
		else
			throw new ClassCastException("arg " + arg + " of " + this.toString());
	}
	private int maxArgs() {
		// TODO Auto-generated method stub
		return 0;
	}
	public Object getValue() {
		ArrayList<Object> values = new ArrayList<Object>(paramTypes.size());
		for(int i = 0; i < paramTypes.size(); ++i)
			values.set(i, params.get(i).getValue());
		try {
			if(isStatic())
				return m.invoke(null,
						values.toArray());
			else
				return m.invoke(values.get(0),
						values.subList(1,
								values.size()).toArray());
		} catch(Throwable t) {
			return t;
		}
	}
	public String toString() { return m.toGenericString(); }
	@Override
	public Node getOut() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean canAddIns() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void addIn(Node n) {
		// TODO Auto-generated method stub

	}
	@Override
	public void setOut(Node n) {
		// TODO Auto-generated method stub

	}
}
