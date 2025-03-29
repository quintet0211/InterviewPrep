/**

Question was the following:

Assuming you have these functions:

Delete(path) -> bool: deletes the file or empty directory
IsDirectory(path) -> bool: checks whether filepath is directory or not
GetAllFiles(path) -> List<string>: gets the absolute paths of all files in a directory, including other directories
Implement rm -rf.
define DeleteAllFilesAndDir(path):

How do you code it in a way that prevents out of memory (OOM) errors
 */
import java.util.*;

public class Solution {

    // ----- Mocked system API functions -----

    // Simulates file system structure (dir → children)
    private Map<String, List<String>> fs = new HashMap<>();
    private Set<String> files = new HashSet<>();

    // Mocked: check if path is directory
    public boolean IsDirectory(String path) {
        return fs.containsKey(path);
    }

    // Mocked: get immediate children
    public List<String> GetAllFiles(String path) {
        return fs.getOrDefault(path, Collections.emptyList());
    }

    // Mocked: delete file or empty dir
    public boolean Delete(String path) {
        if (IsDirectory(path) && !GetAllFiles(path).isEmpty()) {
            // Not empty dir → can't delete
            return false;
        }
        System.out.println("Deleted: " + path);
        files.remove(path);
        fs.remove(path);
        return true;
    }

    // recursion cause OOM
    public void DeleteAllFilesAndDirReursion(String path) {
      if (IsDirectory(path)) {
          for (String child : GetAllFiles(path)) {
              DeleteAllFilesAndDir(child); // ❗ recursive call
          }
      }
      Delete(path); // only deletes file or empty dir
    }
    // ----- The actual iterative solution -----

    public void DeleteAllFilesAndDirUseSet(String rootPath) {
        Stack<String> stack = new Stack<>();
        Set<String> visited = new HashSet<>();
        stack.push(rootPath);

        while (!stack.isEmpty()) {
            String current = stack.peek();

            if (!IsDirectory(current) || visited.contains(current)) {
                // If file or all children already pushed → safe to delete
                stack.pop();
                Delete(current);
            } else {
                // First time seeing directory → mark visited and push children
                visited.add(current);
                for (String child : GetAllFiles(current)) {
                    stack.push(child);
                }
            }
        }
    }

    class Entry {
        String path;
        boolean readyToDelete;

        Entry(String path, boolean readyToDelete) {
            this.path = path;
            this.readyToDelete = readyToDelete;
        }
    }

    //Avoid of using Set
    public void DeleteAllFilesAndDir(String rootPath) {
        
        Stack<Entry> stack = new Stack<>();
        stack.push(new Entry(rootPath, false));

        while (!stack.isEmpty()) {
            Entry current = stack.pop();

            if (!IsDirectory(current.path) || current.readyToDelete) {
                Delete(current.path);
            } else {
                // First time seeing directory → push marker + children
                stack.push(new Entry(current.path, true));
                for (String child : GetAllFiles(current.path)) {
                    stack.push(new Entry(child, false));
                }
            }
        }
    }

    // ----- Example Usage -----

    public static void main(String[] args) {
        Solution deleter = new Solution();

        // Set up a mock file system:
        deleter.files.addAll(List.of(
            "/a/file1.txt", "/a/file2.txt",
            "/a/b/file3.txt", "/a/b/c/file4.txt"
        ));

        deleter.fs.put("/a", new ArrayList<>(List.of("/a/file1.txt", "/a/file2.txt", "/a/b")));
        deleter.fs.put("/a/b", new ArrayList<>(List.of("/a/b/file3.txt", "/a/b/c")));
        deleter.fs.put("/a/b/c", new ArrayList<>(List.of("/a/b/c/file4.txt")));

        System.out.println("Starting delete:");
        deleter.DeleteAllFilesAndDir("/a");
    }
}
