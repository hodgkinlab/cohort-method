package edu.wehi.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FixedSizeContainer<T> implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private int length;
	private final List<ElementAndPlace<T>> 	elements = new ArrayList<>();
	int count = 0;
	T mostRecentElement = null;
	
	public FixedSizeContainer(int length)
	{
		this.length = length;
	}
	
	public boolean addElement(T element)
	{
		
		if (element == null)
		{
			return false;
		}
		if (elements.stream().anyMatch(e -> e.element.toString().equals(element.toString())))
		{
			return false;
		}
		mostRecentElement = element;
		elements.add(new ElementAndPlace<T>(element, count));
		count++;
		if (elements.size() > length)
		{
			int min = elements.stream().mapToInt(e -> e.place).min().getAsInt();
			for (ElementAndPlace<T> e : elements)
			{
				if (e.place == min)
				{
					elements.remove(e);
				}
			}
		}
		Collections.sort(elements);
		return true;
	}
	
	public T getMostRecentElement()
	{
		return mostRecentElement;
	}
	
	public List<T> getElements()
	{
		List<T> es = new ArrayList<>();
		for (ElementAndPlace<T> e : elements)
		{
			es.add(e.element);
		}
		return es;
	}
	
}