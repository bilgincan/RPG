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
            case "Wizard": this.character = new Wizard("Düşman",null);
            break;
            case "Farmer": this.character = new Farmer("Düşman",null);
            break;
            case "Knight": this.character = new Knight("Düşman",null);
            break;
            case "BlackSmith": this.character = new BlackSmith("Düşman", null);
            break;
            case "Barbarian": this.character = new Barbarian("Düşman", null);
            break;
            case "Wolfman": this.character = new Wolfman("Düşman",null);
            break;
            case "Boss": this.character = new Boss();
            break;
            default: System.out.println("Algılanamadı");
            break;
        }
    }

    public void closeAttack(Character target){
        double attackpoints = this.character.attack(true);
        System.out.println("Düşman "+attackpoints+" vuruş yaptı.");
        target.defence(attackpoints);
        System.out.println(target.getCharacterName()+"ın "+target.getHealth()+" canı kaldı.");
    }

    public void wideAttack(Character target){
        double attackpoints = this.character.attack(false);
        System.out.println("Düşman "+attackpoints+" vuruş yaptı.");
        target.defence(attackpoints);
        System.out.println(target.getCharacterName()+"ın "+target.getHealth()+" canı kaldı.");
    }
}
