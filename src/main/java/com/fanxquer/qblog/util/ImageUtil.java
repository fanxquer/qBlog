package com.fanxquer.qblog.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageUtil {

    String key; //关键字
    List<String> urls; //根据关键字爬取到的url
    int next = 0; //下次获取需要获取的urls引用
    int width = 400; //缩略图宽度

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public ImageUtil(String key) throws IOException {
        this.key = key;
        String xmlSource = getJson(key, 30, 0);
        this.urls = getSrcFromJson(xmlSource);
    }

    /**
     * 获取指定数量图片
     * @param num 获取数量
     * @return
     * @throws IOException
     */
    public List<BufferedImage> getImageWithKey(int num) throws IOException {
        List<BufferedImage> BIs = new ArrayList<>();
        int get = 0;//成功获取到的图片数量
        for (int i = 0;i < urls.size();i++) {
            //如果数量足够，停止运行
            if(get >= num) {
                break;
            }
            //获取下一张图片
            BufferedImage bufferedImage = getNext();
            //如果获取获取成功，保存，get+1
            if (bufferedImage != null) {
                BIs.add(cropImage(getThumbnail(bufferedImage)));
                get++;
            }
        }
        return BIs;
    }

    /**
     * 获取下一张图片
     * @return
     */
    public BufferedImage getNext(){
        try {
            if(urls.get(next).startsWith("http")){
                URL urlObj = null;
                urlObj = new URL(urls.get(next));
                URLConnection con = urlObj.openConnection();
                con.setReadTimeout(2000);
                HttpURLConnection httpCon =(HttpURLConnection) con;
                InputStream in = httpCon.getInputStream();
                BufferedImage bufferedImage = ImageIO.read(in);
                next++;
                return bufferedImage;
            }
        } catch (IOException e) {
            next++;
            return null;
        }
        next++;
        return null;
    }

    /**
     * 根据关键字在百度图库搜索图片，获取返回页面的源码
     * @param key 搜索关键字
     * @param number 每页的图片数量
     * @param page 页数
     * @return
     * @throws IOException
     */
    public static String getJson(String key, int number, int page) throws IOException {
        String url = "http://image.baidu.com/search/avatarjson?tn=resultjsonavatarnew&ie=utf-8&word=" + key + "&cg=star&pn=" + page * 30 + "&rn="+number+"&itg=0&z=0&fr=&width=&height=&lm=-1&ic=0&s=0&st=-1&gsm=" + Integer.toHexString(page * 30);
        Document doc = Jsoup.connect(url).get();
        String xmlSource = doc.toString();
        return xmlSource;
    }

    /**
     * 从源码中获取图片源地址
     * @param xmlSource
     * @return
     * @throws IOException
     */
    public static List<String> getSrcFromJson(String xmlSource) throws IOException {
        List<String> urls = new ArrayList<String>();
        String reg = "objURL\":\"http://.+?\\.(jpeg|png|jpg|bmp)";
        Pattern pattern = Pattern.compile(reg);
        Matcher m = pattern.matcher(xmlSource);
        while (m.find()) {
            String imageURL = m.group().substring(9);
            urls.add(imageURL);
            System.out.println(imageURL);
        }
        return urls;
    }

    /**
     * 从url下载图片
     * @param urls url列表
     * @param index 需要下载的url引用
     * @return
     * @throws IOException
     */
    public static BufferedImage getBIFromUrl(List<String> urls, int index) throws IOException {
        if(urls.get(index).startsWith("http")){
            URL urlObj = new URL(urls.get(index));
            URLConnection con = urlObj.openConnection();
            HttpURLConnection httpCon =(HttpURLConnection) con;
            InputStream in = httpCon.getInputStream();
            BufferedImage bufferedImage = ImageIO.read(in);
//            try {
//                    ImageIO.write(bufferedImage,"jpg",new File("C:\\pokemon\\get1.jpg"));
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
            if (bufferedImage == null) {
                return getBIFromUrl(urls, index+1);
            }
            return bufferedImage;
        }
        return null;
    }

    /**
     * 根据宽度和图片高宽比获取缩略图高度
     * @param bufferedImage 源图片
     * @return
     */
    public int getHeight(BufferedImage bufferedImage) {
        System.out.println(bufferedImage == null? "BI is null": "");
        int height = (int) (bufferedImage.getHeight()/(bufferedImage.getWidth()/width));
        return height;
    }

    /**
     * 生成缩略图
     * @param bufferedImage 源图片
     * @return
     */
    public BufferedImage getThumbnail(BufferedImage bufferedImage) {
        int height = getHeight(bufferedImage);
        Image img = bufferedImage.getScaledInstance(200, height, Image.SCALE_DEFAULT);
        BufferedImage newBI = new BufferedImage(img.getWidth(null), img.getHeight(null),BufferedImage.TYPE_INT_RGB);
        Graphics g = newBI .createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return newBI;
    }

    /**
     * 裁剪图片，获取图片中心部分(考虑到大多数图片白底)
     * @param bufferedImage
     * @return
     */
    public static BufferedImage cropImage(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int startX = (int)(width*0.25);
        int startY = (int)(height*0.25);
        int endX = (int)(width*0.75);
        int endY = (int)(height*0.75);
        BufferedImage result = new BufferedImage(endX - startX, endY - startY, 4);
        for (int x = startX; x < endX; ++x) {
            for (int y = startY; y < endY; ++y) {
                int rgb = bufferedImage.getRGB(x, y);
                result.setRGB(x - startX, y - startY, rgb);
            }
        }
        return result;
    }
}
