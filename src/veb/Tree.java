package veb;

import space.Num;

/**
 * All the tree needs to do is say:
 * Here are the nodes
 * Given any particular node, here are the children or parent nodes.
 * @author ben
 *
 */
public interface Tree<T extends Num> {
	Content<T> getRoot();
	Iterable<Content<T>> getChildrenOf(Content<T> node);
	Content<T> getParentOf(Content<T> node);
}
