package com.x.common.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigInteger;
import java.util.*;
import java.io.*;

public class Dijkstra<T> {
    Set<T> nodes = new TreeSet<>();
    List<T> indexedNodes = new ArrayList<>();
    Map<T, Map<T, Integer>> distances = new HashMap<>();
    int[][] matrix;

    public void addPath(T from, T to, int distance) {
        if (!nodes.contains(from)) {
            nodes.add(from);
            indexedNodes.add(from);
        }
        if (!nodes.contains(to)) {
            nodes.add(to);
            indexedNodes.add(to);
        }
        if (distances.get(from) == null) {
            distances.put(from, new HashMap<>());
        }
        if (distances.get(from).get(to) == null) {
            distances.get(from).put(to, distance);
        } else {
            distances.get(from).put(to, (int) distances.get(from).get(to) + 1);
        }
    }

    public void createGraph() {
        matrix = new int[nodes.size()][nodes.size()]; 
        final int MAX_DISTANCE = 65535;
        for (int i = 0; i < indexedNodes.size(); i ++) {
            T from = indexedNodes.get(i);
            for (int j = 0; j < indexedNodes.size(); j ++) {
                T to = indexedNodes.get(j);
                if (distances.get(from).get(to) == null) {
                    matrix[i][j] = MAX_DISTANCE;
                } else {
                    matrix[i][j] = distances.get(from).get(to);
                }
            }
        }
    }

    public void printMatrix() {
        for (int i = 0; i < indexedNodes.size(); i ++) {
            for (int j = 0; j < indexedNodes.size(); j ++) {
                if (j != 0) {
                    System.out.print(",");
                }
                System.out.print(matrix[i][j]);
            }
            System.out.print("\n");
        }
    }

    public int[] distance(T node) {
        int[] res = new int[indexedNodes.size()];
        for (int i = 0; i < indexedNodes.size(); i ++) {
            if (node.equals(indexedNodes.get(i))) {
                res[i] = 0;
            } else {
                int nodeIndex = indexedNodes.indexOf(node);
                res[i] = matrix[nodeIndex][i];
            }
            System.out.println(node + ":" + String.valueOf(res[i]));
        }
        return res;
    }
}
