import java.util.*;

public abstract class Character{
    private int health;
    private int sanity;
    private String CharacterName;
    private Player User;
    private int money;
    protected List<Item> items;
    private List<Item> wornItems;
    private double[] effectiveAbilities;

    public static int rollDice(){
        return (int) (Math.random() * 20+1);
    }

    public Character(int health, int sanity, int closeDamage, int wideDamage, int defence, int speed, int dodge, int persuasive, int investigation, int sneak,int abilityOfHeal,String CharacterName, Player User,int money){
        this.health = health;
        this.sanity = sanity;
        this.CharacterName = CharacterName;
        this.User = User;
        this.money = money;
        this.items = new ArrayList<Item>();
        this.wornItems = new ArrayList<Item>();

        //effective abilities are the ability points with items
        this.effectiveAbilities = new double[9];
        effectiveAbilities[0] = closeDamage;
        effectiveAbilities[1] = wideDamage;
        effectiveAbilities[2] = defence;
        effectiveAbilities[3] = speed;
        effectiveAbilities[4] = dodge;
        effectiveAbilities[5] = persuasive;
        effectiveAbilities[6] = investigation;
        effectiveAbilities[7] = sneak;
        effectiveAbilities[8] = abilityOfHeal;
    }

    //getters
    public double[] getEffectiveAbilities(){
        return this.effectiveAbilities;
    }

    public int getMoney(){
        return this.money;
    }

    public int getHealth(){
        return this.health;
    }

    public int getSanity(){
        return this.sanity;
    }

    public String getCharacterName(){
        return this.CharacterName;
    }

    public List<Item> getItems(){
        return items;
    }

    public List<Item> getWornItems(){
      return this.wornItems;
    }

    public void printItemsList(){
        System.out.println("Items: ");
        for(Item i: items){
            System.out.print(i.toString()+" ");
        }
    }

    public double[] getAbilities(){
        return this.effectiveAbilities;
    }

    //setters
    public void setSanity(int sanity){
        this.sanity = sanity;
    }

    public void setHealth(int health){
        this.health = health;
    }

    public void setMoney(int money){
        this.money = money;
    }

    public void setAbilities(double[] ab){
        System.out.println("SetAbilities: \n");
        for(int i = 0; i < ab.length; i++){
          System.out.println(ab[i]);
            this.effectiveAbilities[i] = ab[i];
        }
    }

    //functions depends on rolling dice
    // b == true close attack b == false wideAttack
    public double attack(boolean b){
        if(b){
            return effectiveAbilities[0]*rollDice();
        }
            return effectiveAbilities[1]*rollDice();
    }

    //attackpoints is the damage of the attacking character
    public double defence(double attackpoints){
      try {
        int dodged = (int) this.dodgeAttack();
        if(attackpoints < 60 && dodged > 75){
            GameServer.logWriter(this.CharacterName+" saldırıyı savuşturdu, çatışmadan hasar almadan çıktı.");
            return 0;
        }
        double defence = attackpoints - ((effectiveAbilities[2]*(rollDice())/2));
        if(defence <= 0){
            GameServer.logWriter(this.CharacterName+" saldırıdan hiç hasar almadan çıktı.");
            return 0;
        }
        else{
            this.health = this.health - (int) defence;
            if(this.health < 0)
                GameServer.logWriter(this.CharacterName+" öldü.");
            return defence;
        }

      } catch(Exception e) {
        e.printStackTrace();
      }
      return 0;
    }

    //dodgeAttack is a hepling method for defence
    private double dodgeAttack(){
        return (effectiveAbilities[3]*rollDice()+(effectiveAbilities[4]*rollDice())/2);
    }

    public double investigate(){
        return effectiveAbilities[6]*rollDice();
    }

    public double run(){
      return effectiveAbilities[3]*rollDice();
    }
    public double sneak(){
        return effectiveAbilities[7]*rollDice();
    }

    public double heal(){
        return effectiveAbilities[8]*rollDice();
    }

    public double convince(){
        return effectiveAbilities[5]*rollDice();
    }

    //functions about items
    //could be implemented better in the future, after the implementation of the class Item
    public boolean purchase(Item item){
        if(item.getPrice() > money){
            return false;
        }else{
            //farmer gets products with 10% discount
            if(this instanceof Farmer){
                //convert int money into double
                double m =item.getPrice();
                int a =(int) (m * (0.9));
                this.money = this.money - a;
            }
            else
                this.money = this.money - item.getPrice();

            items.add(item);
            return true;

        }
    }

    public void sellItem(Item item){
        this.removeItem(item);
        if(items.remove(item)){
            double m = item.getPrice();
            int a = (int) (m*(0.8));
            this.money = this.money + a;
        }
        else{
          throw new IllegalArgumentException("Eşya satılamadı.");
        }
    }

    //this function updates effectiveAbilities
    public boolean wearItem(Item item){
        //a character can only wear one weapon, one armor, one pair of shoes
        //for wizardish he can wear everything he wants
        int weapons = 0;
        int armors = 0;
        int shoes = 0;
        for(Item i: wornItems){
            if(i instanceof Weapon)
                weapons++;
            else if(i instanceof Shoe)
                shoes++;
            else if(i instanceof Armor)
                armors++;
        }

        if(item instanceof Weapon && weapons >= 1){
          try {
            GameServer.logWriter("Uyarı:"+this.CharacterName+" seçilen ürünü giyemez");
            System.out.println("Bu item giyilemez");
          }catch(Exception e){
            e.printStackTrace();
          }

            return false;
        }
        if(item instanceof Shoe && shoes >= 1){
          try {
          GameServer.logWriter("Uyarı:"+this.CharacterName+" seçilen ürnü giyemez");
          System.out.println("Bu item giyilemez");
          } catch(Exception e) {
            e.printStackTrace();
          }
            return false;
        }
        if(item instanceof Armor && armors >= 1){
          try {
            GameServer.logWriter("Uyarı:"+this.CharacterName+" seçilen ürünü giyemez");
            System.out.println("Bu item giyilemez");
          } catch(Exception e) {
            e.printStackTrace();
          }
            return false;
        }

    //control if the character posses the item
    if(!items.contains(item)){
      try {
        GameServer.logWriter("Uyarı:"+this.CharacterName+" seçilen ürünü giyemez, öncelikle eşya satın alınmalı");
        System.out.println("Bu işlemi gerçekleştirebilmek için önce bu eşyayı satın almalısınız");
      } catch(Exception e) {
          e.printStackTrace();
      }
        return false;
    }
        wornItems.add(item);
        for(int i = 0; i < this.effectiveAbilities.length; i++){
            this.effectiveAbilities[i] = this.effectiveAbilities[i] + item.getPlusEffect()[i];
        }
        return true;
    }

    public void removeItem(Item item){

        //if it is not removed, it means the item is not worn
        //remove method returns boolean
        if(wornItems.remove(item)){
            for(int i = 0; i < this.effectiveAbilities.length; i++){
                this.effectiveAbilities[i] = this.effectiveAbilities[i] - item.getPlusEffect()[i];
            }
        }
    }

    public Item getItemByType(String type){
        for(Item item: this.items){
            String itemClass = item.getClass().toString();
            if(itemClass.equals("class "+type)){
                return item;
            }
        }
        return null;
    }
}
