package com.fanxquer.qblog.util;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class Kmeans {
    private final int K = 6;//质心数量

    ImageNode[] nodes;//样本
    List<ImageNode> KPoint;//质心
    List<List<ImageNode>> clusters;//聚类
    int number;//样本数量

    public ImageNode[] getNodes() {
        return nodes;
    }

    public void setNodes(ImageNode[] nodes) {
        this.nodes = nodes;
    }

    public List<ImageNode> getKPoint() {
        return KPoint;
    }

    public void setKPoint(List<ImageNode> KPoint) {
        this.KPoint = KPoint;
    }

    public List<List<ImageNode>> getClusters() {
        return clusters;
    }

    public void setClusters(List<List<ImageNode>> clusters) {
        this.clusters = clusters;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Kmeans(ImageNode[] nodes) {
        this.nodes = nodes;
        this.number = nodes.length;
        System.out.println("length:" + nodes.length);
        //初始化聚类存储对象
        this.clusters = new ArrayList<List<ImageNode>>();
        for (int i = 0;i < K;i++) {
            clusters.add(new ArrayList<ImageNode>());
        }
        //随机初始化质心
        this.KPoint = initKPoint();
    }

    /**
     * 随机初始化质心
     * @return
     */
    public List<ImageNode> initKPoint() {
        List<ImageNode> kPoint = new ArrayList<ImageNode>();
        int n;
        //随机在节点数组内获取K个质心
        for (int i = 0;i < K;i++) {
            n = (int) (Math.random()*number);
            System.out.println(i+"  "+n);
            kPoint.add(nodes[n]);
        }
        return kPoint;
    }

    /**
     * 平均分布初始化质心
     * @return
     */
    public List<ImageNode> initAVGKPoint() {
        List<ImageNode> kPoint = new ArrayList<ImageNode>();
        int n;
        for (int i = 0;i < K;i++) {
            n = (nodes.length/(K+1))*(i+1);
            System.out.println(i+"  "+n+"  "+nodes[n].r);
            kPoint.add(nodes[n]);
        }
        return kPoint;
    }

    /**
     * 计算两个节点之间的距离
     * @param n1 节点1
     * @param n2 节点2
     * @return
     */
    public static double getDis(ImageNode n1, ImageNode n2) {
        return sqrt((float)((n1.r-n2.r)*(n1.r-n2.r)+(n1.g-n2.g)*(n1.g-n2.g)+(n1.b-n2.b)*(n1.b-n2.b)));
    }

    /**
     * 将样本放入对应的分类中
     * @param node 样本
     */
    public void joinCluster(ImageNode node) {
        //计算该节点到第一个质心的欧氏距离作为初始值，dis用于保存最小值
        double dis = getDis(KPoint.get(0), node);
        double tem;
        //最近质心的分类的引用
        int index = 0;
        for (int i = 0;i < K;i++) {
            //获取节点到质心的距离
            tem = getDis(KPoint.get(i), node);
            //如果距离比当前dis小，赋值给dis，保存该质心的分类的引用
            if (tem < dis) {
                dis = tem;
                index = i;
            }
        }
        //将该节点加入距离最近的质心的分类
        clusters.get(index).add(node);
    }

    /**
     * 重新计算质心
     */
    public void reGetKPoint() {
        for (int i = 0;i < clusters.size();i++) {
            int sumR = 0, sumG = 0, sumB = 0;
            //计算每个分类的通道值和
            for (ImageNode n:clusters.get(i)) {
                sumR += n.r;
                sumG += n.g;
                sumB += n.b;
            }
            //质心重新赋值为通道值得平均数
            if (clusters.get(i).size() == 0) {
                KPoint.get(i).r = sumR;
                KPoint.get(i).g = sumG;
                KPoint.get(i).b = sumB;
            } else {
                KPoint.get(i).r = sumR/clusters.get(i).size();
                KPoint.get(i).g = sumG/clusters.get(i).size();
                KPoint.get(i).b = sumB/clusters.get(i).size();
            }
            System.out.println("R:" + KPoint.get(i).r + " G:" + KPoint.get(i).g + " B:" + KPoint.get(i).b);
        }
    }

    /**
     * 计算平方误差和的平均数(样本与质心的平均距离)
     * @param clusters 已经分好类的样本
     * @param KPoint 质心
     * @return
     */
    public float getVar(List<List<ImageNode>> clusters, List<ImageNode> KPoint) {
        float var = 0;
        //计算平方误差和
        for (int i = 0;i < K;i++) {
            List<ImageNode> cNodes = clusters.get(i);
            for (int j = 0;j < cNodes.size();j++) {
                var += getDis(cNodes.get(j), KPoint.get(i));
            }
        }
        //计算平均数
        var = var/nodes.length;
        return var;
    }

    /**
     * 按分类的数量对质心降序排序
     */
    public void sort() {
        //构造一个白色节点
        ImageNode node1 = new ImageNode();
        node1.r = 255;
        node1.g = 255;
        node1.b = 255;
        //如果第一个节点与白色相似度过高(距离较近)，替换至列表末尾
        if(Kmeans.getDis(node1, KPoint.get(0)) <= 60) {
            ImageNode tem = KPoint.get(0);
            KPoint.set(0, KPoint.get(KPoint.size()-1));
            KPoint.set(KPoint.size()-1, tem);
        }
        //冒泡排序
        for (int i = 0;i < clusters.size()-1;i++) {
            for (int j = 0;j < KPoint.size()-1 - i;j++) {
                //如果节点与白色相似度过高(距离较近)，替换至列表末尾
                if(Kmeans.getDis(node1, KPoint.get(j+1)) <= 60) {
                    ImageNode tem = KPoint.get(j+1);
                    KPoint.set(j+1, KPoint.get(KPoint.size()-1));
                    KPoint.set(KPoint.size()-1, tem);
                    continue;
                }
                if (clusters.get(j).size() < clusters.get(j+1).size()) {
                    ImageNode tem = KPoint.get(j);
                    KPoint.set(j, KPoint.get(j+1));
                    KPoint.set(j+1, tem);
                }
            }
        }
    }

    /**
     * 迭代，自行设置迭代次数
     * @param x 迭代次数
     */
    public void iterate(int x) {
        //初始化聚类列表
        for (List<ImageNode> l: clusters) {
            l.clear();
        }
        //循环x次
        for (int i = 0;i < x;i++) {
            System.out.println("第"+(i+1)+"次迭代");
            //将每个节点加入对应分类
            for (int j = 0;j < number;j++) {
                joinCluster(nodes[j]);
            }
            //重新计算质心
            reGetKPoint();
        }
    }

    /**
     * 迭代，算法收敛到一定程度自动结束
     */
    public void iterate() {
        int i = 0;
        //上一次迭代完成后的误差
        float oldVar;
        //本次迭代后的误差
        float newVar = -1;
        do {
            //初始化聚类列表
            for (List<ImageNode> l: clusters) {
                l.clear();
            }

            System.out.println("第"+(i+1)+"次迭代");
            //将每个节点加入对应分类
            for (int j = 0;j < number;j++) {
                joinCluster(nodes[j]);
            }
            //重新计算质心
            reGetKPoint();
            oldVar = newVar;
            //获取本次误差值
            newVar = getVar(clusters, KPoint);
            System.out.println("误差值" + newVar);
            i++;
        } while (i<100&&abs(newVar - oldVar)>=0.1);//当两次误差值的变化在0.1以内，停止迭代
    }
}
