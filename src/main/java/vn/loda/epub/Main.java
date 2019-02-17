package vn.loda.epub;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Node;
import org.junit.Assert;
import vn.loda.epub.model.LineNovelDocumentBuilder;
import vn.loda.epub.model.LineTag;
import vn.loda.epub.parser.Converter;
import vn.loda.epub.parser.EpubToZipLineNovelConverter;
import vn.loda.epub.parser.LineNovelParser;
import vn.loda.epub.parser.Parser;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

  public static void main(String[] args) throws Exception {
    readFile();
//    hello();
  }

  public static void readFile() throws Exception {
    EpubReader epubReader = new EpubReader();
    Book book = epubReader.readEpub(new FileInputStream("epub_files/3.epub"));
    System.out.println(book.getCoverImage());
    List<ZipEntry> zipEntries = new ArrayList<>();
    File f = new File("test.zip");
    ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
    ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f));

    book.getResources().getAll().forEach(resource -> {
//      System.out.println(resource.getMediaType());
      if (resource.getMediaType().getName().equals("image/jpeg")) {
        System.out.println(resource);
        ZipEntry entry = new ZipEntry(resource.getHref().replace("images/", ""));
//        try {
//          out.putNextEntry(entry);
//          byte[] data = resource.getData();
//          out.write(data, 0, data.length);
//          out.closeEntry();
//        } catch (IOException e) {
//          e.printStackTrace();
//        }

        zipEntries.add(entry);
      }
    });
    out.close();

  }

  public static void hello() throws Exception {
    Parser parser = new LineNovelParser();
    parser.addOnParseListener(new Parser.OnParseListener() {
      @Override
      public void beforeParseNode(LineNovelDocumentBuilder builder, Node node, List<Node> stackTrace) {
        if(LineTag.of(node) == LineTag.IMG){
          System.out.println(node);
        }
      }

      @Override
      public void afterParseNode(LineNovelDocumentBuilder builder, Node node, List<Node> stackTrace) { }
    });
    Converter converter = new EpubToZipLineNovelConverter(parser);

    EpubReader epubReader = new EpubReader();
    Book book = epubReader.readEpub(new FileInputStream("epub_files/3.epub"));
    for (int i = 0; i < book.getContents().size(); i++) {
      Resource content = book.getContents().get(i);
//      System.out.println(new String(content.getData()));

      String output = converter.convert(new String(content.getData()));
//      System.out.println(output);
    }
  }

  public static void main() throws Exception {
    Map<String, String> map = getDatas();
    Converter converter = new EpubToZipLineNovelConverter(new LineNovelParser());

    for (Map.Entry<String, String> entry : map.entrySet()) {
      String result = converter.convert(entry.getKey());
      System.out.println("============");
      System.out.println("Input: " + entry.getKey());
      System.out.println("Output: " + entry.getValue());
      System.out.println("Result: " + result.trim());
      Assert.assertEquals(entry.getValue(), result.trim());
    }
  }



  public static Map<String, String> getDatas() {
    Map<String, String> map = new LinkedHashMap<>();
    map.put("<ruby>紫陽花<rt>あじさい</rt></ruby>", "｜紫陽花《あじさい》");
    map.put(
        "<ruby><img class=\"gaiji\" src=\"../image/gaiji-001.png\" alt=\"\"/><rt>ルビ</rt>",
        "｜<img class=\"gaiji\" src=\"../image/gaiji-001.png\" alt=\"\" />《ルビ》"
    );
    map.put(
        "<ruby>親文字<rt><img class=\"gaiji\" src=\"../image/gaiji-001.png\" alt=\"\"/></rt></ruby>",
        "｜親文字《<img class=\"gaiji\" src=\"../image/gaiji-001.png\" alt=\"\" />》"
    );
    map.put(
        "<ruby><span class=\"tcy\">Ä</span><rt>ルビ</rt></ruby>",
        "｜Ä《ルビ》"
    );
    map.put(
        "<ruby>親文字<rt><span class=\"tcy\">9</span></rt></ruby>",
        "｜親文字《9》"
    );

    map.put(
        "<ruby><span class=\"upright\">É</span><rt>ルビ</rt></ruby>",
        "｜É《ルビ》"
    );

    map.put(
        "<ruby>親文字<rt><span class=\"upright\">È</span></rt></ruby>",
        "｜親文字《È》"
    );

    map.put(
        "<ruby><span class=\"sideways\">あ</span><rt>ルビ</rt></ruby>",
        "｜あ《ルビ》"
    );

    map.put(
        "<ruby>親文字<rt><span class=\"sideways\">あ</span></rt></ruby>",
        "｜親文字《あ》"
    );

    map.put(
        "<ruby>H<span class=\"super\">2</span>O<rt>あいうえお</rt></ruby>",
        "｜H2O《あいうえお》"
    );

    map.put(
        "<ruby>親文字<rt>H<span class=\"super\">2</span>O</rt></ruby>",
        "｜親文字《H2O》"
    );

    map.put(
        "<span class=\"color-red\"><ruby>親文字<rt>ルビ</rt></ruby></span>",
        "｜親文字《ルビ》"
    );

    map.put(
        "<span class=\"bg-red\"><ruby>親文字<rt>ルビ</rt></ruby></span>",
        "｜親文字《ルビ》"
    );

    map.put(
        "<span class=\"inverse\">あ<ruby>親文字<rt>ルビ</rt></ruby>あ</span>",
        "あ｜親文字《ルビ》あ"
    );

    map.put(
        "この<span class=\"em-sesame\">例の文章</span>は<span class=\"em-sesame-open\">サンプル</span>です",
        "この《《例の文章》》は《《サンプル》》です"
    );

    map.put(
        "この<span class=\"em-sesame\">例の<ruby>文章<rt>ルビ</rt></ruby>はサンプルです。</span>",
        "この《《例の》》｜文章《ルビ》《《はサンプルです。》》"
    );

    map.put(
        "<ruby>親文字<rt><span class=\"em-sesame\">ルビ</span></rt></ruby>",
        "｜親文字《ルビ》"
    );
    map.put(
        "<span class=\"bold\">＊<span class=\"tcy\">Ä</span><span class=\"tcy\">MW</span><span class=\"tcy\">999</span></span>",
        "_b_＊ÄMW999_b_"
    );
    map.put(
        "<span class=\"bold\">あいアイ安以ABab12!?ＡＢａｂ１２！？</span>",
        "_b_あいアイ安以ABab12!?ＡＢａｂ１２！？_b_"
    );

    return map;
  }
}
