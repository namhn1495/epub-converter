package vn.loda.epub.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import vn.loda.epub.model.JsoupHelper;
import vn.loda.epub.model.LineNovelDocumentBuilder;
import vn.loda.epub.model.LineTag;

import static vn.loda.epub.model.LineTag.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LineNovelParser implements Parser {


  List<OnParseListener> listeners = new ArrayList<>();

  public void addOnParseListener(OnParseListener listener){
    listeners.add(listener);
  }

  private void notifyBefore(LineNovelDocumentBuilder builder, Node node, List<Node> stackTrace){
    for (OnParseListener listener : listeners) {
      listener.beforeParseNode(builder, node, stackTrace);
    }
  }
  private void notifyAfter(LineNovelDocumentBuilder builder, Node node, List<Node> stackTrace){
    for (OnParseListener listener : listeners) {
      listener.afterParseNode(builder, node, stackTrace);
    }
  }


  @Override
  public String parse(String str) {
    return doParse(
        new LineNovelDocumentBuilder(),
        Jsoup.parse(str,"", org.jsoup.parser.Parser.xmlParser()),
        null
    );
  }

  @Override
  public String parse(InputStream in) throws IOException {
    return doParse(
        new LineNovelDocumentBuilder(),
        Jsoup.parse(in,"UTF-8","", org.jsoup.parser.Parser.xmlParser()),
        null
    );
  }

  private void onRuby(LineNovelDocumentBuilder builder, Node node, List<Node> stackTrace) {
    boolean parentIsDot = JsoupHelper.haveDot(stackTrace);
    if (parentIsDot) {
      builder.close(DOT);
    }

    builder.open(RUBY);

    doParse(builder, node, stackTrace);

    if (parentIsDot) {
      builder.open(DOT);
    }
  }

  private void onTag(LineTag tag, LineNovelDocumentBuilder builder, Node node, List<Node> stackTrace) {
    builder.open(tag);
    doParse(builder, node, stackTrace);
    builder.close(tag);
  }

  private void onDot(LineNovelDocumentBuilder builder, Node node, List<Node> stackTrace) {
    Node parent = stackTrace.get(stackTrace.size() - 1);
    boolean parentIsNotRt = !JsoupHelper.isRt(parent);

    if (parentIsNotRt) {
      builder.open(DOT);
    }

    doParse(builder, node, stackTrace);

    if (parentIsNotRt) {
      builder.close(DOT);
    }
  }

  private String doParse(LineNovelDocumentBuilder builder,Node parent, List<Node> parents) {
    if(parents == null) parents = new ArrayList<>();
    parents.add(parent);

    for (Node child : parent.childNodes()) {
//      System.out.println("trace: "+child);
      notifyBefore(builder,child,parents);
      switch (of(child)) {
        case RUBY:
          onRuby(builder,child,parents);
          break;
        case RT:
          onTag(RT,builder,child,parents);
          break;
        case BOLD:
          onTag(BOLD,builder,child,parents);
          break;
        case DOT:
          onDot(builder,child,parents);
          break;
        case IMG:
        case TEXT:
          builder.append(child.toString());
          break;
        default:
          doParse(builder, child, parents);
          break;
      }
      notifyAfter(builder,child,parents);
    }
    return builder.toString();
  }
}
