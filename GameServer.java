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
                System.out.println(character);

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
            for(int i = 0; i< items.size(); i++){
                Item item = items.get(i);
                if(item instanceof Weapon){
                    jsCode += "['Weapon','"+item.getClass()+"']";
                }
                else if(item instanceof Shoe){
                    jsCode += "['Shoe','"+item.getClass()+"']";
                }
                else if(item instanceof Armor){
                    jsCode += "['Armor','"+item.getClass()+"']";
                }
                else if(item instanceof Wizardish){
                    jsCode += "['Wizardish','"+item.getClass()+"']";
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
    private void alert(){
        try{
            PrintStream out = new PrintStream(socket.getOutputStream());
            out.println("<script>window.alert('Lütfen karakterin tipi seçiniz, karakterin ismini yazınız ve kendi isminizi yazınız. Not: karakterinin ismi başka bir karakterle aynı ismi taşıyorsa da bu mesajı alıyor olabilirsin.')</script>");
            out.flush();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    public static void main(String[] a) throws IOException{
        GameServer server = new GameServer(8080);
        while(true)
        server.run();
    }
}
