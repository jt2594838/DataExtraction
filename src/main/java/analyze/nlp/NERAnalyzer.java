package analyze.nlp;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.*;

public class NERAnalyzer {

    private StanfordCoreNLP pipeline;

    public NERAnalyzer() {
        String props="StanfordCoreNLP-chinese.properties";
        pipeline = new StanfordCoreNLP(props);
    }

    public Map<String, List<String>> analyze(String src) {
        CoreDocument doc = new CoreDocument(src);
        pipeline.annotate(doc);

        Map<String, List<String>> nerMap = new HashMap<>();

        List<CoreLabel> tokens = doc.tokens();
        tokens.forEach(tok -> nerMap.computeIfAbsent(tok.ner(), k -> new ArrayList<>()).add(tok.word()));

        return nerMap;
    }
}
