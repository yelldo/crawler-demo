package us.codecraft.webmagic.samples;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 花瓣网-预览小图片的抓取
 * luzy
 */
public class _HuaBanProcessor implements PageProcessor {
    //这种写法，这里还不乐意了，匹配不了   ？和（ 不需要转义
    //public static final String URL_LIST = "http://huaban\\.com/\\(boards/\\d+/\\)\\?";

    //public static final String URL_LIST = "http://huaban\\.com/(boards/\\d+)?";
    public static final String URL_LIST = "\\w*/(boards/\\d+)?";

    //public static final String URL_POST = "http://huaban\\.com/pins/13\\\\d{8}/";
    public static final String URL_POST = "\\w*/pins/13\\d{8}/";

    private Site site = Site
            .me()
            .setDomain("huaban.com")
            .setSleepTime(3000)
            .setUserAgent(
                    "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");

    @Override
    public void process(Page page) {
        //列表页
        if (page.getUrl().regex(URL_LIST).match()) {
            page.addTargetRequests(page.getHtml()
                    .xpath("//div[@class='side-part']/div[@class='board_pins_waterfall']")
                    .links().regex(URL_POST).all());
            page.addTargetRequests(page.getHtml().links().regex(URL_LIST).all());
            //文章页
        } else {
            page.putField("img",page.getHtml().xpath("//div[@class='pin-view-wrapper']/img/@src").get());
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new _HuaBanProcessor())
                .addUrl("http://huaban.com/boards/33939568/")
                //.addPipeline(new JsonFilePipeline("D:\\webmagic\\"))
                .addPipeline(new JsonFilePipeline("E:\\git_download\\Crawler_Projects"))
                .run();
    }
}
