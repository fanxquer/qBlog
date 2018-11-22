package com.fanxquer.qblog;

import com.fanxquer.qblog.util.ColorUtil;
import com.fanxquer.qblog.util.ImageNode;
import com.fanxquer.qblog.util.ImageUtil;
import com.fanxquer.qblog.util.Kmeans;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QBlogApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UtilTest {
    @Test
    public void getColor() throws IOException {
        Date date1 = new Date();
        ImageUtil imageUtil = new ImageUtil("树");
        List<BufferedImage> BIs = imageUtil.getImageWithKey(10);
        for (int i = 0;i < BIs.size();i++) {
            try {
                ImageIO.write(BIs.get(i),"jpg",new File("C:\\pokemon\\get"+i+".jpg"));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        for (BufferedImage bufferedImage: BIs) {
            List<ImageNode> nodes = ColorUtil.getColorFromOne(bufferedImage);
            for (ImageNode node: nodes) {
                System.out.println("Result:  r:"+node.r+" g:"+node.g+" b:"+node.b);
            }
        }
//        List<ImageNode> colors = ColorUtil.getColor(BIs);
//        for (ImageNode node: colors) {
//            System.out.println("r:"+node.r+" g:"+node.g+" b:"+node.b);
//        }
        Date date2 = new Date();
        System.out.println("耗时"+(date2.getTime()-date1.getTime())+"ms");
    }

    @Test
    public void testWhite() {
        ImageNode node1 = new ImageNode();
        ImageNode node2 = new ImageNode();
        node1.r = 0;
        node1.g = 0;
        node1.b = 0;
        node2.r = 255;
        node2.g = 255;
        node2.b = 255;
        System.out.println(Kmeans.getDis(node1, node2));
    }

    @Test
    public void testReplace() {
        String s1 = "udhaibd\newads";
        System.out.println(s1);
        s1.replace("\n", "<br>");
        System.out.println(s1);
    }
}
