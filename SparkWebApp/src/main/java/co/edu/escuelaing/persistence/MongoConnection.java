package co.edu.escuelaing.persistence;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class MongoConnection {
    private DBCollection coleccion;
    private DB db;

    /**
     * Establezco coneccion con la DB por el puerto por defecto de mongo 27017
     * @throws UnknownHostException
     */
    public MongoConnection() throws UnknownHostException { //127.0.0.1
        // ec2-54-234-55-207.compute-1.amazonaws.com
        MongoClient mongoClient = new MongoClient("ec2-54-234-55-207.compute-1.amazonaws.com" , 27017);
        db= mongoClient.getDB("DockerWeb");
        coleccion= db.getCollection("logs");
    }

    /**
     * Recibe el mensaje del log y la fehcha de creacion y lo inserta en
     * la coleccion de los logs
     * @param message
     * @param date
     */
    public void add(String message, Date date) {
        BasicDBObject objeto= new BasicDBObject();
        objeto.put("message",message);
        objeto.put("date",date);
        coleccion.insert(objeto);
    }

    /**
     * Retorna un arraylist con los elementos dentro de la coleccion
     * @return
     */
    public ArrayList<BasicDBObject> view() {
        ArrayList<BasicDBObject> registros = new ArrayList<BasicDBObject>();
        DBCursor mensajes = coleccion.find();
        while (mensajes.hasNext()){
            registros.add((BasicDBObject) mensajes.next());
        }
        return registros;
    }

}