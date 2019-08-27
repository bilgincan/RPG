import java.net.*;
import java.io.*;
import java.util.*;

public class GameServer{
    private final ServerSocket server;
    private static final String homePage = "../htmlCodes/index.html";
    private static final String player_page = "../htmlCodes/player.html";
    private Admin admin;
    private List<Player> players;
    private List<Villian> villians;
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
            if(input.contains("GET /htmlCodes/")){
                String[] parts = input.split("/");
                String file = parts[2];
                parts = file.split(" ");
                file = "../htmlCodes/";
                file += parts[0];

                printHTMLPage(file);
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
                if(initializePlayer(playerName,characterName,character))
                    printHTMLPage(player_page);
                else{
                    printHTMLPage(homePage);
                    alert();
                }
            }

            System.out.println(this.admin.getPlayers().toString());

        }catch(IOException e){
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
        players = this.admin.getPlayers();
        villians = this.admin.getVillians();
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
