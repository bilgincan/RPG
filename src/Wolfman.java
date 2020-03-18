public class Wolfman extends Character{
  private boolean isWolf;
  private int humanHealth;

    public Wolfman(String CharacterName,Player User){
        super(80,100,3,2,5,4,3,10,4,3,0,CharacterName,User,200);
        this.isWolf = false;
    }

    public void alone(){
        this.setSanity(this.getSanity()-rollDice());
    }

    public void beWolf(){
      //if sanity will be 0 dont take that action
      if(this.getSanity()>35){
        this.setSanity(this.getSanity()-35);
        double[] abilities = this.getAbilities();
        //closeAttack
        abilities[0] = 10;
        //wideAttack
        abilities[1] = 8;
        //defence
        abilities[2] = 9;
        //speed
        abilities[3] = 8;
        //dodge
        abilities[4] = 8;
        //persuasive
        abilities[5] = 5;

        this.setAbilities(abilities);
        this.isWolf = true;
        humanHealth = this.getHealth();
        this.setHealth(130);
      }else{
        try{
          GameServer.logWriter(this.getCharacterName()+" kurt adama dönüşemedi.");
        }catch(Exception e){
        e.printStackTrace();
        }
      }
    }

    public void beHuman(){
      this.setSanity(this.getSanity()+30);
      double[] abilities = this.getAbilities();
      //closeAttack
      abilities[0] = 3;
      //wideAttack
      abilities[1] = 2;
      //defence
      abilities[2] = 5;
      //speed
      abilities[3] = 4;
      //dodge
      abilities[4] = 3;
      //persuasive
      abilities[5] = 10;

      this.setAbilities(abilities);
      this.isWolf = false;
      this.setHealth(humanHealth);
    }

    public boolean isWolf(){
      return this.isWolf;
    }
}
