package analyze.page;

import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

public class Utils {
    public static String selectTitle(Html html) {
        String title = html.xpath("//title/text()").regex("([^\\|]*)\\|.*", 1).toString();
        return title;
    }

    public static String selectDate(Html html) {
        String date = html.xpath("//*[@id=\"top_bar\"]/div/div[2]/span/text()").toString();
        return date;
    }

    public static String selectContent(Html html) {
        Selectable article = html.xpath("//*[@class=\"article\"]");
        String content = article.smartContent().toString();
        if ("".equals(content))
            content = mySmartContent(article.toString());
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
}
