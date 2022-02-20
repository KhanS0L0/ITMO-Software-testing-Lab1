package second;


import java.util.ArrayList;
import java.util.Arrays;

public class BTree{
    BTreeNode root;
    int MinDeg;

    public BTree(int deg){
        this.root = null;
        this.MinDeg = deg;
    }

    // Обход дерева (аналог toString())
    public String bypass(){
        if (root != null){
            return root.show().toString();
        }
        return null;
    }

    // Поиск ключа во всем дереве
    public BTreeNode search(int key){
        return root == null ? null : root.search(key);
    }

    // Вставка ключа
    public void insert(int key){

        if (root == null){
            root = new BTreeNode(MinDeg,true);
            root.keys[0] = key;
            root.num = 1;
        } else {
            // Если корневой узел заполнится, то дерево станет выше (создаться новый узел в который перейдет часть значений)
            if (root.num == 2*MinDeg-1){
                BTreeNode s = new BTreeNode(MinDeg,false);
                // Старый корневой узел становится дочерним узлом нового корневого узла
                s.children[0] = root;
                // Отделяем старый корневой узел и даем ключ новому узлу
                s.splitChild(0,root);
                // Новый корневой узел имеет 2 дочерних узла, переместим туда старый корневой узел
                int i = 0;
                if (s.keys[0]< key) i++;
                s.children[i].insertNotFull(key);
                root = s;
            } else root.insertNotFull(key);
        }

    }

    // Удаление ключа
    public void remove(int key){

        if (root == null){
//            System.out.println("The tree is empty");
            return;
        }

        root.remove(key);

        // Если у корневого узла 0 ключей
        // Если у него есть дочерний узел, используйте его первый дочерний узел как новый корневой узел,
        // В противном случае установите корневой узел в ноль
        if (root.num == 0){
            if (root.isLeaf) root = null;
            else root = root.children[0];
        }

    }

}
