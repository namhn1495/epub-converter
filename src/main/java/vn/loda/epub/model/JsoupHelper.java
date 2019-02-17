package vn.loda.epub.model;

import org.jsoup.nodes.Node;

import java.util.List;

public class JsoupHelper {

  public static boolean isRt(Node node){
    return node.nodeName().equals("rt");
  }
  public static boolean isBold(Node node) {
    return (node.attr("class").contains("bold"));
  }

  public static boolean isDot(Node node) {
    return node.attr("class").contains("em-");
  }

  public static boolean haveDot(List<Node> nodes){
    return nodes.stream().anyMatch(JsoupHelper::isDot);
  }

}
