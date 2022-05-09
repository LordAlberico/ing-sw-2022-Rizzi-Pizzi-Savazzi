package it.polimi.ingsw.model;

import it.polimi.ingsw.client.ClientCloud;

import java.util.ArrayList;
import java.util.HashMap;

public class Cloud extends Tile {

    public Cloud(int ID) {
        super(ID);
    }

    public HashMap<Colour,Integer> empty() {
        HashMap<Colour, Integer> temp = new HashMap<>(students);
        for(Colour c:Colour.values())
            students.put(c,0);
        notifyChange();
        return temp;
    }

    @Override
    public void notifyChange()
    {
        notify(new ClientCloud(ID,new HashMap<>(students)));

    }
}