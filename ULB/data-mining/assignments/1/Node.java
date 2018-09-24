import java.util.ArrayList;
import java.util.List;

public class Node {
    private String value;
    private List<Node> children;
    private int level;

    Node(String value, int level) {
        this.value = value;
        this.children = new ArrayList<>();
        this.level = level;
    }

    void addChild(Node child) {
        children.add(child);
    }

    @Override
    public String toString() {
        String node = "";
        if (level > 0) {
            node = "|";
        }
        for (int i = 0; i < level; i++) {
            node += "-";
        }
        if (this.value == null) {
            System.out.println("FUCK YOU");
        }
        node += this.value + "\n";
        if (children.size() > 0) {
            for (int i = 0; i < children.size(); i++) {
                node += children.get(i).toString();
            }
        }
        return node;
    }
}
