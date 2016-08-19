package edu.wehi.util;

import java.io.Serializable;

public class ElementAndPlace<T> implements Comparable<ElementAndPlace<T>>, Serializable
{
	private static final long serialVersionUID = 1L;

	public ElementAndPlace(T element, int place)
	{
		super();
		this.element = element;
		this.place = place;
	}
	public final T element;
	public final int place;
	
	
	@Override
	public int compareTo(ElementAndPlace<T> o)
	{
		return place - o.place;
	}

}