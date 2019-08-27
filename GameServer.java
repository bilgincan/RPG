import java.net.*;
import java.io.*;

public class GameServer{
    private final ServerSocket server;
    private static final String index_page = "../htmlCodes/index.html";
    private static final String jquery = "../htmlCodes/jquery-3.2.1.js";
    private Admin admin;

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
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintStream out = new PrintStream(socket.getOutputStream());

            BufferedReader htmlCode = new BufferedReader(new FileReader(index_page));

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

            if(input.equals("GET /jquery-3.2.1.js HTTP/1.1")){
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

            //extract the input of the choise of character
            if(input.contains("GET /initializeCharacters?character=")){
                String[] parts = input.split("character=");
                String neccessarypart = parts[1];
                parts = neccessarypart.split("&");
                neccessarypart = parts[0];
                initializeCharacter(neccessarypart);
            }
        }catch(IOException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
        }

        //for initializing characters from the query
    private void initializeCharacter(String character){

    }
    public static void main(String[] a) throws IOException{
        GameServer server = new GameServer(8080);
        while(true)
        server.run();
    }
}
