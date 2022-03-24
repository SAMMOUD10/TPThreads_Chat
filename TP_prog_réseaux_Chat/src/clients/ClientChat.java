package clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class ClientChat extends Application {
	
	PrintWriter pw;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage Firstpage) throws Exception {
		Firstpage.setTitle("Client Chat space");
		
		/* BorderPane container qui est divisé en 5 zones distinctes
		 * chacune des zones peut contenir un sous composant
		 */
		BorderPane borderPane = new BorderPane();
		
        /* Label et TextField sont des composants de contrôle
         * d'interface 
         */
		Label labelHost = new Label("Host:");
		labelHost.setPadding(new Insets(5,0,5,0));
		labelHost.setTextFill(Color.web("#00467F"));
		labelHost.setFont(Font.font("Verdana", FontWeight.BOLD, 11));
		
		//Zone de text pour ecrire l'adresse ip
		TextField textfieldHost = new TextField("Localhost");
		
		Label labelPort = new Label("Port:");
		labelPort.setPadding(new Insets(5,0,5,0));
		labelPort.setTextFill(Color.web("#00467F"));
		labelPort.setFont(Font.font("Verdana", FontWeight.BOLD, 11));
		
		//Zone de text pour ecrire le port 
		TextField textfieldPort = new TextField("1234");
		
		//Button Connecter pour se connecter au serveur
		Button btnConnecter = new Button("Connecter");
		
		/* Horizontal Box container qui arrange les sous composants
		 * sur une seule ligne
		 */
		HBox hbox = new HBox();
		hbox.setSpacing(10);
		hbox.setPadding(new Insets(10));
		hbox.setBackground(new Background(new BackgroundFill(Color.web("#BBD2C5"), null, null)));
		hbox.getChildren().addAll(labelHost,textfieldHost,labelPort,textfieldPort,btnConnecter);
		
		// ajouter le horizontal box en haut de la fenêtre
		borderPane.setTop(hbox);
		
		ObservableList<String> listModel = FXCollections.observableArrayList();
		ListView<String> listView = new ListView<String>(listModel);
		VBox vBox = new VBox();
		vBox.setSpacing(10);
		vBox.setPadding(new Insets(10));
		vBox.setBackground(new Background(new BackgroundFill(Color.web("#BBD2C5"), null, null)));
		vBox.getChildren().add(listView);
		borderPane.setCenter(vBox);
		
		//une composant du text pour indiquer l'invite de saisir d'un message
		Label labelMessage = new Label("Message:");
		labelMessage.setPadding(new Insets(5,0,5,0));
		labelMessage.setTextFill(Color.web("#00467F"));
		labelMessage.setFont(Font.font("Verdana", FontWeight.BOLD, 11));
		
		//Zone de text pour ecrire le message 
		TextField textfieldMessage = new TextField();
		textfieldMessage.setPrefWidth(250);
		
		//Button envoyer pour transmettre le message
		Button btnEnvoyer = new Button("Envoyer");
		
		// horizontal box pour regrouper les sous composants labelMessaeg et btnEnvoyer
		HBox hbox2 = new HBox();
		hbox2.setSpacing(10);
		hbox2.setPadding(new Insets(10));
		hbox2.setBackground(new Background(new BackgroundFill(Color.web("#BBD2C5"), null, null)));
		hbox2.getChildren().addAll(labelMessage,textfieldMessage,btnEnvoyer);
		
		//ajouter le dernière horizontal box en bas de la fenêtre
		borderPane.setBottom(hbox2);
		
		/* l'objet scene est le point d'entrée pour tous les contenus graphiques
		 * de l'application.
		 */
		Scene Myscene = new Scene(borderPane,500,400);
		Firstpage.setScene(Myscene);
		Firstpage.show();
		
		/*
		 * Les événements (Listeners)
		 */
		
		// décrire le comportement de la bouton Connecter
		btnConnecter.setOnAction((evt)->{
			String host = textfieldHost.getText();
			int port = Integer.parseInt(textfieldPort.getText());
			try {
				Socket socket = new Socket(host,port);
				InputStream is = socket.getInputStream();
				InputStreamReader isr = new InputStreamReader(is); 
				BufferedReader br = new BufferedReader(isr);
				OutputStream os = socket.getOutputStream();
				pw = new PrintWriter(os,true);
				
				new Thread(()->{
						while(true) {
								try {
									String reponse = br.readLine();
									Platform.runLater(()->{
										listModel.add(reponse);
									});
								} catch(IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						}
					
				}).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		// décrire le comportement de la bouton Envoyer
		btnEnvoyer.setOnAction((evt)->{
			String message = textfieldMessage.getText();
			pw.println(message);
		});
	}

}
