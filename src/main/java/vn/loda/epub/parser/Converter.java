package vn.loda.epub.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Converter {
  public void convert(InputStream in, OutputStream out) throws IOException;
  public String convert(String str);
}
