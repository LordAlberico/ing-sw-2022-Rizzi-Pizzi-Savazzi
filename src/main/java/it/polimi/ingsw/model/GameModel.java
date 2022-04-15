package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotEnoughCoinsException;
import it.polimi.ingsw.exceptions.StudentsOutOfBoundsException;
import it.polimi.ingsw.exceptions.TileOutOfBoundsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GameModel {
    private int unusedCoins;
    private int motherNature;
    private final int numOfPlayers;
    private final HashMap<Colour, Integer> bag;
    private final ArrayList<Player> players;
    protected HashMap<Colour, Player> professors;
    private final ArrayList<Isle> isles;
    private final ArrayList<Cloud> clouds;
    private ArrayList<Team> teams;
    private final ArrayList<Character> activeCharacters;

    public GameModel(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
        unusedCoins = 20;
        motherNature = (new Random()).nextInt(12);
        bag = new HashMap<>();
        generateBag();
        players = new ArrayList<Player>();
        isles = new ArrayList<Isle>();
        generateIsle();
        clouds = new ArrayList<Cloud>();
        activeCharacters = new ArrayList<Character>();
        professors = new HashMap<Colour, Player>();
        final int[] id = new Random().ints(0, 12).distinct().limit(3).toArray();
        for (int i = 0; i < 3; i++) {
            if (id[i] == 0 || id[i] == 6 || id[i] == 10)
                activeCharacters.add(new CharacterStudents(id[i], id[i] % 3 + 1));
            else
                activeCharacters.add(new Character(id[i], id[i] % 3 + 1));
        }
    }

    public int numberOfProfessors(Player p) {
        int num = 0;
        for (Colour c : Colour.values()) {
            num += (professors.get(c).equals(p)) ? 1 : 0;
        }
        return num;
    }

    private void generateBag() {
        for (Colour c : Colour.values()) {
            //it only generates 24 students for each colour (instead of 26)
            //because 2 students of each colour get removed at the start of the game
            bag.put(c, 24);
        }
    }

    private void generateIsle() {
        for (int i = 0; i < 12; i++)
            isles.add(new Isle());
    }

    public int getMotherNature() {
        return motherNature;
    }

    public HashMap<Colour, Integer> extractStudents(int num) {
        HashMap<Colour, Integer> extractedStud = new HashMap<>();
        for (Colour c : Colour.values()) {
            extractedStud.put(c, 0);
        }
        if (num >= 0 && num < bag.size()) {
            for (int i = 0; i < num; i++) {
                Colour extracted = getRandomStudent();
                extractedStud.put(extracted, extractedStud.get(extracted) + 1);
            }
            return extractedStud;
        }
        return null;
    }

    public void moveMN(int moves) {
        motherNature = (motherNature + moves) % 12;
    }

    public HashMap<Colour, Player> getProfessors(){
        return professors;
    }

    public boolean checkEmptyBag() {
        int size = 0;
        for (Colour c : Colour.values()) {
            size += bag.get(c);
        }
        return size <= 0;
    }

    public ArrayList<Team> getTeams(){
        return teams;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Player getPlayer(int playerID) {
        for (Player p : players) {
            if (p.getID() == playerID) {
                return p;
            }
        }
        return null;
    }

    public ArrayList<Cloud> getClouds() {
        return clouds;
    }

    public ArrayList<Isle> getIsles() {
        return isles;
    }

    public Isle getIsle(int isleID) throws TileOutOfBoundsException {
        if (isleID >= 0 && isleID < isles.size()) {
            return isles.get(isleID);
        }
        throw new TileOutOfBoundsException();
    }

    public Cloud getCloud(int cloudID) throws TileOutOfBoundsException {
        if (cloudID >= 0 && cloudID < clouds.size()) {
            return clouds.get(cloudID);
        }
        throw new TileOutOfBoundsException();
    }


    public Team getTeam(int teamID) {
        // FOR EXCEPTION
        if (teamID >= 0 && teamID < teams.size()) {
            return teams.get(teamID);
        }
        return null;
    }

    public int getCoins() {
        return unusedCoins;
    }

    public void addCoins(int numCoins) throws NotEnoughCoinsException {
        if (numCoins >= 0 && numCoins + unusedCoins <= 20)
            this.unusedCoins += numCoins;
        else if (numCoins + unusedCoins > 20) {
            throw new NotEnoughCoinsException();
        }

    }

    public void removeCoin() throws NotEnoughCoinsException {
        if (unusedCoins > 0)
            unusedCoins--;
        else {
            throw new NotEnoughCoinsException();
        }
    }

    public void setMotherNPos(int isleID) throws TileOutOfBoundsException {
        if (isleID <= 11 && isleID >= 0)
            this.motherNature = isleID;
        else
            throw new TileOutOfBoundsException();
    }

    public int getStudents(Colour c) {
        return bag.get(c);
    }

    public void addPlayer(int id, String nickname) {
        if (players.size() < numOfPlayers) {
            Player p = new Player(id, nickname);
            players.add(p);
            if (numOfPlayers == 4)
                teams.get(id > 1 ? 1 : 0).addPlayer(p);
        }
    }


    public Colour getRandomStudent(){
        int totStud = 0;
        for(Colour c : bag.keySet()){
            totStud += bag.get(c);
        }
        Random rand = new Random();
        int selected = rand.nextInt(totStud);
        Colour selectedColour = null;

        int verify = 0;
        for(Colour c : Colour.values()){
            verify += bag.get(c);
            if(selected <= verify){
                selectedColour = c;
            }
        }

        bag.replace(selectedColour, bag.get(selectedColour) - 1);
        return selectedColour;

    }

    public void addStudents(HashMap<Colour, Integer> students){
        for(Colour c: students.keySet()){
            bag.replace(c,students.get(c)+bag.get(c));
        }
    }

    public void addStudent(Colour student){
        bag.replace(student, bag.get(student)+1);
    }

    public Player getProfessor (Colour c)
    {
        return professors.get(c);
    }

    public void setProfessor(Colour c, Player p)
    {
        // professors.replace(c,p);
        professors.put(c, p);
    }

    public Character getCharacter (int id)
    {
        return activeCharacters.get(id);
    }

    public void joinIsle(int isle1,int isle2)
    {
        AggregatedIsland temp =isles.get(isle1).join(isles.get(isle2));
        if(isle1>isle2)
        {
            isles.remove(isle1);
            isles.remove(isle2);
            isles.add(isle2,temp);
        }
        else
        {
            isles.remove(isle2);
            isles.remove(isle1);
            isles.add(isle1,temp);
        }
    }
}
