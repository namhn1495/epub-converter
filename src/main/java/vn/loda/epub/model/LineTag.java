package vn.loda.epub.model;

import org.jsoup.nodes.Node;
import static vn.loda.epub.model.JsoupHelper.*;
public enum LineTag {
  RUBY,
  RT,
  DOT,
  BOLD,
  IMG,
  TEXT,
  UNDENTIFIED;


  public static LineTag of(Node node){
    switch (node.nodeName()){
      case "ruby":
        return LineTag.RUBY;
      case "rt":
        return LineTag.RT;
      case "span":
        if(isBold(node)) return LineTag.BOLD;
        if(isDot(node)) return LineTag.DOT;
        break;
      case "img":
        return LineTag.IMG;
      case "#text":
        return LineTag.TEXT;
    }
    return LineTag.UNDENTIFIED;
  }
}
