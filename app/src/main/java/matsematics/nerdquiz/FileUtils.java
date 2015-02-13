package matsematics.nerdquiz;

import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * FileUtils can read and write Strings and Ints into a given Stream
 */
public class FileUtils {

  /**
   * readInt32 reads 4 bytes from the given InputStream and returns as an int
   * @param is InputStream to read from
   * @return int from InputStream
   * @throws IOException will be thrown if InputStream is at EOF
   */
  public static int readInt32(InputStream is) throws IOException {
    int int32 = 0;

    for (int i = 0; i < 4; ++i)
      int32 = (int32 << 8) | is.read();

    return int32;
  }

  /**
   * writeInt32 writes an int into the given OutputStream and returns a boolean,
   * if the operation was successful
   * @param os OutputStream to write to
   * @param int32 int to write
   * @return true, if writing was successful
   */
  public static boolean writeInt32(OutputStream os, int int32){
    try {
      for (int i = 3; i >= 0; --i)
        os.write((int32 >>> (8 * i)) & 0xFF);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  /**
   * readString will read a String from an InputStream,
   * where the length (int32) of the String is placed in front of the String
   * @param is InputString to read from
   * @return String from InputStream, null, if operation wasn't successful
   */
  public static String readString(InputStream is) {
    int size;
    try {
      size = readInt32(is);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }

    if (size <= 0)
      return null; // EOF or length not acceptable

    byte line[] = new byte[size];

    try {
      if (is.read(line) == -1)
        return null; // Should never occur, if String was written with writeString
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }

    return new String(line);
  }

  /**
   * writeString writes a given String to a given OutputStream;
   * the length (int32) of the String will be placed in front of the String
   * @param os OutputStream to write to
   * @param str String to write
   * @return true, if operation was successful
   */
  public static boolean writeString(OutputStream os, String str) {
    if (os == null)
      return false;

    try {
      writeInt32(os, str.length());
      os.write(str.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

    public static boolean deleteFile(File f){
       try{
           deleteFile(f);
           return true;
       }catch(Exception e){
           Log.i("deleteFile",e.getMessage());
           return false;
       }
    }
}
