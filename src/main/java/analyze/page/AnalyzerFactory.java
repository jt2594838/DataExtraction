package analyze.page;

import conf.Topics;

public class AnalyzerFactory {

    public static PageAnalyzer get(Topics topic) {
        switch (topic) {
            case MILITARY:
            case SPORTS:
            case SOCIETY:
            case WORLD:
            case FINANCE:
                return new SinaPageAnalyzer(topic);
        }
        return null;
    }
}
