package crawl;

import analyze.page.AnalyzerFactory;
import conf.Configuration;
import conf.Topics;
import pipeline.XmlPipeLine;
import us.codecraft.webmagic.Spider;

import static conf.Configuration.numSpiderThreads;
import static conf.Configuration.pageLimit;
import static conf.Configuration.pagesDir;

public class SpiderFactory {

    public static Spider get(Topics topic) {
        BaseCrawler crawler = new BaseCrawler(topic, pageLimit, AnalyzerFactory.get(topic));
        Spider spider = Spider.create(crawler)
                .addUrl(Configuration.topicSeedUrls.get(topic))
                .addPipeline(new XmlPipeLine(pagesDir))
                .thread(numSpiderThreads);
        crawler.setMaster(spider);
        return spider;
    }
}
