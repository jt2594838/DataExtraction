package analyze.nlp;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotator;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class RelationAnalyzer {
    private static final Logger LOGGER = LoggerFactory.getLogger(RelationAnalyzer.class);
    private StanfordCoreNLP pipeline;

    public RelationAnalyzer() throws IOException {
        String props="E:\\codestore\\DataExtraction\\src\\main\\java\\conf\\StanfordCoreNLP-chinese.properties";
        Properties properties = new Properties();
        properties.load(new BufferedReader(new FileReader(props)));
        pipeline = new StanfordCoreNLP(properties);
        pipeline.addAnnotator(StanfordCoreNLP.getExistingAnnotator(Annotator.STANFORD_RELATION));
    }

    public Map<String, List<Pair<String, String>>> analyze(String src) {
        CoreDocument doc = new CoreDocument(src);
        pipeline.annotate(doc);

        Map<String, List<Pair<String, String>>> relMap = new HashMap<>();

        List<CoreSentence> tokens = doc.sentences();
        LOGGER.debug("Processing doc with {} sentences", tokens.size());
        for (CoreSentence stc : tokens) {
            List<RelationTriple> relations = stc.relations();
            if (relations == null){
                LOGGER.debug("No relation in this sentence.");
                continue;
            }
            LOGGER.debug("Processing sentence {} ", stc.toString());
            LOGGER.debug("This sentence has {} relations", relations.size());
            for (RelationTriple rel : relations) {
                LOGGER.debug("Processing relation {}", rel.toString());
                List<String> relList = new ArrayList<>();
                List<String> objectList = new ArrayList<>();
                List<String> subjectList = new ArrayList<>();
                for (CoreLabel r : rel.relation) {
                    relList.add(r.word());
                }
                for (CoreLabel o : rel.object) {
                    objectList.add(o.word());
                }
                for (CoreLabel s : rel.subject) {
                    subjectList.add(s.word());
                }
                String relStr = String.join(",", relList);
                String objStr = String.join(",", objectList);
                String subStr = String.join(",", subjectList);

                relMap.computeIfAbsent(relStr, k -> new ArrayList<>()).add(new Pair<>(objStr, subStr));
            }
        }

        return relMap;
    }
}
