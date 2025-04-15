import java.io.FileWriter;
import java.io.IOException;

public class BufferedFileWriter {
    private final FileWriter fileWriter;
    private final int charBufferSize;
    private final StringBuilder buffer;

    public BufferedFileWriter(String filePath, int charBufferSize) throws IOException {
        this.fileWriter = new FileWriter(filePath, true);
        this.charBufferSize = charBufferSize;
        this.buffer = new StringBuilder();
    }

    public void write(byte[] bytes) throws IOException {
        String data = new String(bytes); // assume UTF-8-compatible input
        for (char c : data.toCharArray()) {
            buffer.append(c);
            if (buffer.length() == charBufferSize) {
                flush();
            }
        }
    }

    public void flush() throws IOException {
        if (buffer.length() > 0) {
            System.out.println("Flushing: " + buffer);
            fileWriter.write(buffer.toString());
            fileWriter.flush();
            buffer.setLength(0);
        }
    }
    
    public static void main(String[] args) {
        try {
            // Create BufferedFileWriter with buffer size of 5 characters
            BufferedFileWriter writer = new BufferedFileWriter("output.txt", 5);

            // Simulate writing chunks of text as byte arrays
            writer.write("hello ".getBytes());           // "hello" triggers flush
            writer.write("world! ".getBytes());          // "world" triggers flush, "!" stays
            writer.write("this is a test".getBytes());   // flushes as needed
            writer.flush();                              // flush any remaining characters

            System.out.println("Write complete. Check output.txt.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
