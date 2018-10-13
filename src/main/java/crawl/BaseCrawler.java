package crawl;

import analyze.page.PageAnalyzer;
import conf.Configuration;
import conf.Topics;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.concurrent.atomic.AtomicInteger;

import static conf.Configuration.KEY_PROCESSED_PAGE;
import static conf.Configuration.crawlInterval;
import static conf.Configuration.maxRetry;

public class BaseCrawler implements PageProcessor {

    private Site site = Site.me().setRetryTimes(maxRetry).setSleepTime(crawlInterval);
    private String urlPattern;
    private int limit;
    private AtomicInteger index;
    private PageAnalyzer analyzer;

    private Spider master;

    public BaseCrawler(Topics topic, int limit, PageAnalyzer analyzer) {
        this.urlPattern = Configuration.topicUrlPatterns.get(topic);
        this.limit = limit;
        this.analyzer = analyzer;
        this.index = new AtomicInteger(-1);
    }

    public void process(Page page) {
        int currIndex = index.get();
        if (currIndex >= limit) {
            master.stop();
            page.setSkip(true);
            return;
        }
        if (index.get() == -1) {
            // skip the seed page
            index.compareAndSet(-1, 0);
            page.addTargetRequests(page.getHtml().links().regex(urlPattern).all());
            page.setSkip(true);
            return;
        } else {
            page.addTargetRequests(page.getHtml().links().regex(urlPattern).all());
        }
        page.getResultItems().put(KEY_PROCESSED_PAGE, analyzer.analyze(page));
        index.incrementAndGet();

    }

    public Site getSite() {
        return site;
    }

    public void setMaster(Spider master) {
        this.master = master;
    }


}
