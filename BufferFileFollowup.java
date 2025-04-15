import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BufferedFileWriter {
    private final FileWriter fileWriter;
    private final int bufferSizeInChars;
    private final StringBuilder buffer;

    public BufferedFileWriter(File file, int bufferSizeInBytes) throws IOException {
        this.fileWriter = new FileWriter(file, true); // append mode
        this.bufferSizeInChars = bufferSizeInBytes / 2; // assuming each char is 2 bytes
        this.buffer = new StringBuilder();
    }

    /**
     * Write at most numBytes from the input char buffer into the internal buffer.
     * Flushes to disk if the internal buffer becomes full.
     */
    public void write(char[] buf, int numBytes) throws IOException {
        int maxCharsToWrite = numBytes / 2;
        int charsWritten = 0;

        for (char c : buf) {
            if (charsWritten >= maxCharsToWrite) {
                break;
            }

            buffer.append(c);
            charsWritten++;

            if (buffer.length() == bufferSizeInChars) {
                flush();
            }
        }
    }

    /**
     * Flush the internal buffer to disk.
     */
    public void flush() throws IOException {
        if (buffer.length() > 0) {
            System.out.println("Flushing to disk: " + buffer);
            fileWriter.write(buffer.toString());
            fileWriter.flush();
            buffer.setLength(0);
        }
    }

    public void close() throws IOException {
        flush();
        fileWriter.close();
    }
}
