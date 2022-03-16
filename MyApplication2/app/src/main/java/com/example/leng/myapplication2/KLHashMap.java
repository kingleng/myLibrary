package com.example.leng.myapplication2;

/**
 * Created by leng on 2021/3/8.
 */
class KLHashMap<K,V> {

    //当前存储数据个数
    private int size;

    //数组长度
    private int length;

    //加载系数
    private float loadFactor = 0.75f;
    //扩容阈值
    private int threshold;
    
    private Node<K,V>[] table;

    public KLHashMap() {
        this(8);
    }

    public static final int MAX_SIZE = 1<<30;

    public KLHashMap(int size) {
        length = getNewSize(size);
        threshold = (int)(length*loadFactor);
        table = new Node[length];
    }

    public int size() {
        return size;
    }

    private int getNewSize(int size){
        int ll = size-1;
        ll |= ll>>>1;
        ll |= ll>>>2;
        ll |= ll>>>4;
        ll |= ll>>>8;
        ll |= ll>>>16;
        ll = (ll < MAX_SIZE)?ll+1:MAX_SIZE;

        return ll;
    }


    public void put(K key, V value){

        int i;
        int hash = hash(key);
        Node<K,V> node;
        if((node = table[i = hash&(table.length-1)]) == null){
            table[i] = new Node(hash,key,value,null);
            size++;
        }else{
            Node<K,V> mN;
            for(int j = 0;;j++){
                if(node.hash == hash && (node.key == key || (node.key!=null && node.key.equals(key)))){
                    node.value = value;
                    break;
                }

                if(node.next == null){
                    mN = new Node<K,V>(hash,key,value,table[i]);
                    table[i] = mN;
                    size++;
                    break;
                }else{
                    node = node.next;
                }
            }



        }


        if(size>threshold){
            resize(length*2);
        }

    }

    public V get(V key){
        int hash = hash(key);
        Node<K,V> node;
        if((node = table[hash&table.length-1]) == null){
            return null;
        }else{
            for(int i=0;;i++){
                if(node.hash == hash && (node.key == key || (node.key!=null && node.key.equals(key)))){
                    return (V)node.value;
                }
                if(node.next == null){
                    break;
                }
                node = node.next;
            }
        }
        return null;
    }

    public V remove(K key){
        int hash = hash(key);
        Node<K,V> node;
        Node<K,V> lastNode;
        if((node = table[hash&(length-1)]) == null){
            node = null;
        }else {
            if(node.hash == hash && (node.key == key || (node.key!=null && node.key.equals(key)))){
                node = table[hash&(length-1)];
                table[hash&(length-1)] = null;
                size--;
            }else {

                for (int i=0;;i++){
                    lastNode = node;
                    node = node.next;
                    if(node.hash == hash && (node.key == key || (node.key!=null && node.key.equals(key)))){
                        lastNode.next = node.next;
                        size--;
                        break;
                    }

                    if(node.next == null){
                        break;
                    }
                }
            }
        }


        return node==null?null:node.value;
    }

    public void clear(){

        if(table!=null && size>0){
            size = 0;
            for(int i=0;i<length;i++){
                table[i] = null;
            }
        }
    }

    public void resize(int size){

        length = getNewSize(size);
        threshold = (int)(length*loadFactor);
        Node<K,V>[] newTable = new Node[length];
        Node<K,V>[] oldTable = table;

        Node<K,V> node;
        Node<K,V> newNode;
        Node<K,V> topNode;
        for(int i=0;i<oldTable.length;i++){
            node = oldTable[i];
            if(node==null){
               continue;
            }

            if((newNode = newTable[node.hash&(length-1)]) == null){
                newTable[node.hash&(length-1)] = new Node<>(node.hash,node.key,node.value,null);
            }else{
                newTable[node.hash&(length-1)] = new Node<>(node.hash,node.key,node.value,newNode);
            }


            while (node.next!=null){

                topNode = newTable[node.hash&(length-1)];
                node = node.next;
                newTable[node.hash&(length-1)] = new Node<>(node.hash,node.key,node.value,topNode);

            }
        }

        table = newTable;

    }


    public int hash(Object key) {
        int hash = key.hashCode();
        return hash ^ (hash >>> 16);
    }

    class Node<K,V>{

        private int hash;
        private K key;
        private V value;
        private Node<K,V> next;

        public Node(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }
    }
}
