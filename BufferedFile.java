import java.io.FileWriter;
import java.io.IOException;

class BufferedFileWriter {
    private final FileWriter fileWriter;
    private final int bufferSize;
    private final StringBuilder buffer;

    public BufferedFileWriter(String filePath, int bufferSize) throws IOException {
        this.fileWriter = new FileWriter(filePath, true); // append mode
        this.bufferSize = bufferSize;
        this.buffer = new StringBuilder();
    }

    public void write(byte[] bytes) throws IOException {
        // Convert byte array to String (assuming UTF-8)
        String data = new String(bytes);
        System.out.println("write: " + data);

        int index = 0;
        while (index < data.length()) {
          int appendSize = Math.min(bufferSize - buffer.length(), data.length() - index);
          buffer.append(data.substring(index, index + appendSize));
          index += appendSize;
          if (buffer.length() == bufferSize) {
            flush();
          }
        }
    }

    public void flush() throws IOException {
        if (buffer.length() > 0) {
            System.out.println("flushing to disk: " + buffer.toString());
            fileWriter.write(buffer.toString());
            fileWriter.flush();
            buffer.setLength(0); // clear buffer
        }
    }

    // public void close() throws IOException {
    //     flush();         // flush remaining data
    //     fileWriter.close(); // close file writer
    // }
}

class Solution {
    // Example usage
    public static void main(String[] args) throws IOException {
        BufferedFileWriter fileWriter = new BufferedFileWriter("output.txt", 3);
        fileWriter.write("hello ".getBytes());
        fileWriter.write("world! ".getBytes());   // still in buffer
        fileWriter.write("This is a test.".getBytes()); // triggers flush
        fileWriter.write("One more line.".getBytes());
        fileWriter.flush(); // flush remaining
        //fileWriter.close(); // optional
    }
}
