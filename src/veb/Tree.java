package veb;

/**
 * All the tree needs to do is say:
 * Here are the nodes
 * Given any particular node, here are the children or parent nodes.
 * @author ben
 *
 */
public interface Tree {
	Content getRoot();
	Iterable<Content> getChildrenOf(Content node);
	Content getParentOf(Content node);
}
