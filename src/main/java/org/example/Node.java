package org.example;

import java.awt.*;

public class Node {
    int id;
    double longitude, latitude;

    public Color color = Color.green;

    public Node(int id, double longitude, double latitude) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
