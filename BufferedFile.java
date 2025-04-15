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
}
