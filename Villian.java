public class Villian{
    private Character character;


    public Villian(String charactertype){
        intansiateCharacter(charactertype);
    }

    public Character getCharacter(){
        return this.character;
    }

    public void setMoney(int money){
        this.character.setMoney(money);
    }

    public void purchaseItem(Item item){
        this.character.purchase(item);
    }

    public void wearItem(Item item){
        this.character.wearItem(item);
    }

    public void removeItem(Item item){
        this.character.removeItem(item);
    }

    private void intansiateCharacter(String character){
        switch(character){
            case "Wizard": this.character = new Wizard(null,null);
            break;
            case "Farmer": this.character = new Farmer(null,null);
            break;
            case "Knight": this.character = new Knight(null,null);
            break;
            case "BlackSmith": this.character = new BlackSmith(null, null);
            break;
            case "Barbarian": this.character = new Barbarian(null, null);
            break;
            case "Wolfman": this.character = new Wolfman(null,null);
            break;
            case "Boss": this.character = new Boss();
            break;
            default: System.out.println("Algılanamadı");
            break;
        }
    }

    public void closeAttack(Character target){
        double attackpoints = this.character.attack(true);
        target.defence(attackpoints);
    }

    public void wideAttack(Character target){
        double attackpoints = this.character.attack(false);
        target.defence(attackpoints);
    }
}
