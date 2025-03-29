/**
Question 2

Given a file system structure representing directories and files, return the total size of files in the structure. The follow up to this was to find if the given path exists and then provide the size. 
 */
import java.util.*;

class Solution {
    public static abstract class FSEntry {
        String name;

        private FSEntry(String name) {
            this.name = name;
        }
    }

    public static class Directory extends FSEntry {
        public List<FSEntry> content;

        public Directory(String name, FSEntry... entries) {
            super(name);
            this.content = List.of(entries);
        }
    }

    public static class File extends FSEntry {
        public int size;

        public File(String name, int size) {
            super(name);
            this.size = size;
        }
    }

    public static void main(String[] args) {
        Directory root = new Directory(
                "",
                new Directory(
                        "home",
                        new Directory(
                                "me",
                                new File("foo.txt", 416),
                                new File("metrics.txt", 5892),
                                new Directory(
                                        "src",
                                        new File("site.html", 6051),
                                        new File("site.css", 5892),
                                        new File("data.csv", 332789))),
                        new Directory("you",
                                new File("dict.json", 4913364))),
                new Directory("bin",
                        new File("bash", 618416),
                        new File("cat", 23648),
                        new File("ls", 38704)),
                new Directory(
                        "var",
                        new Directory(
                                "log",
                                new File("dmesg", 1783894),
                                new File("wifi.log", 924818),
                                new Directory(
                                        "httpd",
                                        new File("access.log", 17881),
                                        new File("access.log.0.gz", 4012)))));

        System.out.println(getTotalSize(root, "/"));                     // 8675777
        System.out.println(getTotalSize(root, "/home"));                 // 5264404
        System.out.println(getTotalSize(root, "/bin"));                  // 680768
        System.out.println(getTotalSize(root, "var/"));                  // 2730605
        System.out.println(getTotalSize(root, "/home/me/"));            // 351040
        System.out.println(getTotalSize(root, "/var/log/wifi.log"));    // 924818
    }

    public static int getTotalSize(FSEntry entry, String path) {
      if (path == null || path.length() == 0) {
        return 0;
      }

      if (path.equals("/")) {
        return getTotalSize(entry);
      }

      List<String> parts = Arrays.asList(path.split("/")).stream().filter(p -> !p.isBlank()).toList();
      FSEntry subEntry = findEntry(entry, parts);

      return getTotalSize(subEntry);
    }

    private static FSEntry findEntry(FSEntry current, List<String> parts) {
      for (String part : parts) {
        Directory currDir = (Directory)current;
        for (FSEntry entry : currDir.content) {
          if (entry.name.equals(part)) {
            current = entry;
          }
        }

        if (current == null) {
          throw new IllegalArgumentException("entry not exist");
        }
      }

      return current;
    } 

    private static int getTotalSize(FSEntry entry) {
      if (entry == null) {
        return 0;
      }

      if (entry instanceof File) {
        return ((File)entry).size;
      }

      int totalSize = 0;
      Directory dir = (Directory)entry;
      for (FSEntry subEntry : dir.content) {
        totalSize += getTotalSize(subEntry);
      }
      return totalSize;
    }

    // public static int getTotalSize(FSEntry entry, String path) {
    //     if (path == null || path.length() == 0) return 0;

    //     if (path.equals("/")) {
    //         return getTotalSizeUtil(entry);
    //     }

    //     String[] parts = Arrays.stream(path.split("/"))
    //             .filter(p -> !p.isEmpty())
    //             .toArray(String[]::new);

    //     FSEntry outEntry = findEntry(entry, parts, 0);
    //     return getTotalSizeUtil(outEntry);
    // }

    // // 🛠 Fixed recursive lookup
    // private static FSEntry findEntry(FSEntry current, String[] pathParts, int index) {
    //     if (index == pathParts.length) {
    //         return current;
    //     }

    //     if (current instanceof Directory) {
    //         Directory dir = (Directory) current;
    //         for (FSEntry child : dir.content) {
    //             if (child.name.equals(pathParts[index])) {
    //                 return findEntry(child, pathParts, index + 1);
    //             }
    //         }
    //     }

    //     return null;
    // }

    // private static int getTotalSizeUtil(FSEntry entry) {
    //     if (entry == null) return 0;

    //     if (entry instanceof File) {
    //         return ((File) entry).size;
    //     }

    //     int total = 0;
    //     for (FSEntry child : ((Directory) entry).content) {
    //         total += getTotalSizeUtil(child);
    //     }

    //     return total;
    // }
}
