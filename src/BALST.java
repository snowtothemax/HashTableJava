import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/**
 * 
 * Class to implement a BalanceSearchTree in the form of a Red Black Tree
 * 
 * @param <K> is the generic type of key
 * @param <V> is the generic type of value
 */
public class BALST<K extends Comparable<K>, V> implements BALSTADT<K, V> {

	// inner class
	/**
	 * BSTNode class for a BST node object that is used in the contruction of a red
	 * Black Tree
	 * 
	 * @author front
	 *
	 * @param <K>
	 * @param <V>
	 */
	static class BSTNode<K extends Comparable<K>, V> {

		// private fields of a node
		private K key;
		private V value;
		private BSTNode leftChild;
		private BSTNode rightChild;
		private BSTNode parent;
		boolean isRed; // Tells whether the node is red, if not, its black

		/**
		 * Constructor
		 * 
		 * @param key
		 * @param value
		 */
		BSTNode(K key, V value) {
			this.key = key;
			this.value = value;

			// children defined as null
			leftChild = null;
			rightChild = null;
			parent = null;
			isRed = true;
		}

		
		
		/**
		 * Returns the key for the given node
		 * 
		 * @return key
		 */
		K getKey() {
			return this.key;
		}

		/**
		 * Returns the value of the given node
		 * 
		 * @return value
		 */
		V getValue() {
			return this.value;
		}

		/**
		 * Sets the right child of the given node
		 * 
		 * @param right the node that is to be the right child
		 */
		void setRight(BSTNode right) {
			this.rightChild = right;
		}

		/**
		 * Sets the left child of the given node
		 * 
		 * @param left the node that is to be the left child
		 */
		void setLeft(BSTNode left) {
			this.leftChild = left;
		}

		/**
		 * Returns the current Nodes right child
		 * 
		 * @return Node rightChild
		 */
		BSTNode getRight() {
			return rightChild;
		}

		/**
		 * Returns the current Nodes left Child
		 * 
		 * @return Node leftChild
		 */
		BSTNode getLeft() {
			return leftChild;
		}

		/**
		 * returns whether the given node is a red node or not.
		 * 
		 * @return isRed true if the color is red, false if black
		 */
		boolean isRed() {
			return isRed;
		}

		/**
		 * sets the color of the Node through boolean logic
		 * 
		 * @param wantRed
		 */
		void setRed(boolean wantRed) {
			isRed = wantRed;
		}

		/**
		 * sets the parent node of the node
		 * 
		 * @param parent
		 */
		void setParent(BSTNode parent) {
			this.parent = parent;
		}

		/**
		 * returns the parent of the current node
		 * 
		 * @return
		 */
		BSTNode getParent() {
			return parent;
		}

	}

	// private fields
	private BSTNode<K, V> root;// root of the tree
	private int numKeys; // number of keys in the tree
	private int size; // size of the tree

	/**
	 * Constructor for the tree
	 */
	public BALST() {
		root = null;
		numKeys = 0;
		size = 0;
	}

	/**
	 * Gets the key at the root of the tree
	 * 
	 * @return K
	 */
	@Override
	public K getKeyAtRoot() {
		if (root == null) {
			return null;
		}
		return root.getKey();
	}

	/**
	 * Gets the key of the lefChild of the current key
	 * 
	 * @return K key of the left child of the given node with the given key
	 * @param K key of the node to be found and whos key of the leftChild will be
	 *          returned
	 */
	@Override
	public K getKeyOfLeftChildOf(K key) throws IllegalNullKeyException, KeyNotFoundException {

		// checks if the key input is null
		if (key == null) {
			throw new IllegalNullKeyException();
		}

		// checks if the key exists in the tree
		if (!contains(key)) {
			throw new KeyNotFoundException();
		}

		// creates a new node to compare to nodes in the tree
		BSTNode comp = new BSTNode(key, null);

		// the left child of the found node from the given key
		BSTNode foundLeft = getNode(root, comp).getLeft();

		// checks if the node is null or not, and returns null if so
		if (foundLeft == null) {
			return null;
		}

		// returns the key of the left child of the node found in the tree of the given
		// key
		return (K) foundLeft.getKey();

	}

	/**
	 * Gets the key of the right Child of the current key
	 * 
	 * @return K key of the right child of the given node with the given key
	 * @param K key of the node to be found and who's key of the right Child will be
	 *          returned
	 */
	@Override
	public K getKeyOfRightChildOf(K key) throws IllegalNullKeyException, KeyNotFoundException {

		// checks if the key input is null
		if (key == null) {
			throw new IllegalNullKeyException();
		}

		// checks if the key exists in the tree
		if (!contains(key)) {
			throw new KeyNotFoundException();
		}

		// creates a new node to compare to nodes in the tree
		BSTNode comp = new BSTNode(key, null);

		// the right child of the found node from the given key
		BSTNode foundRight = getNode(root, comp).getRight();

		// checks if the node is null or not, and returns null if so
		if (foundRight == null) {
			return null;
		}

		// returns the key of the left child of the node found in the tree of the given
		// key
		return (K) foundRight.getKey();

	}

	@Override
	public int getHeight() {
		return heightHelper(root);
	}

	@Override
	public List<K> getInOrderTraversal() {
		List<K> list = new ArrayList<K>();
		inOrderTraversal(list, root);
		return list;
	}

	@Override
	public List<K> getPreOrderTraversal() {
		List<K> list = new ArrayList<K>();
		preOrderTraversal(list, root);
		return list;
	}

	@Override
	public List<K> getPostOrderTraversal() {
		List<K> list = new ArrayList<K>();
		postOrderTraversal(list, root);
		return list;
	}

	@Override
	public List<K> getLevelOrderTraversal() {
		List<K> list = new ArrayList<K>();
		int h = heightHelper(root);
		int i;
		for (i = 1; i <= h; i++) {
			levelOrderTraversal(list, root, i);
		}
		
		return list;
	}

	@Override
	public void insert(K key, V value) throws IllegalNullKeyException, DuplicateKeyException {

		// checks if the key input is null
		if (key == null) {
			throw new IllegalNullKeyException();
		}

		// checks if the key already exists in the tree
		if (contains(key)) {
			throw new DuplicateKeyException();
		}

		// creates a new BSTNode with input key and value
		BSTNode comp = new BSTNode(key, value);

		size++;
		// checksif the root of the tree is null and if so,makes new key the root
		if (root == null) {
			root = comp;
			root.setRed(false);// sets the root node equal to black.
			return;
		}

		// calls the insertHelper to insert the node in an BST fashion, then it reshapes
		// it
		insertHelper(root, comp, null);

	}

	@Override
	public boolean remove(K key) throws IllegalNullKeyException, KeyNotFoundException {
		if (key == null) {
			throw new IllegalNullKeyException();
		}
		if (!contains(key)) {
			throw new KeyNotFoundException();
		}
		BSTNode curr = new BSTNode(key, null);

		size--;
		removeHelp(root, curr);
		return true;
	}

	/**
	 * finds a node in the tree and returns its value
	 * 
	 * @param K key to be found
	 * @return V value of the found key
	 */
	@Override
	public V get(K key) throws IllegalNullKeyException, KeyNotFoundException {

		// checks if the key input is null
		if (key == null) {
			throw new IllegalNullKeyException();
		}

		// checks if the key exists in the tree
		if (!contains(key)) {
			throw new KeyNotFoundException();
		}

		// creates a new node to be found in the tree;
		BSTNode comp = new BSTNode(key, null);
		BSTNode found = getNode(root, comp);

		// returns the value of the found node
		return (V) found.getValue();
	}

	/**
	 * Checks if the current input key exists within the tree
	 * 
	 * @param K key
	 * @return boolean true if it exists, false otherwise
	 */
	@Override
	public boolean contains(K key) throws IllegalNullKeyException {
		// checks if the input key is null
		if (key == null) {
			throw new IllegalNullKeyException();
		}
		// check if the node exists in the tree
		BSTNode comp = new BSTNode(key, null);

		// returns whether the node is found or not
		return (getNode(root, comp) != null);
	}

	/**
	 * returns the size of a given BALST
	 * 
	 * @return int size of the tree
	 */
	@Override
	public int numKeys() {
		return size;
	}

	@Override
	public void print() {
		printHelper();
	}

	// helper methods
	/**
	 * returns the node of a given key in the tree if it exists
	 * 
	 * @param key
	 * @return BSTNode of the key found, else null
	 */
	private static BSTNode getNode(BSTNode curr, BSTNode comp) {

		if(curr == null) {
			System.out.println("CURR NULL");
			return null;
		}
		if(comp == null) {
			System.out.println("COMP NULL");
			return null;
		}
		//checks if they are both equal
		if(curr.getKey().compareTo(comp.getKey()) == 0 ) {
			return curr;
		}
		// checks if the comp node is less than the current node and that the current
		// node has a left child
		if (curr.getLeft()!= null && comp.getKey().compareTo(curr.getKey()) < 0) {
			return getNode(curr.getLeft(), comp);
		}

		// checks if the comp node is greater than the current node and that the current
		// node has a right child
		if (comp.getKey().compareTo(curr.getKey()) > 0 && curr.getRight() != null) {
			return getNode(curr.getRight(), comp);
		}


		// otherwise returns null if all else fails
		return null;
	}

	/**
	 * Insert Helper Method. Uses the RBT tree properties and recursion to do so.
	 * 
	 * @param curr
	 * @param comp
	 */
	private static void insertHelper(BSTNode curr, BSTNode comp, BSTNode compParent) {

		// checks if the current node being compared to is null, and if so it sets it
		// equal to the new node
		if (curr == null) {

			// sets the current node equal to the comp node and its parents likewise.
			curr = comp;
			curr.setParent(compParent);

			// checks if this certain case of the RBT Tree was violation: Red parent red
			// child
			redPropViolation(curr);
			return;
		}

		// checks if the new node created is greater than the current node, and
		// recursively calls the insert method if so
		if (comp.getKey().compareTo(curr.getKey()) > 0) {
			insertHelper(curr.getRight(), comp, curr);
		}

		// otherwise the insert is called recursively on the left node
		else {
			insertHelper(curr.getLeft(), comp, curr);
		}

	}

	/**
	 * rotates the parent node left around the parent
	 * 
	 * @param curr
	 */
	private static void rotateLeft(BSTNode curr) {

		BSTNode par = curr.getParent();
		BSTNode parR = par.getRight();
		BSTNode parL = par.getLeft();
		BSTNode gp = par.getParent();

		// checks if the current node is the right node of the parent or not
		if (curr == parR) {

			// case for a normal left rotation
			if (gp.getRight() == par) {

				// all hypothetical references for the new structure of the tree for easy
				// understanding
				BSTNode left = gp;// left of the new root node will be the gp
				BSTNode right = parR;// right of the new node is still parR
				BSTNode sib = parL;// the sibling of the current key is parL

				// since the parent is now the new root node, must set its new left and right
				// children, etc
				par.setParent(gp.getParent());// sets the new parent of the root node"par" equal to the old root nodes
												// parent
				par.setLeft(left);// sets the left of the new root node equal to left (old root)
				left.setParent(par);// sets lefts parent to the root node
				left.setRight(sib);// sets lefts new right reference to the old sibling of the new key
				sib.setParent(left);// sets the siblings parent equal to the new left node
				par.setRed(false);// sets the root node to black
				left.setRed(true);// sets the left nodes color to red
				return;

			}

			// case for a left-Right rotation
			else {
				BSTNode left = par;// the new left node of the "root" after reconstruction
				BSTNode right = gp;// the new right node of the "Root" after reconstruction
				BSTNode root = parR;// the new "root" of the subtree
				BSTNode leftS = parR.getLeft();// the leftsibling of the key node being moved
				BSTNode rightS = parR.getRight();// the right sibling of the key node being moved

				// sets the children of the "root"
				root.setLeft(left);
				root.setRight(right);
				root.setParent(gp.getParent());// sets the roots parent equal to the gp parent

				// sets the parents of the children to the root
				left.setParent(root);
				right.setParent(root);

				// assigns the old siblings equal to the children of the roots children
				left.setRight(leftS);
				right.setLeft(rightS);

				// sets the roots color equal to
				root.setRed(false);
				right.setRed(true);
				return;
			}
		}
	}

	/**
	 * rotates the parent node right around the parent
	 * 
	 * @param curr
	 */
	private static void rotateRight(BSTNode curr) {

		BSTNode par = curr.getParent();
		BSTNode parR = par.getRight();
		BSTNode parL = par.getLeft();
		BSTNode gp = par.getParent();

		// checks if the current node is the right node of the parent or not
		if (curr == parL) {

			// case for a normal right rotation
			if (gp.getLeft() == par) {

				// all hypothetical references for the new structure of the tree for easy
				// understanding
				BSTNode left = parL;// left of the new root node will be parL
				BSTNode right = gp;// right of the root is gp
				BSTNode sib = parR;// the sibling of the current key is parR

				// since the parent is now the new root node, must set its new left and right
				// children, etc
				par.setParent(gp.getParent());// sets the new parent of the root node"par" equal to the old root nodes
												// parent
				par.setRight(right);// sets the left of the new root node equal to left (old root)
				right.setParent(par);// sets rights parent to the root node
				right.setLeft(sib);// sets lefts new right reference to the old sibling of the new key
				sib.setParent(right);// sets the siblings parent equal to the new right node
				par.setRed(false);// sets the root node to black
				right.setRed(true);// sets the right nodes color to red
				return;

			}

			// case for a Right-left rotation
			else {
				BSTNode left = gp;// the new left node of the "root" after reconstruction
				BSTNode right = par;// the new right node of the "Root" after reconstruction
				BSTNode root = parL;// the new "root" of the subtree
				BSTNode leftS = parL.getLeft();// the leftsibling of the key node being moved
				BSTNode rightS = parL.getRight();// the right sibling of the key node being moved

				// sets the children of the "root"
				root.setLeft(left);
				root.setRight(right);
				root.setParent(gp.getParent());// sets the roots parent equal to the gp parent

				// sets the parents of the children to the root
				left.setParent(root);
				right.setParent(root);

				// assigns the old siblings equal to the children of the roots children
				left.setRight(leftS);
				right.setLeft(rightS);

				// sets the roots color equal to
				root.setRed(false);
				left.setRed(true);
				return;
			}
		}
	}

	/**
	 * method used to recolor certain nodes when inserted into a tree that has a
	 * 
	 * @param curr
	 */
	private static void recolor(BSTNode curr) {

		// creates three nodes that reference all the surrounding nodes of the parent
		// for easy access
		BSTNode par = curr.getParent();
		BSTNode parR = par.getRight();
		BSTNode parL = par.getLeft();
		BSTNode gp = par.getParent();

		// checks if the current parent node is the right child of the grandparent
		if (par == gp.getRight()) {

			// in the case that the gp is the root of the tree
			if (gp.getParent() == null) {
				// sets both children of the root equal to black
				gp.getLeft().setRed(false);
				par.setRed(false);
			}

			else {// sets the colors of the four nodes at hand so the top and bottom nodes are
					// red, and anything in between is black
				gp.setRed(true);
				par.setRed(false);
				gp.getLeft().setRed(false);
				curr.setRed(true);
			}
		}

		// case for the parent being the left child of the root node of the subtree
		else {

			// in the case that the gp is the root of the tree
			if (gp.getParent() == null) {
				// sets both children of the root equal to black
				gp.getRight().setRed(false);
				par.setRed(false);
			}

			else {// sets the colors of the four nodes at hand so the top and bottom nodes are
					// red, and anything in between is black
				gp.setRed(true);
				par.setRed(false);
				gp.getRight().setRed(false);
				curr.setRed(true);
			}
		}
	}

	/**
	 * Helper method that removes the given node from a tree
	 * 
	 * @param curr node that is currently being compared to
	 * @param comp the node to be removed
	 */
	private static void removeHelp(BSTNode curr, BSTNode comp) {
		// checks if the current node is equal to the node to be removed
		if (curr.getKey().compareTo(comp.getKey()) == 0) {

			// checks if the current node is a leaf with no children
			if (curr.getLeft() == null && curr.getRight() == null) {
				curr = null;// removes the node
				return;
			}

			/*
			 * checks if there is a present right child or left child of the node, one not
			 * the other. This covers the case of in order predecessors and successors in
			 * certain situations
			 */
			if (curr.getLeft() == null && curr.getRight() != null) {
				BSTNode temp = curr.getRight();// the temp node that will be the right child of the new node to be put
												// in the removed nodes position
				BSTNode newNode = inOrderSucc(temp);// gets the in order successor of the node
				curr = newNode;// sets the current node equal to the in order successor
				newNode.setRight(temp);// sets the right child of the new node to the old right child
				return;
			}
			if (curr.getLeft() != null && curr.getRight() == null) {
				BSTNode temp = curr.getLeft();// the temp node that will be the left child of the new node to be put in
												// the removed nodes position
				BSTNode newNode = inOrderPred(temp);// gets the in order predecessor of the node
				curr = newNode;// sets the current node equal to the in order predecessor
				newNode.setLeft(temp);// sets the right child of the new node to the old right child
				return;
			}

			// This is now removing a node from a tree that has children on either side,
			// using the in order predecessor.
			BSTNode temp = curr.getLeft();// the temp node that will be the left child of the new node to be put in the
											// removed nodes position
			BSTNode newNode = inOrderPred(temp);// gets the in order predecessor of the node
			curr = newNode;// sets the current node equal to the in order predecessor
			newNode.setLeft(temp);// sets the right child of the new node to the old right child
			return;
		}

		// checks if the compared node is greater than the current node
		if (comp.getKey().compareTo(curr.getKey()) > 0) {
			removeHelp(curr.getRight(), comp);// recursively calls the method again on the right child of the current
												// node
		}

		// checks if the compared node is less than the current node
		if (comp.getKey().compareTo(curr.getKey()) < 0) {
			removeHelp(curr.getLeft(), comp);// recursively calls the method again on the left child of the current node
		}
	}

	/**
	 * retrieves and removes the reference to the in order predecessor of the given
	 * node for the BST
	 * 
	 * @param curr the node that needs the in order predecessor
	 */
	private static BSTNode inOrderPred(BSTNode curr) {

		// checks if the current nodes right node is null or not and returns the current
		// node if so. also removes that node, given that this will only be used in the
		// remove method
		if (curr.getRight() == null) {
			BSTNode temp = curr;
			curr = curr.getLeft();
			return temp;
		}
		// recursively calls the method again if not
		return inOrderPred(curr.getRight());
	}

	/**
	 * retrieves and removes the reference to the in order predecessor of the given
	 * node for the bst
	 * 
	 * @param curr
	 * @return Node in order successor
	 */
	private static BSTNode inOrderSucc(BSTNode curr) {

		// checks if the current nodes left node is null or not and returns the current
		// node if so. also removes that node, given that this will only be used in the
		// remove method
		if (curr.getLeft() == null) {
			BSTNode temp = curr;
			curr = curr.getRight();
			return temp;
		}
		// recursively calls the method again if not
		return inOrderSucc(curr.getLeft());
	}

	private static void redPropViolation(BSTNode curr) {
		// creates three nodes that reference all the surrounding nodes of the parent
		// for easy access
		if(curr.getParent() == null) {
			return;
		}
		BSTNode par = curr.getParent();
		BSTNode parR = par.getRight();
		BSTNode parL = par.getLeft();
		BSTNode gp = par.getParent();

		if (curr.isRed() && par.isRed()) {
			if (curr == par.getRight()) {
				if (par.getLeft() == null || par.getRight().isRed()) {
					recolor(curr);
					redPropViolation(par);r
					return;
				} else {
					rotateLeft(curr);
					redPropViolation(par);
					return;
				}
			} else {
				if (par.getRight() == null || par.getLeft().isRed()) {
					recolor(curr);
					redPropViolation(par);
					return;
				} else {
					rotateRight(curr);
					redPropViolation(par);
					return;
				}
			}
		}
		return;
	}

	private static int heightHelper(BSTNode node) {
		if (node == null)
			return 0;
		else {
			// gets the depth of the tree with left and right nodes
			int lDepth = heightHelper(node.getLeft());
			int rDepth = heightHelper(node.getRight());

			/* use the larger one */
			if (lDepth > rDepth)
				return (lDepth + 1);
			else
				return (rDepth + 1);
		}
	}

	private void inOrderTraversal(List<K> list, BSTNode node) {

		if (node == null)
			return ;

		// first recur on left subtree
		inOrderTraversal(list, node.getLeft());

		list.add((K) node.getKey());

		// then recur on right subtree
		inOrderTraversal(list, node.getRight());

		return;
	}

	private void preOrderTraversal(List<K> list, BSTNode node) {

		if (node == null)
			return;

		list.add((K) node.getKey());

		// first recur on left subtree
		inOrderTraversal(list, node.getLeft());

		// then recur on right subtree
		inOrderTraversal(list, node.getRight());

		return;
	}

	private void postOrderTraversal(List<K> list, BSTNode node) {

		if (node == null)
			return;

		list.add((K) node.getKey());

		// first recur on left subtree
		inOrderTraversal(list, node.getLeft());

		// then recur on right subtree
		inOrderTraversal(list, node.getRight());

		return;
	}

	private void levelOrderTraversal(List<K> list, BSTNode root, int level){
		if (root == null) 
	        return; 
	    if (level == 1) 
	        list.add((K) root.getKey()); 
	    else if (level > 1) 
	    { 
	        levelOrderTraversal(list,root.getLeft(), level-1); 
	        levelOrderTraversal(list,root.getRight(), level-1); 
	    } 
	}
	
	private List<List<BSTNode>> traverseLevels(BSTNode root) {
	    if (root == null) {
	        return Collections.emptyList();
	    }
	    List<List<BSTNode>> levels = new LinkedList<>();

	    Queue<BSTNode> nodes = new LinkedList<>();
	    nodes.add(root);

	    while (!nodes.isEmpty()) {
	        List<BSTNode> level = new ArrayList<>(nodes.size());
	        levels.add(level);

	        for (BSTNode node : new ArrayList<>(nodes)) {
	            level.add(node);
	            if (node.getLeft() != null) {
	                nodes.add(node.getLeft());
	            }
	            if (node.getRight() != null) {
	                nodes.add(node.getRight());
	            }
	            nodes.poll();
	        }
	    }
	    return levels;
	}
	
	private void printHelper() {
		    List<List<BSTNode>> levels = traverseLevels(root);

		    for (List<BSTNode> level: levels) {
		        for (BSTNode node : level) {
		            System.out.print(node.getKey() + " ");
		        }
		        System.out.println();
		    }
		
	}
}

