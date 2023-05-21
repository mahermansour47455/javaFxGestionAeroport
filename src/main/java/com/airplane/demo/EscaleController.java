package com.airplane.demo;

import com.airplane.demo.entities.Aeroport;
import com.airplane.demo.entities.Vol;
import com.airplane.demo.entities.escale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class EscaleController implements Initializable {
    @FXML
    private Button AddEscale;

    @FXML
    private TextField villeAer;

    @FXML
    private Button btn;

    @FXML
    private Button RetournerAuVol;

    @FXML
    private TextField Aeroport;

    @FXML
    private TableColumn<Vol, String> ColAA;

    @FXML
    private TableColumn<Vol, String> ColAd;

    @FXML
    private TableColumn<Vol, String> ColDA;

    @FXML
    private TableColumn<Vol, String> ColDD;

    @FXML
    private TableColumn<Vol, String> ColHA;

    @FXML
    private TableColumn<Vol, String> ColHD;


    @FXML
    private Button SelectBtn;

    @FXML
    private TableView<Vol> TablVol;

    @FXML
    private TableColumn<Vol, Integer> colId;

    @FXML
    private TableColumn<Vol, String> colEtat;

    @FXML
    private TextField heureArriver;

    @FXML
    private TextField heureDepart;


    @FXML
    private DatePicker dateArriver;

    @FXML
    private DatePicker datedepart;

    @FXML
    private TextField vharriver;

    @FXML
    private TextField vhdepart;

    @FXML
    private Button btnaddvol;
    @FXML
    private ComboBox<Aeroport> aeroportArriver;

    @FXML
    private ComboBox<Aeroport> aeroportDepart;
    @FXML
    private ComboBox<Aeroport> aeroportDepart1;

    //tab escale
    @FXML
    private TableColumn<escale, String> HAriiver;

    @FXML
    private TableColumn<escale, String> HEscale;

    @FXML
    private TableColumn<escale, String> AEscale;

    @FXML
    private TableView<escale> tabEscale;
    //tab aeroport
    @FXML
    private TableColumn<Aeroport, String> nomaeroport;

    @FXML
    private TableColumn<Aeroport, Integer> numeroAeroport;

    @FXML
    private TableView<Aeroport> tabAeroport;

    @FXML
    private TableColumn<Aeroport, String> villeaeroport;


    @FXML
    public void loadAllAeroportIntheComboBox(ActionEvent event) {
        PreparedStatement st = null;
        ResultSet rs = null;
        Connection con = Connexion.getConnexionn();
        String sql = "SELECT * FROM aeroport";
        //je veux recuperer les aeroport dans la base de donnee et les met dans les combobox
        ObservableList<Aeroport> list = FXCollections.observableArrayList();
        try {
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while (rs.next()) {
                list.add(new Aeroport(rs.getInt("idAeroport"), rs.getString("nomAeroport"), rs.getString("villeAeroport")));


            }
            aeroportArriver.setItems(list);
            aeroportDepart.setItems(list);
            aeroportDepart1.setItems(list);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @FXML
    void addVol(ActionEvent event) {
        PreparedStatement st = null;
        ResultSet rs = null;
        Connection con = Connexion.getConnexionn();
        String dateArriver = this.dateArriver.getValue().toString();
        String datedepart = this.datedepart.getValue().toString();
        String varriver = this.aeroportArriver.getSelectionModel().getSelectedItem().toString();
        String vdepart = this.aeroportDepart.getSelectionModel().getSelectedItem().toString();
        String vharriver = this.vharriver.getText();
        String vhdepart = this.vhdepart.getText();
        try {

            String sql = "INSERT INTO vol(aerropDepart,aeroportArrivee,heureDepart,heureArrivee,dateDepart,dateArrivee) VALUES (?,?,?,?,?,?)";
            st = con.prepareStatement(sql);
            st.setString(1, vdepart);
            st.setString(2, varriver);
            st.setString(3, vhdepart);
            st.setString(4, vharriver);
            st.setString(5, datedepart);
            st.setString(6, dateArriver);
            int e = st.executeUpdate();
            if (e == 1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Vol added successfully");
                alert.showAndWait();
                TablVol.setItems(getVols());

                this.vharriver.setText("");
                this.vhdepart.setText("");


            } else {
                System.out.println("error");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(cellData -> cellData.getValue().idVolProperty().asObject());
        ColAd.setCellValueFactory(cellData -> cellData.getValue().aeroportArriveeProperty());
        ColAA.setCellValueFactory(cellData -> cellData.getValue().aerropDepartProperty());
        ColHA.setCellValueFactory(cellData -> cellData.getValue().dateArriveeProperty());
        ColHD.setCellValueFactory(cellData -> cellData.getValue().dateDepartProperty());
        ColDA.setCellValueFactory(cellData -> cellData.getValue().heureArriveeProperty());
        ColDD.setCellValueFactory(cellData -> cellData.getValue().heureDepartProperty());
        colEtat.setCellValueFactory(cellData -> cellData.getValue().etatProperty());
        TablVol.setItems(getVols());
    }

    public void AffecterEscaleAuVol() {
        int idVol = TablVol.getSelectionModel().getSelectedItem().getIdVol();
        String aeroport = aeroportDepart1.getSelectionModel().getSelectedItem().toString();
        String heureA = heureArriver.getText();
        String heureD = heureDepart.getText();

        Connection con = Connexion.getConnexionn();
        PreparedStatement preparedStatement = null;

        try {
            String query = "INSERT INTO escale (nomAeroport, heureDepart, heureArrivee, idVol) VALUES (?, ?, ?, ?)";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, aeroport);
            preparedStatement.setString(2, heureA);
            preparedStatement.setString(3, heureD);
            preparedStatement.setInt(4, idVol);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Escale ajoutée avec succès");
                alert.showAndWait();

                heureArriver.setText("");
                heureDepart.setText("");


            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (preparedStatement != null) {
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private ObservableList<Vol> getVols() {
        ObservableList<Vol> vols = FXCollections.observableArrayList();

        Connection con = Connexion.getConnexionn();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = con.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM `vol`");
            while (resultSet.next()) {
                int id = resultSet.getInt("idVol");
                String aeroportArriver = resultSet.getString("aeroportArrivee");
                String aeroportDepart = resultSet.getString("aerropDepart");
                String dateArriver = resultSet.getString("dateArrivee");
                String dateDepart = resultSet.getString("dateDepart");
                String heureArriver = resultSet.getString("heureArrivee");
                String heureDepart = resultSet.getString("heureDepart");
                String etat = resultSet.getString("etat");


                Vol vol = new Vol(id, aeroportArriver, aeroportDepart, dateArriver, dateDepart, heureArriver, heureDepart, etat);
                vols.add(vol);

            }


        } catch (Exception e) {
            e.printStackTrace();

        }

        return vols;
    }

    public void RetournerAuVol(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Vous allez être redirigé vers la page des vols");
        alert.showAndWait();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("compagnie.fxml"));
        Parent home = fxmlLoader.load();
        Scene homeScene = new Scene(home, 1000, 800);

        Stage currentStage = (Stage) RetournerAuVol.getScene().getWindow();
        currentStage.setTitle("Home");
        currentStage.setScene(homeScene);
        currentStage.show();

    }

    public void FermerVol(ActionEvent actionEvent) {
        if (TablVol.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un vol");
            alert.showAndWait();
            return;
        } else {
            if (TablVol.getSelectionModel().getSelectedItem().getEtat().equals("Fermé")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Ce vol est déjà fermé");
                alert.showAndWait();
                return;
            } else {
                // get the selected item (idVol
                int idVol = TablVol.getSelectionModel().getSelectedItem().getIdVol();

                Connection con = Connexion.getConnexionn();
                PreparedStatement preparedStatement = null;

                try {
                    String query = "UPDATE vol SET etat = ? WHERE idVol = ?";
                    preparedStatement = con.prepareStatement(query);
                    preparedStatement.setString(1, "Fermé");
                    preparedStatement.setInt(2, idVol);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Vol fermé avec succès");
                        alert.showAndWait();
                        TablVol.setItems(getVols());

                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        if (preparedStatement != null) {
                        }
                        if (con != null) {
                            con.close();
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }
    }

    @FXML
    public void OuvrirVolBtn(ActionEvent event) {
        if (TablVol.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un vol");
            alert.showAndWait();
            return;
        } else {
            if (TablVol.getSelectionModel().getSelectedItem().getEtat().equals("Ouvert")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Ce vol est déjà ouvert");
                alert.showAndWait();
                return;
            } else {
                int idVol = TablVol.getSelectionModel().getSelectedItem().getIdVol();

                Connection con = Connexion.getConnexionn();
                PreparedStatement preparedStatement = null;

                try {
                    String query = "UPDATE vol SET etat = ? WHERE idVol = ?";
                    preparedStatement = con.prepareStatement(query);
                    preparedStatement.setString(1, "Ouvert");
                    preparedStatement.setInt(2, idVol);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Vol ouvert avec succès");
                        alert.showAndWait();
                        TablVol.setItems(getVols());

                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        if (preparedStatement != null) {
                        }
                        if (con != null) {
                            con.close();
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }
    }

    @FXML
    public void ModifierVol(ActionEvent event) {
        if (TablVol.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un vol");
            alert.showAndWait();
            return;
        } else {
            if (TablVol.getSelectionModel().getSelectedItem().getEtat().equals("Fermé")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Ce vol est fermé");
                alert.showAndWait();
                return;
            } else {
                int idVol = TablVol.getSelectionModel().getSelectedItem().getIdVol();

                Connection con = Connexion.getConnexionn();
                PreparedStatement preparedStatement = null;


                try {
                    String query = "UPDATE vol SET aeroportArrivee = ?, aerropDepart = ?, heureArrivee = ?, heureDepart = ? WHERE idVol = ?";
                    preparedStatement = con.prepareStatement(query);
                    preparedStatement.setString(1, aeroportArriver.getSelectionModel().getSelectedItem().toString());
                    preparedStatement.setString(2, aeroportDepart.getSelectionModel().getSelectedItem().toString());
                    preparedStatement.setString(3, heureArriver.getText());
                    preparedStatement.setString(4, heureDepart.getText());
                    preparedStatement.setInt(5, idVol);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Vol modifié avec succès");
                        alert.showAndWait();
                        TablVol.setItems(getVols());

                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        if (preparedStatement != null) {
                        }
                        if (con != null) {
                            con.close();
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }


    }

    @FXML
    public void SupprmerVolBtn(ActionEvent event) {
        if (TablVol.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un vol");
            alert.showAndWait();
            return;
        } else {
            if (TablVol.getSelectionModel().getSelectedItem().getEtat().equals("Fermé")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Ce vol est fermé");
                alert.showAndWait();
                return;
            } else {
                int idVol = TablVol.getSelectionModel().getSelectedItem().getIdVol();

                Connection con = Connexion.getConnexionn();
                PreparedStatement preparedStatement = null;

                try {
                    String query = "DELETE FROM vol WHERE idVol = ?";
                    preparedStatement = con.prepareStatement(query);
                    preparedStatement.setInt(1, idVol);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Vol supprimé avec succès");
                        alert.showAndWait();
                        TablVol.setItems(getVols());

                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        if (preparedStatement != null) {
                        }
                        if (con != null) {
                            con.close();
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }
    }

    @FXML
    private TextField idNomAeroportt;

    @FXML
    private TextField idVilleAeroport;


    @FXML
    public void AjouterAeroport(ActionEvent event) {
        Connection con = Connexion.getConnexionn();
        PreparedStatement preparedStatement = null;

        try {
            String query = "INSERT INTO aeroport (nomAeroport, villeAeroport) VALUES (?, ?)";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, idNomAeroportt.getText());
            preparedStatement.setString(2, idVilleAeroport.getText());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Aéroport ajouté avec succès");
                alert.showAndWait();


            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (preparedStatement != null) {
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @FXML
    public void ListerLesEscales(ActionEvent event) {
        HAriiver.setCellValueFactory(cellData -> cellData.getValue().heurArriverProperty());
        HEscale.setCellValueFactory(cellData -> cellData.getValue().heureDepartProperty());
        AEscale.setCellValueFactory(cellData -> cellData.getValue().nomAeroportProperty());
        tabEscale.setItems(getEscale());

    }

    private ObservableList<escale> getEscale() {
        ObservableList<escale> escales = FXCollections.observableArrayList();
        if (TablVol.getSelectionModel().getSelectedItem() != null) {
            //recuperer l'id du vol selectionner (idVol
            int idVol = TablVol.getSelectionModel().getSelectedItem().getIdVol();
            System.out.println(idVol);

            Connection con = Connexion.getConnexionn();
            Statement statement = null;
            ResultSet resultSet = null;
            try {
                statement = con.createStatement();
                resultSet = statement.executeQuery("SELECT * FROM escale where idVol = " + idVol + "");
                while (resultSet.next()) {
                    int id = resultSet.getInt("idEscale");
                    String nomAeroport = resultSet.getString("nomAeroport");
                    String heureArriver = resultSet.getString("heureDepart");
                    String heureDepart = resultSet.getString("heureArrivee");
                    escale escale = new escale(nomAeroport, heureArriver, heureDepart, idVol);
                    escales.add(escale);


                }


            } catch (Exception e) {
                e.printStackTrace();

            }

            return escales;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur");
            alert.setContentText("Veuillez selectionner un vol");
            alert.showAndWait();


        }
        return null;

    }

    @FXML
    public void listerTouslesaeroport(ActionEvent event) {
        numeroAeroport.setCellValueFactory(cellData -> cellData.getValue().idAeroportProperty().asObject());
        villeaeroport.setCellValueFactory(cellData -> cellData.getValue().villeAeroportProperty());
        nomaeroport.setCellValueFactory(cellData -> cellData.getValue().nomAeroportProperty());
        tabAeroport.setItems(getAeroport());


    }

    private ObservableList<Aeroport> getAeroport() {
        ObservableList<Aeroport> aeroports = FXCollections.observableArrayList();
        Connection con = Connexion.getConnexionn();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = con.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM aeroport");
            while (resultSet.next()) {
                int id = resultSet.getInt("idAeroport");
                String nomAeroport = resultSet.getString("nomAeroport");
                String villeAeroport = resultSet.getString("villeAeroport");
                Aeroport aeroport = new Aeroport(id, nomAeroport, villeAeroport);
                aeroports.add(aeroport);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return aeroports;
    }
    @FXML
    public void RechercherAeroportByVille(ActionEvent event)
    {
        String ville = villeAer.getText();
        Connection con = Connexion.getConnexionn();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = con.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM aeroport where villeAeroport = '"+ville+"'");
            List<Aeroport> aeroports = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("idAeroport");
                String nomAeroport = resultSet.getString("nomAeroport");
                String villeAeroport = resultSet.getString("villeAeroport");
                Aeroport aeroport = new Aeroport(id, nomAeroport, villeAeroport);
                aeroports.add(aeroport);

            }
            tabAeroport.setItems(FXCollections.observableArrayList(aeroports));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }



    @FXML
    public void Logout(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent home = fxmlLoader.load();
        Scene homeScene = new Scene(home, 1000, 800);

        Stage currentStage = (Stage) btn.getScene().getWindow();
        currentStage.setTitle("Home");
        currentStage.setScene(homeScene);
        currentStage.show();
    }
}


