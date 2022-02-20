package third.Entities;

import third.Locations.Location;

public class Human extends Entity implements Action {

    public Human() {
    }

    public Human(String firstName, String secondName, int age) {
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
            System.out.println("Humans can not start war with no one");
            location.setWar(false);
            return;
        }

        String enemy = location.getSecondSide().get(0).getClass().getName();
        if(place.equals("third.Locations.Beach") && enemy.equals("third.Entities.Human")) {
            location.setWar(false);
            System.out.println("Human can not start war with human");
            return;
        }
        if(place.equals("third.Locations.Beach") && enemy.equals("third.Entities.Humanoid")){
            location.setWar(true);
            System.out.println("Humans started war with Humanoid on: " + location.getLocationName());
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
