package vn.loda.epub.model;

public class LineNovelDocumentBuilder {
  private StringBuilder sb = new StringBuilder();

  public LineNovelDocumentBuilder open(LineTag tag){
    return addTag(tag, true);
  }

  public LineNovelDocumentBuilder close(LineTag tag){
    return addTag(tag, false);
  }

  public LineNovelDocumentBuilder append(String str){
    sb.append(str);
    return this;
  }

  private LineNovelDocumentBuilder addTag(LineTag tag, boolean isOpen){
    switch (tag) {
      case RUBY:
        sb.append("｜");
        break;
      case RT:
        if(isOpen) sb.append("《");
        else sb.append("》");
        break;
      case DOT:
        if(isOpen) sb.append("《《");
        else sb.append("》》");
        break;
      case BOLD:
        sb.append("_b_");
        break;
      case IMG:
        break;
    }
    return this;
  }

  public String toString(){
    return sb.toString();
  }
}
