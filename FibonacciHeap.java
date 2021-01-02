import java.util.Arrays;

/**
 * FibonacciHeap
 *
 * An implementation of fibonacci heap over integers.
 */
public class FibonacciHeap {
	
	public static void main(String[] args) {
		FibonacciHeap heap=new FibonacciHeap();
		for (int i=0;i<17;i++) {
		heap.insert(i);}
		
		heap.deleteMin();
		heap.mayaPrint();
		HeapNode node= heap.firstNode.getChild().getChild();
	    System.out.println("Deliting node: " + node.getKey());
		heap.delete(node);
		heap.mayaPrint();
		
		

		
	}

	public static Integer numCuts = 0;
	public static Integer numLinks = 0;
	private HeapNode firstNode; // pointer to the root of first tree
	private HeapNode minNode; // pointer to the min node
	private int treeCount; // num of trees in the heap
	private int size; // num of nodes in the heap
	private int marked = 0; // num  of marked nodes in the heap

	public FibonacciHeap() {
		firstNode = null;
		minNode = null;
		treeCount = 0;
		size = 0;
		marked = 0;
	}

	public FibonacciHeap(HeapNode root) {
		firstNode = root;
		minNode = root;
		treeCount = 1;
		size = 1;
		marked = 0;
	}
	
	public void print() {
		if (isEmpty()) {
			System.out.println("I'm empty!");
			return;
		}
		HeapNode first = this.firstNode;
		HeapNode node = first;
		if (node.getChild() != null) {
			System.out.println("node: " + node.getKey() + " child: " + node.getChild().getKey() + " childK: "
					+ node.getChild().getRank());
		} else {
			System.out.println("node: " + node.getKey() + " child: " + null);
		}
		node = node.getNext();
		while (node.getKey() != first.getKey()) {
			if (node.getChild() != null) {
				System.out.println("node: " + node.getKey() + " child: " + node.getChild().getKey() + " childK: "
						+ node.getChild().getRank());
			} else {
				System.out.println("node: " + node.getKey() + " child: " + null);
			}
			node = node.getNext();

		}
	}
	
	public void mayaPrint() {
		if (isEmpty()) {
			System.out.println("I'm empty!");
			return;
		}
		System.out.println("numCuts= " + numCuts);
		System.out.println("Marked nodes= " + marked);
		System.out.println("Number of trees= " + treeCount);
		System.out.println("Number of nodes= " + size);
		System.out.println("Min Node= " + findMin().key);
		System.out.println(" "); 
		
	HeapNode node=firstNode;
	do {
		System.out.println("Root: " + node.getKey());
		HeapNode son=node.getChild();
		do {
		printTree(son);
		son=son.getNext();
		}
		while(son!=node.getChild());
		node=node.getNext();
	}
	while (node!=firstNode);
	System.out.println(" "); 
	}
	
	public void printTree(HeapNode x) {
		while(x!= null) {
			HeapNode node=x;
			do {
				System.out.print(" " + node.getKey());
				if (node.getMark()!= 0) System.out.print("(" +node.getMark()+")");
				
				node=node.getNext();
			}
			while (node!=x);
			x=node.getChild();
			//printTree(node);
			System.out.println(" ");
		}
		
		/*
		if (temp.getChild()!= null) {
			 HeapNode temp2=temp.getChild().getNext();
			while (temp2!=temp.getChild()) {
				System.out.println(" Root: " + temp2.getKey());
				printTree(temp2);
				temp2=temp2.getNext();
			}*/
		
		
	}
	
	/**
	 * public boolean isEmpty()
	 *
	 * precondition: none
	 * 
	 * The method returns true if and only if the heap is empty.
	 * 
	 */
	public boolean isEmpty() { //O(1)
		if (this.firstNode == null) {
			return true;
		}
		return false; 
	}

	/**
	 * public HeapNode insert(int key)
	 *
	 * Creates a node (of type HeapNode) which contains the given key, and inserts
	 * it into the heap.
	 * 
	 * Returns the new node created.
	 */
	public HeapNode insert(int key) { //O(1)
		HeapNode newNode = new HeapNode(key);
		if (size == 0) { //if tree is empty
			firstNode = newNode;
			minNode = newNode;
			size = 1; 
			treeCount = 1; 
			return newNode;
		}
		//if tree isn't empty
		this.size++;
		this.treeCount++;
		HeapNode first = this.firstNode; //setting the new node at the beginning of the heap
		newNode.setNext(first);
		newNode.setPrev(first.getPrev());
		first.getPrev().setNext(newNode);
		first.setPrev(newNode);
		this.firstNode = newNode;
		if (key < this.minNode.getKey()) { //updating the min if necessary
			this.minNode = newNode;
		}
		
		return newNode;
	}
	
	
	/**
	 * public void deleteMin()
	 *
	 * Delete the node containing the minimum key.
	 *
	 */
	public void deleteMin() { //O(n) wc
		if (size == 0) { //if tree is empty
			return;
		}else if (size == 1) { //if tree has only one node
			this.minNode = null;
			this.firstNode = null;
			this.size = 0;
			this.treeCount = 0;
			this.marked=0;
			return;
		}
		this.size--;
		if (minNode.getMark()!= 0) this.marked--;
		HeapNode min = this.minNode;
		if (min.getChild() != null) { //if min has a child, delete min and replace its place with its children
			if (min.getKey() == this.firstNode.getKey()) { //if also the first node, change first to its child
				this.firstNode = min.getChild();
			}
			if (min.getNext().getKey() != min.getKey()) {
				min.getNext().setPrev(min.getChild().getPrev());
				min.getPrev().setNext(min.getChild());
				min.getChild().getPrev().setNext(min.getNext());
				min.getChild().setPrev(min.getPrev());
				
			}
			min.getChild().setParent(null);
			min.setChild(null);

		}else {// min is node with rank 0
			if (min.getKey() == this.firstNode.getKey()) {//if also the first node, change first to min.next
				this.firstNode = min.getNext();
			}
			min.getPrev().setNext(min.getNext());
			min.getNext().setPrev(min.getPrev());
		}
		min.setNext(null);
		min.setPrev(null);
		this.minNode = Link(); //Linking the tree, returns the new minimun
		return; 

	}
	
	private HeapNode Link() {// O(n) wc
		HeapNode first = this.firstNode;
		HeapNode currentMin = first;
		HeapNode node = first;
		int numCells = (int) (Math.log10(this.size) / Math.log10(2)) + 1;
		HeapNode[] arr = new HeapNode[numCells];
		arr[node.getRank()] = node;
		node = node.getNext();
		while (node.getKey() != this.firstNode.getKey()) { //while we need to check more nodes
			if (node.getKey() < currentMin.getKey()) {
				currentMin = node;
			}
			if (arr[node.getRank()] == null) { //node from this rank has not been linked yet
				arr[node.getRank()] = node;
				node = node.getNext();
			} else { // node with this rank is ready to be linked
				while (arr[node.getRank()] != null && arr[node.getRank()].getKey() != node.getKey()) {
					int index = node.getRank();
					HeapNode Linked = linkingNodes(arr[index], node);
					arr[index] = null;
					node = Linked;

				}
				arr[node.getRank()] = node;
				node = node.getNext();
			}

		}
		int trees = 0;
		for (HeapNode hn: arr) { //O(logn) - checking how many trees there are
			if (hn != null) {
				trees++;
			}
		}
		this.treeCount = trees;
		return currentMin;
	}
	
	private HeapNode linkingNodes(HeapNode node1, HeapNode node2) { //O(1)
		numLinks++;
		if (node1.getKey() < node2.getKey()) { //changing the nodes so node2 will be the father of node1
			HeapNode tmp = node1;
			node1 = node2;
			node2 = tmp;
		}
		if (node1.getKey() == this.firstNode.getKey()) {//changing the firstNode pointer to the father
			this.firstNode = node2;
		}
		//changing pointers if needed
		if (node2.getNext().getKey() == node1.getKey() || node1.getPrev().getKey() == node2.getKey()) { //node2.next = node1 or node1.prev = node2
			node1.getNext().setPrev(node2);
			node2.setNext(node1.getNext());
		}if (node1.getNext().getKey() == node2.getKey() || node2.getPrev().getKey() == node1.getKey()) { //node1.next = node2 or node2.prev = node1
			node1.getPrev().setNext(node2);
			node2.setPrev(node1.getPrev());
		}		
		
		if (node2.getChild() != null) { //set node1 with new siblings
			HeapNode firstChild = node2.getChild();
			firstChild.getPrev().setNext(node1);
			node1.setPrev(firstChild.getPrev());
			firstChild.setPrev(node1);
			node1.setNext(firstChild);
		}else { //node1 has no new siblings
			node1.setNext(node1);
			node1.setPrev(node1);
		}
		node2.setChild(node1);
		node1.setParent(node2);
		node2.setRank(node2.getRank() + 1);

		return node2;
		
	}

	/**
	 * public HeapNode findMin()
	 *
	 * Return the node of the heap whose key is minimal.
	 *
	 */
	public HeapNode findMin() { //O(1)
		return this.minNode;
	}

	/**
	 * public void meld (FibonacciHeap heap2)
	 *
	 * Meld the heap with heap2
	 *
	 */
	public void meld(FibonacciHeap heap2) { //O(1)
		// melding the heaps
		this.firstNode.getPrev().setNext(heap2.firstNode);
		heap2.firstNode.getPrev().setNext(this.firstNode);
		heap2.firstNode.setPrev(this.firstNode.getPrev());
		this.firstNode.setPrev(heap2.firstNode.getPrev());
		if (heap2.minNode.getKey() < this.minNode.getKey()) { //find new minimum
			this.minNode = heap2.minNode;
		}
		this.size += heap2.size; //fix sizes
		this.treeCount += heap2.treeCount; //fix tree count	
		return; 
	}

	/**
	 * public int size()
	 *
	 * Return the number of elements in the heap
	 * 
	 */
	public int size() {//O(1)
		return this.size; 
	}

	/**
	 * public int[] countersRep()
	 *
	 * Return a counters array, where the value of the i-th entry is the number of
	 * trees of order i in the heap.
	 * 
	 */
	public int[] countersRep() { //O(n) wc
		if (this.size == 0) {
			return new int[0];
		}
		HeapNode node = this.firstNode;
		int numCells = (int) (Math.log10(this.size) / Math.log10(2)) + 1;
		int[] arr = new int[numCells];	
		arr[node.getRank()] = 1;
		node = node.getNext();
		while (node.getKey() != this.firstNode.getKey()) {//for each tree adding 1 to the corresponding place in arr
			int k = node.getRank();
			arr[k]++;
			node = node.getNext();
		}
		return arr; 
	}

	/**
	 * public void delete(HeapNode x)
	 *
	 * Deletes the node x from the heap.
	 *
	 */
	public void delete(HeapNode x) { // O(n)
		if (x!=findMin()) {
		int m = findMin().getKey(); 
		int k = x.getKey();
		decreaseKey(x, (k-m)+1); // making node x to be the min - O(logn)
		System.out.println("-------------Tree after decreasekey-------------");
		mayaPrint();
		System.out.println("------------------------------------------------");
		}
		deleteMin(); // deleting the min node - O(n)
	}

	/**
	 * public void decreaseKey(HeapNode x, int delta)
	 *
	 * The function decreases the key of the node x by delta. The structure of the
	 * heap should be updated to reflect this chage (for example, the cascading cuts
	 * procedure should be applied if needed).
	 */
	public void decreaseKey(HeapNode x, int delta) { // O(logn)
		x.setKey(x.getKey()-delta);
		int k = x.getKey();
		if (k<minNode.getKey()) minNode=x;
		HeapNode parent=x.getParent();
		if (parent!=null) { // checks if x isn't a root
			if (k<parent.getKey()) { // checks if cascading cuts needed
				parent= cut(x);
				while (parent.getMark()==2) { // doing cascading cuts as needed
					if (parent.getParent()!= null) {
						parent=cut(parent);
					} else { // parent is a root
						parent.setMark(0);
						marked--;
					}
				}
				if (parent.getParent()== null && parent.getMark() != 0) { //fixing the root if needed
					parent.setMark(0);
					marked--;
				}
			}
		}
	}
	
	private HeapNode cut(HeapNode x) { //O(1)
		if (x.getParent().getMark() == 0 )marked ++;
		numCuts++;
		HeapNode parent = x.getParent();
		parent.setMark(parent.getMark()+1);
		parent.setRank(parent.getRank()-1);
		// cutting x from the tree: 
		if (x.getNext()==x)parent.setChild(null);
		else {
		x.getPrev().setNext(x.getNext());
		x.getNext().setPrev(x.getPrev());
		x.setParent(null);
		if (parent.getChild()==x) parent.setChild(x.getNext());
		}
		// connecting x to the heap
		x.setNext(firstNode);
		x.setPrev(firstNode.getPrev());
		firstNode.setPrev(x);
		x.getPrev().setNext(x);
		firstNode=x;
		this.treeCount++;
		if (x.getMark()!=0) {
			x.setMark(0);
			marked--;
		}
		return parent;
	}

	/**
	 * public int potential()
	 *
	 * This function returns the current potential of the heap, which is: Potential
	 * = #trees + 2*#marked The potential equals to the number of trees in the heap
	 * plus twice the number of marked nodes in the heap.
	 */
	public int potential() { //O(1)
		return this.treeCount + 2*this.marked; 
	}

	/**
	 * public static int totalLinks()
	 *
	 * This static function returns the total number of link operations made during
	 * the run-time of the program. A link operation is the operation which gets as
	 * input two trees of the same rank, and generates a tree of rank bigger by one,
	 * by hanging the tree which has larger value in its root on the tree which has
	 * smaller value in its root.
	 */
	public static int totalLinks() { //O(1)
		return numLinks ; 
	}

	/**
	 * public static int totalCuts()
	 *
	 * This static function returns the total number of cut operations made during
	 * the run-time of the program. A cut operation is the operation which
	 * diconnects a subtree from its parent (during decreaseKey/delete methods).
	 */
	public static int totalCuts() { //O(1)
		return numCuts; 
	}

	/**
	 * public static int[] kMin(FibonacciHeap H, int k)
	 *
	 * This static function returns the k minimal elements in a binomial tree H. The
	 * function should run in O(k*deg(H)). You are not allowed to change H.
	 */
	public static int[] kMin(FibonacciHeap H, int k) {
		int[] arr = new int[42];
		return arr; // should be replaced by student code
	}

	/**
	 * public class HeapNode
	 * 
	 * If you wish to implement classes other than FibonacciHeap (for example
	 * HeapNode), do it in this file, not in another file
	 * 
	 */
	public class HeapNode {

		public int key;
		private int rank;
		private int mark;
		private HeapNode child;
		private HeapNode next;
		private HeapNode prev;
		private HeapNode parent;

		public HeapNode(int key) {
			this.key = key;
			this.rank = 0;
			this.mark = 0;
			this.child = null;
			this.next = this;
			this.prev = this;
			this.parent = null;

		}

		public int getKey() {
			return this.key;
		}

		public void setKey(int newKey) {
			this.key = newKey;
		}

		public int getRank() {
			return this.rank;
		}

		public void setRank(int newRank) {
			this.rank = newRank;
		}

		public int getMark() {
			return this.mark;
		}

		public void setMark(int newMark) {
			this.mark = newMark;
		}

		public HeapNode getChild() {
			return this.child;
		}

		public void setChild(HeapNode newChild) {
			this.child = newChild;
		}

		public HeapNode getNext() {
			return this.next;
		}

		public void setNext(HeapNode newNext) {
			this.next = newNext;
		}

		public HeapNode getPrev() {
			return this.prev;
		}

		public void setPrev(HeapNode newPrev) {
			this.prev = newPrev;
		}

		public HeapNode getParent() {
			return this.parent;
		}

		public void setParent(HeapNode newParent) {
			this.parent = newParent;
		}

	}
}
