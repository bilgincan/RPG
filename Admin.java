import java.util.*;

public class Admin{
    private List<Player> players;
    private List<Villian> villians;

    public Admin(){
        this.players = new ArrayList<Player>();
        this.villians = new ArrayList<Villian>();
    }

    public List<Player> getPlayers(){
        return this.players;
    }

    public List<Villian> getVillians(){
        return this.villians;
    }

    public void giveMoney(int plusMoney,Player player){
        Character a = player.getCharacter();
        a.setMoney(a.getMoney()+plusMoney);
    }

    public void giveHealth(int plusHealth, Player player){
        Character a = player.getCharacter();
        a.setHealth(a.getHealth()+plusHealth);
    }

    public void giveSanity(int plusSanity, Player player){
        Character a = player.getCharacter();
        a.setSanity(a.getSanity()+plusSanity);
    }

    public void setAbilities(double[] abilities, Player player){
        player.getCharacter().setAbilities(abilities);
    }

    public int rollDice(){
        return Player.rollDice();
    }

    public void generateEnemy(String type){
        Villian enemy = new Villian(type);
        villians.add(enemy);
        enemy.setAdmin(this);
    }

    public void generatePlayer(String playerName,String characterName,String character){
        Player player = new Player(playerName, characterName, character);
        players.add(player);
        player.setAdmin(this);
    }

    //resets characters and villians
    public void resetStory(){
        this.players = new ArrayList<Player>();
        this.villians = new ArrayList<Villian>();
    }
}
