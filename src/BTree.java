public class BTree {
    public final int T;
    public BTNode root;

    public BTree(int t) {
        T = t;
        this.root = new BTNode(true);
        root.n = 0;
    }

    public BTree(int t, BTNode root) {
        T = t;
        this.root = root;
    }

    public BTNode search(int key) {
        return searchHelper(root, key);
    }

    public boolean remove(int key){
        return removeHelper(root, key);
    }

    private void split(BTNode x, int pos, BTNode y) {
        BTNode z = new BTNode(y.isLeaf);
        z.n = T - 1;

        for (int j = 0; j < T - 1; j++) {
            z.keys[j] = y.keys[j + T];
        }

        if (!y.isLeaf) {
            for (int j = 0; j < T; j++) {
                z.children[j] = y.children[j + T];
            }
        }

        y.n = T - 1;

        // Inserting a new child node into a child node
        for (int j = x.n - 1; j >= pos + 1; j--) {
            x.children[j + 1] = x.children[j];
        }
        x.children[pos + 1] = z;

        // Moving the key y to node x
        for (int j = x.n - 2; j >= pos; j--) {
            x.keys[j + 1] = x.keys[j];
        }
        x.keys[pos] = y.keys[T - 1];

        x.n = x.n + 1;
    }

    private void insertNotFull(BTNode node, int key) {

        int count = node.n - 1;

        if (node.isLeaf) {
            while (count >= 0 && node.keys[count] > key) {
                node.keys[count + 1] = node.keys[count];
                count--;
            }
            node.keys[count + 1] = key;
            node.n = node.n + 1;
        } else {
            while (count >= 0 && node.keys[count - 1] > key) {
                count--;
            }
            if (node.children[count] != null && node.children[count].n == 2 * T - 1) {
                split(node, count + 1, node.children[count + 1]);
                // After splitting, the key in the middle of the child node is moved up, and the child node is split into two
                if (node.keys[count + 1] < key)
                    count++;
            }
            if (node.children[count] != null) {
                insertNotFull(node.children[count], key);
            }
        }
    }

    public boolean insert(int key) {
        if (root == null) {
            root = new BTNode(true);
            root.keys[0] = key;
            root.n = 1;
            return true;
        } else {
            if (root.n == 2 * T - 1) {
                BTNode s = new BTNode(false);
                // The old root node becomes a child node of the new root node
                s.children[0] = root;
                // We separate the old root node and give the key to the new node
                split(s, 0, root);
                insertNotFull(s, key);
                root = s;
            } else {
                insertNotFull(root, key);
            }
        }
        return true;
    }

    public void insertToNode(int key, BTNode node) {
        node.keys[node.n] = key;
        node.n = node.n + 1;
        sort(node);
    }

    public void sort(BTNode node) {
        int m;
        for (int i = 0; i < (2 * T - 1); i++) {
            for (int j = i + 1; j <= (2 * T - 1); j++) {
                if ((node.keys[i] != 0) && (node.keys[j] != 0)) {
                    if ((node.keys[i]) > (node.keys[j])) {
                        m = node.keys[i];
                        node.keys[i] = node.keys[j];
                        node.keys[j] = m;
                    }
                } else break;
            }
        }
    }

    public void removeFromNode(int key, BTNode node) {
        if (node == null) {
            return;
        }

        if (searchHelper(node, key) == null) {
            return;
        }

        BTNode ptr = node;
        int position = 0;
        for (int i = 0; i <= node.n - 1; i++) {
            if (key == node.keys[i]) {
                position = i;
                break;
            }
        }
        int positionSon;
        if (ptr.parent != null) {
            for (int i = 0; i <= ptr.parent.n; i++) {
                if (ptr.children[i] == ptr) {
                    positionSon = i;
                    break;
                }
            }
        }
        ptr = ptr.children[position + 1];
        int newkey;
        while (!ptr.isLeaf) {
            ptr = ptr.children[0];
        }
        if (ptr.n > (T - 1)) {
            newkey = ptr.keys[0];
            removeFromNode(newkey, ptr);
            node.keys[position] = newkey;
        } else {
            ptr = node;
            ptr = ptr.children[position];
            newkey = ptr.keys[ptr.n - 1];
            while (!ptr.isLeaf){
                ptr = ptr.children[ptr.n];
            }
            newkey = ptr.keys[ptr.n - 1];
            node.keys[position] = newkey;
            if (ptr.n > (T - 1)) removeFromNode(newkey, ptr);
            else {
                removeLeaf(newkey, ptr);
            }
        }
    }

    private void removeLeaf(int key, BTNode node) {
        if (node == null) {
            return;
        }

        if (searchHelper(node, key) == null) {
            return;
        }
        if ((node == root) && (node.n == 1)) {
            removeFromNode(key, node);
            root.children[0] = null;
            root = null;
            return;
        }
        if (node == root) {
            removeFromNode(key, node);
            return;
        }
        if (node.n > (T - 1)) {
            removeFromNode(key, node);
            return;
        }
        BTNode ptr = node;
        int k1; //k1 - the maximum key of the ... brother
        int k2; //k2 is the key of the parent, less than the one being deleted and more than k1 or conversely
        int position = 0; //key position in the node
        int positionSon = 0; //position of the node relative to the parent
        int i;
        for (i = 0; i <= node.n - 1; i++) {
            if (key == node.keys[i]) {
                position = i;
                break;
            }
        }
        BTNode parent = ptr.parent;
        for (int j = 0; j <= parent.n; j++) {
            if (parent.children[j] == ptr) {
                positionSon = j;
                break;
            }
        }
        if (positionSon == 0) {
            if (parent.children[positionSon + 1].n > (T - 1)) {
                k1 = parent.children[positionSon + 1].keys[0];
                k2 = parent.keys[positionSon];
                insertToNode(k2, ptr);
                removeFromNode(key, ptr);
                parent.keys[positionSon] = k1;
                removeFromNode(k1, parent.children[positionSon + 1]);
            }
        } else {
            if (positionSon == parent.n) {
                if (parent.children[positionSon - 1].n > (T - 1)) {
                    BTNode temp = parent.children[positionSon - 1];
                    k1 = temp.keys[temp.n - 1];
                    k2 = parent.keys[positionSon - 1];
                    insertToNode(k2, ptr);
                    removeFromNode(key, ptr);
                    parent.keys[positionSon - 1] = k1;
                    removeFromNode(k1, temp);
                }
            } else {
                if (parent.children[positionSon + 1].n > (T - 1)) {
                    k1 = parent.children[positionSon + 1].keys[0];
                    k2 = parent.keys[positionSon];
                    insertToNode(k2, ptr);
                    removeFromNode(key, ptr);
                    parent.keys[positionSon] = k1;
                    removeFromNode(k1, parent.children[positionSon + 1]);
                } else {
                    if (parent.children[positionSon - 1].n > (T - 1)) {
                        BTNode temp = parent.children[positionSon - 1];
                        k1 = temp.keys[temp.n - 1];
                        k2 = parent.keys[positionSon - 1];
                        insertToNode(k2, ptr);
                        removeFromNode(key, ptr);
                        parent.keys[positionSon - 1] = k1;
                        removeFromNode(k1, temp);
                    }
                }
            }
        }
    }
    private BTNode searchHelper(BTNode node, int key) { //auxiliary method for search
        int count = -1;
        if (node == null)
            return null;

        for (int i = 0; i < node.n; i++) {
            if (key < node.keys[i]) {
                break;
            }

            if (key == node.keys[i]) {
                return node;
            }
        }
        if (node.isLeaf) {
            return null;
        }
        //Recursively going through the children
        else {
            return searchHelper(node.children[++count], key);
        }
    }

    private boolean removeHelper(BTNode node, int key) { //auxiliary method for remove
        if (node == null) {
            return false;
        }

        if (searchHelper(node, key) == null) {
            return false;
        }

        BTNode ptr = this.root;
        int position;
        int positionSon;
        int i;
        for (i = 0; i <= ptr.n - 1; i++) {
            if (ptr.keys[i] != 0) {
                if (key == ptr.keys[i]) {
                    position = i;
                    break;
                } else {
                    if ((key < ptr.keys[i])) {
                        ptr = ptr.children[i];
                        positionSon = i;
                        i = -1;
                    } else {
                        if (i == (ptr.n - 1)) {
                            ptr = ptr.children[i + 1];
                            positionSon = i + 1;
                            i = -1;
                        }
                    }
                }
            } else {
                break;
            }
        }
        if (ptr.isLeaf) {
            if (ptr.n > (T - 1)) removeFromNode(key, ptr);
            else removeLeaf(key, ptr);
        } else {
            removeHelper(ptr, key);
        }

        return true;
    }


    public class BTNode {
        public int keys[] = new int[2 * T - 1];
        public int n = keys.length; // number of keys in node
        BTNode children[] = new BTNode[n + 1];
        boolean isLeaf = true;
        BTNode parent;

        public BTNode(Boolean isLeaf) {
            this.isLeaf = isLeaf;
        }
    }
}
