package third.Entities;

import third.Locations.Location;

import java.util.List;

public interface Action {
    public <T extends Location> void startWar(T location);

    public <T extends Location> void stopWar(T location);
}
