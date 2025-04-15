class Solution {

  static class FileOut {
    public void write(byte[] buf, int len) {
      System.out.write(buf, 0, len);
      System.out.write('\n');
    }
  }

  static class BufferedFile {
    private final FileOut fileOut;
    private final byte[] buffer;
    private int pos;

    BufferedFile(FileOut fw, int bufferSize) {
      this.fileOut = fw;
      this.buffer = new byte[bufferSize];
      this.pos = 0;
    }

    void write(byte[] data) {
      int index = 0;
      while (index < data.length) {
        int spaceLeft = buffer.length - pos;
        int copyLen = Math.min(spaceLeft, data.length - index);

        System.arraycopy(data, index, buffer, pos, copyLen);
        pos += copyLen;
        index += copyLen;

        if (pos == buffer.length) {
          flush();
        }
      }
    }

    void flush() {
      if (pos > 0) {
        fileOut.write(buffer, pos);
        pos = 0;
      }
    }
  }

  // Test the implementation
  public static void main(String[] args) {
    FileOut out = new FileOut();
    BufferedFile bufferedFile = new BufferedFile(out, 8); // buffer size of 8 bytes

    bufferedFile.write("hello ".getBytes());
    bufferedFile.write("world! ".getBytes());
    bufferedFile.write("This is a test.".getBytes());
    bufferedFile.flush(); // manually flush any remaining data
  }
}

//Follow up, if public void write(byte[] buf, int len) 
//change to     public int write(byte[] buf, int len)
//with incorrect number of bytes written, say only 1 byte, and return 1 byte.
//How can we fix this. (See below)

/*
void flush() {
  int offset = 0;

  while (offset < pos) {
    int remaining = pos - offset;
    int bytesWritten = fileOut.write(buffer, remaining);

    // Defensive check in case write returns 0 or less (shouldn't, but just in case)
    if (bytesWritten <= 0) {
      throw new RuntimeException("Failed to flush buffer: write returned " + bytesWritten);
    }

    offset += bytesWritten;

    // Shift remaining bytes to the front if not everything is written
    if (offset < pos) {
      System.arraycopy(buffer, offset, buffer, 0, pos - offset);
      pos = pos - offset;
      offset = 0;
    } else {
      pos = 0;
    }
  }
}
*/
