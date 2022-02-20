package third.Locations;

import third.Entities.Entity;

import java.util.List;

public class Beach extends Location {
    @Override
    public void setFirstSide(List<? extends Entity> firstSide) {
        if(!this.checkSides(firstSide)) return;
        super.setFirstSide(firstSide);
    }

    @Override
    public void setSecondSide(List<? extends Entity> secondSide) {
        if(!this.checkSides(secondSide)) return;
        super.setSecondSide(secondSide);
    }

    @Override
    public boolean checkSides(List<? extends Entity> side){
        if(side == null || side.isEmpty()) {
            return false;
        }else if(side.get(0).getClass().getName().equals("third.Entities.CrabHumanoid")){
            System.out.println("crab humanoids can not be on beach");
            return false;
        }
        return true;
    }

}
