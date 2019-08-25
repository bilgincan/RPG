import java.util.*;

public abstract class Character{
    private int health;
    private int sanity;
    private String CharacterName;
    private Player User;
    private int money;
    protected List<Item> items;
    private List<Item> wornItems;
    protected int dice;
    public double[] effectiveAbilities;

    protected void rollDice(){
        this.dice = (int) (Math.random() * 10+1);
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
        for(int i = 0; i < ab.length; i++){
            this.effectiveAbilities[i] = ab[i];
        }
    }

    //functions depends on rolling dice
    // b == true close attack b == false wideAttack
    public double attack(boolean b){
        rollDice();
        if(b){
            return effectiveAbilities[0]*dice;
        }
            return effectiveAbilities[1]*dice;
    }

    //attackpoints is the damage of the attacking character
    public double defence(double attackpoints){
        rollDice();
        // System.out.println("Saldırıyı savuşturmak için zar atıldı, gelen sayı: "+this.dice);
        int dodged = (int) this.dodgeAttack();
        if(dodged > 50){
            System.out.println("Saldırı savuşturuldu");
            return 0;
        }
        rollDice();
        //System.out.println("Savunma için zar atıldı gelen sayı: "+this.dice);
        double defence = attackpoints - (effectiveAbilities[2]*dice/2);
        if(defence <= 0){
            System.out.println("Saldırıdan hiç hasar almadan çıkıldı.");
            return 0;
        }
        else{
            this.health = this.health - (int) defence;
            if(this.health < 0)
                System.out.println(this.CharacterName+" öldü.");
            return defence;
        }
    }

    //dodgeAttack is a hepling method for defence
    private double dodgeAttack(){
        return (effectiveAbilities[3]*dice+(effectiveAbilities[4]*dice)/2);
    }

    public double investigate(){
        rollDice();
        //System.out.println("Zar atıldı gelen sayı: "+this.dice);
        return effectiveAbilities[6]*dice;
    }

    public double sneak(){
        rollDice();
        System.out.println("Zar atıldı gelen sayı: "+this.dice);
        return effectiveAbilities[7]*dice;
    }

    public double heal(){
        rollDice();
        System.out.println("Zar atıldı gelen sayı: "+this.dice);
        return effectiveAbilities[8]*dice;
    }

    public double convince(){
        rollDice();
        //System.out.println("Zar atıldı gelen sayı: "+this.dice);
        return effectiveAbilities[5]*dice;
    }

    //functions about items
    //could be implemented better in the future, after the implementation of the class Item
    public void purchase(Item item){
        if(item.getPrice() > money){
            System.out.println("Bu itemi satın almaya paranız yetmiyor / You can't buy this item, because you don't have enough money");
        }else{
            this.money = this.money - item.getPrice();
            items.add(item);
            System.out.println("Satın alma işlemi başarı ile gerçekleştirilmiştir");
        }
    }

    public void sellItem(Item item){
        wornItems.remove(item);
        if(items.remove(item)){
            this.money = this.money + item.getPrice()*(4/5);
        }
    }

    //this function updates effectiveAbilities
    public void wearItem(Item item){
        //a character can only wear one weapon, one armor, one pair of shoes
        //for wizardish he can wear everything he wants
        int weapons = 0;
        int armors = 0;
        int shoes = 0;
        for(Item i: wornItems){
            if(i instanceof Shoe)
                shoes++;
            if(i instanceof Weapon)
                weapons++;
            if(i instanceof Armor)
                armors++;
        }

        if(item instanceof Weapon && weapons >= 2){
            System.out.println("Bu item giyilemez");
            return;
        }
        if(item instanceof Shoe && shoes >= 1){
            System.out.println("Bu item giyilemez");
            return;
        }
        if(item instanceof Armor && armors >= 1){
            System.out.println("Bu item giyilemez");
            return;
        }

    //control if the character posses the item
    if(!items.contains(item))
        System.out.println("Bu işlemi gerçekleştirebilmek için önce bu eşyayı satın almalısınız");
    else{
        wornItems.add(item);
        for(int i = 0; i < this.effectiveAbilities.length; i++){
            this.effectiveAbilities[i] = this.effectiveAbilities[i] + item.getPlusEffect()[i];
        }
    }
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
}
