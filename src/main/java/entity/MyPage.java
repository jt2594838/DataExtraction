package entity;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

import static conf.Configuration.*;

public class MyPage {
    public long id;

    public String url;
    public String topic;
    public String title;
    public String time;
    public String content;
    public String editor;
    public String source;

    public List<String> imgURLs = new ArrayList<>();

    public Document toXml() {
        Document doc = DocumentHelper.createDocument();
        Element pageEle = doc.addElement(ELE_PAGE);
        Element idEle = pageEle.addElement(ELE_ID);
        Element dateEle = pageEle.addElement(ELE_DATE);
        Element urlEle = pageEle.addElement(ELE_URL);
        Element topicEle = pageEle.addElement(ELE_TOPIC);
        Element titleEle = pageEle.addElement(ELE_TITLE);
        Element contentEle = pageEle.addElement(ELE_CONTENT);
        Element editorEle = pageEle.addElement(ELE_EDITOR);
        Element picsEle = pageEle.addElement(ELE_IMGS);
        Element srcEle = pageEle.addElement(ELE_SRC);

        idEle.addText(String.valueOf(this.id));
        dateEle.addText(this.time);
        urlEle.addText(this.url);
        topicEle.addText(this.topic);
        titleEle.addText(this.title);
        contentEle.addText(this.content);
        editorEle.addText(this.editor);
        srcEle.addText(this.source);
        for (String imgURL : imgURLs) {
            picsEle.addElement(ELE_IMG).addText(imgURL);
        }
        return doc;
    }

    public static MyPage fromXML(Element page) {
        MyPage ret = new MyPage();
        ret.id = Long.parseLong(page.element(ELE_ID).getText());
        ret.content = page.element(ELE_CONTENT).getText();
        ret.time = page.element(ELE_DATE).getText();
        ret.title = page.element(ELE_TITLE).getText();
        ret.topic = page.element(ELE_TOPIC).getText();
        ret.url = page.element(ELE_URL).getText();
        ret.editor = page.elementText(ELE_EDITOR);
        ret.source = page.elementText(ELE_SRC);

        List<Element> picEles = page.element(ELE_IMGS).elements(ELE_IMG);
        for(Element picEle : picEles) {
            ret.imgURLs.add(picEle.getText());
        }

        return ret;
    }
}
