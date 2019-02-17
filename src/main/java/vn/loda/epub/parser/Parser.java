package vn.loda.epub.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import vn.loda.epub.model.LineNovelDocumentBuilder;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface Parser {
  public static interface OnParseListener{
    public void beforeParseNode(LineNovelDocumentBuilder builder, Node node, List<Node> stackTrace);
    public void afterParseNode(LineNovelDocumentBuilder builder, Node node, List<Node> stackTrace);
  }

  public void addOnParseListener(OnParseListener listener);
  public String parse(String str);
  public String parse(InputStream in) throws IOException;
}
