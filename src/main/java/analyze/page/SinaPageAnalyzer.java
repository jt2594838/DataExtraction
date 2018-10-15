package analyze.page;

import conf.Topics;
import entity.MyPage;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

// supported topic: military, culture, society, world, finance, sports
public class SinaPageAnalyzer implements PageAnalyzer {
    private Topics topic;

    public SinaPageAnalyzer(Topics topic) {
        this.topic = topic;
    }

    public MyPage analyze(Page page) {
        MyPage ret = new MyPage();

        ret.url = page.getUrl().toString();
        ret.topic = topic.toString();
        Html html = page.getHtml();

        ret.title = Utils.selectTitle(html);
        if (ret.title == null) {
            throw new RuntimeException("Title not found! " + ret.url);
        } else if (ret.title.trim().equals("页面没有找到")) {
            page.setSkip(true);
            return null;
        }
        ret.time = Utils.selectDate(html);
        if (ret.time == null) {
            throw new RuntimeException("Date not found! " + ret.url);
        }
        ret.content = Utils.selectContent(html);
        if (ret.content == null) {
            throw new RuntimeException("Content not found! " + ret.url);
        }

        return ret;
    }
}
