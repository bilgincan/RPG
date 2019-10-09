class player{
    constructor(character,characterType,playerName,abilities,money,sanity,health){
      this.character = character;
      this.characterType = characterType;
      this.playerName = playerName;
      this.abilities = abilities;
      this.money = money;
      this.sanity = sanity;
      this.health = health;
    }
}

class villian{
    constructor(characterType,health){
      this.characterType = characterType;
      this.health = health;
    }
}

function reload(loadtime){
    setTimeout(function(){
        window.location.reload()
    },loadtime)

}
