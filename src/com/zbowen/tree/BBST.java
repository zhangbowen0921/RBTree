package com.zbowen.tree;

import java.util.Comparator;

/**
 * 平衡二叉搜索树
 * @author zbowen
 *
 */
public class BBST<E> extends BST<E> {

	public BBST() {
		this(null);
	}

	public BBST(Comparator<E> comparator) {
		this.comparator = comparator;
	}
	
	/**
	 * 左旋转
	 * @param grand
	 */
	protected void rotateLeft(Node<E> grand) {
		Node<E> parent = grand.right;
		Node<E> child = parent.left;
		grand.right = child;
		parent.left = grand;
		afterRotate(grand, parent, child);
	}
	
	/**
	 * 右旋转
	 * @param grand
	 */
	protected void rotateRight(Node<E> grand) {
		Node<E> parent = grand.left;
		Node<E> child = parent.right;
		grand.left = child;
		parent.right = grand;
		afterRotate(grand, parent, child);
	}
	
	/**
	 * 旋转的重复代码抽取
	 * @param grand
	 * @param parent
	 * @param child
	 */
	protected void afterRotate(Node<E> grand, Node<E> parent, Node<E> child) {
		// 让parent称为子树的根节点
		parent.parent = grand.parent;
		if (grand.isLeftChild()) {
			grand.parent.left = parent;
		} else if (grand.isRightChild()) {
			grand.parent.right = parent;
		} else { // grand是root节点
			root = parent;
		}
		
		if (child != null) {
			child.parent = grand;
		}
		grand.parent = parent;
	}
	
	/**
	 * 统一旋转代码
	 * @param r
	 * @param b
	 * @param c
	 * @param d
	 * @param e
	 * @param f
	 */
	protected void rotate(
			Node<E> r, // 子树的根节点
			Node<E> b, Node<E> c,
			Node<E> d,
			Node<E> e, Node<E> f) {
		
		d.parent = r.parent;
		if (r.isLeftChild()) {
			r.parent.left = d;
		}else if (r.isRightChild()) {
			r.parent.right = d;
		}else { //根节点
			root = d;
		}
		
		b.right = c;
		if (c!=null) {
			c.parent = b;
		}
		
		f.left = e;
		if (e!=null) {
			e.parent = f;
		}
		
		d.left = b;
		b.parent = d;
		d.right = f;
		f.parent = d;
		
	}
}
