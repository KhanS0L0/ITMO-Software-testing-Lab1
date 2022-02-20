package third.Entities;

import third.Locations.Location;

import java.util.List;

public class Humanoid extends Entity implements Action {

    public Humanoid() {
    }

    public Humanoid(String firstName, String secondName, int age) {
        super(firstName, secondName, age);
    }

    @Override
    public <T extends Location> void startWar(T location) {
        if(location == null){
            System.out.println("Location can not be null");
            return;
        }

        String place = location.getClass().getName();

        if(location.getSecondSide() == null || location.getSecondSide().isEmpty()){
            System.out.println("Humanoids can not start war with no one");
            location.setWar(false);
            return;
        }

        String enemy = location.getSecondSide().get(0).getClass().getName();

        if(place.equals("third.Locations.Beach") && enemy.equals("third.Entities.Human")) {
            location.setWar(true);
            System.out.println("Humanoids started war with human on: " + location.getLocationName());
            return;
        }
        if(place.equals("third.Locations.Sea") && enemy.equals("third.Entities.CrabHumanoid")){
            location.setWar(true);
            System.out.println("Humanoids started war with CrabHumanoids on: " + location.getLocationName());
            return;
        }

        location.setWar(false);
    }

    @Override
    public <T extends Location> void stopWar(T location) {
        if(location.getSecondSide() != null && !location.getSecondSide().isEmpty() && location.isWar())
            location.setWar(false);
    }
}
