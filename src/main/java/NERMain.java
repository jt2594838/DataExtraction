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
import task.NERTask;
import utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static conf.Configuration.*;

public class NERMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(NERMain.class);

    private void run() throws IOException {
        List<String> pageXMLPaths = new ArrayList<>();
        File baseDir = new File(pagesDir);
        Utils.searchDirRecursive(baseDir, "pages.xml", pageXMLPaths);

        NERAnalyzer analyzer = new NERAnalyzer();
        ExecutorService threadPool = Executors.newFixedThreadPool(4);

        int i = 0;
        for (String path : pageXMLPaths) {
            NERTask task = new NERTask(path, analyzer, new SAXReader(), i++);
            threadPool.submit(task);
        }
        threadPool.shutdown();
        while (((ThreadPoolExecutor) threadPool).getActiveCount() > 0) {

        }
        WriterManager.getInstance().closeAll();
    }

    public static void main(String[] args) throws IOException {
        NERMain main = new NERMain();
        main.run();
    }
}
