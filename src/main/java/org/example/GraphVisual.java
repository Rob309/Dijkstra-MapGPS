package org.example;


import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.*;

public class GraphVisual extends JPanel {
    private final List<Node> nodes;
    private final List<Arc> arcs;
    private final double minLongitude, maxLongitude, minLatitude, maxLatitude;
    private Point lastDragPoint;
    private double scale = 1;
    private double offsetX, offsetY;
    private Node startNode, endNode;
    private final DijkstraAlgorithm dijkstraAlgorithm = new DijkstraAlgorithm();
    private boolean isFirstOrSecond = true;


    public GraphVisual(List<Node> nodes, List<Arc> arcs) {
        this.nodes = nodes;
        this.arcs = arcs;

        // Calculate min/max longitude and latitude for scaling and centering
        minLongitude = nodes.stream().mapToDouble(node -> node.longitude).min().orElse(0);
        maxLongitude = nodes.stream().mapToDouble(node -> node.longitude).max().orElse(0);
        minLatitude = nodes.stream().mapToDouble(node -> node.latitude).min().orElse(0);
        maxLatitude = nodes.stream().mapToDouble(node -> node.latitude).max().orElse(0);


        JCheckBox checkBox = new JCheckBox("Select First/Second Node");
        checkBox.setOpaque(false);
        checkBox.setFocusPainted(false);
        checkBox.setForeground(Color.WHITE);
        this.add(checkBox);
        this.setBackground(Color.BLACK);


        checkBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isFirstOrSecond = !checkBox.isSelected();
            }
        });

        addMouseWheelListener(this::handleZoom);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastDragPoint = e.getPoint();
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                selectClosestNode(e);
                if(startNode != null && endNode != null) {
                    dijkstraAlgorithm.findShortestPath(startNode,endNode,nodes,arcs);
                }
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                handlePanning(e);
            }
        });
    }

    private void handleZoom(MouseWheelEvent e) {
        double zoomFactor = 1.1;
        if (e.getWheelRotation() < 0) {
            scale *= zoomFactor;
        } else {
            scale /= zoomFactor;
        }
        repaint();
    }

    private void handlePanning(MouseEvent e) {
        if (lastDragPoint != null) {
            Point currentPoint = e.getPoint();
            offsetX += (currentPoint.x - lastDragPoint.x) / scale*3;
            offsetY += (currentPoint.y - lastDragPoint.y) / scale*3;
            lastDragPoint = currentPoint;
            repaint();
        }
    }

    private void selectClosestNode(MouseEvent e) {
        Point clickPoint = e.getPoint();
        double closestDistance = Double.MAX_VALUE;
        Node closestNode = null;

        double panelWidth = getWidth();
        double panelHeight = getHeight();
        double longitudeRange = maxLongitude - minLongitude;
        double latitudeRange = maxLatitude - minLatitude;

        double scaleFinal = Math.min(panelWidth / longitudeRange, panelHeight / latitudeRange) * scale;
        double offsetXFinal = (panelWidth - (longitudeRange * scaleFinal)) / 2 + offsetX;
        double offsetYFinal = (panelHeight - (latitudeRange * scaleFinal)) / 2 + offsetY;

        for (Node node : nodes) {
            int x = (int) ((node.longitude - minLongitude) * scaleFinal + offsetXFinal);
            int y = (int) ((node.latitude - minLatitude) * scaleFinal + offsetYFinal);

            double distance = clickPoint.distance(x, y);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestNode = node;
            }
        }

        if (closestNode != null) {
            if(isFirstOrSecond){
                endNode = closestNode;
            }else{
                startNode = closestNode;
            }
            System.out.println("Selected Node ID: " + closestNode.id);
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        double panelWidth = getWidth();
        double panelHeight = getHeight();
        double longitudeRange = maxLongitude - minLongitude;
        double latitudeRange = maxLatitude - minLatitude;
        double scaleX = panelWidth / longitudeRange;
        double scaleY = panelHeight / latitudeRange;
        double scaleFinal = Math.min(scaleX, scaleY)*scale;

        double offsetXFinal = (panelWidth - (longitudeRange * scaleFinal)) / 2 + offsetX;
        double offsetYFinal = (panelHeight - (latitudeRange * scaleFinal)) / 2 + offsetY;

        for (Arc arc : arcs){
            arc.color = Color.white;
        }
        for (Arc arc : arcs) {
            if(dijkstraAlgorithm.pathArcs!=null){
                for(Arc arcPath:dijkstraAlgorithm.pathArcs){
                    if (arcPath.from==arc.from && arcPath.to==arc.to || arcPath.from==arc.to && arcPath.to==arc.from){
                        arc.color = Color.red;
                        break;
                    }
                }
            }

            Node fromNode = nodes.get(arc.from);
            Node toNode = nodes.get(arc.to);
            int x1 = (int) ((fromNode.longitude - minLongitude) * scaleFinal + offsetXFinal);
            int y1 = (int) ((fromNode.latitude - minLatitude) * scaleFinal + offsetYFinal);
            int x2 = (int) ((toNode.longitude - minLongitude) * scaleFinal + offsetXFinal);
            int y2 = (int) ((toNode.latitude - minLatitude) * scaleFinal + offsetYFinal);
            g2d.setColor(arc.color);
            g2d.drawLine(x1, y1, x2, y2);
        }
        for (Node node : nodes){
            node.color = Color.green;
        }

        // Draw nodes
        for (Node node : nodes) {
            if(dijkstraAlgorithm.pathNodes!=null){
                for(Node nodePath:dijkstraAlgorithm.pathNodes){
                    if (node.id==nodePath.id){
                        node.color = Color.red;
                        break;
                    }
                }
            }

            if(node == startNode || node == endNode)
            {
                node.color = Color.blue;
            }


            g2d.setColor(node.color);
            int x = (int) ((node.longitude - minLongitude) * scaleFinal + offsetXFinal);
            int y = (int) ((node.latitude - minLatitude) * scaleFinal + offsetYFinal);
            g2d.fillOval(x - 2, y - 2, 4, 4);
        }
    }
}