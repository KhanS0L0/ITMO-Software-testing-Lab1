package third.Decorations;

public abstract class Decoration {
    private String name;
    private String description;
    private String material;

    public Decoration() {
    }
    public Decoration(String name, String description, String material) {
        this.name = name;
        this.description = description;
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}
