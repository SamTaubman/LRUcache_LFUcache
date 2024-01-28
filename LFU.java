import java.util.*;

public class LFU {

    public static List<Integer> solve(int N, int Q, String Query) {
        LFUCache lfuCache = new LFUCache(N);
        List<Integer> result = new ArrayList<>();
        String[] queries = Query.split(" ");
        int index = 0;

        for (int i = 0; i < Q; i++) {
            String operation = queries[index++];

            if (operation.toUpperCase().equals("SET")) {
                int key = Integer.parseInt(queries[index++]);
                int value = Integer.parseInt(queries[index++]);
                lfuCache.put(key, value);
            } else if (operation.toUpperCase().equals("GET")) {
                int queryKey = Integer.parseInt(queries[index++]);
                Integer queryResult = lfuCache.get(queryKey);
                result.add(queryResult != null ? queryResult : -1);
            }
        }
        return result;
    }

    private static class LFUCache {
        private final int capacity;
        private final Map<Integer, Node> cache;
        private final Map<Integer, LinkedHashSet<Node>> frequencyMap;
        private int minFrequency = 0;

        public LFUCache(int capacity) {
            this.capacity = capacity;
            this.cache = new HashMap<>();
            this.frequencyMap = new HashMap<>();
        }

        public Integer get(int key) {
            if (cache.containsKey(key)) {
                Node node = cache.get(key);
                updateFrequency(node);
                return node.value;
            }
            return null;
        }

        public void put(int key, int value) {
            if (capacity <= 0) {
                return;
            }

            if (cache.containsKey(key)) {
                Node node = cache.get(key);
                node.value = value;
                updateFrequency(node);
            } else {
                if (cache.size() >= capacity) {
                    evictLFU();
                }

                Node newNode = new Node(key, value);
                cache.put(key, newNode);
                addToFrequencyMap(newNode);
                minFrequency = 1;
            }
        }

        private void evictLFU() {
            LinkedHashSet<Node> minFrequencySet = frequencyMap.get(minFrequency);
            if (minFrequencySet != null && !minFrequencySet.isEmpty()) {
                Node toRemove = minFrequencySet.iterator().next();
                minFrequencySet.remove(toRemove);
                cache.remove(toRemove.key);
            }
        }

        private void updateFrequency(Node node) {
            int frequency = node.frequency;
            LinkedHashSet<Node> nodeSet = frequencyMap.get(frequency);
            nodeSet.remove(node);

            if (nodeSet.isEmpty() && frequency == minFrequency) {
                minFrequency++;
            }

            node.frequency++;
            addToFrequencyMap(node);
        }

        private void addToFrequencyMap(Node node) {
            int frequency = node.frequency;
            frequencyMap.computeIfAbsent(frequency, k -> new LinkedHashSet<>()).add(node);
        }

        private static class Node {
            private final int key;
            private int value;
            private int frequency = 1;

            Node(int key, int value) {
                this.key = key;
                this.value = value;
            }
        }
    }

    public static void main(String[] args) {
        int N = 2;
        int Q = 5;
        String Query = "Set 1 2 Get 1 Set 2 3 Set 3 1 Get 2";
        List<Integer> result = solve(N, Q, Query);
        System.out.println(result);
    }
}