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
        assertEquals("[2]", emptyTree.bypass());
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

    @Test
    void balanceTest(){
        BTree tree = new BTree(2);

        tree.insert(1);
        tree.insert(2);
        tree.insert(3);
        // 1
        //На данном этапе корневой узел заполняется значениями ключей (до 3)
        //При последуещем добавлении ключа дерево должно перестроиться
        assertEquals("[1, 2, 3]", Arrays.toString(tree.root.keys));

        tree.insert(4);
        // 2
        //Проверяем каждый узел дерева, чтобы удостовериться что дерево перебалансировалось правильно
        assertEquals("[2, 0, 0]", Arrays.toString(tree.root.keys));
        assertEquals("[1]", tree.root.children[0].show().toString());
        assertEquals("[3, 4]", tree.root.children[1].show().toString());

        tree.insert(5);
        tree.insert(6);

        //3
        //После добавления ключей 5 и 6 к корневому узлу добавляется ключ из дочернего узла
        //Из-за чего дерево должно перебалансироваться и перестроиться
        //Проверяем каждый узел дерева
        assertEquals("[2, 4, 0]", Arrays.toString(tree.root.keys));
        assertEquals("[1]", tree.root.children[0].show().toString());
        assertEquals("[3]", tree.root.children[1].show().toString());
        assertEquals("[5, 6]", tree.root.children[2].show().toString());

        tree.insert(7);
        tree.insert(8);

        //4
        //после добавления 7 и 8 в корневой узел заполняется до 3 ключей
        //Это означает что при добавлении нового ключа дерево увеличить высоту и перебалансироваться
        //Так же проверяем каждый узел дерева
        assertEquals("[2, 4, 6]", Arrays.toString(tree.root.keys));
        assertEquals("[1]", tree.root.children[0].show().toString());
        assertEquals("[3]", tree.root.children[1].show().toString());
        assertEquals("[5]", tree.root.children[2].show().toString());
        assertEquals("[7, 8]", tree.root.children[3].show().toString());

        tree.insert(9);

        //5
        //После добавления 9 высота дерева равняется 3
        //Проверяем каждый узел дерева
        //У произвольно выбранных узлов проверяем корректность значения num (количество ключей узла)
        //Т.е. не произошло ли переполнение узла и правильно ли высчитывается значение минимальной степени дерева
        assertEquals("[4, 0, 0]", Arrays.toString(tree.root.keys));

        BTreeNode firstChild = tree.search(2);
        assertNotEquals(4, firstChild.num);
        assertEquals(1, firstChild.num);
        assertFalse(firstChild.isLeaf);

        BTreeNode secondChild = tree.search(6);
        assertNotEquals(4, secondChild.num);
        assertEquals(1, secondChild.num);
        assertFalse(secondChild.isLeaf);

        assertEquals("[1]", tree.root.children[0].children[0].show().toString());
        assertEquals("[3]", tree.root.children[0].children[1].show().toString());
        assertEquals("[5]", tree.root.children[1].children[0].show().toString());
        assertEquals("[7, 8, 9]", tree.root.children[1].children[1].show().toString());
        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9]", tree.bypass());

        tree.remove(6);

        //6
        //После удаления ключа 6(единственный ключ в узле) дерево должно перестроиться
        //При этом вместо 6, в соответствии с алгоритмом, должен быть выбран новый ключ из дочерних узлов
        //После чего дерево должно перестроиться
        assertEquals("[2, 4, 7]", Arrays.toString(tree.root.keys));
        assertEquals("[1]", tree.root.children[0].show().toString());
        assertEquals("[3]", tree.root.children[1].show().toString());
        assertEquals("[5]", tree.root.children[2].show().toString());
        assertEquals("[8, 9]", tree.root.children[3].show().toString());

        //7
        //Добавляем 6 чтобы вывести ключ 4 в корневой узел и попробуем удолить корневой узел
        tree.insert(6);
        tree.remove(4);

        //8
        //После удаления корневого узла проверяем правильность построения дерева
        assertEquals("[2, 5, 7]", Arrays.toString(tree.root.keys));
        assertEquals("[1]", tree.root.children[0].show().toString());
        assertEquals("[3]", tree.root.children[1].show().toString());
        assertEquals("[6]", tree.root.children[2].show().toString());
        assertEquals("[8, 9]", tree.root.children[3].show().toString());
    }
}
