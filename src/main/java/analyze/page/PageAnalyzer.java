package analyze.page;

import entity.MyPage;
import us.codecraft.webmagic.Page;

public interface PageAnalyzer {
    MyPage analyze(Page page);

}
