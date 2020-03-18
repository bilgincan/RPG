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
    constructor(characterType,health,characterName){
      this.characterType = characterType;
      this.health = health;
      this.characterName = characterName;
    }
}
function reload(loadtime){
    setTimeout(function(){
        window.location.reload()
    },loadtime)
}

function printTurkishLetters(characterName){
//make the turkish letters visible
while(characterName.includes("%20")){
  characterName = characterName.replace("%20"," ");
}
while(characterName.includes("%C5%9F")){
  characterName = characterName.replace("%C5%9F","ş");
}
while(characterName.includes("%C3%BC")){
  characterName = characterName.replace("%C3%BC","ü");
}
while(characterName.includes("%C3%A7")){
  characterName = characterName.replace("%C3%A7","ç");
}
while(characterName.includes("%C4%9F")){
  characterName = characterName.replace("%C4%9F","ğ");
}
while(characterName.includes("%C4%B0")){
  characterName = characterName.replace("%C4%B0","İ");
}
while(characterName.includes("%C3%9C")){
  characterName = characterName.replace("%C3%9C","Ü");
}
while(characterName.includes("%C4%B1")){
  characterName = characterName.replace("%C4%B1","ı");
}
while(characterName.includes("%C5%9E")){
  characterName = characterName.replace("%C5%9E","Ş");
}
while(characterName.includes("%C4%9E")){
  characterName = characterName.replace("%C4%9E","Ğ");
}
while(characterName.includes("%C3%87")){
  characterName = characterName.replace("%C3%87","Ç");
}
while(characterName.includes("%C3%B6")){
  characterName = characterName.replace("%C3%B6","ö")
}
while(characterName.includes("%C3%96")){
  characterName = characterName.replace("%C3%96","Ö")
}
return characterName;
}

function loading(){
  $("#loading").show()
  setTimeout(function(){
      $("#loading").hide()
  },1000)
}
