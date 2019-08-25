public class Wolfman extends Character{

    public Wolfman(String CharacterName,Player User){
        super(120,100,8,9,8,8,8,10,4,3,0,CharacterName,User,200);
    }

    public void alone(){
        rollDice();
        this.setSanity(this.getSanity()-dice);
    }
}
