package analyze.page;

import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

public class Utils {
    public static String selectTitle(Html html) {
        String title = html.xpath("//title/text()").regex("([^\\|]*).*", 1).toString();
        if (title == null) {
            title = html.xpath("//body/div[4]/h1[@class=\"main-title\"]/text()").regex("([^\\|]*).*", 1).toString();
        }
        return title;
    }

    public static String selectDate(Html html) {
        String date = html.xpath("//*[@id=\"top_bar\"]/div/div[2]/span/text()").toString();
        if (date == null) {
            date = html.xpath("//*[@id=\"pub_date\"]/text()").toString();
        }
        if (date == null) {
            date = html.xpath("//*[@id=\"wrapOuter\"]/div/div[4]/span/text()").toString();
        }
        if (date == null) {
            date = html.regex(".*published at ([^\\]]*)\\].*", 1).toString();
        }
        return date;
    }

    public static String selectContent(Html html) {
        Selectable article = html.xpath("//*[@class=\"article\"]");
        String content = article.smartContent().toString();
        if ("".equals(content))
            content = mySmartContent(article.toString());
        if (content == null) {
            article = html.xpath("//*[@id=\"artibody\"]");
            content = article.smartContent().toString();
            if ("".equals(content))
                content = mySmartContent(article.toString());
        }
        if (content == null) {
            article = html.xpath("//*[@id=\"mainArticle\"]");
            content = article.smartContent().toString();
            if ("".equals(content))
                content = mySmartContent(article.toString());
        }
        return content;
    }

    private static String mySmartContent(String html) {
        html = html.replaceAll("(?is)<!DOCTYPE.*?>", "");
        html = html.replaceAll("(?is)<!--.*?-->", "");				// remove html comment
        html = html.replaceAll("(?is)<script.*?>.*?</script>", ""); // remove javascript
        html = html.replaceAll("(?is)<style.*?>.*?</style>", "");   // remove css
        html = html.replaceAll("&.{2,5};|&#.{2,5};", " ");			// remove special char
        html = html.replaceAll("(?is)</p>", "\n");
        html = html.replaceAll("(?is)<.*?>", "");

        return html;
    }

    public static List<String> selectPics(Html html) {
        Selectable selectable = html.xpath("//*[@id=\"artibody\"]/*/img/@src");
        if (selectable.all().size() == 0) {
            selectable = html.xpath("//*[@id=\"article\"]/*/img/@src");
        }
        if (selectable.all().size() == 0) {
            selectable = html.xpath("//*[@id=\"mainArticle\"]/*/img/@src");
        }
        return selectable.all();
    }

    public static String selectEditor(Html html) {
        Selectable selectable = html.xpath("//*[@class=\"article-editor\"]/text()");
        return selectable.toString();
    }

    public static String selectSource(Html html) {
        Selectable selectable = html.xpath("//*[@class=\"source ent-source\"]/text()");
        String content = selectable.toString();
        if (content == null) {
            selectable = html.xpath("//*[@class=\"source\"]/text()");
        }
        return selectable.toString();
    }
}
