import java.net.*;
import java.io.*;
import java.util.*;

public class GameServer{
    private final ServerSocket server;
    private static final String index_page = "../htmlCodes/index.html";
    private String currentScreen;
    private static final String jquery = "../htmlCodes/jquery-3.2.1.js";
    private static final String characterInfos = "../htmlCodes/characters.html";
    private Admin admin;
    private List<Player> players;
    private List<Villian> villians;

    public GameServer(int port) throws IOException{
        server = new ServerSocket(port);
        currentScreen = index_page;

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

        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream out = new PrintStream(socket.getOutputStream());

            BufferedReader htmlCode = new BufferedReader(new FileReader(currentScreen));

            //content is the content of the html file
            String content = "";

            while( htmlCode.ready()){
                content += htmlCode.readLine();
                content += "\n";
            }

            htmlCode.close();
            //for getting inputs from the user
            String input = in.readLine();
            System.out.println(input);

            //loads character info screen
            if(input.equals("GET /htmlCodes/characters.html HTTP/1.1")){
                BufferedReader characters = new BufferedReader(new FileReader(characterInfos));

                String charactersContent = "";

                while(characters.ready()){
                    charactersContent += characters.readLine();
                    charactersContent += "\n";
                }
                characters.close();

                out.println("HTTP/1.1 \nContent-Type: text/html\n\r\n");
                out.println(charactersContent);
                out.flush();
            }
            //loads jquery
            else if(input.equals("GET /jquery-3.2.1.js HTTP/1.1")){
                BufferedReader jqueryCode = new BufferedReader(new FileReader(jquery));

                String jquerycontent = "";

                while(jqueryCode.ready()){
                    jquerycontent += jqueryCode.readLine();
                    jquerycontent += "\n";
                }
                jqueryCode.close();

                out.println(jquerycontent);
                out.flush();

            }
            else{
                out.println("HTTP/1.1 \nContent-Type: text/html\n\r\n");
                out.println(content);
                out.flush();
            }


            //extract the input
            if(this.admin != null && input.contains("GET /initializeCharacters?character=")){
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

                //neccessarypart is in that case the type of the character
                initializePlayer(playerName,characterName,character);
            }

            System.out.println(this.admin.getPlayers().toString());

        }catch(IOException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
        }

        //for initializing players from the query
    private void initializePlayer(String playerName, String characterName,String character){
        this.admin.generatePlayer(playerName, characterName, character);
    }

    private void initializeAdmin(){
        this.admin = new Admin();
        players = this.admin.getPlayers();
        villians = this.admin.getVillians();
    }
    public static void main(String[] a) throws IOException{
        GameServer server = new GameServer(8080);
        while(true)
        server.run();
    }
}
