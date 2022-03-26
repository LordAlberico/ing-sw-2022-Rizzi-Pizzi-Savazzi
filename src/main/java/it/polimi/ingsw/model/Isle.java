package it.polimi.ingsw.model;

import java.util.HashMap;

public class Isle extends Tile {

    protected Faction tower;
    protected boolean prohibited;
    protected influenceStrategy   infStrategy;

    /**
     * Default constructor
     */
    public Isle() {
        super();
        tower = Faction.Empty;
    }

    public void setTower(Faction f) {
        this.tower = f;
    }

    public int getInfluence(Player p, HashMap<Colour,Player> professors) {
        return infStrategy.getInfluence(p,students,1,tower, professors);
    }

    public int getInfluence(Team t,HashMap<Colour,Player> professors){
        return infStrategy.getInfluence(t,students,1,tower, professors);
    }

    public int getInfluenceNoColour(Player p, Colour c,HashMap<Colour,Player> professors)
    {
        int influence = 0;

        influence+= students.stream()
                .filter(student -> professors.get(student.getType()).equals(p)&& c!=(student.getType()))
                .count();

        if(p.getBoard().getFaction() == tower)
            influence += 1;
        return influence;
    }
    public int getInfluenceNoColour(Team t, Colour c,HashMap<Colour,Player> professors)
    {
        return getInfluenceNoColour(t.getLeader(),c,professors)+ getInfluenceNoColour(t.getMember(),c,professors)-1;
    }
    public void setProhibited(){ prohibited = true;}

    public boolean removeProhibited()
    {
        boolean temp = prohibited;
        prohibited = false;
        return temp;
    }

    public Faction getTower(){
        return this.tower;
    }

    public AggregatedIsland join (Isle isle)
    {
        return new AggregatedIsland(this ,isle);
    }

    public AggregatedIsland join (AggregatedIsland isle)
    {
        return isle.join(this);
    }
}