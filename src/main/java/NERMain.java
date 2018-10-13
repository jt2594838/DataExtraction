import analyze.nlp.NERAnalyzer;
import entity.MyPage;
import io.MyXMLWriter;
import io.WriterManager;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static conf.Configuration.*;

public class NERMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(NERMain.class);

    SAXReader reader = new SAXReader();

    private List<Element> readPages(String path) throws DocumentException {
        Document document = reader.read(new File(path));
        return (List<Element>) document.getRootElement().elements(ELE_PAGE);
    }

    private MyXMLWriter prepareWriter(String path) throws IOException {
        // prepare writer for ner results
        String outpuPath = path.replace(File.separatorChar + pagesDir + File.pathSeparatorChar,
                File.separatorChar + nerDir + File.pathSeparatorChar);
        outpuPath = outpuPath.replace(PAGE_FILE_NAME, NER_FILE_NAME);
        File outputFile = new File(outpuPath);
        outputFile.mkdirs();
        MyXMLWriter writer = null;
        writer = WriterManager.getInstance().getWriter(outpuPath);
        return writer;
    }

    private void run() throws IOException {
        List<String> pageXMLPaths = new ArrayList<>();
        File baseDir = new File(pagesDir);
        Utils.searchDirRecursive(baseDir, ".xml", pageXMLPaths);

        NERAnalyzer analyzer = new NERAnalyzer();

        pageXMLPaths.forEach(path -> {
            List<Element> pageEles = null;
            try {
                pageEles = readPages(path);
            } catch (DocumentException e) {
                LOGGER.error("Cannot read page file {}.", path, e);
            }

            MyXMLWriter writer;
            try {
                writer = prepareWriter(path);
            } catch (IOException e) {
                LOGGER.error("Cannot open output file of {}", path, e);
                return;
            }


            MyXMLWriter finalWriter = writer;
            pageEles.forEach(ele -> {
                // analyze this page
                MyPage page = MyPage.fromXML(ele);
                String titleWithContent = page.title + "\n" + page.content;
                Map<String, List<String>> nerMap = analyzer.analyze(titleWithContent);

                // write the ner results to another xml
                Document doc = DocumentHelper.createDocument();
                Element pageEle = doc.addElement(ELE_PAGE);
                pageEle.addElement(ELE_ID).addText(String.valueOf(page.id));

                nerMap.entrySet().forEach(e -> {
                    pageEle.addElement(e.getKey()).addText(String.join(",", e.getValue()));
                });
                try {
                    finalWriter.write(pageEle);
                    finalWriter.flush();
                } catch (IOException e) {
                    LOGGER.error("Cannot write page {}", page.id, e);
                }
            });
        });

        WriterManager.getInstance().closeAll();
    }

    public static void main(String[] args) throws IOException {
        NERMain main = new NERMain();
        main.run();
    }
}
