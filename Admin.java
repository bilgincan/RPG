import java.util.*;

public class Admin{
    private List<Player> players;
    private List<Villian> villians;

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

}
