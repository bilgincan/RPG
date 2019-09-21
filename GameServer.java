import java.net.*;
import java.io.*;
import java.util.*;

public class GameServer{
    private final ServerSocket server;
    private static final String homePage = "../htmlCodes/index.html";
    private static final String player_page = "../htmlCodes/player.html";
    private Admin admin;
    private Socket socket;

    public GameServer(int port) throws IOException{
        server = new ServerSocket(port);

        //you must delete this line after tests
        initializeAdmin();
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
            PrintStream out = new PrintStream(socket.getOutputStream());

            //for getting inputs from the user
            String input = in.readLine();
            System.out.println(input);


            if(input.equals("GET / HTTP/1.1")){
            printHTMLPage(homePage);
        }

        if(input.contains("purchase")){
            String []parts = input.split("chase=");
            buyItem(parts[1]);
        }

        if(input.contains("wear")){
            String[] parts = input.split("wear=");
            wearItem(parts[1]);
        }
        if(input.contains("remove")){
            String[] parts = input.split("remove=");
            removeItem(parts[1]);
        }
        if(input.contains("sell")){
            String[] parts = input.split("sell=");
            sellItem(parts[1]);
        }
        if(input.contains("closeattack")){
            String[] parts = input.split("closeattack");
            Attack(parts[1],true);
        }
        if(input.contains("wideattack")){
          String[] parts = input.split("wideattack");
          Attack(parts[1],false);
        }

        //load html files by names
        if(input.contains("GET /htmlCodes/") && !input.contains("player.html")){
                String[] parts = input.split("/");
                String file = parts[2];
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


                defineAbilitiesForJS(character);

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
                    System.out.println("Karakter yaratıldı");
                    defineAbilitiesForJS(characterName);
                }
                else{
                    printHTMLPage(homePage);
                    alert();
                }
            }

            System.out.println(this.admin.getPlayers().toString());

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

    private void initializeAdmin(){
        this.admin = new Admin();
        this.admin.generateEnemy("BlackSmith");
    }

    private void defineAbilitiesForJS(String character){
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
            //printing abilities of the character in to the web browser
            Character charprint = neededOne.getCharacter();
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
            printHTMLPage(player_page,jsCode);
        }
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
        out.flush();
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
    private void Attack(String enemyString,boolean close){
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
      if(close){
        character.closeAttack(enemy);
      }else{
        character.wideAttack(enemy);
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
    private void alert(){
        alert("Lütfen karakterin tipi seçiniz, karakterin ismini yazınız ve kendi isminizi yazınız. Not: karakterinin ismi başka bir karakterle aynı ismi taşıyorsa da bu mesajı alıyor olabilirsin.");
    }
    private void alert(String message){
      System.out.println("tetiklendi");
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
    public static void main(String[] a) throws IOException{
        GameServer server = new GameServer(8080);
        while(true)
        server.run();
    }
}
