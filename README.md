# Dijkstra-MapGPS

A Java application that calculates the shortest path in a graph using **Dijkstra's algorithm**. The graph is defined in an XML file using custom `<node>` and `<arc>` elements, representing a simplified map.

---

## 🚀 Features

- XML-based map loading
- Graph construction from nodes and arcs
- Shortest path calculation using Dijkstra’s algorithm
- Easily pluggable with different map files
- Interactive map with zoom and pan

---

## 🛠 Technologies Used

- **Java 17+**
- **Maven** for dependency management and project structure

---

## 🗺️ Map XML Format

The map must contain a list of `<node>` and `<arc>` elements with the following structure:

### ✅ Node Format

```xml
<node id="24852" longitude="4950166" latitude="631884"/>
<arc from="10" to="23587" length="54"/>
```

---

🔧 How to Use
Clone the repository.

Place your XML map file in src/main/resources/.
In MapParser for filePath change the name of the xml to your own xml file's name. 

Compile and run the code.
That's it!

You can find an example xml already in the resources.

---

📌 Notes
Make sure the XML file is accessible and follows the exact structure for nodes and arcs.

Use relative paths to avoid issues with hardcoded file paths.


