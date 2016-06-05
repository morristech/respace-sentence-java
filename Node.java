package ai;

import java.util.*;

public class Node<T> {

	final T data;
	final Node<T> parent;
	final List<Node<T>> children;
	
	Node(T data, Node<T> parent, List<Node<T>> children) {
		
		this.data = data;
		this.parent = parent;
		this.children = children;
	}
	
	Node(T data, Node<T> parent) {
		
		this(data, parent, new ArrayList<Node<T>>());
	}
	
	Node(T data) {
		
		this(data, null);
	}
	
	void addChild(Node<T> child) {
		
		children.add(child);
	}
	
	void removeChild(Node<T> child) {
		
		children.remove(child);
	}
	
	Node<T> getChild(int index) {
		
		return children.get(index);
	}
	
	int childAmount() {
		
		return children.size();
	}
	
	boolean isLeaf() {
		
		return children.size() == 0;
	}
}