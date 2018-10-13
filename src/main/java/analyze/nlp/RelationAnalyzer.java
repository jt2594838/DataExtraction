package analyze.nlp;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelationAnalyzer {
    private StanfordCoreNLP pipeline;

    public RelationAnalyzer() {
        String props="StanfordCoreNLP-chinese.properties";
        pipeline = new StanfordCoreNLP(props);
    }

    public Map<String, List<Pair<String, String>>> analyze(String src) {
        CoreDocument doc = new CoreDocument(src);
        pipeline.annotate(doc);

        Map<String, List<Pair<String, String>>> relMap = new HashMap<>();

        List<CoreSentence> tokens = doc.sentences();
        tokens.forEach(stc -> {
            List<RelationTriple> relations = stc.relations();
            relations.forEach(rel -> {
                List<String> relList = new ArrayList<>();
                List<String> objectList = new ArrayList<>();
                List<String> subjectList = new ArrayList<>();
                rel.relation.forEach(r -> relList.add(r.word()));
                rel.object.forEach(o -> objectList.add(o.word()));
                rel.subject.forEach(s -> subjectList.add(s.word()));
                String relStr = String.join(",", relList);
                String objStr = String.join(",", objectList);
                String subStr = String.join(",", subjectList);

                relMap.computeIfAbsent(relStr, k -> new ArrayList<>()).add(new Pair<>(objStr, subStr));
            });
        });

        return relMap;
    }
}
