package com.cztec.spider.utils.image;

import org.junit.Test;

import java.io.IOException;

public class TestFingerPrint {

    @Test
    public void testCompare() throws IOException {
        System.out.println(ImageComparable.compare("http://mtime-1252014125.picgz.myqcloud.com//img/555/image/115cec46d2eb424d8d2217448f15c90d.jpg","http://img.iwatch365.com/watchimg/s/Rolex/116610LV-0002/1.jpg"));
    }


}
