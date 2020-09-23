package co.edu.escuelaing;

import static spark.Spark.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import spark.Request;
import spark.Response;

public class App {
    //Posibles puetos para el load balancer
    private static int ports[]= {35001,35002,35003};
    //Puerto inivial, esta variable va a cambiar con el load balancer
    private static int myPort = 0 ;

    /**
     *
     * Metodo principal
     * @param args
     * @throws UnknownHostException
     */
    public static void main( String[] args ) throws UnknownHostException {
        port(getPort());
        get("/", (req, res) ->  inputView(req, res));
        post("/", (req, res) ->  registerView(req, res));
    }

    /**
     * Es la vista principal, aca de van a registrar los datos, tambien se van a jalar los
     * datos que estan en la bd para mostrarlos
     * @param req
     * @param res
     * @return
     */
    private static String  inputView(Request req, Response res){
        String myView="";
        //String server="xxx";
        //String logsToTable="";
        try {
            System.out.println("HOlaaa fuera de URL");//http://ec2-54-234-55-207.compute-1.amazonaws.com:
            URL url = new URL("http://ec2-54-234-55-207.compute-1.amazonaws.com:"+String.valueOf(ports[myPort])+"/viewlogs");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            System.out.println("HOla cogio la URL");
            String inputLine;
            String data="";
            while ((inputLine = reader.readLine()) != null) {
                System.out.println(inputLine);
                data+=inputLine;
            }
            String logs[]=data.split(",");
            String tableDrawer="";
            roundRobin();
            for(String log:logs) {
                System.out.println("----- Log msg: "+log);
                String info[]=log.split("-");
                tableDrawer+="<tr><td>"+info[0]+"</td><td>"+info[1]+"</td><td>"+info[2]+"</td></tr>";
            }

            myView = "<!DOCTYPE html>"
                    + "<html>"
                    + "<style>"
                    + "table, th, td {"
                    + "border: 1px solid black;"
                    + "border-collapse: collapse;"
                    + "border-spacing: 0;"
                    + "}"
                    + "</style>"
                    +"<center>"
                    +"<h1>Lista de Logs AREP</h1>"
                    +"<br/>"
                    +"<p>Ingrese el Log:</p>"
                    + "<form name='loginForm' method='post' action='/'>"
                    +"<input type='text' name='message'/> <br/>"
                    +"<br/>"
                    +"<input type='submit' value='Ingresar' />"
                    +"</form>"
                    +"<br/>"
                    + "<Table>"
                    + "<tr>"
                    + "<th>Log No</th>"
                    + "<th>Mensaje</th>"
                    + "<th>Fecha</th>"
                    + "</tr>"
                    + tableDrawer
                    + "</Table>"
                    +"</center>"
                    + "</body>"
                    + "</html>";
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myView;
    }

    /**
     * Donde se van a registrar los datos y se van a guardar en la base de datos, es el metodo post, va a majejar una url la cual va a
     * contener los datos a registrar
     * @param req
     * @param res
     * @return
     */
    private static String  registerView(Request req, Response res){
        try {
            URL url = new URL("http://ec2-54-234-55-207.compute-1.amazonaws.com:"+String.valueOf(ports[myPort])+"/savelogs?message="+(req.queryParams("message").replace(' ','_')));
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            roundRobin();
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                System.out.println(inputLine);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputView(req,res);
    }

    /**
     * Balancedor de carga RoundRobin
     */
    private static void roundRobin() {
        if(myPort<2) {
            myPort+=1;
        }else {
            myPort  = 0;
        }
    }

    /**
     * Puerto por defecto
     * @return
     */
    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4000;
    }
}