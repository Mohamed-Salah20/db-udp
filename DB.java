import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class DB{
    //Database configuration
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/chat_udp";    
    private static final String DB_USER = "postgres";
    private static final String DB_PASS = "postgres";
    // private static final String JDBC_JAR = "org.postgresql.Driver";

    private Connection connection;
    public DB() {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
            createMsgTable();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("getConnection failed");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.err.println("can not find driver");
        }
    }
    
    /*Droping table for testing*/
    // private boolean dropMsqTable() throws SQLException{
    //     String dropTableSQL = "DROP TABLE IF EXISTS logs;";
    //     PreparedStatement preparedStatement = this.connection.prepareStatement(dropTableSQL);
    //     boolean isDroped = preparedStatement.execute();
    //     return isDroped;
    // }
    /* */

    private boolean createMsgTable() throws SQLException{
        // dropMsqTable();
        String createTableSQL = "CREATE TABLE IF NOT EXISTS logs ("
            +"id SERIAL PRIMARY KEY,"
            +"sender_address VARCHAR(15) NOT NULL,"
            +"sender_port INT NOT NULL CHECK (sender_port >= 0 AND sender_port <= 65535),"
            +"msg TEXT NOT NULL,"
            +"log_timestamp TIMESTAMP NOT NULL" 
            +");";
        PreparedStatement preparedStatement = this.connection.prepareStatement(createTableSQL);
        boolean isCreated  = preparedStatement.execute();
        return isCreated ; 
    }

    public boolean insertMsgTable(InetAddress ipAddress,int senderPort,String msg) throws SQLException{
        
        String insertSQL = "INSERT INTO logs (sender_address,sender_port,msg,log_timestamp) VALUES (?,?,?, NOW())";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
        
        // preparedStatement.setObject(1, ipAddress, Types.OTHER); //Can't infer the SQL type to use for an instance of java.net.Inet4Address. Use setObject() with an explicit Types value to specify the type to use.
        //ERROR: invalid input syntax for type inet: "/127.0.0.1"
        preparedStatement.setString(1, ipAddress.getHostAddress());
        preparedStatement.setInt(2, senderPort);
        preparedStatement.setString(3, msg);
        preparedStatement.executeUpdate();
    
        int rowsAffected = preparedStatement.executeUpdate();
        return rowsAffected>0; // Return true if at least one row was affected (row created)
    }

}