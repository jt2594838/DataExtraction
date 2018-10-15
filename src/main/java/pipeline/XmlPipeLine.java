package pipeline;

import entity.MyPage;
import io.WriterManager;
import org.dom4j.Document;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import static conf.Configuration.KEY_PROCESSED_PAGE;
import static conf.Configuration.PAGE_FILE_NAME;

public class XmlPipeLine implements Pipeline {
    private static final Logger LOGGER = LoggerFactory.getLogger(XmlPipeLine.class);

    private AtomicLong serialNum = new AtomicLong();
    private String baseDir;

    public XmlPipeLine(String baseDir) {
        this.baseDir = baseDir;
    }

    public void process(ResultItems resultItems, Task task) {
        MyPage page = resultItems.get(KEY_PROCESSED_PAGE);
        if (page == null) {
            return;
        }
        LOGGER.info("Pipeline is to write a page, id {}, url {}.", serialNum.get(), page.url);
        page.id = serialNum.getAndIncrement();

        File pageDir = new File(baseDir, page.topic);
        if(!pageDir.exists())
            pageDir.mkdirs();

        File xmlFile = new File(pageDir, PAGE_FILE_NAME);
        XMLWriter xmlWriter;

        try {
           xmlWriter = WriterManager.getInstance().getWriter(xmlFile.getPath());
        } catch (IOException e) {
            LOGGER.error("Cannot open the page file {}.", xmlFile.getPath(), e);
            return;
        }

        Document xml = page.toXml();
        try {
            xmlWriter.write(xml.getRootElement());
        } catch (IOException e) {
            LOGGER.error("Cannot write the page. Title: {}, URL: {}.", page.title, page.url, e);
            return;
        }

        try {
            xmlWriter.flush();
        } catch (IOException e) {
            LOGGER.error("Cannot write the page. Title: {}, URL: {}.", page.title, page.url, e);
        }
    }
}
