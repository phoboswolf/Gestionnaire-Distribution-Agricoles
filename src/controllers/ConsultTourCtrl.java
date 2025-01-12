package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import modele.Commande;
import modele.Tournee;
import utility.ControllersUtils;
import utility.DateManager;
import utility.GenerateurUrl;

/**
 * Contrôleur permettant l'aperçu Tournée.
 */
public class ConsultTourCtrl implements Initializable {

  @FXML
  private Text tourLabelText;

  @FXML
  private Text tourWeightText;

  @FXML
  private Text tourDatetimeText;

  @FXML
  private Text startText;

  @FXML
  private Text endText;

  @FXML
  private Text vehicleImmatText;

  @FXML
  private Text capacityText;

  @FXML
  private ListView<Commande> commListView;

  @FXML
  private WebView tourMapWebView;

  private static Tournee tournee;

  @Override
  public void initialize(URL arg0, ResourceBundle arg1) {
    tourLabelText.setText(tournee.getLibelle());
    tourWeightText.setText(String.valueOf(tournee.getPoids()).concat("kg"));
    tourDatetimeText.setText(DateManager.TimestampToDateString(tournee.getHoraireDebut()));
    startText.setText(DateManager.TimestampToHourString(tournee.getHoraireDebut()));
    endText.setText(DateManager.TimestampToHourString(tournee.getHoraireFin()));
    vehicleImmatText.setText(tournee.getVehicule().getNumImmat());
    capacityText.setText(String.valueOf(tournee.getPoids()).concat("kg"));
    commListView.getItems().addAll(tournee.getCommandes());

    // Affichage du libellé uniquement sur le listView.
    commListView.setCellFactory(lv -> new ListCell<>() {
      @Override
      public void updateItem(Commande row, boolean empty) {
        super.updateItem(row, empty);
        setText(empty ? null : row.getLibelle() + " | " + row.getHoraireDebut() + " | " + row.getHoraireFin());
      }
    } // updateItem
    ); // setCellFactory

    WebEngine webEngine = tourMapWebView.getEngine();
    webEngine.load(GenerateurUrl.AffichageTourneeUrl(tournee));
  } // initialize

  /**
   * Méthode qui ferme la vue de consultation
   * des informations d'une Tournée.
   * 
   * @param event ActionEvent
   */
  public void closeConsultTour(ActionEvent event) {
    ControllersUtils.closePopupAndUpdateParent(event);
  } // closeConsultTour

  /**
   * Méthode qui récupère la Tournée sélectionnée dans la listView
   * de la vue précédente (prodSelectMenu)
   * 
   * @param tour Tournee
   */
  public static void setTournee(Tournee tour) {
    tournee = tour;
  } // setTournee

} // ConsultTourCtrl