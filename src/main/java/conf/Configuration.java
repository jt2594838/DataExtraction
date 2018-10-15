package conf;

import java.util.HashMap;
import java.util.Map;

public class Configuration {

    public static Map<Topics, String> topicSeedUrls = new HashMap<Topics, String>();

    static {

        topicSeedUrls.put(Topics.MILITARY, "https://mil.news.sina.com.cn/");
        topicSeedUrls.put(Topics.SPORTS, "https://sports.sina.com.cn/");
        topicSeedUrls.put(Topics.FINANCE, "https://finance.sina.com.cn/");
        topicSeedUrls.put(Topics.SOCIETY, "https://news.sina.com.cn/society/");
        topicSeedUrls.put(Topics.WORLD, "https://news.sina.com.cn/world/");

    }

    public static Map<Topics, String> topicUrlPatterns = new HashMap<Topics, String>();
    static {
        topicUrlPatterns.put(Topics.MILITARY, "(http(s)?://mil.news.sina.com.cn/.*)");
        topicUrlPatterns.put(Topics.SPORTS, "(http(s)?://sports.sina.com.cn/.*)");
        topicUrlPatterns.put(Topics.FINANCE, "(http(s)?://finance.sina.com.cn/.*)");
        topicUrlPatterns.put(Topics.SOCIETY, "(http(s)?://news.sina.com.cn/s/.*)");
        topicUrlPatterns.put(Topics.WORLD, "(http(s)?://news.sina.com.cn/w/.*)");
    }
    
    public static Map<Topics, String> topicNewsUrlPatterns = new HashMap<Topics, String>();
    static {
        // the first reg is subtopic, and the second reg is date
        topicNewsUrlPatterns.put(Topics.MILITARY, "(http(s)?://mil.news.sina.com.cn/(\\w+/)*\\d+-\\d+-\\d+/.*\\.shtml#?)");
        topicNewsUrlPatterns.put(Topics.SPORTS, "(http(s)?://sports.sina.com.cn/(\\w+/)*\\d+-\\d+-\\d+/.*\\.shtml#?)");
        topicNewsUrlPatterns.put(Topics.SOCIETY, "(http(s)?://news.sina.com.cn/s/(\\w+/)*\\d+-\\d+-\\d+/.*\\.shtml#?)");
        topicNewsUrlPatterns.put(Topics.WORLD, "(http(s)?://news.sina.com.cn/w/(\\w+/)*\\d+-\\d+-\\d+/.*\\.shtml#?)");
        topicNewsUrlPatterns.put(Topics.FINANCE, "(http(s)?://finance.sina.com.cn/(\\w+/)*\\d+-\\d+-\\d+/.*\\.shtml#?)");
    }

    public static final String KEY_PROCESSED_PAGE = "processedPage";

    public static final String ELE_PAGE = "page";
    public static final String ELE_ID = "id";
    public static final String ELE_DATE = "date";
    public static final String ELE_URL = "url";
    public static final String ELE_TOPIC = "topic";
    public static final String ELE_TITLE = "title";
    public static final String ELE_CONTENT = "content";

    public static final String TAG_PAGES = "<pages>";
    public static final String TAG_END_PAGES = "</pages>";

    public static final String PAGE_FILE_NAME = "pages.xml";
    public static final String NER_FILE_NAME = "ner.xml";
    public static final String pagesDir = "pages";
    public static final String nerDir = "ner";

    // crawling
    public static int pageLimit = 300;
    public static int numSpiderThreads = 1;
    public static int maxRetry = 3;
    public static int crawlInterval = 500;
}
