/*
We are building an in-memory API to support two use cases.

1) We have users (officers) that wear a GPS device that streams location data to our system in real-time.

2) We also have users (dispatchers) who want to know where a specific officer was at a specific point in time. 
If there is no location at the timestamp specified, then return a location with the closest timestamp.

Example: 
Officer 1: GPS coordinate {1, 4} sent at time = 1
Officer 1: GPS coordinate {2, 5} sent at time = 5
Officer 2: GPS coordinate {9, 8} sent at time = 8
Officer 1: GPS coordinate {3, 6} sent at time = 10

If the dispatcher searches for Officer 1 at time = 8, return GPS coordinate {3, 6} sent at time = 10. 
If the dispatcher searches for Officer 1 at time = 4, return GPS coordinate {2, 5} sent at time = 5.

assumptions
- no overwrite for timestamp
- assume timestamp always goes up

*/
import java.util.*;
class Main {
    class Entry {
        int x, y, timestamp;
        public Entry(int x, int y, int timestamp) {
            this.x = x;
            this.y = y;
            this.timestamp = timestamp;
        }
    }
    
    Map<Integer, List<Entry>> map = new HashMap<>();
    
    //O(1)
    public void add(int officerId, int x, int y, int timestamp) {
        map.putIfAbsent(officerId, new ArrayList<>());
        Entry entry = new Entry(x, y, timestamp);
        map.get(officerId).add(entry);
    }
    
    //O(logN) where N is the number of entries for given officerId
    public Entry get(int officerId, int timestamp) {
        if (!map.containsKey(officerId)) {
            throw new RuntimeException("invalid officerId");   
        }
        
        List<Entry> entries = map.get(officerId);
        int resultIndex = binarySearch(entries, timestamp);
        System.out.println("resultIndex " + resultIndex);
        return entries.get(resultIndex);
    }
    
    private int binarySearch(List<Entry> entries, int timestamp) {
        int left = 0, right = entries.size() - 1;
        while (left + 1 < right) {
            int mid = left + (right - left) / 2;
            if (entries.get(mid).timestamp == timestamp) {
                return mid;
            }
            
            if (entries.get(mid).timestamp < timestamp) {
                left = mid;
            } else {
                right = mid;
            }
        }
        System.out.println("left " + left + " right " + right + " " + timestamp);
        if (timestamp - entries.get(left).timestamp < entries.get(right).timestamp - timestamp) {
            
            return left;
        } else {
            return right;
        }
    }
    
    /*
    Example: 
Officer 1: GPS coordinate {1, 4} sent at time = 1
Officer 1: GPS coordinate {2, 5} sent at time = 5
Officer 2: GPS coordinate {9, 8} sent at time = 8
Officer 1: GPS coordinate {3, 6} sent at time = 10
    
    
If the dispatcher searches for Officer 1 at time = 8, return GPS coordinate {3, 6} sent at time = 10. 
If the dispatcher searches for Officer 1 at time = 4, return GPS coordinate {2, 5} sent at time = 5.

    */
    
    public static void main(String[] args) {
        Main sol = new Main();
        sol.add(1, 1, 4, 1);
        sol.add(1, 2, 5, 5);
        //sol.add(2, 9, 8, 8);
        sol.add(1, 3, 6, 10);
        Entry entry = sol.get(1, 8);
        System.out.println(entry.x + " " + entry.y + " " + entry.timestamp);
        Entry entry2 = sol.get(1, 4);
        System.out.println(entry2.x + " " + entry2.y + " " + entry2.timestamp);
        
        //System.out.println(sol.get(1, 8));
        
        
        System.out.println("hello, world");
    }
}
