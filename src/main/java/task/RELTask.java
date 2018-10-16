package task;

import analyze.nlp.NERAnalyzer;
import analyze.nlp.RelationAnalyzer;
import entity.MyPage;
import io.MyXMLWriter;
import io.WriterManager;
import javafx.util.Pair;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static conf.Configuration.*;

public class RELTask implements Runnable{

    private static final Logger LOGGER = LoggerFactory.getLogger(RELTask.class);

    private String path;
    private RelationAnalyzer analyzer;
    private SAXReader reader;
    private int taskNum;

    public RELTask(String path, RelationAnalyzer analyzer, SAXReader reader, int taskNum) {
        this.path = path;
        this.analyzer = analyzer;
        this.reader = reader;
        this.taskNum = taskNum;
    }

    private List<Element> readPages(String path) throws DocumentException {
        Document document = reader.read(new File(path));
        return (List<Element>) document.getRootElement().elements(ELE_PAGE);
    }

    private MyXMLWriter prepareWriter(String path) throws IOException {
        // prepare writer for ner results
        String outpuPath = path.replace(PAGE_FILE_NAME, REL_FILE_NAME);
        File outputFile = new File(outpuPath);
        outputFile.mkdirs();
        MyXMLWriter writer = null;
        writer = WriterManager.getInstance().getWriter(outpuPath);
        return writer;
    }

    @Override
    public void run() {
        try {
            LOGGER.info("[{}] Processing xml {}", taskNum, path);
            List<Element> pageEles = null;
            try {
                pageEles = readPages(path);
            } catch (DocumentException e) {
                LOGGER.error("[{}]Cannot read page file {}.", taskNum, path, e);
            }

            MyXMLWriter writer;
            try {
                writer = prepareWriter(path);
            } catch (IOException e) {
                LOGGER.error("[{}]Cannot open output file of {}", taskNum, path, e);
                return;
            }

            int pageCnt = 0;
            MyXMLWriter finalWriter = writer;
            for (Element ele : pageEles) {// analyze this page
                MyPage page = MyPage.fromXML(ele);
                pageCnt++;
                LOGGER.info("[{}] Proceesing page {}-{}.", taskNum, pageCnt, page.title);
                String titleWithContent = page.title + "\n" + page.content;
                Map<String, List<Pair<String, String>>> nerMap = analyzer.analyze(titleWithContent);
                LOGGER.info("[{}] Page Analyzed.", taskNum);

                // write the ner results to another xml
                Document doc = DocumentHelper.createDocument();
                Element pageEle = doc.addElement(ELE_PAGE);
                pageEle.addElement(ELE_ID).addText(String.valueOf(page.id));

                for (Map.Entry<String, List<Pair<String, String>>> stringListEntry : nerMap.entrySet()) {
                    StringBuilder field = new StringBuilder();
                    for (Pair<String, String> pair : stringListEntry.getValue()) {
                        field.append("[").append(pair.getKey()).append("#").append(pair.getValue()).append("] ");
                    }
                    Element relation = pageEle.addElement(ELE_REL);
                    relation.addElement(ELE_REL_NAME).addText(stringListEntry.getKey());
                    relation.addElement(ELE_REL_PARTICIPANTS).addText(field.toString());
                }
                LOGGER.info("[{}] Page ner results converted.", taskNum);
                try {
                    finalWriter.write(pageEle);
                    finalWriter.flush();
                } catch (IOException e) {
                    LOGGER.error("[{}] Cannot write page {}", taskNum, page.id, e);
                }
            }
            try {
                writer.close();
            } catch (IOException e) {
                LOGGER.error("[{}] Cannot close writer.", taskNum, e);
            }
            LOGGER.info("[{}] All pages analyzed.", taskNum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
