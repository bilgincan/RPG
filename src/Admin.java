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

    public Player getPlayerByName(String name){
        for(Player p : this.players){
            if(p.getPlayerName().equals(name))
                return p;
        }
        return null;
    }

    public Player getPlayerByCharacterName(String characterName){
      for(Player p: this.players){
            if(p.getCharacter().getCharacterName().equals(characterName))
                return p;
        }
        return null;
    }
    public Villian getVillianByCharacterName(String characterName){
      for(Villian v: this.villians){
        if(v.getCharacter().getCharacterName().equals(characterName))
          return v;
      }
      return null;
    }
    public Villian getVillianByClass(String name){
      for(Villian v: this.villians){
            if(v.getCharacter().getClass().toString().equals("class "+name))
                return v;
        }
        return null;
    }
    public Character getCharacterByName(String name){
        for(Player p: this.players){
            if(p.getCharacter().getCharacterName().equals(name))
                return p.getCharacter();
        }
        return null;
    }
    public Character getVillianCharacterByName(String name){
      for(Villian v: this.villians){
        if(v.getCharacter().getCharacterName().equals(name))
          return v.getCharacter();
      }
      return null;
    }

    public void giveMoney(int plusMoney,Player player){
        Character a = player.getCharacter();
        a.setMoney(a.getMoney()+plusMoney);
    }

    public void giveHealth(int plusHealth, GeneralPlayer player){
        Character a = player.getCharacter();
        a.setHealth(a.getHealth()+plusHealth);
    }

    public void giveSanity(int plusSanity, GeneralPlayer player){
        Character a = player.getCharacter();
        a.setSanity(a.getSanity()+plusSanity);
    }

    public void setMoney(int money, GeneralPlayer player){
      Character a = player.getCharacter();
      a.setMoney(money);
    }
    public void setHealth(int health,GeneralPlayer player){
      Character a = player.getCharacter();
      a.setHealth(health);
    }
    public void setSanity(int sanity,GeneralPlayer player){
      Character a = player.getCharacter();
      a.setSanity(sanity);
    }

    public void setAbilities(double[] abilities, GeneralPlayer player){
        player.getCharacter().setAbilities(abilities);
    }

    public int rollDice(){
        return Player.rollDice();
    }

    public void generateEnemy(String type,String characterName){
        Villian enemy = new Villian(type,characterName);
        villians.add(enemy);
        enemy.setAdmin(this);
    }

    public boolean generatePlayer(String playerName,String characterName,String character){
        for(Player p: players){
            if(p.getCharacter().getCharacterName().equals(characterName))
                return  false;
            if(p.getPlayerName().equals(playerName))
                return  false;
        }
        Player player = new Player(playerName, characterName, character);
        players.add(player);
        player.setAdmin(this);
        return true;
    }

    public Villian VillianisKilled(){
        for(Villian v: villians){
          if(v.getCharacter().getHealth() <= 0){
            villians.remove(v);
            return v;
          }
        }
        return null;
    }

    public void removePlayer(Player player){
      this.players.remove(player);
    }

    public void removeVillian(Villian villian){
      this.villians.remove(villian);
    }
    //resets characters and villians
    public void resetStory(){
        this.players = new ArrayList<Player>();
        this.villians = new ArrayList<Villian>();
    }
}
