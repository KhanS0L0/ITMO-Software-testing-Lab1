package third.Locations;

import third.Decorations.Decoration;
import third.Entities.Entity;

import java.util.List;

public abstract class Location {
    private String locationName;
    private String description;
    private List<? extends Entity> firstSide;
    private List<? extends Entity> secondSide;
    private List<? extends Decoration> decorations;
    private boolean war;

    public Location() {
    }

    public Location(String locationName, String description, boolean war){
        this.locationName = locationName;
        this.description = description;
        this.war = false;
    }

    abstract boolean checkSides(List<? extends Entity> side);

    public void setFirstSide(List<? extends Entity> firstSide) {
        this.firstSide = firstSide;
    }

    public void setSecondSide(List<? extends Entity> secondSide) {
        this.secondSide = secondSide;
    }

    public void setDecorations(List<? extends Decoration> decorations) {
        this.decorations = decorations;
    }

    public void setWar(boolean war) {
        this.war = war;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationName() {
        return locationName;
    }

    public List<? extends Entity> getFirstSide() {
        return firstSide;
    }

    public List<? extends Entity> getSecondSide() {
        return secondSide;
    }

    public List<? extends Decoration> getDecorations() {
        return decorations;
    }

    public boolean isWar() {
        return war;
    }
}
