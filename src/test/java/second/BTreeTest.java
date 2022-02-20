package second;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("(0_0)")
public class BTreeTest {

    private BTree emptyTree;
    private BTree filledTree;

    @BeforeEach
    public void init() {
        emptyTree = new BTree(2);
        filledTree = new BTree(2);

        filledTree.insert(1);
        filledTree.insert(3);
        filledTree.insert(7);
        filledTree.insert(10);
        filledTree.insert(11);
        filledTree.insert(4);
        filledTree.insert(5);
        filledTree.insert(2);
        filledTree.insert(12);
        filledTree.insert(6);
    }

    @Test
    void emptyRootTest(){
        assertNull(emptyTree.root);
    }

    @Test
    void insertRootEmptyTreeTest(){
        emptyTree.insert(2);
        assertNotNull(emptyTree.root);
        assertEquals(2, emptyTree.root.keys[0]);
    }

    @Test
    void insertRootNonEmptyTreeTest(){
        emptyTree.insert(2);
        emptyTree.insert(3);
        assertNotNull(emptyTree.root);
        assertEquals(2, emptyTree.root.num);
        assertEquals("[2, 3]", emptyTree.bypass());
    }

    @Test
    void insertTest(){
        assertNotNull(filledTree.root);
        assertFalse(filledTree.root.isLeaf);
        assertEquals("[1, 2, 3, 4, 5, 6, 7, 10, 11, 12]", filledTree.bypass());
        assertEquals("[3, 5, 10]", Arrays.toString(filledTree.root.keys));
        assertEquals("[6, 7, 0]", Arrays.toString(filledTree.root.children[2].keys));
    }

    @Test
    void removeTest(){
        assertNotNull(filledTree.root);
        assertFalse(filledTree.root.isLeaf);
        // после инициализации дерево должно выглядеть вот так [1, 2, 3, 4, 5, 6, 7, 10, 11, 12]
        filledTree.remove(3);
        assertEquals("[1, 2, 4, 5, 6, 7, 10, 11, 12]", filledTree.bypass());
        filledTree.remove(4);
        assertEquals("[1, 2, 5, 6, 7, 10, 11, 12]", filledTree.bypass());
        filledTree.remove(4);
        assertEquals("[1, 2, 5, 6, 7, 10, 11, 12]", filledTree.bypass());
    }

    @Test
    void searchTest(){
        BTreeNode searchedNode;

        searchedNode = filledTree.search(7);
        assertTrue(searchedNode.isLeaf);
        assertEquals("[6, 7, 0]", Arrays.toString(searchedNode.keys));

        searchedNode = filledTree.search(5);
        assertFalse(searchedNode.isLeaf);
        assertEquals("[3, 5, 10]", Arrays.toString(searchedNode.keys));

        searchedNode = filledTree.search(11);
        assertTrue(searchedNode.isLeaf);
        assertEquals("[11, 12, 0]", Arrays.toString(searchedNode.keys));

        searchedNode = filledTree.search(12);
        assertTrue(searchedNode.isLeaf);
        assertEquals("[11, 12, 0]", Arrays.toString(searchedNode.keys));

        searchedNode = filledTree.search(14);
        assertNull(searchedNode);
    }

    @Test
    void insertAndRemoveAndSearchWhenNodesEquals(){
        emptyTree.insert(1);
        emptyTree.insert(1);
        emptyTree.insert(1);
        emptyTree.insert(1);
        emptyTree.insert(1);
        assertEquals("[1, 1, 1, 1, 1]", emptyTree.bypass());
        assertEquals("[1, 0, 0]", Arrays.toString(emptyTree.root.keys));
        assertEquals("[1, 1, 1]", Arrays.toString(emptyTree.root.children[0].keys));
        emptyTree.remove(1);
        assertEquals("[1, 1, 1, 1]", emptyTree.bypass());
        assertEquals("[1, 0, 0]", Arrays.toString(emptyTree.search(1).keys));
    }
}
