import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LRU {

    public static List<Integer> solve(int N, int Q, String Query) {
        LRUCache lruCache = new LRUCache(N);
        List<Integer> result = new ArrayList<>();
        String[] queries = Query.split(" ");
        int index = 0;

        for (int i = 0; i < Q; i++) {
            String operation = queries[index++];

            if (operation.toUpperCase().equals("SET")) {
                int key = Integer.parseInt(queries[index++]);
                int value = Integer.parseInt(queries[index++]);
                lruCache.put(key, value);
            }
            else if (operation.toUpperCase().equals("GET")) {
                int queryKey = Integer.parseInt(queries[index++]);
                Integer queryResult = lruCache.get(queryKey);
                result.add(queryResult != null ? queryResult : -1);
            }
        }
        return result;
    }

    private static class LRUCache {
        private final int capacity;
        private final Map<Integer, Node> cache;
        private final Node head;
        private final Node tail;

        public LRUCache(int capacity) {
            this.capacity = capacity;
            this.cache = new HashMap<>();
            this.head = new Node(-1,-1,null,null);
            this.tail = new Node(-1,-1,null,head);
            head.next = tail;
        }

        public Integer get(int key) {
            if (cache.containsKey(key)) {
                Node node = cache.get(key);
                node.next.prev = node.prev;
                node.prev.next = node.next;
                node.next = head.next;
                node.prev = head;
                head.next = node;
                node.next.prev = node;
                return node.value;
            }
            return null;
        }

        public void put(int key, int value) {
            if (cache.containsKey(key)) {
                Node node = cache.get(key);
                node.value = value;
                node.next.prev = node.prev;
                node.prev.next = node.next;
                node.next = head.next;
                node.prev = head;
                head.next = node;
                node.next.prev = node;
            } else {
                if (cache.size() >= capacity) {
                    Node toRemove = tail.prev;
                    toRemove.next.prev = toRemove.prev;
                    toRemove.prev.next = toRemove.next;
                    cache.remove(toRemove.key);
                }

                Node newNode = new Node(key, value);
                cache.put(key, newNode);
                newNode.prev = head;
                newNode.next = head.next;
                head.next.prev = newNode;
                head.next = newNode;
            }
        }

        private static class Node {
            private final int key;
            private int value;
            public Node next = null;
            public Node prev = null;

            Node(int key, int value) {
                this.key = key;
                this.value = value;
            }

            Node(int key, int value, Node next, Node prev) {
                this.key = key;
                this.value = value;
                this.next = next;
                this.prev = prev;
            }
        }
    }

    public static void main(String[] args) {
        int N = 2;
        int Q = 5;
        String Query = "Set 1 2 Get 1 Set 2 3 Set 3 1 Get 1";
        List<Integer> result = solve(N, Q, Query);
        System.out.println(result);
    }
}
