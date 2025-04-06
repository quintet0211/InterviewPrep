/*
Modification of LRU cache.
Alice, an engineer, notices that a database for dasher assignments is slowing down due to the number of dashers and entries in the database. She notices that recently assigned dashers are more likely to be looked up and would like to cache these dashers so that lookup is faster.
This database stores the most recent delivery that a dasher made (looking up a dasher returns a single delivery id that they were last assigned). Specifically, she would like the cache to support the following functions:
put(dasher_id: Int, delivery_id: Int). Adds the delivery that a dasher is working into the cache. If the dasher exists in the cache, then the delivery is updated (and this key, value pair is now the most recently used pair)
get(dasher_id: Int). Gets the most recent delivery for a dasher. Returns -1 if the dasher_id is not in the cache
The server only has space to store capacity dasher’s information in the cache. Alice decides that only the most recently updated dashers should be stored in the cache (ones that had put called most recently). When the cache goes above capacity, the oldest entry is removed from the cache. The cache is initialized with the following function:
init(capacity: Int). Initializes the cache with specified capacity

*/

import java.util.*;

public class DasherCache {

    private class Node {
        int dasherId;
        int deliveryId;
        Node prev, next;

        Node(int dasherId, int deliveryId) {
            this.dasherId = dasherId;
            this.deliveryId = deliveryId;
        }
    }

    private int capacity;
    private Map<Integer, Node> cache;
    private Node head, tail;

    public DasherCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();

        // Dummy head and tail
        head = new Node(-1, -1);
        tail = new Node(-1, -1);
        head.next = tail;
        tail.prev = head;
    }

    public int get(int dasherId) {
        if (!cache.containsKey(dasherId)) return -1;

        Node node = cache.get(dasherId);
        moveToHead(node); // mark as recently used
        return node.deliveryId;
    }

    public void put(int dasherId, int deliveryId) {
        if (cache.containsKey(dasherId)) {
            Node node = cache.get(dasherId);
            node.deliveryId = deliveryId;
            moveToHead(node);
        } else {
            if (cache.size() == capacity) {
                // Remove least recently used node (from tail)
                Node lru = tail.prev;
                removeNode(lru);
                cache.remove(lru.dasherId);
            }
            Node newNode = new Node(dasherId, deliveryId);
            cache.put(dasherId, newNode);
            addToHead(newNode);
        }
    }

    // Doubly Linked List helpers

    private void moveToHead(Node node) {
        removeNode(node);
        addToHead(node);
    }

    private void addToHead(Node node) {
        node.prev = head;
        node.next = head.next;

        head.next.prev = node;
        head.next = node;
    }

    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    // Optional: For debugging
    public void printCache() {
        Node curr = head.next;
        while (curr != tail) {
            System.out.print("(" + curr.dasherId + ", " + curr.deliveryId + ") ");
            curr = curr.next;
        }
        System.out.println();
    }

    // Test
    public static void main(String[] args) {
        DasherCache cache = new DasherCache(2);

        cache.put(1, 101);
        cache.put(2, 102);
        System.out.println(cache.get(1)); // 101

        cache.put(3, 103); // Evicts dasher 2
        System.out.println(cache.get(2)); // -1
        System.out.println(cache.get(3)); // 103

        cache.put(1, 104); // Update dasher 1
        System.out.println(cache.get(1)); // 104
    }
}
