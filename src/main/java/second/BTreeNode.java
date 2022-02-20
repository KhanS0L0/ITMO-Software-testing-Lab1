package second;

import java.util.ArrayList;

class BTreeNode{

    public int[] keys; // Ключи узла
    public int minDeg; // Минимальная степень узла B-дерева
    public BTreeNode[] children; // Список дочерних узлов
    public int num; // Количество ключей узла
    public boolean isLeaf; // Истина, если это листовой узел

    public BTreeNode(int deg,boolean isLeaf){

        this.minDeg = deg; // Указываем минимальную степень узла
        this.isLeaf = isLeaf; // Указываем является ли узел листом (не имеет дочерних узлов)
        this.num = 0; // Указываем количество ключей узла (изначально 0)
        this.keys = new int[2*this.minDeg -1]; // Указываем размер узла (узел имеет не более 2 * MinDeg-1 ключей)
        this.children = new BTreeNode[2*this.minDeg]; // Указываем количество дочерних узлов (узел имеет не более 2 * MinDeg дочерних узлов)

    }

    // Находим индекс первой позиции, равный или больший, чем ключ
    public int findKey(int key){

        int index = 0;
        // Условия выхода из цикла:
        // 1.index == num, то есть сканировать все
        // 2.index < num, т.е. найти ключ или больше ключа
        while (index < num && keys[index] < key)
            ++index;
        return index;

    }

    // Удаляем ключ из узла
    public void remove(int key){

        // В первую очередь нам надо найти ключ
        int index = findKey(key);
        if (index < num && keys[index] == key){
            // Если ключ находится в листовом узле то удаляем ключ из листового узла
            if (isLeaf)
                removeFromLeaf(index);
            // Если ключ отсутствует в листовом узле то идем дальше и удаляем ключ из дочернего узла
            else
                removeFromNonLeaf(index);
        } else{
            // Если узел является листовым узлом, то этот узел не входит в B-дерево
            if (isLeaf){
//                System.out.printf("The key %d is does not exist in the tree\n",key);
                return;
            }

            // Иначе нужный ключ существует в поддереве с корнем в этом узле
            // Этот флаг указывает, существует ли ключ в поддереве с корнем в последнем дочернем узле узла
            // Когда index равно num, сравнивается весь узел, и приравниваем флаг true
            boolean flag = index == num;

            // Когда дочерний узел узла не заполнен, нужно сначала заполнить его
            if (children[index].num < minDeg)
                fill(index);

            // Если последний дочерний узел был объединен, то он должен был быть объединен с предыдущим дочерним узлом
            // Из-за этого мы рекурсивно переходим к (index-1) дочернему узлу.
            // В противном случае мы рекурсивно переходим к (index) дочернему узлу
            // Который теперь имеет как минимум ключей с наименьшей степени
            if (flag && index > num)
                children[index-1].remove(key);
            else
                children[index].remove(key);

        }
    }

    // Удаляем ключ из листового узла
    public void removeFromLeaf(int index){

        // Удаляем ключ с индексом = index
        // После удаления ключа изменяем массив с ключами узла (сдвигаем влево)
        if (num - (index + 1) >= 0)
            System.arraycopy(keys, index + 1, keys, index + 1 - 1, num - (index + 1));
        num --;

    }

    // Удаляем ключ из НЕ листового узла
    public void removeFromNonLeaf(int index){

        int key = keys[index];

        // Если поддерево перед ключом (children [index]) имеет не менее t ключей то мы
        // 1.Находим предшественника key(pred) в поддереве с корнем в children [index]
        // 2.Заменить ключ на pred и рекурсивно удалияем pred в дочерних [index]
        if (children[index].num >= minDeg){
            int pred = getPred(index);
            keys[index] = pred;
            children[index].remove(pred);
        } else if (children[index+1].num >= minDeg){
            // Если у детей [index] меньше ключей, чем у minDeg, проверяем дочерние элементы [index + 1]
            // Если дочерние элементы [index + 1] имеют хотя бы один ключ MinDeg, в поддереве с корнем дочерних элементов [index + 1]
            // Находим преемника ключа succ для рекурсивного удаления succ в дочерних элементах [index + 1]
            int succ = getSucc(index);
            keys[index] = succ;
            children[index+1].remove(succ);
        } else{
            // Если ключи children [index] и children [index + 1] меньше MinDeg
            // 1.Объединяем ключ и дочерние элементы [index + 1] в дочерние элементы [index]
            // 2.children [index] содержит ключ 2t-1
            // 3.Освобождаем дочерние элементы [index + 1], рекурсивно удаляем ключ в children [index]
            merge(index);
            children[index].remove(key);
        }

    }

    // Получаем узел предшественника
    public int getPred(int index){

        // Узелпредшественник должен найти крайний правый узел из левого поддерева
        // Продолжаем двигаться к крайнему правому узлу, пока не достигнем листового узла
        BTreeNode cur = children[index];
        while (!cur.isLeaf)
            cur = cur.children[cur.num];
        return cur.keys[cur.num-1];

    }

    // Получем узел преемника
    public int getSucc(int index){

        // Узел-преемник находится где-то от правого поддерева к левому
        // Продолжаем перемещать крайний левый узел от дочерних [index + 1], пока не достигнем конечного узла
        BTreeNode cur = children[index+1];
        while (!cur.isLeaf)
            cur = cur.children[0];
        return cur.keys[0];

    }

    // Заполняем дочерние элементы [index], у которых меньше ключей MinDeg
    public void fill(int index){

        // Если предыдущий дочерний узел имеет несколько ключей MinDeg-1, заимствовать из них
        if (index != 0 && children[index-1].num >= minDeg)
            borrowFromPrev(index);
        // Последний дочерний узел имеет несколько ключей MinDeg-1, заимствуем от них
        else if (index != num && children[index+1].num >= minDeg)
            borrowFromNext(index);
        else{
            // Объединить потомков [index] и его дочернего узла
            // Если children [index] - последний дочерний узел
            // Объединяем его с предыдущим дочерним узлом, иначе объединить его со следующим дочерним узлом
            if (index != num)
                merge(index);
            else
                merge(index-1);
        }

    }

    // Заимствуем ключ у потомков [index-1] и вставляем его потомкам [index]
    public void borrowFromPrev(int index){

        BTreeNode child = children[index];
        BTreeNode sibling = children[index-1];

        // Последний ключ из дочерних [index-1] переходит к родительскому узлу
        // ключ [index-1] из родительского узла вставляется как первый ключ в дочерних [index]
        // Соседние узлы уменьшаются на единицу, а дочерние увеличивается на единицу
        // дочерние [index] продвигаются вперед
        if (child.num - 1 + 1 >= 0)
            System.arraycopy(child.keys, 0, child.keys, 1, child.num - 1 + 1);

        // Если дочерний узел [index] не является листовым, перемещаем его дочерний узел назад
        if (!child.isLeaf)
            if (child.num + 1 >= 0)
                System.arraycopy(child.children, 0, child.children, 1, child.num + 1);


        // Устанавливаем первый ключ дочернего узла на ключи текущего узла [index-1]
        child.keys[0] = keys[index-1];

        // Устанавливаем последний дочерний узел в качестве первого дочернего узла дочерних элементов [index]
        if (!child.isLeaf)
            child.children[0] = sibling.children[sibling.num];

        // Перемещаем последний ключ брата к последнему из текущего узла
        keys[index-1] = sibling.keys[sibling.num-1];
        child.num += 1;
        sibling.num -= 1;

    }

    // Симметричное заимствование из FromPrev
    public void borrowFromNext(int index){

        BTreeNode child = children[index];
        BTreeNode sibling = children[index+1];

        child.keys[child.num] = keys[index];

        if (!child.isLeaf)
            child.children[child.num+1] = sibling.children[0];

        keys[index] = sibling.keys[0];

        if (sibling.num - 1 >= 0)
            System.arraycopy(sibling.keys, 1, sibling.keys, 0, sibling.num - 1);

        if (!sibling.isLeaf){
            if (sibling.num >= 0)
                System.arraycopy(sibling.children, 1, sibling.children, 0, sibling.num);
        }
        child.num += 1;
        sibling.num -= 1;

    }

    // Объединить children [index + 1] в children [index]
    public void merge(int index){

        BTreeNode child = children[index];
        BTreeNode sibling = children[index+1];

        // Вставляем последний ключ текущего узла в позицию MinDeg-1 дочернего узла
        child.keys[minDeg -1] = keys[index];

        // ключи: children [index + 1] скопированы в children [index]
        if (sibling.num >= 0)
            System.arraycopy(sibling.keys, 0, child.keys, minDeg, sibling.num);

        // children: children [index + 1] скопированы в children [index]
        if (!child.isLeaf){
            if (sibling.num + 1 >= 0)
                System.arraycopy(sibling.children, 0, child.children, minDeg, sibling.num + 1);
        }

        // Перемещаем клавиши вперед, а не зазор, вызванный перемещением ключей [index] к дочерним [index]
        if (num - (index + 1) >= 0)
            System.arraycopy(keys, index + 1, keys, index + 1 - 1, num - (index + 1));
        // Перемещаем соответствующий дочерний узел вперед
        if (num + 1 - (index + 2) >= 0)
            System.arraycopy(children, index + 2, children, index + 2 - 1, num + 1 - (index + 2));

        child.num += sibling.num + 1;
        num--;

    }

    // Вставляем новый ключ в узел
    public void insertNotFull(int key){

        // Инициализируем i индексом самого правого значения
        int i = num -1;

        // Если это листовой узел
        if (isLeaf){
            // Ищем, куда нужно вставить новый ключ
            while (i >= 0 && keys[i] > key){
                keys[i+1] = keys[i];
                i--;
            }
            keys[i+1] = key;
            num = num +1;
        } else{
            // Находим позицию дочернего узла, который нужно вставить
            while (i >= 0 && keys[i] > key) i--;
            if (children[i+1].num == 2 * minDeg - 1){
                // Когда дочерний узел заполнен
                splitChild(i+1,children[i+1]);
                // После разделения ключ в середине дочернего узла перемещается вверх, а дочерний узел разделяется на два
                if (keys[i+1] < key) i++;
            }
            children[i+1].insertNotFull(key);
        }

    }

    // Разделение дочернего узла
    public void splitChild(int i ,BTreeNode y){

        // Сначала создаем узел, содержащий ключи MinDeg-1 y
        BTreeNode z = new BTreeNode(y.minDeg,y.isLeaf);
        z.num = minDeg - 1;

        // Передаем все атрибуты y в z
        if (minDeg - 1 >= 0)
            System.arraycopy(y.keys, minDeg, z.keys, 0, minDeg - 1);

        // Если узел не является листом (конечным узлом)
        if (!y.isLeaf){
            if (minDeg >= 0)
                System.arraycopy(y.children, minDeg, z.children, 0, minDeg);
        }

        y.num = minDeg -1;

        // Вставляем новый дочерний узел в дочерний узел
        if (num + 1 - (i + 1) >= 0)
            System.arraycopy(children, i + 1, children, i + 1 + 1, num + 1 - (i + 1));

        children[i+1] = z;

        // Перемещаем ключ по y к этому узлу
        if (num - i >= 0)
            System.arraycopy(keys, i, keys, i + 1, num - i);

        keys[i] = y.keys[minDeg -1];

        num = num + 1;

    }

    // Аналог toString();
    public ArrayList<Integer> show() {
        int i;
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (i = 0; i < num; i++) {
            if (!isLeaf) arrayList.addAll(children[i].show());

            arrayList.add(keys[i]);
//            System.out.printf(" %d", keys[i]);

        }

        if (!isLeaf)
            arrayList.addAll(children[i].show());
        return arrayList;
    }

    // Поиск ключа в дереве
    public BTreeNode search(int key){
        int i = 0;
        while (i < num && key > keys[i]) i++;

        if(i >= keys.length) return children[i].search(key);
        if (keys[i] == key) return this;
        if (isLeaf) return null;

        return children[i].search(key);
    }
}