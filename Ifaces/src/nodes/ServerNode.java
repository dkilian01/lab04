package nodes;
import java.io.Serializable;

public class ServerNode implements Serializable {
    private String name;
    private String algorithm;

    public ServerNode(String name, String algorithm) {
        this.name = name;
        this.algorithm = algorithm;
    }

    public ServerNode() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
}
