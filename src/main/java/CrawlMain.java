import conf.Topics;
import crawl.SpiderFactory;
import io.WriterManager;
import us.codecraft.webmagic.Spider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CrawlMain {

    public static void main(String[] args) throws IOException, InterruptedException {
        List<Spider> spiders = new ArrayList<>();
//        for (Topics topic : Topics.values()) {
//            Spider spider = SpiderFactory.get(topic);
//            spiders.add(spider);
//            spider.start();
//        }

        Spider spd = SpiderFactory.get(Topics.WORLD);
        spiders.add(spd);
        spd.start();

        long sleepTime = 1000;
        long deadTime = 0;
        long deadThreshold = 10000;
        while (true) {
            Thread.sleep(sleepTime);
            int threadCnt = 0;
            for (Spider spider : spiders) {
                threadCnt += spider.getThreadAlive();
            }
            if (threadCnt == 0) {
                deadTime += sleepTime;
            } else {
                deadTime = 0;
            }
            if (deadTime >= deadThreshold)
                break;
        }
        Thread.sleep(10000);
        WriterManager.getInstance().closeAll();
        Thread.sleep(5000);
    }
}
