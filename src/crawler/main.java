package crawler;

import static crawler.main.g;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class main  {

    public static DB db = new DB();
    static GUI g = new GUI();
    static boolean start = true;
   

    public static void main(String[] args) throws SQLException, IOException {

        g.setVisible(true);

        db.runSql2("TRUNCATE Record;");
        

    }

     static void addDbvalue(String link) throws SQLException, IOException {
        String sql = "select * from Record where URL = '" + link + "'";
        ResultSet rs = db.runSql(sql);
        if (rs.next()) {
            

        } else {

            sql = "INSERT INTO  `Crawler`.`Record` " + "(`URL`) VALUES " + "(?);";
            PreparedStatement stmt = db.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, link);
            stmt.execute();
            g.TextArea1.append(link + "\n");
            processPage(link);

        }
    }

     public static void processPage(String URL) throws SQLException, IOException {
         
        Document doc = Jsoup.connect(URL).userAgent("Mozilla").get();

        Elements questions = doc.select("a[href]");
        
        for (Element link : questions) {

            addDbvalue(link.attr("abs:href"));
            g.TextArea1.setCaretPosition(g.TextArea1.getText().length() - 1);
            g.TextArea1.update(g.TextArea1.getGraphics());

        
    }}
}

class multi extends main  implements Runnable {

    String link;
    

    

    public void run() {
        try {
            processPage(link);
        } catch (SQLException ex) {
            Logger.getLogger(multi.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(multi.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
