public class Barbarian extends Character{

    private int timesTalked;

    public Barbarian(String CharacterName,Player User){
        super(150,40,10,2,8,7,7,1,0,2,0,CharacterName,User,200);
        this.timesTalked = 0;
    }

    public void talk(){
        if(timesTalked == 5){
            rollDice();
            int sanity = this.getSanity()-this.dice;
            this.setSanity(sanity);
        }
        timesTalked = (timesTalked+1)%6;
    }
}
