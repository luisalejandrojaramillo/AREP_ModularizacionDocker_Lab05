package co.edu.escuelaing;

import co.edu.escuelaing.persistence.MongoConnection;
import static spark.Spark.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

import spark.Request;
import spark.Response;
import com.mongodb.BasicDBObject;

public class App {
    private static MongoConnection mongo;

    /**
     * Metodo principal
     * @param args
     * @throws UnknownHostException
     */
    public static void main( String[] args ) throws UnknownHostException {
        port(getPort());
        mongo = new MongoConnection();
        get("/viewlogs", (req, res) ->  view(req, res));
        get("/savelogs", (req, res) ->  save(req, res));

    }

    /**
     * Recibe un nuevo Log, por medio de la request, este se encarga de registralo en la DB
     * mandando el mensaje y la fecha, ademas este actualiza la vista con el nuevo log
     * @param req
     * @param res
     * @return
     */
    private static String  save(Request req, Response res){
        mongo.add(req.queryParams("message"), new Date());
        return  view(req,res);
    }

    /**
     * Retorna un JSON con los ultimos 10 logs insertados en la DB
     * @param req
     * @param res
     * @return
     */
    private static String  view(Request req, Response res){
        res.type("application/json");
        String datos[]=null;
        ArrayList<BasicDBObject> list= mongo.view();
        int cont = 1;
        if(list.size()<=10) {
            datos =  new String[list.size()];
            for(BasicDBObject d:list) {
                datos[cont-1]=String.valueOf(cont)+"-"+d.get("message").toString()+"-"+d.get("date").toString();
                cont++;
            }
        }
        else {
            datos =  new String[10];
            for(int i=list.size()-10;i<list.size();i++) {
                datos[cont-1]=String.valueOf(cont)+"-"+(list.get(i)).get("message").toString()+"-"+(list.get(i)).get("date").toString();
                cont++;
            }
        }
        return String.join(",", datos);
    }

    /**
     * Puerto por defecto
     * @return
     */
    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }

}
