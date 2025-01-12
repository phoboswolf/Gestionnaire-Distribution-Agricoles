package tests.database;

import utility.DatabaseConnection;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertNull;

/**
 * Testons DatabaseConnection.
 */
public class DatabaseConnectionTest {

    /**
     * Fermons la connection avant chaque test.
     *
     * @throws SQLException
     */
    @BeforeEach
    void init() throws SQLException {
        DatabaseConnection.close("testing");
    } // init

    /**
     * Nous vérifions que le Singleton renvoie bien une Instance lors que
     * getInstance est invoqué pour la premiere fois.
     *
     *  @throws IOException
     *  @throws ClassNotFoundException
     */
    @Test
    @DisplayName("Première Connection valide")
    void connectionValide() throws IOException, ClassNotFoundException {
        try {
            Connection conn = DatabaseConnection.getInstance("testing");
            assertNotNull(conn);
        } catch (SQLException e) {
            fail("Serveur MySQL indisponible");
        } // try/catch
    } // connectionValide

    /**
     * Nous vérifions qu'un environnement inexistant du fichier de configuration
     * génère bien une erreur IOException.
     *
     *  @throws ClassNotFoundException
     */
    @Test
    @DisplayName("Chargeur d'attribut fonctionnel")
    void chargeurAttributFonctionnel() throws ClassNotFoundException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getInstance("existePas");
            fail("Connection qui ne devrait pas exister.");
        } catch (SQLException e) {
            fail("Problème avec le constructeur.");
        } catch (IOException e) {
            assertNull(conn);
        } // try/catch
    } // chargeurAttributFonctionnel

    /**
     * Nous cherchons à tester l'unicité de la connection.
     * Le Singleton doit renvoyer une Instance si elle existe deja.
     */
    @Test
    @DisplayName("Singleton retournant une et une seule Connection")
    void singletonFonctionnel() throws IOException, ClassNotFoundException {
        Connection conn;
        Connection conn2;
        try {
            conn = DatabaseConnection.getInstance("testing");
            conn2 = DatabaseConnection.getInstance("testing");
            assertEquals(conn, conn2);
        } catch (SQLException e) {
            fail("Le Singleton ne renvoie pas une unique Connection");
        } // try/catch
    } // singletonFonctionnel

    /**
     * Fermeture de la Connection apres les tests.
     * 
     * @throws SQLException
     */
    @AfterAll
    public static void tearDown() throws SQLException {
        DatabaseConnection.close("testing");
    } // tearDown

} //DatabaseConnectionTest
