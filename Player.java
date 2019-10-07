import java.util.*;

public class Player{
    private Character character;
    private String playerName;
    private Admin admin;

    public Player(String playerName,String characterName, String character){
        intansiateCharacter(character,characterName);
        this.playerName = playerName;
    }

    public void setAdmin(Admin admin){
        this.admin = admin;
    }

    public Admin getAdmin(){
        return this.admin;
    }

    public String getPlayerName(){
        return this.playerName;
    }

    public Character getCharacter(){
        return this.character;
    }

    private void intansiateCharacter(String character,String characterName){
        switch(character){
            case "Wizard": this.character = new Wizard(characterName,this);
            break;
            case "Farmer": this.character = new Farmer(characterName,this);
            break;
            case "Knight": this.character = new Knight(characterName,this);
            break;
            case "BlackSmith": this.character = new BlackSmith(characterName, this);
            break;
            case "Barbarian": this.character = new Barbarian(characterName, this);
            break;
            case "Wolfman": this.character = new Wolfman(characterName,this);
            break;
            default: System.out.println("Algılanamadı");
            break;
        }
    }

    public void purchase(Item item){
        this.character.purchase(item);
    }

    public void sell(Item item){
        this.character.sellItem(item);
    }

    public void wearItem(Item item){
        this.character.wearItem(item);
    }

    public void removeItem(Item item){
        this.character.removeItem(item);
    }

    public void closeAttack(Character target){
      try{
        double attackpoints = this.character.attack(true);
        GameServer.logWriter(this.character.getCharacterName()+" "+attackpoints+" vuruş yaptı");
        target.defence(attackpoints);
        GameServer.logWriter(target.getCharacterName()+"ın "+target.getHealth()+" canı kaldı.");
      }catch(Exception e){
        e.printStackTrace();
      }
    }

    public void wideAttack(Character target){
      try{
        double attackpoints = this.character.attack(false);
        GameServer.logWriter(this.character.getCharacterName()+" "+attackpoints+" vuruş yaptı");
        target.defence(attackpoints);
        GameServer.logWriter(target.getCharacterName()+"ın "+target.getHealth()+" canı kaldı.");
      }catch(Exception e){
        e.printStackTrace();
      }
    }

    public double investiage(){
        return this.character.investigate();
    }

    public double sneak(){
        return this.character.sneak();
    }
    public double heal(){
        return this.character.heal();
    }

    public double convince(){
        return this.character.convince();
    }

    public String toString(){
        return this.playerName;
    }
    public static int rollDice(){
      return Character.rollDice();
    }
}
