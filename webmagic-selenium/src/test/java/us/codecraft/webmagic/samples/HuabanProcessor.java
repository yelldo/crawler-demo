package us.codecraft.webmagic.samples;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 花瓣网抽取器。<br>
 * 使用Selenium做页面动态渲染。<br>
 * @author code4crafter@gmail.com <br>
 * Date: 13-7-26 <br>
 * Time: 下午4:08 <br>
 */
public class HuabanProcessor implements PageProcessor {

    private Site site;

    @Override
    public void process(Page page) {
        page.addTargetRequests(page.getHtml().links().regex("http://huaban\\.com/.*").all());
        if (page.getUrl().toString().contains("pins")) {
            page.putField("img", page.getHtml().xpath("//div[@class='image-holder']/a/img/@src").toString());
        } else {
            page.getResultItems().setSkip(true);
        }
    }

    @Override
    public Site getSite() {
        if (null == site) {
            site = Site.me().setDomain("huaban.com").setSleepTime(0);
        }
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new HuabanProcessor()).thread(5)
                //.addPipeline(new FilePipeline("/data/webmagic/test/"))
                .addPipeline(new FilePipeline("E:\\git_download\\Crawler_Projects"))// yelldo
                //.setDownloader(new SeleniumDownloader("/Users/yihua/Downloads/chromedriver"))
                //.setDownloader(new SeleniumDownloader("E:\\chrome_extensions\\selenium\\chromedriver.exe"))// yelldo
                .setDownloader(new SeleniumDownloader("E:\\chrome_extensions\\chromedriver_win32\\chromedriver.exe"))// yelldo
                //.setDownloader(new SeleniumDownloader("E:\\git_download\\Crawler_Projects\\CrawlTest-master\\CrawlTest-master\\CrawlTest\\driver\\chromedriver.exe"))// yelldo
                .addUrl("http://huaban.com/")
                .runAsync();

        // FilePipeline : 保存的文件内容是一个html文本
        // 内容格式如下：
        // url:	http://huaban.com/pins/1045857425
        // img:	null
        // or :
        // url:	http://huaban.com/pins/1285098161/
        // img:	//img.hb.aicdn.com/bbdd333eb838168ee15ca6f6712cdc1f80299e7d6bdce-zG56LG_fw658
    }
}
