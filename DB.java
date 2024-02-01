import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
        }
    }

    private boolean createMsgTable() throws SQLException{
        String createTableSQL = "CREATE TABLE IF NOT EXISTS logs ("
            +"id SERIAL PRIMARY KEY,"
            +"sender_address INET NOT NULL,"
            +"sender_port SMALLINT NOT NULL CHECK (sender_port >= 0 AND sender_port <= 65535)"
            +"msg TEXT NOT NULL"
            +"log_date DATE NOT NULL " 
            +");";
        PreparedStatement preparedStatement = this.connection.prepareStatement(createTableSQL);
        int rowsAffected = preparedStatement.executeUpdate();
        return rowsAffected>0; // Return true if at least one row was affected (table created)
    }

    public boolean insertMsgTable(InetAddress ipAddress,int senderPort,String msg) throws SQLException{
        
        String insertSQL = "INSERT INTO logs (sender_address,sender_port,msg,log_date) VALUES (?,?,?, CURRENT_DATE)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
        
        preparedStatement.setObject(1, ipAddress);
        preparedStatement.setInt(2, senderPort);
        preparedStatement.setString(3, msg);
        preparedStatement.executeUpdate();
    
        int rowsAffected = preparedStatement.executeUpdate();
        return rowsAffected>0; // Return true if at least one row was affected (row created)
    }

}