package com.zbowen.tree;

import java.util.Comparator;

public class RBTree<E> extends BBST<E> {

	private static final boolean RED = false;
	private static final boolean BLACK = true;

	public RBTree() {
		this(null);
	}

	public RBTree(Comparator<E> comparator) {
		this.comparator = comparator;
	}

	public static class RBNode<E> extends Node<E> {

		boolean color = RED;

		public RBNode(E element, Node<E> parent) {
			super(element, parent);
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			if (color == RED) {
				sb.append("R_");
			}
			sb.append(element);
			return sb.toString();
		}

	}

	@Override
	protected void afterAdd(Node<E> node) {
		// 能够执行到这 说明node必定是RBNode
		RBNode<E> parent = (RBNode<E>) node.parent;

		// 说明 node是 根节点
		if (parent == null) {
			// 将根节点 设置成 黑色
			black(node);
			return;
		}

		// 如果 node 的父节点为 黑色
		if (isBlack(parent)) {
			// 不做处理
			return;
		}
		// 获取 他父亲的兄弟节点 即他的叔父节点
		Node<E> uncle = parent.sibling();
		Node<E> grand = red(parent.parent);
		
		// 叔父节点是红色
		if (isRed(uncle)) {
			black(parent);
			black(uncle);
			afterAdd(grand);
			return;
		}

		// 叔父节点不是红色
		if (parent.isLeftChild()) { // L
			if (node.isLeftChild()) { // LL
				black(parent);
			} else { // LR
				black(node);
				rotateLeft(parent);
			}
			rotateRight(grand);
		} else {
			if (node.isLeftChild()) { // RL
				black(node);
				rotateRight(parent);
			} else { // RR
				black(parent);
			}
			rotateLeft(grand);
		}

	}

	/**
	 * 染色
	 * 
	 * @param node
	 * @param color
	 */
	private Node<E> color(Node<E> node, boolean color) {
		if (node == null)
			return node;
		((RBNode<E>) node).color = color;
		return node;
	}

	/**
	 * 染成红色
	 * 
	 * @param node
	 * @return
	 */
	private Node<E> red(Node<E> node) {
		return color(node, RED);
	}

	/**
	 * 染成黑色
	 * 
	 * @param node
	 * @return
	 */
	private Node<E> black(Node<E> node) {
		return color(node, BLACK);
	}

	/**
	 * 获取一个节点的颜色 如果传入null 默认为BLACK
	 * 
	 * @param node
	 * @return
	 */
	private boolean colorOf(Node<E> node) {
		return node == null ? BLACK : ((RBNode<E>) node).color;
	}

	/**
	 * 判断一个节点是否为黑色
	 * 
	 * @param node
	 * @return
	 */
	private boolean isBlack(Node<E> node) {
		return colorOf(node) == BLACK;
	}

	/**
	 * 判断一个节点是否为红色
	 * 
	 * @param node
	 * @return
	 */
	private boolean isRed(Node<E> node) {
		return colorOf(node) == RED;
	}

	@Override
	protected Node<E> createNode(E element, Node<E> parent) {
		return new RBNode<>(element, parent);
	}

}
