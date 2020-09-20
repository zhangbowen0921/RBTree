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
	protected void afterRemove(Node<E> node) {
		// 如果删除的节点是红色
		// 或者 用以取代删除节点的子节点是红色
		if (isRed(node)) {
			black(node);
			return;
		}

		Node<E> parent = node.parent;
		// 删除的是根节点
		if (parent == null)
			return;

		// 删除的是黑色叶子节点【下溢】
		// 判断被删除的node是左还是右
		boolean left = parent.left == null || node.isLeftChild();
		Node<E> sibling = left ? parent.right : parent.left;
		if (left) { // 被删除的节点在左边，兄弟节点在右边
			if (isRed(sibling)) { // 兄弟节点是红色
				black(sibling);
				red(parent);
				rotateLeft(parent);
				// 更换兄弟
				sibling = parent.right;
			}

			// 兄弟节点必然是黑色
			if (isBlack(sibling.left) && isBlack(sibling.right)) {
				// 兄弟节点没有1个红色子节点，父节点要向下跟兄弟节点合并
				boolean parentBlack = isBlack(parent);
				black(parent);
				red(sibling);
				if (parentBlack) {
					afterRemove(parent);
				}
			} else { // 兄弟节点至少有1个红色子节点，向兄弟节点借元素
				// 兄弟节点的左边是黑色，兄弟要先旋转
				if (isBlack(sibling.right)) {
					rotateRight(sibling);
					sibling = parent.right;
				}

				color(sibling, colorOf(parent));
				black(sibling.right);
				black(parent);
				rotateLeft(parent);
			}
		} else { // 被删除的节点在右边，兄弟节点在左边
			if (isRed(sibling)) { // 兄弟节点是红色
				black(sibling);
				red(parent);
				rotateRight(parent);
				// 更换兄弟
				sibling = parent.left;
			}

			// 兄弟节点必然是黑色
			if (isBlack(sibling.left) && isBlack(sibling.right)) {
				// 兄弟节点没有1个红色子节点，父节点要向下跟兄弟节点合并
				boolean parentBlack = isBlack(parent);
				black(parent);
				red(sibling);
				if (parentBlack) {
					afterRemove(parent);
				}
			} else { // 兄弟节点至少有1个红色子节点，向兄弟节点借元素
				// 兄弟节点的左边是黑色，兄弟要先旋转
				if (isBlack(sibling.left)) {
					rotateLeft(sibling);
					sibling = parent.left;
				}

				color(sibling, colorOf(parent));
				black(sibling.left);
				black(parent);
				rotateRight(parent);
			}
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
