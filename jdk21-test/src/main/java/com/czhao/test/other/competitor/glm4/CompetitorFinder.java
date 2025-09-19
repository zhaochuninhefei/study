package com.czhao.test.other.competitor.glm4;

import java.util.*;

/**
 * @author zhaochun
 */
public class CompetitorFinder {
    public static void main(String[] args) {
        // 假设products列表已经按照某种方式填充了1万条产品数据
        List<Product> products = new ArrayList<>();
        // 计算每个产品的竞品Top10
        for (Product product : products) {
            List<Product> topCompetitors = findTopCompetitors(product, products, 10);
            System.out.println("产品ID: " + product.id + " 的竞品Top10: ");
            for (Product competitor : topCompetitors) {
                System.out.println(competitor.id);
            }
        }
    }
    public static List<Product> findTopCompetitors(Product product, List<Product> products, int k) {
        PriorityQueue<Map.Entry<Product, Double>> queue = new PriorityQueue<>(k, Comparator.comparingDouble(Map.Entry::getValue));
        for (Product other : products) {
            if (product != other) { // 跳过自身
                double similarity = jaccardSimilarity(product.features, other.features);
                if (queue.size() < k || similarity > queue.peek().getValue()) {
                    queue.offer(Map.entry(other, similarity));
                    if (queue.size() > k) {
                        queue.poll(); // 保持队列大小为k
                    }
                }
            }
        }
        List<Product> topCompetitors = new ArrayList<>();
        while (!queue.isEmpty()) {
            topCompetitors.add(queue.poll().getKey());
        }
        return topCompetitors;
    }
    public static double jaccardSimilarity(BitSet set1, BitSet set2) {
        BitSet intersection = (BitSet) set1.clone();
        BitSet union = (BitSet) set1.clone();
        intersection.and(set2);
        union.or(set2);
        return (double) intersection.cardinality() / union.cardinality();
    }
}

class Product {
    int id;
    BitSet features; // 使用BitSet来表示产品的20项指标
    public Product(int id, BitSet features) {
        this.id = id;
        this.features = features;
    }
}
