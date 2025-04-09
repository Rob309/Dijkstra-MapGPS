package org.example;

import java.util.*;

public class DijkstraAlgorithm {
    public List<Node> pathNodes = new ArrayList<>();
    public List<Arc> pathArcs = new ArrayList<>();


    public void findShortestPath(Node startNode, Node endNode, List<Node> nodes, List<Arc> arcs) {
        //Initialize distances and previous nodes
        Map<Integer, Double> distances = new HashMap<>();
        Map<Integer, Node> previousNodes = new HashMap<>();
        PriorityQueue<NodeDistance> pq = new PriorityQueue<>(Comparator.comparingDouble(NodeDistance::getDistance));

        // Initialize distances
        for (Node node : nodes) {
            distances.put(node.id, Double.POSITIVE_INFINITY);
            previousNodes.put(node.id, null);
        }
        distances.put(startNode.id, 0.0);
        pq.add(new NodeDistance(startNode, 0.0));

        //Build adjacency list for the graph
        Map<Integer, List<Arc>> adjList = new HashMap<>();
        for (Arc arc : arcs) {
            adjList.putIfAbsent(arc.from, new ArrayList<>());
            adjList.putIfAbsent(arc.to, new ArrayList<>());
            adjList.get(arc.from).add(arc);
            adjList.get(arc.to).add(arc);
        }

        //Perform Dijkstra's algorithm
        while (!pq.isEmpty()) {
            NodeDistance currentNodeDist = pq.poll();
            Node currentNode = currentNodeDist.getNode();
            double currentDistance = currentNodeDist.getDistance();

            // If we have reached the destination node, stop the algorithm
            if (currentNode == endNode) {
                break;
            }

            // Update distances for all neighbors
            if (adjList.containsKey(currentNode.id)) {
                for (Arc arc : adjList.get(currentNode.id)) {
                    Node neighborNode = (arc.from == currentNode.id) ? nodes.get(arc.to) : nodes.get(arc.from);
                    double newDist = currentDistance + arc.length;

                    if (newDist < distances.get(neighborNode.id)) {
                        distances.put(neighborNode.id, newDist);
                        previousNodes.put(neighborNode.id, currentNode);
                        pq.add(new NodeDistance(neighborNode, newDist));
                    }
                }
            }
        }

        // Step 4: Reconstruct the path
        pathArcs.clear();
        pathNodes.clear();
        Node currentNode = endNode;

        while (currentNode != null) {
            pathNodes.add(currentNode);
            Node prevNode = previousNodes.get(currentNode.id);

            // Find the edge connecting the previous node to the current node
            if (prevNode != null) {
                for (Arc arc : arcs) {
                    if (arc.from == prevNode.id && arc.to == currentNode.id) {
                        pathArcs.add(arc);
                        break;
                    } else if (arc.to == prevNode.id && arc.from == currentNode.id) {
                        pathArcs.add(arc);
                        break;
                    }
                }
            }
            currentNode = prevNode;
        }

        // Reverse the path as we built it from end to start
        Collections.reverse(pathNodes);
        Collections.reverse(pathArcs);

        // Step 5: Print or save the results (pathNodes and pathArcs)
        System.out.println("Shortest path nodes:");
        for (Node node : pathNodes) {
            System.out.println("Node ID: " + node.id);
        }

        System.out.println("Shortest path arcs:");
        for (Arc arc : pathArcs) {
            System.out.println("Arc from " + arc.from + " to " + arc.to + " with length " + arc.length);
        }
    }

    // Helper class to store node with its distance
    public static class NodeDistance {
        private final Node node;
        private final double distance;

        public NodeDistance(Node node, double distance) {
            this.node = node;
            this.distance = distance;
        }

        public Node getNode() {
            return node;
        }

        public double getDistance() {
            return distance;
        }
    }

}
