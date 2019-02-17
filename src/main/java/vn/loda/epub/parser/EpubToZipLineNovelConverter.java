package vn.loda.epub.parser;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EpubToZipLineNovelConverter implements Converter{
  Parser parser;
  private static EpubReader epubReader;

  public EpubToZipLineNovelConverter(Parser parser){
    this.parser = parser;
    if(epubReader != null) epubReader = new EpubReader();
  }

  public String convert(String str){
    return parser.parse(str);
  }

  @Override
  public void convert(InputStream in, OutputStream out) throws IOException {
    Book book = epubReader.readEpub(in);
    for (int i = 0; i < book.getContents().size(); i++) {
      Resource content = book.getContents().get(i);
      String output = parser.parse(content.getInputStream());
    }
  }
}
