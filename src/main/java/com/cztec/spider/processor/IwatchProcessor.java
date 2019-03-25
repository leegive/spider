package com.cztec.spider.processor;

import cn.hutool.core.util.ImageUtil;
import cn.hutool.core.util.ReUtil;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;

public class IwatchProcessor implements PageProcessor {

    private static final String URL_LIST = "http://www\\.iwatch365\\.com/watch/ks0-jg0-lx0-xz0-cz0-bd0-ys0-gn0-xl0-pp0/p\\d+/";
    private static final String URL_POSt = "http://www\\.iwatch365\\.com/\\w+/\\w+/";

    private Site site = Site.me()
            .setDomain("www.iwatch365.com")
            .setRetryTimes(3)
            .setSleepTime(3000)
            .setTimeOut(10000)
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");

    @Override
    public void process(Page page) {
        if (page.getUrl().regex(URL_LIST).match()) {
            page.addTargetRequests(page.getHtml().xpath("//div[@class='item_r_l_in']").links().regex(URL_POSt).all());
            page.addTargetRequests(page.getHtml().links().regex(URL_LIST).all());
        } else {
            page.putField("goodsId", page.getUrl().regex("http://www\\.iwatch365\\.com/\\w+/(\\w+)/").toString());
            page.putField("brandKey", page.getUrl().regex("http://www\\.iwatch365\\.com/(\\w+)/\\w+/").toString());
            try {
                String imageUrl = page.getHtml().xpath("//div[@class='w_pic']/img/@src").toString();
                BufferedImage bi = ImageIO.read(new URL(imageUrl));
                page.putField("imageBase64", ImageUtil.toBase64(bi, imageUrl.substring(imageUrl.lastIndexOf(".") + 1)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            page.putField("imageUrl", page.getHtml().xpath("//div[@class='w_pic']/img/@src").toString());
            String seriesUrl = page.getHtml().xpath("//div[@class='item']/dl/dt[2]/a/@href").toString();
            List<String> seriesRes = ReUtil.findAllGroup0("(?<=-xl)(.*)(?=-)", seriesUrl);
            if (seriesRes != null && seriesRes.size()>0) {
                page.putField("seriesId", seriesRes.get(0));
            }
            page.putField("source", "iwatch365");
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new IwatchProcessor())
                .addUrl("http://www.iwatch365.com/watch/ks0-jg0-lx0-xz0-cz0-bd0-ys0-gn0-xl0-pp0/p1/")
                .addPipeline(new JsonFilePipeline("/Users/lishangjin/IdeaProjects/spider/src/main/java/com/cztec/spider/processor/"))
                .thread(5)
                .run();

    }

}
