import analyze.nlp.NERAnalyzer;
import analyze.nlp.RelationAnalyzer;
import io.WriterManager;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task.NERTask;
import task.RELTask;
import utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static conf.Configuration.pagesDir;

public class RELMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(RELMain.class);

    private void run() throws IOException {
        List<String> pageXMLPaths = new ArrayList<>();
        File baseDir = new File(pagesDir);
        Utils.searchDirRecursive(baseDir, "pages.xml", pageXMLPaths);

        RelationAnalyzer analyzer = new RelationAnalyzer();
        ExecutorService threadPool = Executors.newFixedThreadPool(4);

        int i = 0;
        for (String path : pageXMLPaths) {
            RELTask task = new RELTask(path, analyzer, new SAXReader(), i++);
            threadPool.submit(task);
        }
        while (((ThreadPoolExecutor) threadPool).getActiveCount() > 0) {

        }
        WriterManager.getInstance().closeAll();
    }

    public static void main(String[] args) throws IOException {
        RELMain main = new RELMain();
        main.run();
    }
}
