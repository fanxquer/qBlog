package com.fanxquer.qblog.util;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ColorUtil {

    /**
     * 获取列表内所有图片的主导颜色，并对获取的颜色重新聚类
     * @param BIs 图片列表
     * @return
     */
    public static List<ImageNode> getColor(List<BufferedImage> BIs) {
        List<ImageNode> colors = new ArrayList<>();
        for (BufferedImage bufferedImage: BIs) {
            colors.addAll(getColorFromOne(bufferedImage));
        }
        ImageNode[] nodes = colors.toArray(new ImageNode[colors.size()]);
        Kmeans kmeans = new Kmeans(nodes);
        kmeans.iterate();
        kmeans.sort();
        return kmeans.getKPoint();
    }

    /**
     * 获取一张图片的主导颜色，对颜色进行降序排序
     * @param bufferedImage 源图片
     * @return
     */
    public static List<ImageNode> getColorFromOne(BufferedImage bufferedImage) {
        //将图片转换成节点数组
        ImageNode[] nodes = imageToNode(bufferedImage);
        //使用节点数组构造Kmeans聚类对象
        Kmeans kmeans = new Kmeans(nodes);
        //迭代，自动收敛
        kmeans.iterate();
        //对获取的颜色占有量降序排序
        kmeans.sort();
        //获取颜色列表
        List<ImageNode> KPoint = kmeans.getKPoint();
        return KPoint;
    }

    /**
     * 将BufferedImage对象转换成节点数组
     * @param bufferedImage
     * @return
     */
    public static ImageNode[] imageToNode(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        ImageNode[] nodes = new ImageNode[width*height];
        int[] rgb = new int[3];
        for (int i = 0;i < width;i++) {
            for (int j = 0;j < height;j++) {
                ImageNode node = new ImageNode();
                node.x = i;
                node.y = j;
                int pixel1 = bufferedImage.getRGB(i, j);
                rgb[0] = (pixel1 >> 16) & 0xFF;
                rgb[1] = (pixel1 >> 8) & 0xFF;
                rgb[2] = (pixel1) & 0xFF;
                node.r = rgb[0];
                node.g = rgb[1];
                node.b = rgb[2];
                nodes[i*height+j] = node;
            }
        }
        return nodes;
    }

    /**
     * 获取最大的通道值
     * @param rgb
     * @return
     */
    public static int getMax(ImageNode rgb) {
        int max = rgb.r;
        if(rgb.g > max) {
            max = rgb.g;
        }
        if(rgb.b > max) {
            max = rgb.b;
        }
        return max;
    }

    /**
     * 给节点颜色降低饱和度
     * @param node
     * @return
     */
    public static ImageNode reduceSaturation(ImageNode node) {
        int x = 255 - getMax(node);
        if (x > 100) {
            x = 100;
        }
        node.r += x;
        node.g += x;
        node.b += x;
        return node;
    }

    public static ImageNode reduceSaturation2(ImageNode node) {
        int x = 255 - getMax(node);
        ImageNode n = new ImageNode();
        n.r=0;
        n.g=0;
        n.b=0;
        x = (int)((440-Kmeans.getDis(node, n))/440)*x;
        if (x > 100) {
            x = 100;
        }
        node.r += x;
        node.g += x;
        node.b += x;
        return node;
    }

    /**
     * 让深色节点的颜色变淡(改变色相)
     * @param node
     * @return
     */
    public static ImageNode balanceSaturation(ImageNode node) {
        ImageNode node1 = new ImageNode();
        node1.r = 0;
        node1.g = 0;
        node1.b = 0;
        double dis = Kmeans.getDis(node1, node);
        if (dis <= 300) {
            node.r += addRgb(node.r, (int)((300-dis)/300)*50);
            node.g += addRgb(node.g, (int)((300-dis)/300)*50);
            node.b += addRgb(node.b, (int)((300-dis)/300)*50);
        }
        return node;
    }

    /**
     * 通道值加法(通道值最大255)
     * @param x
     * @param n
     * @return
     */
    public static int addRgb(int x, int n) {
        int result = x + n;
        if (result > 255) {
            return 255;
        }
        return result;
    }
}
