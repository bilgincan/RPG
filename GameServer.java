import java.net.*;
import java.io.*;
import java.util.*;

public class GameServer{
    private final ServerSocket server;
    private static final String homePage = "../htmlCodes/index.html";
    private static final String player_page = "../htmlCodes/player.html";
    private static final String adminPage = "../htmlCodes/admin.html";
    private Admin admin = null;
    private Socket socket;

    public GameServer(int port) throws IOException{
        server = new ServerSocket(port);
    }

    private void run(){
        java.net.Socket socket = null;

        try{
            socket = server.accept();
            systemon(socket);
        }catch(IOException e){
            e.printStackTrace();
        }
        finally{
            if(socket != null)
            try{
                socket.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    private void systemon(java.net.Socket socket){
        this.socket = socket;

        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //for getting inputs from the user
            String input = in.readLine();

            if(input.equals("GET / HTTP/1.1")){
            printHTMLPage(homePage);
        }
        if(input.contains("Admin")){
          if(this.admin == null)
            this.admin = new Admin();

            prepareAdminPage();
        }
        else if(input.contains("rollDice")){
          logWriter("Rastgele zar atıldı: "+Character.rollDice());
        }
        else if(this.admin == null){
          System.out.println("Admin oluşturulmalı Admin olmak için lütfen [domainName]/Admin adresine giriş yapın");
        }
        else if(input.contains("purchase")){
            String []parts = input.split("chase=");
            buyItem(parts[1]);
        }
        else if(input.contains("wear")){
            String[] parts = input.split("wear=");
            wearItem(parts[1]);
        }
        else if(input.contains("remove")){
            String[] parts = input.split("remove=");
            removeItem(parts[1]);
        }
        else if(input.contains("sell")){
            String[] parts = input.split("sell=");
            sellItem(parts[1]);
        }
        //villain is atacking to a player
        else if(input.contains("closeattack") && input.contains("false")){
          String[] parts = input.split("closeattack");
          Attack(parts[1],true,false);
        }
        else if(input.contains("closeattack")){
            String[] parts = input.split("closeattack");
            Attack(parts[1],true,true);
        }
        else if(input.contains("wideattack")){
          String[] parts = input.split("wideattack");
          Attack(parts[1],false,true);
        }
        else if(input.contains("command")){
          String[] parts = input.split("character=");
          String characterName = parts[1].split(" HTTP")[0];
          takeAction(input, characterName);
        }
        else if(input.contains("generateEnemy")){
          String[] parts = input.split("generateEnemy=");
          String enemyType = parts[1].split(" HTTP")[0];
          this.admin.generateEnemy(enemyType);
        }
        else if(input.contains("abilities=")){
          String parts[] = input.split("abilities=");
          parts = parts[1].split("%20");
          String abilities = parts[0];
          String player = parts[1].split("playerName=")[1];
          player = player.split(" HTTP")[0];
          setAbilities(abilities,player);

        }

        //load html files by names
        if(input.contains("GET /htmlCodes/") && !input.contains("player.html")){
                String[] parts = input.split("/");
                String file = parts[2];
                if(file.contains("bootstrap")){
                  printHTMLPage("../htmlCodes/bootstrap/bootstrap-3.4.1-dist/css/bootstrap.min.css");
                  return;
                }
                parts = file.split(" ");
                file = "../htmlCodes/";
                file += parts[0];
                printHTMLPage(file);
            }
            //write character abilities in the html file
            if(input.contains("current=")){
                String parts[] = input.split("current=");
                String character = parts[1];
                parts = character.split(" HTTP");
                character = parts[0];
                defineAbilitiesForJS(character,player_page);
                }

            //extract the input
            if(this.admin != null && input.contains("GET /initializeCharacters?")){
                String[] parts = input.split("character=");
                String neccessarypart = parts[1];
                parts = neccessarypart.split("&");
                String character = parts[0];
                String playerName = parts[1];
                String characterName = parts[2];

                parts = playerName.split("=");
                playerName = parts[1];

                parts = characterName.split("=");
                characterName = parts[1];
                parts = characterName.split(" HTTP");
                characterName = parts[0];

                //if the character can be initiazed load the character page
                if(initializePlayer(playerName,characterName,character)){
                    defineAbilitiesForJS(characterName,player_page);
                }
                else{
                    printHTMLPage(homePage);
                    alert();
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }catch(NullPointerException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
            printHTMLPage(homePage);
            alert();
        }
        }

        //for initializing players from the query
    private boolean initializePlayer(String playerName, String characterName,String character){
        if(playerName.equals("") || character.equals("") || characterName.equals("") || characterName == null){
            return false;
        }
        else{
            return this.admin.generatePlayer(playerName, characterName, character);
        }
    }

    private void defineAbilitiesForJS(String character,String printPage){
        Iterator playerIterator = this.admin.getPlayers().iterator();
        Player temp;
        Player neededOne = null;

        while(playerIterator.hasNext()){
            temp = (Player) playerIterator.next();
            if(temp.getCharacter().getCharacterName().equals(character)){
                neededOne = temp;
            }
        }
        if(neededOne == null){
            System.out.println("İzinsiz giriş denemesi/ Böyle bir karakter yok");
        }else{
            String jsCode = "";
            Character charprint = neededOne.getCharacter();
            jsCode += renderAbilitiesIntoJsCode(neededOne);
            List<Villian> enemies = this.admin.getVillians();
            jsCode += "var enemies = [";
            for(int i = 0; i < enemies.size(); i++){
                Character enChar = enemies.get(i).getCharacter();
                jsCode += "['"+enChar.getClass() +"' , "+enChar.getHealth()+"]";
                if(i < enemies.size()-1)
                    jsCode += " , ";
            }
            jsCode += "];";

            jsCode += "var items = [";
            List<Item> items = charprint.getItems();
            List<Item> wornItems = charprint.getWornItems();
            boolean worn;

            for(int i = 0; i< items.size(); i++){
                Item item = items.get(i);
                //send items as array in the js code 0 ==> type of item, 1 ==> item, 2 ==> if it is worn or not
                if(wornItems.contains(item)) worn = true;
                else worn = false;

                if(item instanceof Weapon){
                    jsCode += "['Weapon','"+item.getClass()+"',"+worn+"]";
                }
                else if(item instanceof Shoe){
                    jsCode += "['Shoe','"+item.getClass()+"',"+worn+"]";
                }
                else if(item instanceof Armor){
                    jsCode += "['Armor','"+item.getClass()+"',"+worn+"]";
                }
                else if(item instanceof Wizardish){
                    jsCode += "['Wizardish','"+item.getClass()+"',"+worn+"]";
                }
                if(i < items.size()-1){
                    jsCode += " , ";
                }
            }
            jsCode += "];";
            printHTMLPage(printPage,jsCode);
        }
    }
    private String renderAbilitiesIntoJsCode(Player player){
      //printing abilities of the character in to the web browser
      Character charprint = player.getCharacter();
      double[] abilities = charprint.getAbilities();
      String jsCode = "var characterName = '"+charprint.getCharacterName()+"'; ";
      jsCode += "var characterType = '"+charprint.getClass()+"';";
      jsCode += "var money = "+charprint.getMoney()+";";
      jsCode += "var health = "+charprint.getHealth()+";";
      jsCode += "var sanity = "+charprint.getSanity()+";";
      jsCode += "var abilities = [";
      for(int i = 0; i<abilities.length;i++){
          jsCode+=abilities[i];
          if(i < abilities.length-1)
              jsCode += ",";
      }
      jsCode += "];";
      return jsCode;
    }
    private void prepareAdminPage(){
      String jsCode = "var players = new Array(); var villians = new Array();";
      for(Player p: this.admin.getPlayers()){
        Character c = p.getCharacter();
        jsCode += "players.push(new player('"+c.getCharacterName()+"','"+c.getClass()+"','"+p.getPlayerName()+"',"+Arrays.toString(c.getAbilities())+","+c.getMoney()+","+c.getSanity()+","+c.getHealth()+"));";
      }
      for(Villian v:this.admin.getVillians()){
        jsCode += "villians.push(new villian('"+v.getCharacter().getClass()+"','"+v.getCharacter().getHealth()+"'));";
      }
      printHTMLPage(adminPage, jsCode);
    }
    //for printing javascript datas with html
    private void printHTMLPage(String page,String jsCodes){
        try{
        PrintStream out = new PrintStream(socket.getOutputStream());
        BufferedReader htmlCode = new BufferedReader(new FileReader(page));

        //content is the content of the html file
        String content = "";
        String line;
        boolean printed = false;

        while( htmlCode.ready()){
            line = htmlCode.readLine();
            content+=line;
            //in this project I use jquery in every js file, so jquery must be loaded for the script code
            //after the jquery load the script code
            if(line.contains("</script>") && !printed){
                content += "\n";
                String script="<script>"+jsCodes+"</script>";
                content += script;

            //code must be printed only for one time
                printed = true;
            }

            content += "\n";
        }
        htmlCode.close();

        out.println("HTTP/1.1 \nContent-Type: text/html\n\r\n");
        out.println(content);
    }catch(Exception e){
        e.printStackTrace();
    }

    }

    private void printHTMLPage(String page){
        try{

        PrintStream out = new PrintStream(socket.getOutputStream());
        BufferedReader htmlCode = new BufferedReader(new FileReader(page));

        //content is the content of the html file
        String content = "";

        while( htmlCode.ready()){
            content += htmlCode.readLine();
            content += "\n";
        }

        htmlCode.close();

        out.println("HTTP/1.1 \nContent-Type: text/html\n\r\n");
        out.println(content);
        out.flush();
    }catch(Exception e){
        e.printStackTrace();
    }
}

    private void buyItem(String itemName){
        String[] orderQuery = getItemAndCharacterFromQuery(itemName);
        String itemstring = orderQuery[0];
        String characterName = orderQuery[1];
        try{
            Item item = convertStringtoItem(itemstring);

            Character character = admin.getCharacterByName(characterName);

            if(character.purchase(item)){
                printHTMLPage(player_page);
                alert("Eşya alma işlemi başarılı, hayırlı olsun");
            }
            else{
                printHTMLPage(player_page);
                alert("Maalesef eşyayı alamadın, fakir puşt.");
            }

        }catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    private String[] getItemAndCharacterFromQuery(String query){
        String[] parts = query.split("%20");
        String itemstring = parts[0];
        parts = parts[1].split(" HTTP");
        String characterName = parts[0];
        String [] returned = {itemstring,characterName};

        return returned;
    }
    private TwoInOne<Character,Item> getItemAndCharacterObjectsFromQuery(String itemName){
      String[] orderQuery = getItemAndCharacterFromQuery(itemName);
      String itemstring = orderQuery[0];
      String characterName = orderQuery[1];
      try{
          Character character = admin.getCharacterByName(characterName);
          Item item = character.getItemByType(itemstring);
          return new TwoInOne<Character,Item>(character,item);
        }catch(NullPointerException e){
            e.printStackTrace();
        }
        return null;
    }
    private void wearItem(String itemName){
      TwoInOne<Character,Item> characterItemMap = getItemAndCharacterObjectsFromQuery(itemName);
      Character character = characterItemMap.getFirst();
      Item item = characterItemMap.getSecond();

      character.wearItem(item);
    }
    private void removeItem(String itemName){
      TwoInOne<Character,Item> characterItemMap = getItemAndCharacterObjectsFromQuery(itemName);
      Character character = characterItemMap.getFirst();
      Item item = characterItemMap.getSecond();

      character.removeItem(item);
    }
    private void sellItem(String itemName){
      TwoInOne<Character,Item> characterItemMap = getItemAndCharacterObjectsFromQuery(itemName);
      Character character = characterItemMap.getFirst();
      Item item = characterItemMap.getSecond();

      character.sellItem(item);
    }

    private static Item convertStringtoItem(String item){
        switch(item){
            case "ShortSword": return new ShortSword();
            case "LongSword": return new LongSword();
            case "OrdinaryAxe": return new OrdinaryAxe();
            case "LumberjackAxe": return new LumberjackAxe();
            case "BattleAxe": return new BattleAxe();
            case "Bow": return new Bow();
            case "CrossBow": return new CrossBow();
            case "LightArmor": return new LightArmor();
            case "MediumArmor": return new MediumArmor();
            case "HeavyArmor": return new HeavyArmor();
            case "TankArmor": return new TankArmor();
            case "Sandal": return new Sandal();
            case "LightShoe": return new LightShoe();
            case "SportShoe": return new SportShoe();
            case "LightArmoredShoe": return new LightArmoredShoe();
            case "MediumArmoredShoe": return new MediumArmoredShoe();
            case "HeavyShoe": return new HeavyShoe();
            case "Wand": return new Wand();
            case "Sunglasses": return new Sunglasses();
            case "Cloak": return new Cloak();
            default: return null;
        }
    }
    private void Attack(String enemyString,boolean close,boolean player){
      String parts[] = enemyString.split("%20");
      parts = parts[2].split(" HTTP");
      String characterString = parts[0];

      Player character =this.admin.getPlayerByCharacterName(characterString);
      Character enemy = null;

      if(enemyString.contains("Boss")){
        enemy = getEnemy("Boss");
      }
      else if (enemyString.contains("Knight")) {
        enemy = getEnemy("Knight");
      }else if (enemyString.contains("Farmer")) {
        enemy = getEnemy("Farmer");
      }else if (enemyString.contains("Wizard")) {
        enemy = getEnemy("Wizard");
      }else if (enemyString.contains("BlackSmith")) {
        enemy = getEnemy("BlackSmith");
      }else if (enemyString.contains("Barbarian")) {
        enemy = getEnemy("Barbarian");
      }else if (enemyString.contains("Wolfman")) {
        enemy = getEnemy("Wolfman");
      }
      if(enemy == null)
        throw new NullPointerException("Enemy is null");

      Villian enemyVillian=null;
      for(Villian v: this.admin.getVillians()){
        if(v.getCharacter() == enemy){
          enemyVillian = v;
          break;
        }
      }
      if(player){
        if(close){
            character.closeAttack(enemy);
        }else{
            character.wideAttack(enemy);
        }
      }else{
        try{
            enemyVillian.closeAttack(character.getCharacter());
        }catch(Exception e){
          e.printStackTrace();
        }
      }
      Villian villian = this.admin.VillianisKilled();
      if(villian != null){
        alert("Düşman öldürüldü: "+villian);
      }
    }
    private Character getEnemy(String type){
      List<Villian> villians = this.admin.getVillians();
      for(Villian v: villians){
        if(v.getCharacter().getClass().toString().equals("class "+type))
          return v.getCharacter();
      }
      return null;
    }
    private int takeAction(String action,String characterName) throws Exception{
      Character character = admin.getCharacterByName(characterName);
      if(action.contains("run")){
        int val = (int) character.run();
        logWriter(characterName+" "+val+" puan ile depara kalktı");
        return val;
      }
      if(action.contains("persuate")){
        int val = (int) character.convince();
        logWriter(characterName+" "+val+" puan ile ikna kabiliyetini kullandı.");
        return val;
      }
      if(action.contains("investigate")){
        int val = (int) character.investigate();
        logWriter(characterName+" "+val+" puan ile araştırma kabiliyetini kullandı.");
        return val;
      }
      if(action.contains("sneak")){
        int val = (int) character.sneak();
        logWriter(characterName+" "+val+" puan ile gizlendi.");
        return val;
      }
      if(action.contains("heal")){
        int val = (int) character.heal();
        logWriter(characterName+" "+val+" puan ile iyileştirme yeteneğini kullandı.");
      }
      throw new Exception("aksiyon uygulanırken bir hata meydana geldi");
    }
    private void setAbilities(String abilitiesString,String playerName){
      Player  player= this.admin.getPlayerByName(playerName);
      double[] abilities = new double[9];
      String number = "";
      int j = 0;
      int a = 0;
      for(int i = 0; i < abilitiesString.length(); i++){
        char c = abilitiesString.charAt(i);
        if(c == ',' || i == abilitiesString.length()-1){
          if(i == abilitiesString.length()-1) number += c;
          if(j < 9){
            abilities[j] = Double.parseDouble(number);
            j++;
            number = "";
          }
          else{
            switch(a){
              case 0:{
                int health = Integer.parseInt(number);
                this.admin.setHealth(health,player);
              }
              break;
              case 1:{
                int sanity = Integer.parseInt(number);
                this.admin.setSanity(sanity, player);
              }
              break;
              case 2:{
                int money = Integer.parseInt(number);
                this.admin.setMoney(money, player);
              }
              break;
            }
            number = "";
            a++;
          }
        }
        else{
          number += c;
        }
      }
      this.admin.setAbilities(abilities, player);
    }
    private void alert(){
        alert("Lütfen karakterin tipi seçiniz, karakterin ismini yazınız ve kendi isminizi yazınız. Not: karakterinin ismi başka bir karakterle aynı ismi taşıyorsa da bu mesajı alıyor olabilirsin.");
    }
    private void alert(String message){
        try{
            PrintStream out = new PrintStream(socket.getOutputStream());
            out.println("<script>window.alert('"+message+"')</script>");
            out.flush();
        }catch(IOException ex){
            ex.printStackTrace();
        }catch(Exception a){
            a.printStackTrace();
        }
    }
    public static void logWriter(String log)throws IOException {
    controlSizeOfLogFile();
    BufferedWriter writer = new BufferedWriter(new FileWriter("../htmlCodes/log.txt", true));
    writer.append("<br>\n");
    writer.append(">"+log);
    writer.close();
    }
    private static void controlSizeOfLogFile()throws IOException{
    int line = 0;
    BufferedReader reader = new BufferedReader(new FileReader("../htmlCodes/log.txt"));
    while(reader.ready()){
      line++;
      reader.readLine();
    }
    reader.close();
    if(line > 25){
      BufferedWriter writer = new BufferedWriter(new FileWriter("../htmlCodes/log.txt", false));
      writer.write("");
      writer.flush();
      writer.close();
    }
  }
    public static void main(String[] a) throws IOException{
        GameServer server = new GameServer(80);
        while(true)
        server.run();
    }
}
