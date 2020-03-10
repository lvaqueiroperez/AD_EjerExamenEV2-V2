package pvehiculos2;
//VARIANTE DEL EJERCICIO INVENTADA POR MI
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Projections.include;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.bson.Document;

//OJO!!! COMO ES UN PROYECTO NUEVO, LAS CLASES DE OBJECT DB NO SE PUEDEN LLAMAR IGUAL QUE OTRAS
//CLASES QUE YA EXISTEN EN LA BD DE OBJECT.
//TEDREMOS QUE INSERTAR OBJETOS DE OTRA CLASE, POR LO QUE EN LA BASE TENDREMOS OBJETOS
//RELACIONADOS CON EL PROYECTO "Pvehiculos" Y OTROS CON "Pvehiculos2" 
public class Pvehiculos2 {

    //VARIABLES ORACLE
    private static String id;
    private static String dni;
    private static String nomec;
    private static String nomeveh;
    //CAMPOS NUMÉRICOS DENTRO DE OBJETOS ORACLE TIENEN QUE TENER ESTE FORMATO
    private static java.math.BigDecimal pf;

    //VARIABLES MONGO
    private static String codveh;

    //VARIABLES RESTANTES
    private static int ncompras;
    private static int prezoorixe;
    private static int anomatricula;

    public static void ejercicio() throws SQLException {

        //CONEXION A ORACLE
        Connection conn;
        String driver = "jdbc:oracle:thin:";
        String host = "localhost.localdomain"; // tambien puede ser una ip como "192.168.1.14"
        String porto = "1521";
        String sid = "orcl";
        String usuario = "hr";
        String password = "hr";
        String url = driver + usuario + "/" + password + "@" + host + ":" + porto + ":" + sid;

        conn = DriverManager.getConnection(url);

        //OBTENEMOS TODAS LAS VARIABLES DE ORACLE
        PreparedStatement ps = conn.prepareStatement("select * from finalveh where id >= 5");

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            id = rs.getString("id");
            System.out.println("ID: " + id);
            dni = rs.getString("dni");
            System.out.println("DNI: " + dni);
            nomec = rs.getString("nomec");
            System.out.println("NOMEC: " + nomec);

            //AHORA OBTENEMOS LOS DATOS DE LA VARIABLE OBJETO
            java.sql.Struct objSQL = (java.sql.Struct) rs.getObject(4);

            Object[] campos = objSQL.getAttributes();

            nomeveh = (String) campos[0];

            pf = (java.math.BigDecimal) campos[1];

            System.out.println("NOMEVEH: " + nomeveh);
            System.out.println("PF: " + pf);

            //VARIABLES MONGO
            //CONEXIÓN MONGO
            MongoClient mongoClient = new MongoClient("localhost", 27017);

            MongoDatabase database = mongoClient.getDatabase("test");
            //OJO!!! PUEDE SER QUE TENGAMOS QUE ACCEDER A 2 COLECCIONES DISTINTAS DE MONGO,
            //PARA ELLO, SIMPLEMENTE CREAR 2 OBJETOS COMO EL DE ABAJO CON LA MISMA DATABASE,
            //ACCEDIENDO A UNO U OTRO SEGÚN LA COLECCIÓN QUE QUERAMOS USAR
            MongoCollection<Document> collection = database.getCollection("vendas");

            //SEGUIMOS CON LAS VARIABLES DE MONGO
            //NECESITAMOS OBTENER CAMPOS ESPECÍFICOS, USAMOS UN ITERABLE MEJOR
            FindIterable<Document> buscar = collection.find(eq("dni", dni)).projection(include("codveh"));

            //RECORREMOS EL ITERABLE
            //COMO CADA CAMPO SOLO TIENE UN CODVEH, SABEMOS DE ANTEMANO QUE SOLO RECORRERA EL BUCLE FOR 1 VEZ
            //POR CAMPO
            for (Document z : buscar) {

                codveh = z.getString("codveh");
                System.out.println("CODVEH: " + codveh);
                //DEBIDO A UN ERROR QUE HICIMOS EN MONGO, NOS RETORNA 2 VECES EL CODVEH DEL DNI 7777a
            }
            //CERRAMOS MONGO
            mongoClient.close();

            //CREAMOS EL RESTO DE VARIABLES QUE NECESITAMOS
            ncompras = 1;
            System.out.println("NCOMPRAS: " + ncompras);
            if (pf.intValue() > 10000) {

                prezoorixe = 500;

            } else {

                prezoorixe = pf.intValue() - 500;

            }

            System.out.println("PREZOORIXE: " + prezoorixe);

            //ANOMATRICULA
            if (pf.intValue() <= 10000) {
                anomatricula = 2000;
            } else if ((pf.intValue() > 10000) && (pf.intValue() <= 12000)) {
                anomatricula = 2010;
            } else if (pf.intValue() >= 12000) {

                anomatricula = 2015;

            }
            System.out.println("ANOMATRICULA: " + anomatricula);
            System.out.println("************************************************");

            //LO METEMOS TODO EN OBJECTDB 
            //OJO!!! HACER UNA TRANSACCIÓN POR CADA OPERACIÓN Y CERRAR AL FINAL !!!
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("$objectdb/db/vehicli.odb");

            EntityManager em = emf.createEntityManager();

            em.getTransaction().begin();

            Vehiculos2 p = new Vehiculos2(codveh, nomeveh, anomatricula, prezoorixe);
            em.persist(p);

            em.getTransaction().commit();

            em.getTransaction().begin();
            Clientes2 c = new Clientes2(dni, nomec, ncompras);
            em.persist(c);
            em.getTransaction().commit();

            System.out.println("DATOS METIDOS, CERRANDO CONEXIÓN");
            em.close();

        }

        //CERRAMOS ORACLE
        conn.close();

    }

    public static void main(String[] args) throws SQLException {

        ejercicio();

    }

}
