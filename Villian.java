public class Villian{
    private Character character;
    private Admin admin;


    public Villian(String charactertype,String characterName){
        intansiateCharacter(charactertype,characterName);
    }

    public void setAdmin(Admin admin){
        this.admin = admin;
    }

    public Admin getAdmin(){
        return this.admin;
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

    private void intansiateCharacter(String character,String characterName){
        switch(character){
            case "Wizard": this.character = new Wizard(characterName,null);
            break;
            case "Farmer": this.character = new Farmer(characterName,null);
            break;
            case "Knight": this.character = new Knight(characterName,null);
            break;
            case "BlackSmith": this.character = new BlackSmith(characterName, null);
            break;
            case "Barbarian": this.character = new Barbarian(characterName, null);
            break;
            case "Wolfman": this.character = new Wolfman(characterName,null);
            break;
            case "Boss": this.character = new Boss();
            break;
            default: System.out.println("Algılanamadı");
            break;
        }
    }

    public void closeAttack(Character target) throws Exception{
        double attackpoints = this.character.attack(true);
        GameServer.logWriter(this.character.getCharacterName()+" "+attackpoints+" vuruş yaptı.");
        target.defence(attackpoints);
        GameServer.logWriter(target.getCharacterName()+"ın "+target.getHealth()+" canı kaldı.");
    }

    public void wideAttack(Character target) throws Exception{
        double attackpoints = this.character.attack(false);
        GameServer.logWriter(this.character.getCharacterName()+" "+attackpoints+" vuruş yaptı.");
        target.defence(attackpoints);
        GameServer.logWriter(target.getCharacterName()+"ın "+target.getHealth()+" canı kaldı.");
    }
}
