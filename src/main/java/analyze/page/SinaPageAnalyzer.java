package analyze.page;

import conf.Topics;
import entity.MyPage;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

// supported topic: military, culture, society, world, finance
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
        ret.time = Utils.selectDate(html);
        ret.content = Utils.selectContent(html);

        return ret;
    }
}
