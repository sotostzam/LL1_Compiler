package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import static javafx.geometry.HPos.RIGHT;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class Graphics extends Application {

    private int textFieldCount = 0;
    private ArrayList<Label> rulesNumber = new ArrayList<>();
    private ArrayList<TextField> rulesTextField = new ArrayList<>();
    private ArrayList<Button> delButtons = new ArrayList<>();
    private GridPane rulesGrid = new GridPane();
    private GridPane homeInfoGrid = new GridPane();
    private Button btnAddRule = new Button(getLanguageString("btn_addRule"));
    private Button btnExportRules = new Button(getLanguageString("btn_exportRules"));
    private ArrayList<ImageView> imgViewDelete = new ArrayList<>();
    private Image imgDelete = new Image("images/btnDelete.png");
    private Image tick = new Image("images/tick.png");
    private LinkedHashMap<Character, String> firstSets;
    private LinkedHashMap<Character, String> followSets;  
    private Text actiontarget = new Text();
    private Button btnFollow = new Button(getLanguageString("btn_followSets"));
    private Button btnTable = new Button(getLanguageString("btn_parsingTable"));
    private Button btnAnalyze = new Button(getLanguageString("btn_analysis"));
    private Label firstSetsLB = new Label();
    private Label followSetsLB = new Label();
    private Label parsingTableLB = new Label(getLanguageString("lb_parsingTableNC"));
    private VBox parsingTableView = new VBox();
    private GridPane parsingTableGP = new GridPane();
    private Button showStepsBtn = new Button(getLanguageString("btn_showStepsInfo"));
    private ArrayList<Label> stepsLabels = new ArrayList<>();
    private ArrayList<ImageView> stepsImageView = new ArrayList<>();
    private Text stringAnalyzeReady = new Text(getLanguageString("txt_stringNotReadyInfo"));
    private String[][] parsingTable;
    private TextField stringInputTF = new TextField();
    private HBox stringSteps = new HBox();
    private VBox stringStepsRight = new VBox();
    private Label stringStepsTemporary = new Label(getLanguageString("lb_temporaryInfo"));
    private static VBox stringStepsLeft = new VBox();
    private static HBox stackString = new HBox();
    private static HBox stackStringRight = new HBox();
    private static HBox stackStringLeft = new HBox();
    private Label stringLeft2 = new Label(getLanguageString("lb_emptyStack"));
    private Label stringRight2 = new Label(getLanguageString("lb_emptyInput"));
    private static HBox stringInputCurrent = new HBox();
    private static HBox stringInputCurrentLeft = new HBox();
    private static HBox stringInputCurrentRight = new HBox();
    private Label stringInputCurrentLeftLB = new Label();
    private Label stringInputCurrentRightLB = new Label();
    private static ArrayList<Label> stackItems = new ArrayList<Label>();
    private ScrollPane stringStepsSP = new ScrollPane();
    private static int stepCounter = 0;
    private VBox mainStepsPanel = new VBox();
    private Button manualSteps = new Button(getLanguageString("btn_manualSteps"));
    private Button autoSteps = new Button(getLanguageString("btn_autoSteps"));
    private Button resultSteps = new Button(getLanguageString("btn_resultSteps"));
    public static ArrayList<Label> debugMessages = new ArrayList<>();
    private VBox mainInstructionsPanel = new VBox();
    private VBox mainChangelogPanel = new VBox();
    private Label firstSetsTimeMS = new Label("-- ms");
    private Label followSetsTimeMS = new Label("-- ms");
    private Label parsingTableTimeMS = new Label("-- ms");
    private Label totalTimeMS = new Label("-- ms");
    private static VBox consoleLog = new VBox();
    private ScrollPane consoleLogSP = new ScrollPane();
	private Alert stageWarningAlert = new Alert(AlertType.WARNING);
	private Alert stageInformationAlert = new Alert(AlertType.INFORMATION);
	
	private First first = new First();
	private Follow follow = new Follow();
	private Parser parser = new Parser();
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(getLanguageString("mainTitle"));
        primaryStage.getIcons().add(new Image("images/LL1_Logo.png"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        
        Stage stepsStage = new Stage();
        stepsStage.setTitle(getLanguageString("stepsTitle"));
        stepsStage.getIcons().add(new Image("images/LL1_Logo.png"));
        
        Stage instructionsStage = new Stage();
        instructionsStage.setTitle(getLanguageString("instructionsTitle"));
        instructionsStage.getIcons().add(new Image("images/LL1_Logo.png"));
        
        Stage changelogStage = new Stage();
        changelogStage.setTitle(getLanguageString("changelogTitle"));
        changelogStage.getIcons().add(new Image("images/LL1_Logo.png"));
        
        Stage consoleStage = new Stage();
        consoleStage.setTitle(getLanguageString("consoleTitle"));
        consoleStage.getIcons().add(new Image("images/LL1_Logo.png"));
        consoleStage.setResizable(false);
		   
		ImageView stageWarningAlertIV = new ImageView(new Image("images/alert.png"));
		stageWarningAlertIV.setFitHeight(40);
		stageWarningAlertIV.setPreserveRatio(true);
		stageWarningAlertIV.setSmooth(true);
		stageWarningAlertIV.setCache(true);
		stageWarningAlert.setHeaderText(null);
		stageWarningAlert.setGraphic(stageWarningAlertIV);
		
		ImageView stageInformationAlertIV = new ImageView(new Image("images/tick.png"));
		stageInformationAlertIV.setFitHeight(40);
		stageInformationAlertIV.setPreserveRatio(true);
		stageInformationAlertIV.setSmooth(true);
		stageInformationAlertIV.setCache(true);
		stageInformationAlert.setHeaderText(null);
		stageInformationAlert.setGraphic(stageInformationAlertIV);
		
        BorderPane mainPane = new BorderPane();
        HBox topView = new HBox();
        HBox footer = new HBox();
        VBox menu = new VBox();
        VBox menuTop = new VBox();
        VBox menuBottom = new VBox();
        VBox stackView = new VBox();
        VBox info = new VBox();

        mainPane.setTop(topView);
        mainPane.setLeft(menu);
        mainPane.setBottom(footer);
        mainPane.setRight(info);
        mainPane.setCenter(stackView);      

        topView.setAlignment(Pos.CENTER_LEFT);
        topView.setPadding(new Insets(10, 20, 10, 20));
        topView.setPrefHeight(100);
        
        menu.getChildren().add(menuTop);
        menu.getChildren().add(menuBottom);
        menu.prefWidthProperty().bind(primaryStage.widthProperty().divide(5.5));
        
        info.setSpacing(10);
        info.setAlignment(Pos.CENTER);
        info.setVisible(true);
        info.prefWidthProperty().bind(menu.widthProperty());
        info.setStyle("-fx-border-style: solid hidden hidden hidden;" + 
                      "-fx-border-width: 1;" +
                      "-fx-border-radius: 0;" + 
                      "-fx-border-color: lightgrey;");
        
        footer.setPadding(new Insets(15, 12, 15, 12));
        footer.setStyle("-fx-background-color : #cccccc;");
        footer.setAlignment(Pos.CENTER_LEFT);

        Text scenetitle = new Text(getLanguageString("mainTitle"));
        scenetitle.setFont(Font.font("Arial Rounded MT Bold", FontWeight.NORMAL, 30));
        topView.getChildren().add(scenetitle);
		
        // Main Menu Panel //
        HBox updatesPane = new HBox();
        updatesPane.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(updatesPane, Priority.ALWAYS);
        updatesPane.setMaxWidth(Double.MAX_VALUE);
        updatesPane.setMaxHeight(Double.MAX_VALUE);
        updatesPane.setSpacing(20);
        
        VBox updatesPaneLeft = new VBox();
        updatesPaneLeft.setAlignment(Pos.CENTER_LEFT);
        updatesPaneLeft.setSpacing(5);
        
        VBox updatesPaneRight= new VBox();
        updatesPaneRight.setAlignment(Pos.CENTER_RIGHT);
        updatesPaneRight.setSpacing(5);
        
        Label versionLB = new Label(getLanguageString("version"));
        versionLB.setFont(Font.font("Tahoma", FontWeight.BOLD, -1));
        Label revisionLB = new Label(getLanguageString("revision"));
        revisionLB.setFont(Font.font("Tahoma", FontWeight.BOLD, -1));
        
        Label versionValueLB = new Label(Compiler.version);
        Label revisionValueLB = new Label(Compiler.revDate);
        
        updatesPaneLeft.getChildren().addAll(versionLB, revisionLB);
        updatesPaneRight.getChildren().addAll(versionValueLB, revisionValueLB);      
        updatesPane.getChildren().addAll(updatesPaneLeft, updatesPaneRight);
        topView.getChildren().add(updatesPane);
        
        ArrayList<ImageView> menuButtonImage = new ArrayList<>();
        ImageView menuIcon = new ImageView(new Image("images/homepage.png"));
        menuButtonImage.add(menuIcon);
        ImageView grammarIcon = new ImageView(new Image("images/grammarIcon.png"));
        menuButtonImage.add(grammarIcon);
        ImageView firstSetsIcon = new ImageView(new Image("images/algorithmIcon.png"));
        menuButtonImage.add(firstSetsIcon);
        ImageView followSetsIcon = new ImageView(new Image("images/algorithmIcon.png"));
        menuButtonImage.add(followSetsIcon);
        ImageView tableIcon = new ImageView(new Image("images/tableIcon.png"));
        menuButtonImage.add(tableIcon);
        ImageView analysisIcon = new ImageView(new Image("images/analysisIcon.png"));
        menuButtonImage.add(analysisIcon);
        ImageView optionsIcon = new ImageView(new Image("images/options.png"));
        menuButtonImage.add(optionsIcon);
        ImageView aboutIcon = new ImageView(new Image("images/about.png"));
        menuButtonImage.add(aboutIcon);
        ImageView exitIcon = new ImageView(new Image("images/exit.png"));
        menuButtonImage.add(exitIcon);
        for (int i=0; i<menuButtonImage.size(); i++) {      	
        	menuButtonImage.get(i).setFitHeight(30);
        	menuButtonImage.get(i).setFitWidth(30);
        	menuButtonImage.get(i).setPreserveRatio(true);
        	menuButtonImage.get(i).setSmooth(true);
        	menuButtonImage.get(i).setCache(true);
        }
       
        Button btnHome = new Button(getLanguageString("btn_home"), menuButtonImage.get(0));
        HBox.setHgrow(btnHome, Priority.ALWAYS);
        btnHome.setMaxWidth(Double.MAX_VALUE);
        btnHome.setPrefHeight(50);
        btnHome.setAlignment(Pos.CENTER_LEFT);
        btnHome.setStyle("-fx-background-radius: 0;");
        btnHome.setGraphicTextGap(10);
        btnHome.setId("home");
        
        Button btnRules = new Button(getLanguageString("btn_rules"), menuButtonImage.get(1));
        HBox.setHgrow(btnRules, Priority.ALWAYS);
        btnRules.setMaxWidth(Double.MAX_VALUE);
        btnRules.setPrefHeight(50);
        btnRules.setAlignment(Pos.CENTER_LEFT);
        btnRules.setStyle("-fx-background-radius: 0;");
        btnRules.setGraphicTextGap(10);
        btnRules.setId("rules");
        
        Button btnFirst = new Button(getLanguageString("btn_firstSets"), menuButtonImage.get(2));
        btnFirst.setDisable(true);
        HBox.setHgrow(btnFirst, Priority.ALWAYS);
        btnFirst.setMaxWidth(Double.MAX_VALUE);
        btnFirst.setPrefHeight(50);
        btnFirst.setAlignment(Pos.CENTER_LEFT);
        btnFirst.setStyle("-fx-background-radius: 0;");
        btnFirst.setGraphicTextGap(10);
        btnFirst.setId("firstSets");
        
        btnFollow.setDisable(true);
        btnFollow.setGraphic(menuButtonImage.get(3));
        HBox.setHgrow(btnFollow, Priority.ALWAYS);
        btnFollow.setMaxWidth(Double.MAX_VALUE);
        btnFollow.setPrefHeight(50);
        btnFollow.setAlignment(Pos.CENTER_LEFT);
        btnFollow.setStyle("-fx-background-radius: 0;");
        btnFollow.setGraphicTextGap(10);
        btnFollow.setId("followSets");
        
        btnTable.setDisable(true);
        btnTable.setGraphic(menuButtonImage.get(4));
        HBox.setHgrow(btnTable, Priority.ALWAYS);
        btnTable.setMaxWidth(Double.MAX_VALUE);
        btnTable.setPrefHeight(50);
        btnTable.setAlignment(Pos.CENTER_LEFT);
        btnTable.setStyle("-fx-background-radius: 0;");
        btnTable.setGraphicTextGap(10);
        btnTable.setId("parsingTable");
        
        btnAnalyze.setDisable(true);
        btnAnalyze.setGraphic(menuButtonImage.get(5));
        HBox.setHgrow(btnAnalyze, Priority.ALWAYS);
        btnAnalyze.setMaxWidth(Double.MAX_VALUE);
        btnAnalyze.setPrefHeight(50);
        btnAnalyze.setAlignment(Pos.CENTER_LEFT);
        btnAnalyze.setStyle("-fx-background-radius: 0;");
        btnAnalyze.setGraphicTextGap(10);
        btnAnalyze.setId("analysis");
        
        menuTop.getChildren().addAll(btnHome, btnRules, btnFirst, btnFollow, btnTable, btnAnalyze);
        
        Button btnOptions = new Button(getLanguageString("btn_options"), menuButtonImage.get(6));
        HBox.setHgrow(btnOptions, Priority.ALWAYS);
        btnOptions.setMaxWidth(Double.MAX_VALUE);
        btnOptions.setPrefHeight(50);
        btnOptions.setAlignment(Pos.CENTER_LEFT);
        btnOptions.setStyle("-fx-background-radius: 0;");
        btnOptions.setGraphicTextGap(10);
        btnOptions.setId("options");
        
        Button btnAbout = new Button(getLanguageString("btn_about"), menuButtonImage.get(7));
        HBox.setHgrow(btnAbout, Priority.ALWAYS);
        btnAbout.setMaxWidth(Double.MAX_VALUE);
        btnAbout.setPrefHeight(50);
        btnAbout.setAlignment(Pos.CENTER_LEFT);
        btnAbout.setStyle("-fx-background-radius: 0;");
        btnAbout.setGraphicTextGap(10);
        btnAbout.setId("about");
        
        Button btnExit = new Button(getLanguageString("btn_exit"), menuButtonImage.get(8));
        HBox.setHgrow(btnExit, Priority.ALWAYS);
        btnExit.setMaxWidth(Double.MAX_VALUE);
        btnExit.setPrefHeight(50);
        btnExit.setAlignment(Pos.CENTER_LEFT);
        btnExit.setStyle("-fx-background-radius: 0;");
        btnExit.setGraphicTextGap(10);
        
        VBox.setVgrow(menuBottom, Priority.ALWAYS);
        menuBottom.setMaxHeight(Double.MAX_VALUE);
        menuBottom.setAlignment(Pos.BOTTOM_CENTER);
        menuBottom.getChildren().addAll(btnOptions, btnAbout, btnExit);
        
        // Info Panel //       
        VBox infoVBoxTop = new VBox();
        infoVBoxTop.prefHeightProperty().bind(info.heightProperty().subtract(info.heightProperty().divide(3)));
        infoVBoxTop.setAlignment(Pos.CENTER);
        
        VBox infoVBoxGrid = new VBox();
        infoVBoxGrid.getChildren().add(homeInfoGrid);
        infoVBoxGrid.setAlignment(Pos.CENTER);
        homeInfoGrid.setVgap(10);
        infoVBoxGrid.prefHeightProperty().bind(infoVBoxTop.heightProperty().subtract(infoVBoxTop.heightProperty().divide(4)));
        ArrayList<Label> infoLabels = new ArrayList<>();
        infoLabels.add(new Label(getLanguageString("lb_ruleSetInfo")));
        infoLabels.add(new Label(getLanguageString("lb_firstSetsInfo")));
        infoLabels.add(new Label(getLanguageString("lb_followSetsInfo")));
        infoLabels.add(new Label(getLanguageString("lb_parsingTableInfo")));      
        for (int i=0; i<4; i++) {
        	infoLabels.get(i).setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
            infoLabels.get(i).setPadding(new Insets(0, 0, 0, 15));
            homeInfoGrid.add(infoLabels.get(i), 0, i);
        }        
        for (int i=0; i<4; i++) {
            stepsLabels.add(new Label());
            stepsImageView.add(i, new ImageView(imgDelete));
            stepsImageView.get(i).setFitHeight(20);
            stepsImageView.get(i).setFitWidth(20);
            stepsImageView.get(i).setPreserveRatio(true);
            stepsImageView.get(i).setSmooth(true);
            stepsImageView.get(i).setCache(true);        
            stepsLabels.get(i).setGraphic(stepsImageView.get(i));
            stepsLabels.get(i).setPadding(new Insets(0, 15, 0, 0));
            homeInfoGrid.add(stepsLabels.get(i), 1, i);
        }
        
        for (int i=4;i<8;i++) {
        	stepsImageView.add(i, new ImageView(tick));
        	stepsImageView.get(i).setFitHeight(20);
        	stepsImageView.get(i).setFitWidth(20);
        	stepsImageView.get(i).setPreserveRatio(true);
        	stepsImageView.get(i).setSmooth(true);
        	stepsImageView.get(i).setCache(true);   
        }
        
        ColumnConstraints column1 = new ColumnConstraints(100,100,Double.MAX_VALUE);
        column1.setHgrow(Priority.ALWAYS);
        ColumnConstraints column2 = new ColumnConstraints(25);
        column2.setHalignment(RIGHT);
        homeInfoGrid.getColumnConstraints().addAll(column1, column2);       
        infoVBoxTop.getChildren().add(infoVBoxGrid);
        
        VBox infoVBoxString = new VBox();
        infoVBoxString.setAlignment(Pos.CENTER);
        infoVBoxString.prefHeightProperty().bind(infoVBoxTop.heightProperty().divide(4));        
        
        stringAnalyzeReady.setFill(Color.RED);
        stringAnalyzeReady.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        
        Label stringAnalyze = new Label(getLanguageString("lb_stringAnalyzeInfo"));
        stringAnalyze.setAlignment(Pos.CENTER);
        stringAnalyze.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        infoVBoxString.getChildren().addAll(stringAnalyze, stringAnalyzeReady);
        infoVBoxTop.getChildren().add(infoVBoxString);
        
        VBox infoVBoxBottom = new VBox();
        infoVBoxBottom.prefHeightProperty().bind(info.heightProperty().divide(3));
        infoVBoxBottom.setAlignment(Pos.CENTER);
        showStepsBtn.setDisable(true);
        Tooltip showStepsTooltip = new Tooltip(getLanguageString("tt_showSteps"));
        showStepsTooltip.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        showStepsTooltip.setStyle("-fx-text-alignment: center;");
        showStepsTooltip.setWrapText(true);
        showStepsTooltip.setMaxWidth(300);
        showStepsBtn.setTooltip(showStepsTooltip);
        infoVBoxBottom.getChildren().add(showStepsBtn);
           
        info.getChildren().addAll(infoVBoxTop, infoVBoxBottom);
    
        // Home Panel //       
        VBox homeVBox = new VBox();
        homeVBox.setPadding(new Insets(20, 20, 20, 20));
        homeVBox.setAlignment(Pos.CENTER);
        homeVBox.setSpacing(20);
        homeVBox.setStyle("-fx-border-style: solid solid hidden solid;" + 
                "-fx-border-width: 1;" +
                "-fx-border-radius: 0;" + 
                "-fx-border-color: lightgrey;");
        
        VBox homeInfoUpper = new VBox();
        homeInfoUpper.setPadding(new Insets(20, 20, 20, 20));
        homeInfoUpper.setSpacing(5);
        homeInfoUpper.prefHeightProperty().bind(stackView.heightProperty().subtract((stackView.heightProperty().divide(3))).subtract(40));
        homeInfoUpper.setStyle("-fx-border-style: solid solid solid solid;" + 
                "-fx-border-width: 1;" +
                "-fx-border-radius: 0;" + 
                "-fx-border-color: lightgrey;");
        
        Label overview = new Label(getLanguageString("lb_overview"));
        overview.setFont(Font.font("Tahoma", FontWeight.BOLD, 25));
        
        Label welcome = new Label(getLanguageString("lb_welcome"));
        welcome.setWrapText(true);
        welcome.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        
        HBox homeInfoPanels = new HBox();
        homeInfoPanels.setAlignment(Pos.BOTTOM_CENTER);
        homeInfoPanels.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(homeInfoPanels, Priority.ALWAYS);
        homeInfoPanels.setSpacing(50);
        homeInfoPanels.prefHeightProperty().bind(homeInfoUpper.heightProperty().divide(2).subtract(40));
        
        ImageView grammarInfoPanelIcon = new ImageView(new Image("images/grammarIcon.png"));
        grammarInfoPanelIcon.setFitHeight(100);
        grammarInfoPanelIcon.setPreserveRatio(true);
        grammarInfoPanelIcon.setSmooth(true);
        grammarInfoPanelIcon.setCache(true);
        
        Label grammarInfoPanel = new Label(getLanguageString("lb_grammarInfoPanel"));
        grammarInfoPanel.setAlignment(Pos.CENTER);
        grammarInfoPanel.setWrapText(true);
        grammarInfoPanel.setGraphic(grammarInfoPanelIcon);
        grammarInfoPanel.setContentDisplay(ContentDisplay.TOP);
        grammarInfoPanel.prefWidthProperty().bind(homeInfoUpper.widthProperty().divide(4));
        grammarInfoPanel.prefHeightProperty().bind(homeInfoUpper.heightProperty().divide(2));
        grammarInfoPanel.setStyle("-fx-border-style: solid solid solid solid;" + 
                "-fx-border-width: 1;" +
                "-fx-border-radius: 5;" + 
                "-fx-border-color: lightgrey;" +
                "-fx-text-alignment: center;");
        
        ImageView algorithmsInfoPanelIcon = new ImageView(new Image("images/algorithmIcon.png"));
        algorithmsInfoPanelIcon.setFitHeight(100);
        algorithmsInfoPanelIcon.setPreserveRatio(true);
        algorithmsInfoPanelIcon.setSmooth(true);
        algorithmsInfoPanelIcon.setCache(true);
        
        Label calculateInfoPanel = new Label(getLanguageString("lb_calculateInfoPanel"));
        calculateInfoPanel.setAlignment(Pos.CENTER);
        calculateInfoPanel.setWrapText(true);
        calculateInfoPanel.setGraphic(algorithmsInfoPanelIcon);
        calculateInfoPanel.setContentDisplay(ContentDisplay.TOP);
        calculateInfoPanel.prefWidthProperty().bind(homeInfoUpper.widthProperty().divide(4));
        calculateInfoPanel.prefHeightProperty().bind(homeInfoUpper.heightProperty().divide(2));
        calculateInfoPanel.setStyle("-fx-border-style: solid solid solid solid;" + 
                "-fx-border-width: 1;" +
                "-fx-border-radius: 5;" + 
                "-fx-border-color: lightgrey;" +
                "-fx-text-alignment: center;");
        
        ImageView analysisInfoPanelIcon = new ImageView(new Image("images/analysisIcon.png"));
        analysisInfoPanelIcon.setFitHeight(100);
        analysisInfoPanelIcon.setPreserveRatio(true);
        analysisInfoPanelIcon.setSmooth(true);
        analysisInfoPanelIcon.setCache(true);
        
        Label analysisInfoPanel = new Label(getLanguageString("lb_analysisInfoPanel"));
        analysisInfoPanel.setAlignment(Pos.CENTER);
        analysisInfoPanel.setWrapText(true);
        analysisInfoPanel.setGraphic(analysisInfoPanelIcon);
        analysisInfoPanel.setContentDisplay(ContentDisplay.TOP);
        analysisInfoPanel.prefWidthProperty().bind(homeInfoUpper.widthProperty().divide(4));
        analysisInfoPanel.prefHeightProperty().bind(homeInfoUpper.heightProperty().divide(2));
        analysisInfoPanel.setStyle("-fx-border-style: solid solid solid solid;" + 
                "-fx-border-width: 1;" +
                "-fx-border-radius: 5;" + 
                "-fx-border-color: lightgrey;" +
                "-fx-text-alignment: center;");
        
        homeInfoPanels.getChildren().addAll(grammarInfoPanel, calculateInfoPanel, analysisInfoPanel);
        homeInfoUpper.getChildren().addAll(overview, welcome, homeInfoPanels);

        VBox homeInstructionVBox = new VBox();
        homeInstructionVBox.setPadding(new Insets(20, 20, 20, 20));
        homeInstructionVBox.setSpacing(5);
        homeInstructionVBox.prefHeightProperty().bind(stackView.heightProperty().divide(3));
        homeInstructionVBox.setStyle("-fx-border-style: solid solid solid solid;" + 
                "-fx-border-width: 1;" +
                "-fx-border-radius: 0;" + 
                "-fx-border-color: lightgrey;");
        
        Label gettingStarted = new Label(getLanguageString("lb_gettingStarted"));
        gettingStarted.setFont(Font.font("Tahoma", FontWeight.BOLD, 25));
        
        Label parserInfo = new Label(getLanguageString("lb_parserInfo"));
        parserInfo.setWrapText(true);
        parserInfo.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        
        HBox gettingStartedBTHBox = new HBox();
        gettingStartedBTHBox.setAlignment(Pos.BOTTOM_RIGHT);
        gettingStartedBTHBox.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(gettingStartedBTHBox, Priority.ALWAYS);
        Button gettingStartedBT = new Button(getLanguageString("btn_gettingStarted"));
        gettingStartedBT.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        Tooltip gettingStartedTooltip = new Tooltip(getLanguageString("tt_gettingStarted"));
        gettingStartedTooltip.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        gettingStartedTooltip.setStyle("-fx-text-alignment: center;");
        gettingStartedTooltip.setWrapText(true);
        gettingStartedTooltip.setMaxWidth(300);
        gettingStartedBT.setTooltip(gettingStartedTooltip);
        
        gettingStartedBTHBox.getChildren().add(gettingStartedBT);
        homeInstructionVBox.getChildren().addAll(gettingStarted, parserInfo, gettingStartedBTHBox);     
        homeVBox.getChildren().addAll(homeInfoUpper, homeInstructionVBox);       
        stackView.getChildren().add(homeVBox);
        
        // Instructions Panel //
        VBox instructionsPane = new VBox();
        
        ScrollPane instructionsSP = new ScrollPane();  
        instructionsSP.setContent(mainInstructionsPanel);
        instructionsSP.setFitToWidth(true);
        instructionsSP.setHbarPolicy(ScrollBarPolicy.NEVER);
        instructionsSP.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);        
        VBox.setVgrow(instructionsSP, Priority.ALWAYS);
        
        instructionsSP.vvalueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
            	mainInstructionsPanel.setLayoutY(-new_val.doubleValue());
            }
        });
    	
        instructionsPane.getChildren().add(instructionsSP);
        mainInstructionsPanel.setAlignment(Pos.CENTER);
        
        Scene instructionsScene = new Scene(instructionsPane, 600, 600);
        instructionsStage.setScene(instructionsScene);
    
        // Import Rules Panel //
        HBox rulesPanel = new HBox();
        rulesPanel.setStyle("-fx-border-style: solid solid hidden solid;" + 
                "-fx-border-width: 1;" +
                "-fx-border-radius: 0;" + 
                "-fx-border-color: lightgrey;");
        
        ScrollPane rulesGridSP = new ScrollPane();
        rulesGridSP.setContent(rulesGrid);
        rulesGridSP.setFitToWidth(true);
        rulesGridSP.prefWidthProperty().bind(stackView.widthProperty().divide(2));
        rulesGridSP.prefHeightProperty().bind(stackView.heightProperty());
        rulesGridSP.setHbarPolicy(ScrollBarPolicy.NEVER);
        rulesGridSP.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        rulesGridSP.setFitToHeight(true);
        rulesGridSP.setStyle("-fx-focus-color: transparent;" +
        		             "-fx-background-color:transparent;");
        VBox.setVgrow(rulesGridSP, Priority.ALWAYS);
        HBox.setHgrow(rulesGridSP, Priority.ALWAYS);
        
        rulesGridSP.vvalueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
            	rulesGrid.setLayoutY(-new_val.doubleValue());
            }
        });
        
        rulesGrid.setHgap(10);
        rulesGrid.setVgap(10);
        rulesGrid.setPadding(new Insets(15, 20, 20, 15));
        rulesGrid.setAlignment(Pos.CENTER);
        HBox.setHgrow(rulesGrid, Priority.ALWAYS);
        
        addTextFieldRow(null);

        rulesPanel.getChildren().add(rulesGridSP);
        
        VBox ruleSelection = new VBox();
        ruleSelection.prefWidthProperty().bind(stackView.widthProperty().divide(2));
        ruleSelection.prefHeightProperty().bind(stackView.heightProperty());
        
        VBox modifyRules = new VBox();
        modifyRules.prefHeightProperty().bind(stackView.heightProperty().divide(2));
        modifyRules.setStyle("-fx-border-style: hidden hidden hidden solid;" + 
				"-fx-border-width: 1;" +
				"-fx-border-radius: 0;" + 
				"-fx-border-color: lightgrey;");
        
        VBox modifyRulesTop = new VBox();
        modifyRulesTop.setAlignment(Pos.CENTER);

        modifyRulesTop.setSpacing(10);
        modifyRulesTop.prefHeightProperty().bind(modifyRules.heightProperty().divide(2));
        
        VBox modifyRulesBottom = new VBox();
        modifyRulesBottom.setAlignment(Pos.CENTER);
        modifyRulesBottom.setSpacing(10);
        modifyRulesBottom.prefHeightProperty().bind(modifyRules.heightProperty().divide(2));
        modifyRulesBottom.setStyle("-fx-border-style: solid hidden hidden hidden;" + 
				"-fx-border-width: 1;" +
				"-fx-border-radius: 0;" + 
				"-fx-border-color: lightgrey;");
        
        HBox btnAddRuleHBox = new HBox();
        btnAddRuleHBox.getChildren().add(btnAddRule);
        btnAddRule.prefWidthProperty().bind(modifyRulesTop.widthProperty().subtract(modifyRulesTop.widthProperty().divide(2)));
        btnAddRule.setPrefHeight(40);
        btnAddRule.setStyle("-fx-background-image: url(images/add_Rule.png);"
        		          + "-fx-background-size: 30px 30px;"
        		          + "-fx-background-repeat: no-repeat;"
        		          + "-fx-background-position: 3% 50%;");
        Tooltip tooltipAddRule = new Tooltip(getLanguageString("tt_addRule"));
        tooltipAddRule.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        tooltipAddRule.setStyle("-fx-text-alignment: center;");
        tooltipAddRule.setWrapText(true);
        tooltipAddRule.setMaxWidth(300);
        btnAddRule.setTooltip(tooltipAddRule);

        Button btnSaveRules = new Button(getLanguageString("btn_saveRules"));
        btnSaveRules.setPrefHeight(40);
        btnSaveRules.prefWidthProperty().bind(modifyRulesTop.widthProperty().subtract(modifyRulesTop.widthProperty().divide(2)));
        btnSaveRules.setStyle("-fx-background-image: url(images/save.png);"
		          			+ "-fx-background-size: 35px 35px;"
		          			+ "-fx-background-repeat: no-repeat;"
		          			+ "-fx-background-position: 2% 50%;");
        Tooltip tooltipSave = new Tooltip(getLanguageString("tt_saveRules"));
        tooltipSave.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        tooltipSave.setStyle("-fx-text-alignment: center;");
        tooltipSave.setWrapText(true);
        tooltipSave.setMaxWidth(300);
        btnSaveRules.setTooltip(tooltipSave);
        
        Button btnImportRules = new Button(getLanguageString("btn_importRules"));
        btnImportRules.setId("btnImportRules");
        btnImportRules.setPrefHeight(40);
        btnImportRules.prefWidthProperty().bind(modifyRulesTop.widthProperty().subtract(modifyRulesBottom.widthProperty().divide(2)));
        btnImportRules.setStyle("-fx-background-image: url(images/import.png);"
		          			  + "-fx-background-size: 30px 30px;"
		          			  + "-fx-background-repeat: no-repeat;"
		          			  + "-fx-background-position: 3% 60%;");
        Tooltip tooltipImport = new Tooltip(getLanguageString("tt_importRules"));
        tooltipImport.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        tooltipImport.setStyle("-fx-text-alignment: center;");
        tooltipImport.setWrapText(true);
        tooltipImport.setMaxWidth(300);
        btnImportRules.setTooltip(tooltipImport);
        
        btnExportRules.setPrefHeight(40);
        btnExportRules.prefWidthProperty().bind(modifyRulesTop.widthProperty().subtract(modifyRulesBottom.widthProperty().divide(2)));
        btnExportRules.setDisable(true);
        btnExportRules.setStyle("-fx-background-image: url(images/export.png);"
		          			  + "-fx-background-size: 30px 30px;"
		          			  + "-fx-background-repeat: no-repeat;"
		          			  + "-fx-background-position: 3% 60%;");
        Tooltip tooltipExport = new Tooltip(getLanguageString("tt_exportRules"));
        tooltipExport.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        tooltipExport.setStyle("-fx-text-alignment: center;");
        tooltipExport.setWrapText(true);
        tooltipExport.setMaxWidth(300);
        btnExportRules.setTooltip(tooltipExport);
        
        VBox preLoaded = new VBox();
        preLoaded.setAlignment(Pos.CENTER);
        preLoaded.setPadding(new Insets(15, 12, 15, 12));
        preLoaded.setSpacing(10);
        preLoaded.prefHeightProperty().bind(stackView.heightProperty().divide(2));
        preLoaded.setStyle("-fx-border-style: solid hidden hidden solid;" + 
						   "-fx-border-width: 1;" +
						   "-fx-border-radius: 0;" + 
						   "-fx-border-color: lightgrey;");
        
        Label preLoadedGrammars = new Label(getLanguageString("lb_preLoadedGrammars"));
        preLoadedGrammars.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        
        InputStream streamIn = Compiler.class.getResourceAsStream("/grammars/grammars.info");
		Scanner grammarScanner = new Scanner(streamIn);
		grammarScanner.useDelimiter("\\A");
		String[] grammarsList = grammarScanner.next().split("\\r?\\n");
		grammarScanner.close();
        
        ListView<String> list = new ListView<String>();
        ObservableList<String> items = FXCollections.observableArrayList();
        
        for (int i =0; i< grammarsList.length; i++) {
        	items.add(grammarsList[i]);
        }
        list.setItems(items);
        list.prefHeightProperty().bind(preLoaded.heightProperty().subtract(preLoaded.heightProperty().divide(3)));
        
        Button btnSelectRuleSet = new Button(getLanguageString("btn_selectRuleSet"));
        btnSelectRuleSet.setId("btnSelectSet");
        HBox.setHgrow(btnSelectRuleSet, Priority.ALWAYS);
        btnSelectRuleSet.prefWidthProperty().bind(preLoaded.widthProperty().subtract(preLoaded.widthProperty().divide(2)));
        btnSelectRuleSet.setPrefHeight(40);
        btnSelectRuleSet.setDisable(true);
        Tooltip tooltipSelectRuleSet = new Tooltip(getLanguageString("tt_selectRuleSet"));
        tooltipSelectRuleSet.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        tooltipSelectRuleSet.setStyle("-fx-text-alignment: center;");
        tooltipSelectRuleSet.setWrapText(true);
        tooltipSelectRuleSet.setMaxWidth(300);
        btnSelectRuleSet.setTooltip(tooltipSelectRuleSet);
        
        modifyRulesTop.getChildren().addAll(btnAddRule, btnSaveRules);
        modifyRulesBottom.getChildren().addAll(btnImportRules, btnExportRules);
        modifyRules.getChildren().addAll(modifyRulesTop, modifyRulesBottom);
        preLoaded.getChildren().addAll(preLoadedGrammars, list, btnSelectRuleSet);
        ruleSelection.getChildren().addAll(modifyRules, preLoaded);
        rulesPanel.getChildren().add(ruleSelection);
        
        // First Follow Table Layout //
        VBox mainStack = new VBox();
        mainStack.prefHeightProperty().bind(stackView.heightProperty());
        mainStack.setStyle("-fx-border-style: solid solid hidden solid;" + 
                "-fx-border-width: 1;" +
                "-fx-border-radius: 0;" + 
                "-fx-border-color: lightgrey;");
        
        HBox mainStackInner = new HBox();
        mainStackInner.setAlignment(Pos.CENTER);
        mainStackInner.prefHeightProperty().bind(mainStack.heightProperty().divide(2));
        mainStack.getChildren().add(mainStackInner);
        
        VBox firstSetsView = new VBox();
        firstSetsView.setAlignment(Pos.CENTER);
        firstSetsView.prefWidthProperty().bind(mainStackInner.widthProperty().divide(2));
        firstSetsView.setStyle("-fx-border-style: hidden solid hidden hidden;" + 
                "-fx-border-width: 1;" +
                "-fx-border-radius: 0;" + 
                "-fx-border-color: lightgrey;");
        
        firstSetsLB.setVisible(true);
        HBox.setHgrow(firstSetsLB, Priority.ALWAYS);
        firstSetsLB.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(firstSetsLB, Priority.ALWAYS);
        firstSetsLB.setMaxHeight(Double.MAX_VALUE);
        firstSetsLB.setAlignment(Pos.CENTER);
        firstSetsLB.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        firstSetsView.getChildren().add(firstSetsLB);
        mainStackInner.getChildren().add(firstSetsView);
        
        VBox followSetsView = new VBox();
        followSetsView.setAlignment(Pos.CENTER);
        followSetsView.prefWidthProperty().bind(mainStackInner.widthProperty().divide(2));
        
        followSetsLB.setVisible(true);
        HBox.setHgrow(followSetsLB, Priority.ALWAYS);
        followSetsLB.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(followSetsLB, Priority.ALWAYS);
        followSetsLB.setMaxHeight(Double.MAX_VALUE);
        followSetsLB.setAlignment(Pos.CENTER);
        followSetsLB.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        followSetsView.getChildren().add(followSetsLB);
        mainStackInner.getChildren().add(followSetsView);
        
        parsingTableView.prefHeightProperty().bind(mainStack.heightProperty().divide(2));
        parsingTableView.setAlignment(Pos.CENTER);
        
        parsingTableView.getChildren().add(parsingTableLB);
        parsingTableLB.setVisible(true);
        HBox.setHgrow(parsingTableLB, Priority.ALWAYS);
        parsingTableLB.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(parsingTableLB, Priority.ALWAYS);
        parsingTableLB.setMaxHeight(Double.MAX_VALUE);
        parsingTableLB.setAlignment(Pos.CENTER);
        parsingTableLB.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        parsingTableLB.setStyle("-fx-background-color: lightgray;");
        
        mainStack.getChildren().add(parsingTableView);
        
        // Steps Window //
        VBox stepsPane = new VBox();
        
        final ScrollPane sp = new ScrollPane();  
        sp.setContent(mainStepsPanel);
        sp.setFitToWidth(true);
        sp.setHbarPolicy(ScrollBarPolicy.NEVER);
        sp.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);        
        VBox.setVgrow(sp, Priority.ALWAYS);
        
        sp.vvalueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
            	mainStepsPanel.setLayoutY(-new_val.doubleValue());
            }
        });
        
        stepsPane.getChildren().add(sp);
        mainStepsPanel.setAlignment(Pos.CENTER);
        
        Scene stepsScene = new Scene(stepsPane, 400, 720);
        stepsStage.setScene(stepsScene);      
        
        // String Analysis Panel //      
        VBox stringAnalysisVBox = new VBox();
        stringAnalysisVBox.prefHeightProperty().bind(stackView.heightProperty());
        
        HBox stringInputVBox = new HBox();
        stringInputVBox.setAlignment(Pos.CENTER);
        stringInputVBox.prefHeightProperty().bind(stringAnalysisVBox.heightProperty().divide(6));
        stringInputVBox.setStyle("-fx-border-style: solid solid solid solid;" + 
								 "-fx-border-width: 1;" +
								 "-fx-border-radius: 0;" + 
								 "-fx-border-color: lightgrey;");
        
        Label stringInputLB = new Label(getLanguageString("lb_stringInput"));
        HBox.setHgrow(stringInputLB, Priority.ALWAYS);
        stringInputLB.setMaxWidth(Double.MAX_VALUE);
        stringInputLB.setAlignment(Pos.CENTER);
        stringInputLB.prefWidthProperty().bind(stringInputVBox.widthProperty().divide(3));
        stringInputLB.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        
        stringInputTF.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        stringInputTF.prefWidthProperty().bind(stringInputVBox.widthProperty().divide(3));
        
        VBox stringInputBTVBox = new VBox();
        stringInputBTVBox.setAlignment(Pos.CENTER);
        stringInputBTVBox.prefWidthProperty().bind(stringInputVBox.widthProperty().divide(3));
        stringInputBTVBox.setPadding(new Insets(0, 20, 0, 20));
        
        Button stringInputBT = new Button(getLanguageString("btn_stringInput"));
        HBox.setHgrow(stringInputBT, Priority.ALWAYS);
        stringInputBT.setMaxWidth(Double.MAX_VALUE);
        stringInputBT.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        Tooltip stringInputTooltip = new Tooltip(getLanguageString("tt_stringInput"));
        stringInputTooltip.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        stringInputTooltip.setStyle("-fx-text-alignment: center;");
        stringInputBT.setTooltip(stringInputTooltip);
        stringInputBT.setDisable(true);
        
        stringInputBTVBox.getChildren().add(stringInputBT);
        stringInputVBox.getChildren().addAll(stringInputLB, stringInputTF, stringInputBTVBox);
        
        VBox stringAnalysisVBoxCenter = new VBox();
        stringAnalysisVBoxCenter.prefHeightProperty().bind((stringAnalysisVBox.prefHeightProperty().divide(2)).subtract(stringAnalysisVBox.prefHeightProperty().divide(6)));
        stringAnalysisVBoxCenter.setStyle("-fx-border-style: hidden solid hidden solid;" + 
										  "-fx-border-width: 1;" +
										  "-fx-border-radius: 0;" + 
					 					  "-fx-border-color: lightgrey;");
        
        Label stringAnalysisInfo = new Label(getLanguageString("lb_stringAnalysisInfo"));
        stringAnalysisInfo.setAlignment(Pos.CENTER);
        HBox.setHgrow(stringAnalysisInfo, Priority.ALWAYS);
        stringAnalysisInfo.setMaxWidth(Double.MAX_VALUE);
        stringAnalysisInfo.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));
        stringAnalysisInfo.prefHeightProperty().bind(stringAnalysisVBoxCenter.heightProperty().divide(3));
        
        HBox stringInput1 = new HBox();
        stringInput1.setAlignment(Pos.CENTER);
        
        Label stringLeft = new Label(getLanguageString("lb_stack"));
        stringLeft.setFont(Font.font("Tahoma", FontWeight.NORMAL, 25));
        stringLeft.setAlignment(Pos.CENTER);
        stringLeft.prefHeightProperty().bind(stringAnalysisVBoxCenter.heightProperty().divide(4));
        stringLeft.prefWidthProperty().bind(stringAnalysisVBoxCenter.widthProperty().divide(2));
        HBox.setHgrow(stringLeft, Priority.ALWAYS);
        stringLeft.setMaxWidth(Double.MAX_VALUE);
        
        Label stringRight = new Label(getLanguageString("lb_input"));
        stringRight.setFont(Font.font("Tahoma", FontWeight.NORMAL, 25));
        stringRight.setAlignment(Pos.CENTER);
        stringRight.prefHeightProperty().bind(stringAnalysisVBoxCenter.heightProperty().divide(4));
        stringRight.prefWidthProperty().bind(stringAnalysisVBoxCenter.widthProperty().divide(2));
        HBox.setHgrow(stringRight, Priority.ALWAYS);
        stringRight.setMaxWidth(Double.MAX_VALUE); 
        
        stringInput1.getChildren().addAll(stringLeft, stringRight);
        
        VBox stackInputCurrent = new VBox();
        stackInputCurrent.setAlignment(Pos.CENTER);
        stackInputCurrent.prefHeightProperty().bind(stringAnalysisVBoxCenter.heightProperty().divide(2));
        
        stackString.setAlignment(Pos.CENTER);
        
        stackStringLeft.prefWidthProperty().bind(stringAnalysisVBoxCenter.widthProperty().divide(2));
        HBox.setHgrow(stackStringLeft, Priority.ALWAYS);
        stackStringLeft.setMaxWidth(Double.MAX_VALUE); 
        stackStringLeft.setAlignment(Pos.CENTER);
        
        stackStringRight.prefWidthProperty().bind(stringAnalysisVBoxCenter.widthProperty().divide(2));
        HBox.setHgrow(stackStringRight, Priority.ALWAYS);
        stackStringRight.setMaxWidth(Double.MAX_VALUE); 
        stackStringRight.setAlignment(Pos.CENTER);
        
        stringLeft2.setAlignment(Pos.CENTER);
        HBox.setHgrow(stringLeft2, Priority.ALWAYS);
        stringLeft2.setMaxWidth(Double.MAX_VALUE);
        stringLeft2.setFont(Font.font("Tahoma", FontWeight.THIN, 20));
        stackStringLeft.getChildren().add(stringLeft2);
        
        stringRight2.setAlignment(Pos.CENTER);
        HBox.setHgrow(stringRight2, Priority.ALWAYS);
        stringRight2.setMaxWidth(Double.MAX_VALUE);
        stringRight2.setFont(Font.font("Tahoma", FontWeight.THIN, 20));
        stackStringRight.getChildren().add(stringRight2);
        
        stackString.getChildren().addAll(stackStringLeft, stackStringRight);
        
        stringInputCurrentLeftLB.setAlignment(Pos.CENTER);
        HBox.setHgrow(stringInputCurrentLeftLB, Priority.ALWAYS);
        stringInputCurrentLeftLB.setMaxWidth(Double.MAX_VALUE);
        stringInputCurrentLeft.getChildren().add(stringInputCurrentLeftLB);
        
        stringInputCurrentRightLB.setAlignment(Pos.CENTER);
        HBox.setHgrow(stringInputCurrentRightLB, Priority.ALWAYS);
        stringInputCurrentRightLB.setMaxWidth(Double.MAX_VALUE);
        stringInputCurrentRight.getChildren().add(stringInputCurrentRightLB);
        
        stringInputCurrent.getChildren().addAll(stringInputCurrentLeft, stringInputCurrentRight);      
        stackInputCurrent.getChildren().addAll(stackString, stringInputCurrent);
        
        stringInputCurrent.setAlignment(Pos.CENTER);
        stringInputCurrentLeft.prefWidthProperty().bind(stackString.widthProperty().divide(2));
        stringInputCurrentLeft.setAlignment(Pos.CENTER);
        stringInputCurrentRight.prefWidthProperty().bind(stackString.widthProperty().divide(2));
        stringInputCurrentRight.setAlignment(Pos.CENTER);
        
        stringAnalysisVBoxCenter.getChildren().addAll(stringAnalysisInfo, stringInput1, stackInputCurrent);
        
        stringSteps.prefHeightProperty().bind(stringAnalysisVBox.heightProperty().divide(2));
        stringSteps.prefWidthProperty().bind(stackView.widthProperty());
        stringSteps.setStyle("-fx-border-style: solid hidden hidden solid;" + 
 				 "-fx-border-width: 1;" +
 				 "-fx-border-radius: 0;" + 
  				 "-fx-border-color: lightgrey;");
        
        stringStepsTemporary.prefWidthProperty().bind(stackView.widthProperty().subtract(stackView.widthProperty().divide(3)));
        stringStepsTemporary.setAlignment(Pos.CENTER);
        HBox.setHgrow(stringStepsTemporary, Priority.ALWAYS);
        stringStepsTemporary.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(stringStepsTemporary, Priority.ALWAYS);
        stringStepsTemporary.setMaxHeight(Double.MAX_VALUE);
        stringStepsTemporary.setFont(Font.font("Tahoma", FontWeight.THIN, 20));
        
        stringStepsSP.setContent(stringStepsLeft);
        stringStepsSP.setFitToWidth(true);
        stringStepsSP.prefWidthProperty().bind(stackView.widthProperty().subtract(stackView.widthProperty().divide(3)));
        stringStepsSP.setHbarPolicy(ScrollBarPolicy.NEVER);
        stringStepsSP.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        stringStepsSP.setStyle("-fx-focus-color: transparent;" +
	             			   "-fx-background-color:transparent;");
        HBox.setHgrow(stringStepsSP, Priority.ALWAYS);
        
        stringStepsSP.vvalueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
            	stringStepsLeft.setLayoutY(-new_val.doubleValue());
            }
        });
        
        stringStepsLeft.setAlignment(Pos.CENTER);
        
        stringStepsRight.prefWidthProperty().bind(stringSteps.widthProperty().divide(3));
        stringStepsRight.setStyle("-fx-border-style: hidden solid hidden solid;" + 
				  				  "-fx-border-width: 1;" +
				  				  "-fx-border-radius: 0;" + 
	 			  				  "-fx-border-color: lightgrey;");
        
        VBox stepByStepVBox = new VBox();
        stepByStepVBox.setAlignment(Pos.CENTER);
        stepByStepVBox.prefHeightProperty().bind(stringStepsRight.heightProperty().divide(3));
        stepByStepVBox.setStyle("-fx-border-style: hidden hidden solid hidden;" + 
				  				"-fx-border-width: 1;" +
				  				"-fx-border-radius: 0;" + 
 				  				"-fx-border-color: lightgrey;");
        Label stepByStepLB = new Label(getLanguageString("lb_stepByStep"));
        stepByStepLB.setFont(Font.font("Tahoma", FontWeight.BOLD, -1));
        stepByStepLB.setPadding(new Insets(5, 0, 0, 0));
        stepByStepLB.setAlignment(Pos.CENTER);
        HBox stepByStepBtnHBox = new HBox();
        stepByStepBtnHBox.setMaxHeight(Double.MAX_VALUE);
        stepByStepBtnHBox.setAlignment(Pos.CENTER);
        VBox.setVgrow(stepByStepBtnHBox, Priority.ALWAYS);

        manualSteps.prefWidthProperty().bind(stringStepsRight.widthProperty().subtract(stringStepsRight.widthProperty().divide(2)));
        manualSteps.setPrefHeight(30);
        manualSteps.setDisable(true);
        Tooltip manualStepsTooltip = new Tooltip(getLanguageString("tt_manualSteps"));
        manualStepsTooltip.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        manualStepsTooltip.setStyle("-fx-text-alignment: center;");
        manualSteps.setTooltip(manualStepsTooltip);
        stepByStepBtnHBox.getChildren().add(manualSteps);
        stepByStepVBox.getChildren().addAll(stepByStepLB, stepByStepBtnHBox);
        
        VBox autoStepsVBox = new VBox();
        autoStepsVBox.setAlignment(Pos.CENTER);
        autoStepsVBox.prefHeightProperty().bind(stringStepsRight.heightProperty().divide(3));
        Label autoStepsInfo = new Label(getLanguageString("lb_autoSteps"));  
        autoStepsInfo.setFont(Font.font("Tahoma", FontWeight.BOLD, -1));
        autoStepsInfo.setPadding(new Insets(5, 0, 0, 0));
        autoStepsInfo.setAlignment(Pos.CENTER);
        HBox autoStepsTimerVBox = new HBox();
        VBox.setVgrow(autoStepsTimerVBox, Priority.ALWAYS);
        autoStepsTimerVBox.setAlignment(Pos.CENTER);
        TextField autoStepsTimer = new TextField("0.5");
        autoStepsTimer.setPrefWidth(40);
        autoStepsTimer.setAlignment(Pos.CENTER);
        Label autoStepsSecs = new Label(getLanguageString("lb_autoStepsSecs"));
        autoStepsSecs.setPadding(new Insets(0, 10, 0, 5));

        autoSteps.setId("autoSteps");
        autoSteps.setDisable(true);
        autoSteps.prefWidthProperty().bind(stringStepsRight.widthProperty().divide(3));
        autoSteps.setPrefHeight(30);
        Tooltip autoStepsTooltip = new Tooltip(getLanguageString("tt_autoSteps"));
        autoStepsTooltip.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        autoStepsTooltip.setStyle("-fx-text-alignment: center;");
        autoSteps.setTooltip(autoStepsTooltip);
        autoStepsTimerVBox.getChildren().addAll(autoStepsTimer, autoStepsSecs, autoSteps);
        autoStepsVBox.getChildren().addAll(autoStepsInfo, autoStepsTimerVBox);
        
        VBox resultOnceVBox = new VBox();
        resultOnceVBox.setAlignment(Pos.CENTER);
        resultOnceVBox.prefHeightProperty().bind(stringStepsRight.heightProperty().divide(3));
        resultOnceVBox.setStyle("-fx-border-style: solid hidden hidden hidden;" + 
				  				"-fx-border-width: 1;" +
				  				"-fx-border-radius: 0;" + 
 				  				"-fx-border-color: lightgrey;");
        Label showResultLB = new Label(getLanguageString("lb_showResult"));
        showResultLB.setFont(Font.font("Tahoma", FontWeight.BOLD, -1));
        showResultLB.setPadding(new Insets(5, 0, 0, 0));
        showResultLB.setAlignment(Pos.CENTER);
        HBox resultStepsBtnHBox = new HBox();
        resultStepsBtnHBox.setMaxHeight(Double.MAX_VALUE);
        resultStepsBtnHBox.setAlignment(Pos.CENTER);
        VBox.setVgrow(resultStepsBtnHBox, Priority.ALWAYS);

        resultSteps.prefWidthProperty().bind(stringStepsRight.widthProperty().subtract(stringStepsRight.widthProperty().divide(2)));
        resultSteps.setPrefHeight(30);
        resultSteps.setId("resultSteps");
        resultSteps.setDisable(true);
        Tooltip resultStepsTooltip = new Tooltip(getLanguageString("tt_resultSteps"));
        resultStepsTooltip.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        resultStepsTooltip.setStyle("-fx-text-alignment: center;");
        resultSteps.setTooltip(resultStepsTooltip);
        resultStepsBtnHBox.getChildren().add(resultSteps);
        resultOnceVBox.getChildren().addAll(showResultLB, resultStepsBtnHBox);
        
        stringStepsRight.getChildren().addAll(stepByStepVBox, autoStepsVBox, resultOnceVBox);  
        stringSteps.getChildren().addAll(stringStepsTemporary, stringStepsRight);   
        stringAnalysisVBox.getChildren().addAll(stringInputVBox, stringAnalysisVBoxCenter, stringSteps);
                         
        // Options Panel //
        VBox optionsPanel = new VBox();
        optionsPanel.setPadding(new Insets(20, 20, 20, 20));
        VBox.setVgrow(optionsPanel, Priority.ALWAYS);
        optionsPanel.setStyle("-fx-border-style: solid solid hidden solid;" + 
                "-fx-border-width: 1;" +
                "-fx-border-radius: 0;" + 
                "-fx-border-color: lightgrey;");
        
        VBox optionsPanelInner = new VBox();
        optionsPanelInner.setPadding(new Insets(20, 20, 20, 20));
        optionsPanelInner.setSpacing(20);
        optionsPanelInner.setStyle("-fx-border-style: solid solid solid solid;" + 
                "-fx-border-width: 1;" +
                "-fx-border-radius: 0;" + 
                "-fx-border-color: lightgrey;");
        
        Label optionsLabel = new Label(getLanguageString("btn_options"));
        optionsLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 25));
        
        Label consoleLabel = new Label(getLanguageString("lb_console"));
        consoleLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        consoleLabel.prefWidthProperty().bind(optionsPanelInner.widthProperty().divide(3).subtract(20)); 
        consoleLabel.setAlignment(Pos.CENTER_LEFT);
        
        CheckBox consoleCB = new CheckBox();
        consoleCB.setSelected(false);
        Tooltip consoleCBTooltip = new Tooltip(getLanguageString("tt_consoleCB"));
        consoleCBTooltip.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        consoleCBTooltip.setStyle("-fx-text-alignment: center;");
        consoleCBTooltip.setWrapText(true);
        consoleCBTooltip.setMaxWidth(350);
        consoleCB.setTooltip(consoleCBTooltip);
        
        HBox consoleCBHBox = new HBox();
        consoleCBHBox.prefWidthProperty().bind(optionsPanelInner.widthProperty().divide(3).subtract(20));
        consoleCBHBox.setPadding(new Insets(0, 30, 0, 22));
        consoleCBHBox.getChildren().add(consoleCB);
        
        HBox consoleHBox = new HBox();
        consoleHBox.setSpacing(20);
        consoleHBox.getChildren().addAll(consoleLabel, consoleCBHBox);
        
        HBox languageHBox = new HBox();
        languageHBox.setSpacing(20);
        
        Label languageLabel = new Label(getLanguageString("lb_language"));
        languageLabel.prefWidthProperty().bind(optionsPanelInner.widthProperty().divide(3).subtract(20)); 
        languageLabel.setAlignment(Pos.CENTER_LEFT);
        languageLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        
        streamIn = Compiler.class.getResourceAsStream("/bundles/languages.info");
		Scanner languageScanner = new Scanner(streamIn);
		languageScanner.useDelimiter("\\A");
		String[] languageList = languageScanner.next().split("\\r?\\n");
		languageScanner.close();
		
        ObservableList<String> languageFiles = FXCollections.observableArrayList();

        for (int i =0;i< languageList.length; i++) {
        	languageFiles.add(languageList[i]);
        }
        
        ChoiceBox<String> languageSelector = new ChoiceBox<String>();
        languageSelector.setStyle("-fx-background-radius: 0;");
        languageSelector.setItems(languageFiles);
        languageSelector.setValue(Compiler.language);
        HBox.setHgrow(languageSelector, Priority.ALWAYS);
        languageSelector.setMaxWidth(Double.MAX_VALUE);
        languageSelector.setDisable(false);
               
        HBox languageSelectorHBox = new HBox();
        languageSelectorHBox.prefWidthProperty().bind(optionsPanelInner.widthProperty().divide(3).subtract(20));
        languageSelectorHBox.setPadding(new Insets(0, 30, 0, 30));
        languageSelectorHBox.getChildren().add(languageSelector);
        
        Button saveLanguage = new Button(getLanguageString("btn_saveLanguage"));
        saveLanguage.prefWidthProperty().bind(optionsPanelInner.widthProperty().divide(3).subtract(20));
        saveLanguage.setStyle("-fx-background-radius: 0;");
        Tooltip saveLanguageTooltip = new Tooltip(getLanguageString("tt_saveLanguage"));
        saveLanguageTooltip.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        saveLanguageTooltip.setStyle("-fx-text-alignment: center;");
        saveLanguageTooltip.setWrapText(true);
        saveLanguageTooltip.setMaxWidth(300);
        saveLanguage.setTooltip(saveLanguageTooltip);
        HBox.setHgrow(saveLanguage, Priority.ALWAYS);
        saveLanguage.setMaxWidth(Double.MAX_VALUE);
        saveLanguage.setDisable(true);
        
        HBox saveLanguageHBox = new HBox();
        saveLanguageHBox.prefWidthProperty().bind(optionsPanelInner.widthProperty().divide(3).subtract(20));
        saveLanguageHBox.setPadding(new Insets(0, 30, 0, 30));
        saveLanguageHBox.getChildren().add(saveLanguage);
        
        languageHBox.getChildren().addAll(languageLabel, languageSelectorHBox, saveLanguageHBox);
        optionsPanelInner.getChildren().addAll(optionsLabel, consoleHBox, languageHBox);
        optionsPanel.getChildren().add(optionsPanelInner);
        
        // Console Window //
        VBox consolePane = new VBox();
        consolePane.setPadding(new Insets(10, 10, 10, 10));
        consolePane.setSpacing(10);
        consolePane.setStyle("-fx-border-style: solid solid solid solid;" +
 				  			 "-fx-border-width: 1;" +
 				  			 "-fx-border-radius: 2;" +
	   				  		 "-fx-border-color: lightgrey;");
        
        VBox consolePaneUpper = new VBox();
        consolePaneUpper.setPadding(new Insets(10, 10, 10, 10));
        consolePaneUpper.setStyle("-fx-border-style: solid solid solid solid;" +
				   				  "-fx-border-width: 1;" +
				   				  "-fx-border-radius: 2;" +
	   			   				  "-fx-border-color: lightgrey;");
        
        VBox consolePaneLower = new VBox();
        consolePaneLower.setPadding(new Insets(10, 10, 10, 10));
        consolePaneLower.setStyle("-fx-border-style: solid solid solid solid;" +
				   				  "-fx-border-width: 1;" +
				   				  "-fx-border-radius: 2;" +
	   			   				  "-fx-border-color: lightgrey;");
        VBox.setVgrow(consolePaneLower, Priority.ALWAYS);
        consolePaneLower.setMaxHeight(Double.MAX_VALUE);
        
        Label algorithmTimes = new Label(getLanguageString("lb_algorithmTimes"));
        algorithmTimes.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        algorithmTimes.setAlignment(Pos.CENTER);
        algorithmTimes.setPadding(new Insets(0, 0, 10, 0));
        HBox.setHgrow(algorithmTimes, Priority.ALWAYS);
        algorithmTimes.setMaxWidth(Double.MAX_VALUE);
        
        HBox firstTimeVBox = new HBox();
        firstTimeVBox.setPadding(new Insets(0, 0, 5, 0));
        HBox followTimeVBox = new HBox();
        followTimeVBox.setPadding(new Insets(0, 0, 5, 0));
        HBox parsingTableVBox = new HBox();
        parsingTableVBox.setPadding(new Insets(0, 0, 5, 0));
        HBox totalTimeVBox = new HBox();
        totalTimeVBox.setPadding(new Insets(0, 0, 5, 0));
        
        Label firstSetsTime = new Label(getLanguageString("lb_firstSetsTime"));
        firstSetsTime.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        firstSetsTime.prefWidthProperty().bind(consolePaneUpper.widthProperty().subtract(consolePaneUpper.widthProperty().divide(3)));
        firstSetsTimeMS.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        HBox.setHgrow(firstSetsTimeMS, Priority.ALWAYS);
        firstSetsTimeMS.setMaxWidth(Double.MAX_VALUE);
        firstSetsTimeMS.setAlignment(Pos.CENTER_RIGHT);
        
        Label followSetsTime = new Label(getLanguageString("lb_followSetsTime"));
        followSetsTime.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        followSetsTime.prefWidthProperty().bind(consolePaneUpper.widthProperty().subtract(consolePaneUpper.widthProperty().divide(3)));
        followSetsTimeMS.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        HBox.setHgrow(followSetsTimeMS, Priority.ALWAYS);
        followSetsTimeMS.setMaxWidth(Double.MAX_VALUE);
        followSetsTimeMS.setAlignment(Pos.CENTER_RIGHT);
        
        Label parsingTableTime = new Label(getLanguageString("lb_parsingTableTime"));
        parsingTableTime.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        parsingTableTime.prefWidthProperty().bind(consolePaneUpper.widthProperty().subtract(consolePaneUpper.widthProperty().divide(3)));
        parsingTableTimeMS.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        HBox.setHgrow(parsingTableTimeMS, Priority.ALWAYS);
        parsingTableTimeMS.setMaxWidth(Double.MAX_VALUE);
        parsingTableTimeMS.setAlignment(Pos.CENTER_RIGHT);
        
        Label totalTime = new Label(getLanguageString("lb_totalTime"));
        totalTime.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
        totalTime.prefWidthProperty().bind(consolePaneUpper.widthProperty().subtract(consolePaneUpper.widthProperty().divide(3)));
        totalTimeMS.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        HBox.setHgrow(totalTimeMS, Priority.ALWAYS);
        totalTimeMS.setMaxWidth(Double.MAX_VALUE);
        totalTimeMS.setAlignment(Pos.CENTER_RIGHT);
        
        Label logPanel = new Label(getLanguageString("lb_logPanel"));
        logPanel.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        logPanel.setAlignment(Pos.CENTER);
        logPanel.setPadding(new Insets(0, 0, 10, 0));
        HBox.setHgrow(logPanel, Priority.ALWAYS);
        logPanel.setMaxWidth(Double.MAX_VALUE);
        
        consoleLogSP.setContent(consoleLog);
        consoleLogSP.setFitToWidth(true);
        consoleLogSP.prefWidthProperty().bind(stackView.widthProperty().subtract(stackView.widthProperty().divide(3)));
        consoleLogSP.setHbarPolicy(ScrollBarPolicy.NEVER);
        consoleLogSP.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        consoleLogSP.setStyle("-fx-focus-color: transparent;" +
	             			  "-fx-background-color:transparent;");
        HBox.setHgrow(consoleLogSP, Priority.ALWAYS);
        
        consoleLog.setStyle("-fx-border-style: solid solid solid solid;" +
 				  			"-fx-border-width: 1;" +
 				  			"-fx-border-radius: 0;" +
	   				  		"-fx-border-color: lightgrey;");
        
        consoleLogSP.vvalueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
            	consoleLog.setLayoutY(-new_val.doubleValue());
            }
        });
    	
        firstTimeVBox.getChildren().addAll(firstSetsTime, firstSetsTimeMS);
        followTimeVBox.getChildren().addAll(followSetsTime, followSetsTimeMS);
        parsingTableVBox.getChildren().addAll(parsingTableTime, parsingTableTimeMS);
        totalTimeVBox.getChildren().addAll(totalTime, totalTimeMS);
        consolePaneUpper.getChildren().addAll(algorithmTimes, firstTimeVBox, followTimeVBox, parsingTableVBox, totalTimeVBox);
        consolePaneLower.getChildren().addAll(logPanel, consoleLog);
        consolePane.getChildren().addAll(consolePaneUpper, consolePaneLower);
             
    	HBox insertedValue = new HBox();
    	HBox.setHgrow(insertedValue, Priority.ALWAYS);
    	Label typeLabel = new Label(getLanguageString("lb_type"));
    	typeLabel.prefWidthProperty().bind(insertedValue.widthProperty().divide(4));
    	typeLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 17));
    	typeLabel.setAlignment(Pos.CENTER);	
    	typeLabel.setPadding(new Insets(3, 10, 3, 10));
    	Label valueLabel = new Label(getLanguageString("lb_value"));
    	valueLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 17)); 
    	HBox.setHgrow(valueLabel, Priority.ALWAYS);
    	valueLabel.setMaxWidth(Double.MAX_VALUE);
    	valueLabel.setAlignment(Pos.CENTER);
    	valueLabel.setPadding(new Insets(3, 10, 3, 10));
    	valueLabel.setStyle("-fx-border-style: hidden hidden hidden solid;" +
				   			"-fx-border-width: 1;" +
				   			"-fx-border-radius: 0;" +
	  			   			"-fx-border-color: lightgrey;");
    	insertedValue.getChildren().addAll(typeLabel, valueLabel);
    	consoleLog.getChildren().add(insertedValue);
		if (!Compiler.bootFound) Graphics.toConsoleLog(0, getLanguageString("lb_bootFound"));
		
        Scene consoleScene = new Scene(consolePane, 500, 600);
        consoleStage.setScene(consoleScene);
        
        // About Panel //       
        VBox aboutPanel = new VBox();
        aboutPanel.setPadding(new Insets(15, 15, 15, 15));
        aboutPanel.setSpacing(15);
        aboutPanel.setStyle("-fx-border-style: solid solid hidden solid;" + 
                "-fx-border-width: 1;" +
                "-fx-border-radius: 0;" + 
                "-fx-border-color: lightgrey;");
        VBox.setVgrow(aboutPanel, Priority.ALWAYS);
        aboutPanel.setMaxHeight(Double.MAX_VALUE);

        HBox aboutPanelUpper =new HBox();
        aboutPanelUpper.setAlignment(Pos.CENTER);
        aboutPanelUpper.setStyle("-fx-border-style: solid solid solid solid;" + 
                "-fx-border-width: 1;" +
                "-fx-border-radius: 0;" + 
                "-fx-border-color: lightgrey;");
        aboutPanelUpper.prefHeightProperty().bind(stackView.heightProperty().divide(2).subtract(10));
        
        VBox principlesVBoxInner = new VBox();
        
        Label aboutLabel = new Label(getLanguageString("lb_about"));
        aboutLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 25));
        aboutLabel.setPadding(new Insets(10, 10, 10, 10));
    
        Label aboutThisProgramLabel = new Label(getLanguageString("lb_aboutThisProgram"));
        aboutThisProgramLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 17));
        aboutThisProgramLabel.setWrapText(true);
        aboutThisProgramLabel.setAlignment(Pos.TOP_LEFT);
        aboutThisProgramLabel.setPadding(new Insets(0, 10, 10, 10));
        VBox.setVgrow(aboutThisProgramLabel, Priority.ALWAYS);
        aboutThisProgramLabel.setMaxHeight(Double.MAX_VALUE);
        aboutThisProgramLabel.setStyle("-fx-border-style: hidden hidden solid hidden;" + 
				 "-fx-border-width: 1;" +
				 "-fx-border-radius: 0;" + 
				 "-fx-border-color: lightgrey;");
        
        Image ll1_logo = new Image("images/LL1_Logo.png");
        ImageView ll1_logoIV = new ImageView(ll1_logo);
        ll1_logoIV.setFitWidth(220);
        ll1_logoIV.setPreserveRatio(true);
        ll1_logoIV.setSmooth(true);
        ll1_logoIV.setCache(true);
        
        Label logoPicture = new Label();
        logoPicture.setGraphic(ll1_logoIV);
        logoPicture.setStyle("-fx-border-style: hidden hidden hidden solid;" + 
				 "-fx-border-width: 1;" +
				 "-fx-border-radius: 0;" + 
				 "-fx-border-color: lightgrey;");
        logoPicture.prefWidthProperty().bind(aboutPanelUpper.widthProperty().divide(3).subtract(10));
        logoPicture.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(logoPicture, Priority.ALWAYS);
        
        HBox javaChangelog = new HBox();
        
        Image javaFXIMG = new Image("images/JavaFX_Logo.png");
        ImageView javaFXIMGIV = new ImageView(javaFXIMG);
        javaFXIMGIV.fitWidthProperty().bind((stackView.heightProperty().divide(5)));
        javaFXIMGIV.setPreserveRatio(true);
        javaFXIMGIV.setSmooth(true);
        javaFXIMGIV.setCache(true);       
        
        Label javaFXLbl = new Label();
        javaFXLbl.setAlignment(Pos.CENTER);
        javaFXLbl.setGraphic(javaFXIMGIV);
        javaFXLbl.prefWidthProperty().bind(principlesVBoxInner.widthProperty().divide(2));     
        
        VBox changelogBtnVBox = new VBox();
        changelogBtnVBox.setAlignment(Pos.CENTER);
        changelogBtnVBox.prefWidthProperty().bind(principlesVBoxInner.widthProperty().divide(2));
        
        Button changelogBtn = new Button(getLanguageString("btn_changelog"));
        changelogBtn.setPrefHeight(35);
        changelogBtn.setStyle("-fx-background-radius: 2;");
        changelogBtn.prefWidthProperty().bind(changelogBtnVBox.widthProperty().divide(2).add(35));  
        changelogBtnVBox.getChildren().add(changelogBtn);
        
        javaChangelog.getChildren().addAll(javaFXLbl, changelogBtnVBox);
        principlesVBoxInner.getChildren().addAll(aboutLabel, aboutThisProgramLabel, javaChangelog);
        aboutPanelUpper.getChildren().addAll(principlesVBoxInner, logoPicture);
        
        VBox aboutPanelLower = new VBox();
        aboutPanelLower.setPadding(new Insets(10, 10, 10, 10));
        aboutPanelLower.setSpacing(10);
        aboutPanelLower.setStyle("-fx-border-style: solid solid solid solid;" + 
                "-fx-border-width: 1;" +
                "-fx-border-radius: 0;" + 
                "-fx-border-color: lightgrey;");
        aboutPanelLower.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(aboutPanelLower, Priority.ALWAYS);
        
        Label contributorsLabel = new Label(getLanguageString("lb_contributors"));
        contributorsLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 25));
        
        HBox aboutPanelLowerInner = new HBox();
        aboutPanelLowerInner.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(aboutPanelLowerInner, Priority.ALWAYS);
        aboutPanelLowerInner.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(aboutPanelLowerInner, Priority.ALWAYS);
        aboutPanelLowerInner.setSpacing(10);
        
        VBox contributor1VBox = new VBox();
        contributor1VBox.setPadding(new Insets(10, 10, 10, 10));
        contributor1VBox.setStyle("-fx-border-style: solid solid solid solid;" + 
                				  "-fx-border-width: 1;" +
                				  "-fx-border-radius: 3;" + 
                				  "-fx-border-color: lightgrey;");
        contributor1VBox.prefWidthProperty().bind(aboutPanelLowerInner.widthProperty().divide(2).subtract(5));
        contributor1VBox.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(contributor1VBox, Priority.ALWAYS);
        
        Label contributor1Name = new Label(getLanguageString("lb_contributor1Name"));
        contributor1Name.setPadding(new Insets(0, 0, 10, 0));
        contributor1Name.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        contributor1Name.setAlignment(Pos.CENTER);
        contributor1Name.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(contributor1Name, Priority.ALWAYS);
        
        Label contributor1Email = new Label("Email: sotos@tzamaras.com");
        contributor1Email.setFont(Font.font("Tahoma", FontWeight.NORMAL, 17));
        contributor1Email.setAlignment(Pos.CENTER);
        contributor1Email.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(contributor1Email, Priority.ALWAYS);
        
        Label contributor1Ethnic = new Label(getLanguageString("lb_contributorEthnic"));
        contributor1Ethnic.setFont(Font.font("Tahoma", FontWeight.NORMAL, 17));
        contributor1Ethnic.setAlignment(Pos.CENTER);
        contributor1Ethnic.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(contributor1Ethnic, Priority.ALWAYS);
        
        Label contributor1Work = new Label(getLanguageString("lb_contributorWork"));
        contributor1Work.setPadding(new Insets(10, 0, 0, 0));
        contributor1Work.setFont(Font.font("Tahoma", FontWeight.BOLD, 17));
        
        Label contributor1Work01 = new Label(getLanguageString("lb_contributor1Work01"));
        contributor1Work01.setFont(Font.font("Tahoma", FontWeight.NORMAL, 17));
        
        Label contributor1Work02 = new Label(getLanguageString("lb_contributor1Work02"));
        contributor1Work02.setFont(Font.font("Tahoma", FontWeight.NORMAL, 17));
        
        Label contributor1Work03 = new Label(getLanguageString("lb_contributor1Work03"));
        contributor1Work03.setFont(Font.font("Tahoma", FontWeight.NORMAL, 17));
        
        Label contributor1Work04 = new Label(getLanguageString("lb_contributor1Work04"));
        contributor1Work04.setFont(Font.font("Tahoma", FontWeight.NORMAL, 17));
        
        contributor1VBox.getChildren().addAll(contributor1Name, contributor1Email, contributor1Ethnic, contributor1Work,
        									  contributor1Work01, contributor1Work02, contributor1Work03, contributor1Work04);
        
        VBox contributor2VBox = new VBox();
        contributor2VBox.setPadding(new Insets(10, 10, 10, 10));
        contributor2VBox.setAlignment(Pos.TOP_RIGHT);
        contributor2VBox.setStyle("-fx-border-style: solid solid solid solid;" + 
                				  "-fx-border-width: 1;" +
                				  "-fx-border-radius: 3;" + 
                				  "-fx-border-color: lightgrey;");
        contributor2VBox.prefWidthProperty().bind(aboutPanelLowerInner.widthProperty().divide(2).subtract(5));
        contributor2VBox.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(contributor2VBox, Priority.ALWAYS);
        
        Label contributor2Name = new Label(getLanguageString("lb_contributor2Name"));
        contributor2Name.setPadding(new Insets(0, 0, 10, 0));
        contributor2Name.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        contributor2Name.setAlignment(Pos.CENTER);
        contributor2Name.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(contributor2Name, Priority.ALWAYS);
        
        Label contributor2Email = new Label("Email: ang.vasileios@gmail.com");
        contributor2Email.setFont(Font.font("Tahoma", FontWeight.NORMAL, 17));
        contributor2Email.setAlignment(Pos.CENTER);
        contributor2Email.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(contributor2Email, Priority.ALWAYS);
        
        Label contributor2Ethnic = new Label(getLanguageString("lb_contributorEthnic"));
        contributor2Ethnic.setFont(Font.font("Tahoma", FontWeight.NORMAL, 17));
        contributor2Ethnic.setAlignment(Pos.CENTER);
        contributor2Ethnic.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(contributor2Ethnic, Priority.ALWAYS);
        
        Label contributor2Work = new Label(getLanguageString("lb_contributorWork"));
        contributor2Work.setPadding(new Insets(10, 0, 0, 0));
        contributor2Work.setFont(Font.font("Tahoma", FontWeight.BOLD, 17));
        
        Label contributor2Work01 = new Label(getLanguageString("lb_contributor2Work01"));
        contributor2Work01.setFont(Font.font("Tahoma", FontWeight.NORMAL, 17));
            
        Label contributor2Work02 = new Label(getLanguageString("lb_contributor2Work02"));
        contributor2Work02.setFont(Font.font("Tahoma", FontWeight.NORMAL, 17));
        
        contributor2VBox.getChildren().addAll(contributor2Name, contributor2Email, contributor2Ethnic, contributor2Work,
				  							  contributor2Work01, contributor2Work02);
        
        aboutPanelLowerInner.getChildren().addAll(contributor1VBox, contributor2VBox);
        aboutPanelLower.getChildren().addAll(contributorsLabel, aboutPanelLowerInner);
        aboutPanel.getChildren().addAll(aboutPanelUpper, aboutPanelLower);
        
        // Changelog Window //
        VBox changelogPane = new VBox();
        
        ScrollPane changelogSP = new ScrollPane();  
        changelogSP.setContent(mainChangelogPanel);
        changelogSP.setFitToWidth(true);
        changelogSP.setHbarPolicy(ScrollBarPolicy.NEVER);
        changelogSP.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);        
        VBox.setVgrow(changelogSP, Priority.ALWAYS);
        
        changelogSP.vvalueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
            	mainChangelogPanel.setLayoutY(-new_val.doubleValue());
            }
        });
        
        streamIn = Compiler.class.getResourceAsStream("/data/changelog.info");
        InputStreamReader inputReader = new InputStreamReader(streamIn, StandardCharsets.UTF_8);
    	Scanner changelogScanner = new Scanner(inputReader);
    	grammarScanner.useDelimiter("\\r?\\n");
    	boolean languagePass = false;
    	boolean versionLine = false;
		
		VBox lineVBox = new VBox();
		VBox.setVgrow(lineVBox, Priority.ALWAYS);
		lineVBox.setMaxWidth(Double.MAX_VALUE);
		lineVBox.setStyle("-fx-border-style: solid solid solid solid;" + 
    					  "-fx-border-width: 1;" +
    					  "-fx-border-radius: 3;" + 
    					  "-fx-border-color: lightgrey;");
		lineVBox.setPadding(new Insets(0, 0, 10, 0));
		
    	while (changelogScanner.hasNextLine()) {
    		String line = changelogScanner.nextLine();
    		if (line.contains("Language=" + Compiler.language)) {
    			languagePass = true;
    			versionLine = true;
    			line = changelogScanner.nextLine();
    		}
    		if (line.contains(Compiler.language + "-END")) {
    			languagePass = false;
    			mainChangelogPanel.getChildren().add(lineVBox);
    			break;
    		}
    		if (versionLine || line.contains("@Date") && languagePass) {
        		HBox versionHBox = new HBox();
        		HBox.setHgrow(versionHBox, Priority.ALWAYS);
        		versionHBox.setMaxWidth(Double.MAX_VALUE);

    			line = line.substring(1);
    				
    			Label versionLabel = new Label();
    			versionLabel.setPadding(new Insets(10, 10, 10, 10));
    			versionLabel.setAlignment(Pos.CENTER_LEFT);
    	        HBox.setHgrow(versionLabel, Priority.ALWAYS);
    	        versionLabel.setMaxWidth(Double.MAX_VALUE);
    	        versionLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 18));
    	        versionLabel.setText(line.substring(0, line.indexOf('@')-1));
    	        versionLabel.prefWidthProperty().bind(lineVBox.widthProperty().divide(2));
    	        
    	        line = line.substring(line.indexOf('@')+6);
    	        
    	        Label dateLabel = new Label();
    	        dateLabel.setPadding(new Insets(10, 10, 10, 10));
    	        dateLabel.setAlignment(Pos.CENTER_RIGHT);
    	        HBox.setHgrow(dateLabel, Priority.ALWAYS);
    	        dateLabel.setMaxWidth(Double.MAX_VALUE);
    	        dateLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 18));
    	        dateLabel.setText(line);
    	        dateLabel.prefWidthProperty().bind(lineVBox.widthProperty().divide(2));
    	        
    	        versionHBox.getChildren().addAll(versionLabel, dateLabel);
    	        lineVBox.getChildren().add(versionHBox);
    	        
    			line = changelogScanner.nextLine();
    			versionLine = false;
    		}
    	    if (!line.isEmpty() && languagePass && !versionLine) {
        		HBox versionHBox = new HBox();
        		HBox.setHgrow(versionHBox, Priority.ALWAYS);
        		versionHBox.setMaxWidth(Double.MAX_VALUE);
        		
        	    Label dotLabel = new Label();
        	    dotLabel.setPadding(new Insets(0, 0, 0, 10));
        	    dotLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 18));
        	    dotLabel.setText("•");
        	        
        	    line = line.substring(2);
        	        		
        	    Label logValueLabel = new Label();
        	    logValueLabel.setPadding(new Insets(0, 10, 0, 10));
        	    logValueLabel.setWrapText(true);
        	    HBox.setHgrow(logValueLabel, Priority.ALWAYS);
        	    logValueLabel.setMaxWidth(Double.MAX_VALUE);
        	    logValueLabel.setText(line);
            	    
        	    versionHBox.getChildren().addAll(dotLabel, logValueLabel);
        	    lineVBox.getChildren().add(versionHBox);
    	    }
    	    else if (line.isEmpty() && languagePass && !versionLine) {
    	    	mainChangelogPanel.getChildren().add(lineVBox);
    	    	lineVBox = new VBox();
    			lineVBox.setStyle("-fx-border-style: solid solid solid solid;" + 
    	    					  "-fx-border-width: 1;" +
    	    					  "-fx-border-radius: 3;" + 
    	    					  "-fx-border-color: lightgrey;");
    			lineVBox.setPadding(new Insets(0, 0, 10, 0));
    	    	VBox.setVgrow(lineVBox, Priority.ALWAYS);
    			lineVBox.setMaxWidth(Double.MAX_VALUE);
    	    	continue;
    	    }
    	}
    	changelogScanner.close();
    	
        changelogPane.getChildren().add(changelogSP);
        mainChangelogPanel.setAlignment(Pos.CENTER_LEFT);
        mainChangelogPanel.setSpacing(20);
        mainChangelogPanel.setPadding(new Insets(20, 20, 20, 20));
        
        Scene changelogScene = new Scene(changelogPane, 600, 600);
        changelogStage.setScene(changelogScene);
        
        // Footer Panel //
        Label copyright = new Label(getLanguageString("lb_copyright"));
        copyright.setAlignment(Pos.CENTER);
        copyright.prefWidthProperty().bind(footer.widthProperty().divide(3));
        
        HBox actionBox = new HBox();
        actionBox.setAlignment(Pos.CENTER_LEFT);
        actionBox.prefWidthProperty().bind(footer.widthProperty().divide(3));
        
        GridPane.setColumnSpan(actiontarget, 2);
        GridPane.setHalignment(actiontarget, RIGHT);
        actiontarget.setId("actiontarget");
        actiontarget.setFont(Font.font("Tahoma", FontWeight.SEMI_BOLD, -1));
        
        actionBox.getChildren().add(actiontarget);
        footer.getChildren().addAll(actionBox, copyright);
		
        // Event Handlers //
        EventHandler<ActionEvent> checkRules = new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent e) {
        		boolean nullFound = false;
        		boolean commaFound = false;
        		boolean dollarFound = false;
        		boolean spaceFound = false;
        		for (int i=0;i<rulesNumber.size();i++) {
        			if (rulesTextField.get(i).getText().isEmpty()) {
        				nullFound = true;
        				break;
        			}
        			else if (rulesTextField.get(i).getText().contains(",")) {
        				commaFound = true;
        				break;
        			}
        			else if (rulesTextField.get(i).getText().contains("$")) {
        				dollarFound = true;
        				break;
        			}
        			else if (rulesTextField.get(i).getText().contains(" ")) {
        				spaceFound = true;
        				break;
        			}
            	}
        		if (nullFound) {
        			stageWarningAlert.setTitle(getLanguageString("ttl_warning"));
        			stageWarningAlert.setContentText(getLanguageString("txt_emptyRules"));
        			stageWarningAlert.showAndWait();
        		}
        		else if (commaFound) {
        			stageWarningAlert.setTitle(getLanguageString("ttl_warning"));
        			stageWarningAlert.setContentText(getLanguageString("txt_commaRules"));
        			stageWarningAlert.showAndWait();
        		}
        		else if (dollarFound) {
        			stageWarningAlert.setTitle(getLanguageString("ttl_warning"));
        			stageWarningAlert.setContentText(getLanguageString("txt_dollarRules"));
        			stageWarningAlert.showAndWait();
        		}
        		else if (spaceFound) {
        			stageWarningAlert.setTitle(getLanguageString("ttl_warning"));
        			stageWarningAlert.setContentText(getLanguageString("txt_spaceRules"));
        			stageWarningAlert.showAndWait();
        		}
        		else {
        			String scannedGrammarRules = "";
        			for (int i=0;i<rulesNumber.size();i++) {
        				if (i!=rulesNumber.size()-1) scannedGrammarRules += rulesTextField.get(i).getText() + ',';
        				else scannedGrammarRules += rulesTextField.get(i).getText();
        			}
        			if (parser.checkRules(scannedGrammarRules)) {
        				btnFirst.setDisable(false);
        				if (!btnFollow.isDisabled()) btnFollow.setDisable(true);
        				if (!btnTable.isDisabled()) btnTable.setDisable(true);
        				if (!btnAnalyze.isDisabled()) btnAnalyze.setDisable(true);
        				displayInfoText(getLanguageString("txt_correctRules"), 1);
        				stepsLabels.get(0).setGraphic(stepsImageView.get(4));
        				stepsLabels.get(1).setGraphic(stepsImageView.get(1));
        				stepsLabels.get(2).setGraphic(stepsImageView.get(2));
        				stepsLabels.get(3).setGraphic(stepsImageView.get(3));
      					parsingTableView.getChildren().clear();
      					firstSetsLB.setText(getLanguageString("lb_firstSetsNC"));
      					firstSetsLB.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
      					followSetsLB.setText(getLanguageString("lb_followSetsNC"));
      					followSetsLB.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
      					parsingTableView.getChildren().add(parsingTableLB);
      					stringAnalyzeReady.setText(getLanguageString("txt_stringNotReadyInfo"));
      					stringAnalyzeReady.setFill(Color.RED);
      					resetStringAnalysis();
      					stageInformationAlert.setTitle(getLanguageString("ttl_information"));
      					stageInformationAlert.setContentText(getLanguageString("txt_correctRules"));
      					stageInformationAlert.showAndWait();
        			}
        			else {
        				if (!btnFirst.isDisabled()) btnFirst.setDisable(true);
        				if (!btnFollow.isDisabled()) btnFollow.setDisable(true);
        				if (!btnTable.isDisabled()) btnTable.setDisable(true);
        				if (!btnAnalyze.isDisabled()) btnAnalyze.setDisable(true);
        				displayInfoText(getLanguageString("txt_wrongRules"), 0);
        				stepsLabels.get(0).setGraphic(stepsImageView.get(0));
        				stepsLabels.get(1).setGraphic(stepsImageView.get(1));
        				stepsLabels.get(2).setGraphic(stepsImageView.get(2));
        				stepsLabels.get(3).setGraphic(stepsImageView.get(3));
        				stageWarningAlert.setTitle(getLanguageString("ttl_warning"));
            			stageWarningAlert.setContentText(getLanguageString("txt_symbolNotRec") + parser.getUnrecognizedChar() + "'");
            			stageWarningAlert.showAndWait();
        			}
        		}
            }
        };
        
        EventHandler<ActionEvent> fillSelectedGrammar = new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent e) {
        		String buttonID = ((Button)e.getSource()).getId();
        		String selectedGrammar = "";
        		if (buttonID.equals("btnSelectSet")) {
        			selectedGrammar = "/grammars/" + list.getSelectionModel().selectedItemProperty().getValue();
        			InputStream input = Compiler.class.getResourceAsStream(selectedGrammar);
                	InputStreamReader inputReader = new InputStreamReader(input, StandardCharsets.UTF_8);
                    Scanner scanner = new Scanner(inputReader);
                    scanner.useDelimiter("\\A");
            		String[] scannedRuleSet = scanner.next().split("\\r?\\n");
                    if (rulesNumber.size()>0) {
                    	textFieldCount=0;
                    	rulesGrid.getChildren().clear();
                    	rulesNumber.clear();
        	        	rulesTextField.clear();
        	        	delButtons.clear();
                    }
                    for (int i=0; i<scannedRuleSet.length; i++) {
                    	addTextFieldRow(scannedRuleSet[i]);
                    }
                    scanner.close();
        		}
        		else {
        			FileChooser fileChooser = new FileChooser();
        			fileChooser.setTitle(getLanguageString("fileChooser"));
        			fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(getLanguageString("extensionFilter"), "*.txt"));
        			File selectedFile = fileChooser.showOpenDialog(primaryStage);
        			if (selectedFile != null) {
        				selectedGrammar = selectedFile.getAbsolutePath();
        				try {
							FileInputStream file = new FileInputStream(selectedGrammar);
							InputStreamReader inputReader = new InputStreamReader(file, StandardCharsets.UTF_8);
	                        Scanner scanner = new Scanner(inputReader);
	                        scanner.useDelimiter("\\A");
	                		String[] scannedRuleSet = scanner.next().split("\\r?\\n");
	                        if (rulesNumber.size()>0) {
	                        	textFieldCount=0;
	                        	rulesGrid.getChildren().clear();
	                        	rulesNumber.clear();
	            	        	rulesTextField.clear();
	            	        	delButtons.clear();
	                        }
	                        for (int i=0; i<scannedRuleSet.length; i++) {
	                        	addTextFieldRow(scannedRuleSet[i]);
	                        }
	                        scanner.close();
						} catch (FileNotFoundException e1) {
							e1.printStackTrace();
						}
                    	
        			}
        		}
                refreshPanel();
            }
        };
        
        EventHandler<ActionEvent> addTextField = new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent e) {
        		addTextFieldRow (null);
                refreshPanel();
            }
        };
        
        EventHandler<ActionEvent> changeMainStack = new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent e) {
        		String text = ((Button)e.getSource()).getId();
  				stackView.getChildren().clear();	
  				switch (text) {
  				case "home":
  					stackView.getChildren().add(homeVBox);
  					break;
  				case "rules":
  					stackView.getChildren().add(rulesPanel);
  					if (!showStepsBtn.isDisabled()) showStepsBtn.setDisable(true);
  					break;
  				case "firstSets":
  					stackView.getChildren().add(mainStack);
        			CalculateFirstSets();
        			break;
  				case "followSets":
  					stackView.getChildren().add(mainStack);
        			CalculateFollowSets();
        			break;
  				case "parsingTable":
  					stackView.getChildren().add(mainStack);
        			ConstructParsingTable();
        			break;
  				case "analysis":
  					stackView.getChildren().add(stringAnalysisVBox);
  					if (!showStepsBtn.isDisabled()) showStepsBtn.setDisable(true);
  					break;
  				case "options":
  					stackView.getChildren().add(optionsPanel);
  					if (!showStepsBtn.isDisabled()) showStepsBtn.setDisable(true);
  					break;
  				case "about":
  					stackView.getChildren().add(aboutPanel);
  					if (!showStepsBtn.isDisabled()) showStepsBtn.setDisable(true);
  					break;
  				}
            }
        };
        
        EventHandler<ActionEvent> exportGrammar = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {    	
            	String grammarFile = "";
            	for (int i=0;i<rulesNumber.size();i++) {
	        		grammarFile += rulesTextField.get(i).getText() + System.lineSeparator();
            	}
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(getLanguageString("extensionFilter"), "*.txt");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showSaveDialog(primaryStage);
                
                if(file != null){
                    try {
                        Writer fileWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
                        fileWriter.write(grammarFile);
                        fileWriter.close();
                    } catch (IOException ex) {
                        System.out.println("File Writer Error");
                    }
                }
            }
        };
        
        EventHandler<ActionEvent> showStepsEH = new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent e) {
            	if (!stepsStage.isShowing()) stepsStage.show();
            }
        };
        
        EventHandler<ActionEvent> showInstructionsEH = new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent e) {
        		if (!mainInstructionsPanel.getChildren().isEmpty()) mainInstructionsPanel.getChildren().clear();
                String instructionsString = "";
                InputStream streamIn = Compiler.class.getResourceAsStream("/data/instructions.info");
                InputStreamReader inputReader = new InputStreamReader(streamIn, StandardCharsets.UTF_8);
            	Scanner instructionsScanner = new Scanner(inputReader);
            	instructionsScanner.useDelimiter("\\r?\\n");
            	boolean languageFound = false;
            	while (instructionsScanner.hasNextLine()) {
            		String scannedLine = instructionsScanner.nextLine();
            		if (scannedLine.contains("instructions=" + Compiler.language)) {
            			languageFound = true;
            			continue;
            		}
               		if (scannedLine.contains(Compiler.language + "_END")) {
               			if (!instructionsString.isEmpty()) {
        					Label instructionsLabel = new Label();
                    		instructionsLabel.setPadding(new Insets(0, 10, 10, 10));
                    		instructionsLabel.setWrapText(true);
                        	HBox.setHgrow(instructionsLabel, Priority.ALWAYS);
                        	instructionsLabel.setMaxWidth(Double.MAX_VALUE);
                        	instructionsLabel.setStyle("-fx-border-style: hidden;" + 
                                					   "-fx-border-width: 1;" +
                                					   "-fx-border-radius: 0;" + 
                                					   "-fx-border-color: lightgrey;");
                        	instructionsLabel.setText(instructionsString);
                        	mainInstructionsPanel.getChildren().add(instructionsLabel);
                        	instructionsString = "";
                        	break;
        				}
               			else {
               				break;
               			}
            		}
            		if (languageFound) {
            			if (scannedLine.contains("<title>")) {
            				if (!instructionsString.isEmpty()) {
            					Label instructionsLabel = new Label();
                        		instructionsLabel.setPadding(new Insets(0, 10, 10, 10));
                        		instructionsLabel.setWrapText(true);
                            	HBox.setHgrow(instructionsLabel, Priority.ALWAYS);
                            	instructionsLabel.setMaxWidth(Double.MAX_VALUE);
                            	instructionsLabel.setStyle("-fx-border-style: hidden;" + 
                                    					   "-fx-border-width: 1;" +
                                    					   "-fx-border-radius: 0;" + 
                                    					   "-fx-border-color: lightgrey;");
                            	instructionsLabel.setText(instructionsString);
                            	mainInstructionsPanel.getChildren().add(instructionsLabel);
                            	instructionsString = "";
            				}
            				Label titleLabel = new Label(scannedLine.substring(7));
            				titleLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
            				titleLabel.setAlignment(Pos.CENTER);
            				titleLabel.setPadding(new Insets(10, 10, 10, 10));
                            HBox.setHgrow(titleLabel, Priority.ALWAYS);
                            titleLabel.setMaxWidth(Double.MAX_VALUE);
                            titleLabel.setWrapText(true);
                            titleLabel.setStyle("-fx-border-style: solid hidden hidden hidden;" + 
                                    			"-fx-border-width: 1;" +
                                    			"-fx-border-radius: 0;" + 
                                    			"-fx-border-color: lightgrey;");
                            mainInstructionsPanel.getChildren().add(titleLabel);
            			}
            			else if (scannedLine.contains("<b>")) {
            				if (!instructionsString.isEmpty()) {
            					Label instructionsLabel = new Label();
                        		instructionsLabel.setPadding(new Insets(0, 10, 0, 10));
                        		instructionsLabel.setWrapText(true);
                            	HBox.setHgrow(instructionsLabel, Priority.ALWAYS);
                            	instructionsLabel.setMaxWidth(Double.MAX_VALUE);
                            	instructionsLabel.setStyle("-fx-border-style: hidden;" + 
                                    					   "-fx-border-width: 1;" +
                                    					   "-fx-border-radius: 0;" + 
                                    					   "-fx-border-color: lightgrey;");
                            	instructionsLabel.setText(instructionsString);
                            	mainInstructionsPanel.getChildren().add(instructionsLabel);
                            	instructionsString = "";
            				}
            				Label separateLabel = new Label();
            				if (scannedLine.contains("<center>")) {
            					separateLabel.setText(scannedLine.substring(0, scannedLine.length()-11));
            					separateLabel.setAlignment(Pos.CENTER);
            				}
            				else {
            					separateLabel.setText(scannedLine.substring(0, scannedLine.length()-3));
            					separateLabel.setPadding(new Insets(0, 10, 0, 10));
            				}
            				separateLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, -1));
            				separateLabel.setWrapText(true);
                            HBox.setHgrow(separateLabel, Priority.ALWAYS);
                            separateLabel.setMaxWidth(Double.MAX_VALUE);
                            mainInstructionsPanel.getChildren().add(separateLabel);
            			}
            			else if (scannedLine.contains("<p>")) {
            				Label instructionsLabel = new Label();
                        	instructionsLabel.setPadding(new Insets(0, 10, 0, 10));
                        	instructionsLabel.setWrapText(true);
                            HBox.setHgrow(instructionsLabel, Priority.ALWAYS);
                            instructionsLabel.setMaxWidth(Double.MAX_VALUE);
                            instructionsLabel.setStyle("-fx-border-style: hidden;" + 
                                   					   "-fx-border-width: 1;" +
                                   					   "-fx-border-radius: 0;" + 
                                   					   "-fx-border-color: lightgrey;");
                            instructionsLabel.setText(instructionsString);
                            mainInstructionsPanel.getChildren().add(instructionsLabel);
                            instructionsString = "";
            				continue;
            			}
            			else {
            				instructionsString += scannedLine + " ";
            			}
            		}
            	}
            	instructionsScanner.close();
            	if (!instructionsStage.isShowing()) instructionsStage.show();
            }
        };
        
        EventHandler<ActionEvent> showChangelogEH = new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent e) {
            	if (!changelogStage.isShowing()) changelogStage.show();
            }
        };
        
        EventHandler<ActionEvent> analyzeStringEH = new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent e) {
            	stackStringLeft.getChildren().clear();
            	stackStringRight.getChildren().clear();
            	stackItems.clear();
            	stringInputCurrentLeft.getChildren().clear();
            	stringInputCurrentRight.getChildren().clear();
            	stringStepsLeft.getChildren().clear();
            	stringSteps.getChildren().clear();
            	stringSteps.getChildren().addAll(stringStepsSP, stringStepsRight);  
            	if (manualSteps.isDisabled()) manualSteps.setDisable(false);
            	if (autoSteps.isDisabled()) autoSteps.setDisable(false);
            	if (resultSteps.isDisabled()) resultSteps.setDisable(false);
            	stepCounter = 0;
            	toStringAnalysis("Initialization", 0);
            	stepCounter = 1;
    			String inputEncoded = stringInputTF.getText();
    			inputEncoded = inputEncoded.replace("|", "#");
    			parser.initializeString(Compiler.rules, inputEncoded, parsingTable);
            }
        };
        
        EventHandler<ActionEvent> procceedStep = new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent e) {
            	int result = parser.analyzeString();
            	if (result==1 || result==2) {
            		manualSteps.setDisable(true);
            		autoSteps.setDisable(true);
            		resultSteps.setDisable(true);
            	}
            	stringStepsSP.setVvalue(stringStepsSP.getVmax());
            }
        };
        
        EventHandler<ActionEvent> procceedAutoStep = new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent e) {
        		boolean notNumberError = false;
        		String timerString = autoStepsTimer.getText();
        		if (timerString.contains(",")) {
        			timerString = timerString.replace(",", ".");
        		}
        		if (!timerString.matches("\\d*([\\.]\\d{0,1})?")) {
        			stageWarningAlert.setTitle(getLanguageString("ttl_warning"));
        			stageWarningAlert.setContentText(getLanguageString("txt_numberError"));
        			stageWarningAlert.showAndWait();
                    notNumberError = true;
                }
        		if (!notNumberError) {
        			double timer = Double.parseDouble(timerString);
        			String buttonID = ((Button)e.getSource()).getId();
        			if ((timer<0.1 || timer>5) && buttonID.equals("autoSteps")) {
        				stageWarningAlert.setTitle(getLanguageString("ttl_warning"));
        				stageWarningAlert.setContentText(getLanguageString("txt_rangeError"));
        				stageWarningAlert.showAndWait();
        			}
        			else {
            			switch (buttonID) {
            			case "autoSteps":
        					parser.autoAnalyzeString("Timer", timer);
        					break;
            			case "resultSteps":
        					parser.autoAnalyzeString("OneTime", timer);
        					break;
            			}
            			manualSteps.setDisable(true);
                		autoSteps.setDisable(true);
                		resultSteps.setDisable(true);
        			}
        		}
            }
        };
        
        EventHandler<ActionEvent> changeLanguage = new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent e) {
        		String path = "./lang.info";
                File bootLang = new File(path);
				try {
					FileWriter fw = new FileWriter(bootLang);
	                BufferedWriter out = new BufferedWriter(fw);
	                out.write("#LANGUAGE=" + languageSelector.getSelectionModel().selectedItemProperty().getValue());
	                out.newLine();
	                out.write("Auto generated language file, to change language of the compiler at boot time.");
	                out.flush();
	                out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				stageWarningAlert.setTitle(getLanguageString("ttl_restart"));
				stageWarningAlert.setContentText(getLanguageString("txt_restartText"));
				stageWarningAlert.showAndWait();
            }
        };
        
        EventHandler<ActionEvent> exit = new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent e) {
                Platform.exit();
                System.exit(0);
            }
        };
        
        list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
          public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        	  if (btnSelectRuleSet.isDisabled()) btnSelectRuleSet.setDisable(false); 
          }
        });
        
        stringInputTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.isEmpty() && !stringInputBT.isDisabled()) {
                	stringInputBT.setDisable(true);
                }
                else {
                	if (stringInputBT.isDisabled()) stringInputBT.setDisable(false);
                }
            }
        });
        
        consoleCB.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
            	if (new_val) {               
            		consoleStage.show();
            	}
            	else {
            		consoleStage.close();
            	}
            }
        });
        
        consoleStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
            	consoleCB.setSelected(false);
            }
        });   
        
        languageSelector.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
          public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        	  if (newValue.equals(Compiler.language)) saveLanguage.setDisable(true);
        	  else saveLanguage.setDisable(false);
          }
        });
        
        // Buttons Actions //
        btnHome.setOnAction(changeMainStack);
        gettingStartedBT.setOnAction(showInstructionsEH);
        btnRules.setOnAction(changeMainStack);
        btnFirst.setOnAction(changeMainStack);
        btnFollow.setOnAction(changeMainStack);
        btnTable.setOnAction(changeMainStack);
        btnAnalyze.setOnAction(changeMainStack);
        btnOptions.setOnAction(changeMainStack);
        btnAbout.setOnAction(changeMainStack);
        btnAddRule.setOnAction(addTextField);
        btnSaveRules.setOnAction(checkRules);
        btnImportRules.setOnAction(fillSelectedGrammar);
        btnExportRules.setOnAction(exportGrammar);
        btnSelectRuleSet.setOnAction(fillSelectedGrammar);
        showStepsBtn.setOnAction(showStepsEH);
        stringInputBT.setOnAction(analyzeStringEH);
        manualSteps.setOnAction(procceedStep);
        autoSteps.setOnAction(procceedAutoStep);
        resultSteps.setOnAction(procceedAutoStep);
        saveLanguage.setOnAction(changeLanguage);
        changelogBtn.setOnAction(showChangelogEH);
        btnExit.setOnAction(exit);
        
        refreshPanel();

        Scene scene = new Scene(mainPane, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();
        stageInformationAlert.initOwner(primaryStage);
		stageWarningAlert.initOwner(primaryStage);
    }
    
    // Public methods //
    private static String getLanguageString (String input) {
    	
    	InputStream streamIn = Compiler.class.getResourceAsStream("/bundles/deprecated_" + Compiler.language);
    	InputStreamReader inputReader = new InputStreamReader(streamIn, StandardCharsets.UTF_8);
        Scanner scanner = new Scanner(inputReader);
        scanner.useDelimiter("\\A");
		String[] scannedLanguageItems = scanner.next().split("\\r?\\n");
		scanner.close();
		
    	String returnInput = "";
    	for (int i=0; i<scannedLanguageItems.length; i++) {
    		if (scannedLanguageItems[i].contains("@" + input)) {
    			returnInput = scannedLanguageItems[i].substring(scannedLanguageItems[i].indexOf("=") + 2);
    			break;
    		}
    	}   	
    	return returnInput;
    }
    
    private void refreshPanel () {
    	textFieldCount = rulesNumber.size();
		if (textFieldCount>1) {
			if (delButtons.get(0).isDisabled()) delButtons.get(0).setDisable(false);
			if (btnExportRules.isDisabled()) btnExportRules.setDisable(false);			
		}
		if (textFieldCount==1 && rulesTextField.get(0).getText().isEmpty()) {
			delButtons.get(0).setDisable(true);
			btnExportRules.setDisable(true);
		}
    }
    
    private void resetStringAnalysis() {
			stringInputTF.clear();
			stringSteps.getChildren().clear();
			stringSteps.getChildren().addAll(stringStepsTemporary, stringStepsRight);
			stackStringLeft.getChildren().clear();
			stackStringRight.getChildren().clear();
			stackStringLeft.getChildren().add(stringLeft2);
			stackStringRight.getChildren().add(stringRight2);
			stringInputCurrentLeft.getChildren().clear();
			stringInputCurrentRight.getChildren().clear();
			stringInputCurrentLeft.getChildren().add(stringInputCurrentLeftLB);
			stringInputCurrentRight.getChildren().add(stringInputCurrentRightLB);
			manualSteps.setDisable(true);
		    autoSteps.setDisable(true);
		    resultSteps.setDisable(true);
    }
    
    private void addTextFieldRow (String input) {    	       
		
		rulesNumber.add(textFieldCount, new Label(getLanguageString("lb_ruleCount") + (textFieldCount+1)));    
		rulesTextField.add(textFieldCount, new TextField());
		if (input!=null) rulesTextField.get(textFieldCount).setText(input);
		delButtons.add(textFieldCount, new Button());
		delButtons.get(textFieldCount).setId("btn" + textFieldCount);
		delButtons.get(textFieldCount).setOnAction(deleteTextField);
		
		rulesNumber.get(textFieldCount).setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		rulesTextField.get(textFieldCount).setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		rulesTextField.get(textFieldCount).prefWidthProperty().bind(rulesGrid.widthProperty().divide(2).subtract(20));
        
		imgViewDelete.add(textFieldCount, new ImageView(imgDelete));
        imgViewDelete.get(textFieldCount).setFitHeight(20);
        imgViewDelete.get(textFieldCount).setFitWidth(20);
        imgViewDelete.get(textFieldCount).setPreserveRatio(true);
        imgViewDelete.get(textFieldCount).setSmooth(true);
        imgViewDelete.get(textFieldCount).setCache(true);
        delButtons.get(textFieldCount).setGraphic(imgViewDelete.get(textFieldCount));
        VBox.setVgrow(delButtons.get(textFieldCount), Priority.ALWAYS);
        delButtons.get(textFieldCount).setMaxHeight(Double.MAX_VALUE);
        Tooltip tooltipDel = new Tooltip(getLanguageString("tt_delete"));
        tooltipDel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        tooltipDel.setStyle("-fx-text-alignment: center;");
        delButtons.get(textFieldCount).setTooltip(tooltipDel);
		        		
		rulesGrid.add(rulesNumber.get(textFieldCount), 0, textFieldCount);
		rulesGrid.add(rulesTextField.get(textFieldCount), 1, textFieldCount);
        rulesGrid.add(delButtons.get(textFieldCount), 2, textFieldCount);
        
		textFieldCount += 1;
    }
    
    private EventHandler<ActionEvent> deleteTextField = new EventHandler<ActionEvent>() {
    	public void handle(ActionEvent e) {
    		int btnSource = Integer.parseInt(((Button)e.getSource()).getId().substring(((Button)e.getSource()).getId().length()-1));
    		while (btnSource < rulesNumber.size()-1){
    			rulesTextField.get(btnSource).setText(rulesTextField.get(btnSource+1).getText());
    			btnSource+=1;
    		}
    		if (btnSource==rulesNumber.size()-1) {
    			if (rulesNumber.size()-1 != 0) {
    				rulesGrid.getChildren().remove(rulesNumber.get(btnSource));   
    				rulesGrid.getChildren().remove(rulesTextField.get(btnSource));
    				rulesGrid.getChildren().remove(delButtons.get(btnSource));        					
    				rulesNumber.remove(btnSource);
    				rulesTextField.remove(btnSource);
    				delButtons.remove(btnSource);
    			}
    			else {
        			rulesTextField.get(btnSource).clear();
        		}
    		}
    		refreshPanel();
        }
    };
    
    private void CalculateFirstSets() {
    	stepCounter = 0;
    	long startTime = System.nanoTime();
    	if (firstSets != null) firstSets.clear();
		firstSets = first.firstSets(Compiler.rules);
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		firstSetsTimeMS.setText(duration/1000000 + " ms");
		followSetsTimeMS.setText("-- ms");
		parsingTableTimeMS.setText("-- ms");
		totalTimeMS.setText("-- ms");
		if (btnFollow.isDisabled()) btnFollow.setDisable(false);
		mainStepsPanel.getChildren().clear();
		boolean changeBG = true;
		for (int i=0; i<debugMessages.size();i++) {
	    	HBox temp = new HBox();
	    	temp.setAlignment(Pos.CENTER_LEFT);
	    	Label tempLB = new Label(String.valueOf(stepCounter));
	    	tempLB.maxWidthProperty().bind(temp.widthProperty().divide(8));
	    	tempLB.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
	    	tempLB.setMaxHeight(Double.MAX_VALUE);
	    	tempLB.setMinWidth(50);
	        tempLB.setPadding(new Insets(0, 0, 0, 10));
	        temp.getChildren().addAll(tempLB, debugMessages.get(i));
			if (changeBG) {
				tempLB.setStyle("-fx-background-color: white;");
				debugMessages.get(i).setStyle("-fx-background-color: white;" +
						  "-fx-border-style: hidden hidden hidden solid;" + 
	                      "-fx-border-width: 2;" + 
	                      "-fx-border-radius: 0;" + 
					      "-fx-border-color: black");
				changeBG = false;
			}
			else {
				tempLB.setStyle("-fx-background-color: lightgray;");
				debugMessages.get(i).setStyle("-fx-background-color: lightgray;" +
											  "-fx-border-style: hidden hidden hidden solid;" + 
						                      "-fx-border-width: 2;" + 
						                      "-fx-border-radius: 0;" + 
										      "-fx-border-color: black");
				changeBG = true;
			}
			debugMessages.get(i).setMaxWidth(Double.MAX_VALUE);
			debugMessages.get(i).setAlignment(Pos.CENTER_LEFT);
			HBox.setHgrow(debugMessages.get(i), Priority.ALWAYS);
			debugMessages.get(i).setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
			debugMessages.get(i).setPadding(new Insets(0, 0, 0, 10));
	        
			mainStepsPanel.getChildren().add(temp);
			stepCounter += 1;
    	}
		String firstSetsObject = "";
		for (char key : firstSets.keySet()) {
			firstSetsObject += "First(" + key + ")->{" + firstSets.get(key) + "}\n";
		}
		firstSetsLB.setText(firstSetsObject);
		firstSetsLB.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));
		stepsLabels.get(1).setGraphic(stepsImageView.get(5));
		if (showStepsBtn.isDisabled()) showStepsBtn.setDisable(false);
		displayInfoText(getLanguageString("txt_firstSetsSuccess"), 1);
    }
    
    private void CalculateFollowSets() {
    	stepCounter = 0;
		long startTime = System.nanoTime();
    	if (followSets != null) followSets.clear();
		followSets = follow.followSets(Compiler.rules, firstSets);
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		followSetsTimeMS.setText(duration/1000000 + " ms");
		if (btnTable.isDisabled()) {
			btnTable.setDisable(false);
		}
		mainStepsPanel.getChildren().clear();
		boolean changeBG = true;
		for (int i=0; i<debugMessages.size();i++) {
	    	HBox temp = new HBox();
	    	temp.setAlignment(Pos.CENTER_LEFT);
	    	
	    	Label tempLB = new Label(String.valueOf(stepCounter));
	    	tempLB.maxWidthProperty().bind(temp.widthProperty().divide(8));
	    	tempLB.setMinWidth(50);
	    	tempLB.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
	    	tempLB.setMaxHeight(Double.MAX_VALUE);
	        tempLB.setPadding(new Insets(0, 0, 0, 10));
	        temp.getChildren().addAll(tempLB, debugMessages.get(i));
			if (changeBG) {
				tempLB.setStyle("-fx-background-color: white;");
				debugMessages.get(i).setStyle("-fx-background-color: white;" +
						  "-fx-border-style: hidden hidden hidden solid;" + 
	                      "-fx-border-width: 2;" + 
	                      "-fx-border-radius: 0;" + 
					      "-fx-border-color: black");
				changeBG = false;
			}
			else {
				tempLB.setStyle("-fx-background-color: lightgray;");
				debugMessages.get(i).setStyle("-fx-background-color: lightgray;" +
											  "-fx-border-style: hidden hidden hidden solid;" + 
						                      "-fx-border-width: 2;" + 
						                      "-fx-border-radius: 0;" + 
										      "-fx-border-color: black");
				changeBG = true;
			}
			debugMessages.get(i).setWrapText(true);
			debugMessages.get(i).setMaxWidth(Double.MAX_VALUE);
			debugMessages.get(i).setAlignment(Pos.CENTER_LEFT);
			HBox.setHgrow(debugMessages.get(i), Priority.ALWAYS);
			debugMessages.get(i).setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
			debugMessages.get(i).setPadding(new Insets(0, 0, 0, 10));
	        
			mainStepsPanel.getChildren().add(temp);
			stepCounter += 1;
    	}
		String followSetsObject = "";
		for (char key : followSets.keySet()) {
			followSetsObject += "Follow(" + key + ")->{" + followSets.get(key) + "}\n";
		}
		followSetsLB.setText(followSetsObject);
		followSetsLB.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));
		stepsLabels.get(2).setGraphic(stepsImageView.get(6));
		if (showStepsBtn.isDisabled()) showStepsBtn.setDisable(false);
		displayInfoText(getLanguageString("txt_followSetsSuccess"), 1);
    }
    
    private void ConstructParsingTable() {
		long startTime = System.nanoTime();
		int result = parser.constructTable(Compiler.rules, firstSets, followSets);
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		parsingTableTimeMS.setText(duration/1000000 + " ms"); 
		long totalMs = Long.parseLong((firstSetsTimeMS.getText().substring(0, firstSetsTimeMS.getText().length()-3))) + Long.parseLong(followSetsTimeMS.getText().substring(0, followSetsTimeMS.getText().length()-3)) + duration/1000000;
		totalTimeMS.setText(totalMs + " ms");
		
		if (result==0) displayInfoText(getLanguageString("txt_parsingTableSuccess"), 1);
		else displayInfoText(getLanguageString("txt_notLL1"), 0);
		
		parsingTable = parser.getParsingTable();
		parsingTableGP.getChildren().clear();
		parsingTableGP.getColumnConstraints().clear();
		parsingTableGP.getRowConstraints().clear();
		
		for (int i=0;i<parsingTable.length;i++) {
			RowConstraints tempRowCN = new RowConstraints(25,25,Double.MAX_VALUE);
			tempRowCN.setVgrow(Priority.ALWAYS);
			parsingTableGP.getRowConstraints().add(tempRowCN);
			for (int j=0; j<parsingTable[0].length;j++){
				Label tempLabel = new Label(parsingTable[i][j]);
				if (i==0 || j==0) {
					tempLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
					tempLabel.setStyle("-fx-background-color: gray;");
					if (i==0) {
						ColumnConstraints tempColumnCN = new ColumnConstraints(50,50,Double.MAX_VALUE);
						tempColumnCN.setHgrow(Priority.ALWAYS);
						parsingTableGP.getColumnConstraints().add(tempColumnCN);		
					}
				}
				else {
					tempLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
					tempLabel.setStyle("-fx-background-color: lightgray;");
				}			
				tempLabel.setMaxWidth(Double.MAX_VALUE);
				tempLabel.setMaxHeight(Double.MAX_VALUE);
				tempLabel.setAlignment(Pos.CENTER);
				VBox.setVgrow(tempLabel, Priority.ALWAYS);
				HBox.setHgrow(tempLabel, Priority.ALWAYS);
				parsingTableGP.add(tempLabel, j, i);
			}
		}
		
		parsingTableGP.prefHeightProperty().bind(parsingTableView.heightProperty());
		parsingTableGP.setAlignment(Pos.CENTER);
		parsingTableGP.setHgap(1);
		parsingTableGP.setVgap(1);
		
		parsingTableGP.setStyle("-fx-border-style: hidden hidden solid hidden;" + 
                "-fx-border-width: 1;" +
                "-fx-border-radius: 0;" + 
                "-fx-border-color: black;" + 
                "-fx-background-color: black;");
		
		parsingTableView.getChildren().clear();
		parsingTableView.getChildren().add(parsingTableGP);
		
		if (btnAnalyze.isDisabled()) {
			btnAnalyze.setDisable(false);
		}
		
		stepsLabels.get(3).setGraphic(stepsImageView.get(7));
		stringAnalyzeReady.setText(getLanguageString("txt_stringReadyInfo"));
		stringAnalyzeReady.setFill(Color.GREEN);
		if (!showStepsBtn.isDisabled()) showStepsBtn.setDisable(true);
    }
    
    private void displayInfoText(String text, int status) {
    	if (status==1) actiontarget.setFill(Color.GREEN);
    	else actiontarget.setFill(Color.FIREBRICK);
        actiontarget.setText(text);
        if (!actiontarget.isVisible()) actiontarget.setVisible(true);
        new Thread(() -> {
        	try {
        		Thread.sleep(3000);
                actiontarget.setVisible(false);
            } 
        	catch (InterruptedException e1) {
            	e1.printStackTrace();
            }
        }).start();
    }
    
    public static void toDebug (String text) {
    	debugMessages.add(new Label(text));
    }
    
    public static void toStringAnalysis (String text, int type) {
    	HBox temp = new HBox();
    	temp.setSpacing(10);
    	temp.setStyle("-fx-border-style: hidden hidden solid hidden;" + 
				"-fx-border-width: 1;" +
				"-fx-border-radius: 0;" + 
				"-fx-border-color: lightgrey;");      
    	
    	Label tempLabel = new Label("No. " + String.valueOf(stepCounter));
    	tempLabel.prefWidthProperty().bind(temp.widthProperty().divide(6));
    	tempLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        HBox.setHgrow(tempLabel, Priority.ALWAYS);
    		
    	Label tempLabelText = new Label(text);
    	if (type==1) tempLabelText.setTextFill(Color.RED);
    	if (type==2) tempLabelText.setTextFill(Color.GREEN);
    	tempLabelText.prefWidthProperty().bind(temp.widthProperty().subtract(temp.widthProperty().divide(6)));
    	tempLabelText.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));   	

        HBox.setHgrow(tempLabelText, Priority.ALWAYS);
        
    	temp.getChildren().addAll(tempLabel, tempLabelText);
    	stringStepsLeft.getChildren().add(temp);
    	stepCounter += 1;
    }
    
    public static void inputInteract (String string) {
    	stackStringRight.getChildren().clear();
    	stringInputCurrentRight.getChildren().clear();
    	for (int i=0; i<string.length();i++) {
    		Label temp = new Label(String.valueOf(string.charAt(i)));
    		temp.setAlignment(Pos.CENTER);
    		temp.setPrefWidth(30);
    		temp.setFont(Font.font("Tahoma", FontWeight.NORMAL, 25)); 
    		temp.setStyle("-fx-border-style: solid;" + 
    				"-fx-border-width: 1;" +
    				"-fx-border-radius: 0;" + 
    				"-fx-border-color: black;"); 
    		stackStringRight.getChildren().add(temp);
    		
    	    Image imgArrow = new Image("images/arrow.png");
    		ImageView tempImageView = new ImageView(imgArrow);
    		tempImageView.setFitHeight(25);
    		tempImageView.setFitWidth(25);
    		tempImageView.setPreserveRatio(true);
    		tempImageView.setSmooth(true);
    		tempImageView.setCache(true);
            
    		Label temp2 = new Label();
    		temp2.setAlignment(Pos.CENTER);
    		temp2.setPrefWidth(30);
    		temp2.setFont(Font.font("Tahoma", FontWeight.NORMAL, 25)); 
    		temp2.setStyle("-fx-border-style: hidden;" + 
    				"-fx-border-width: 1;" +
    				"-fx-border-radius: 0;" + 
    				"-fx-border-color: black;"); 
    		if (i==0) temp2.setGraphic(tempImageView);
    		stringInputCurrentRight.getChildren().add(temp2);
    	}
    }
    
    public static void stackInteract (Character character, int assignment) {
    	if (assignment == 0) {
    		Label temp = new Label(String.valueOf(character));
    		temp.setAlignment(Pos.CENTER);
    		temp.setPrefWidth(30);
    		temp.setFont(Font.font("Tahoma", FontWeight.NORMAL, 25)); 
    		temp.setStyle("-fx-border-style: solid;" + 
    				"-fx-border-width: 1;" +
    				"-fx-border-radius: 0;" + 
    				"-fx-border-color: black;"); 
    		stackItems.add(temp);
    	}
    	else {
    		stackItems.remove(stackItems.size()-1);
    	}
		stackStringLeft.getChildren().clear();
		stringInputCurrentLeft.getChildren().clear();
		for (int i=0; i<stackItems.size();i++) {
			Label temp = new Label(String.valueOf(stackItems.get(i).getText()));
			temp.setAlignment(Pos.CENTER);
			temp.setPrefWidth(30);
			temp.setFont(Font.font("Tahoma", FontWeight.NORMAL, 25)); 
			temp.setStyle("-fx-border-style: solid;" + 
						"-fx-border-width: 1;" +
					"-fx-border-radius: 0;" + 
					"-fx-border-color: black;"); 
			stackStringLeft.getChildren().add(temp);
			
    	    Image imgArrow = new Image("images/arrow.png");
    		ImageView tempImageView = new ImageView(imgArrow);
    		tempImageView.setFitHeight(25);
    		tempImageView.setFitWidth(25);
    		tempImageView.setPreserveRatio(true);
    		tempImageView.setSmooth(true);
    		tempImageView.setCache(true);
            
    		Label temp2 = new Label();
    		temp2.setAlignment(Pos.CENTER);
    		temp2.setPrefWidth(30);
    		temp2.setFont(Font.font("Tahoma", FontWeight.NORMAL, 25)); 
    		temp2.setStyle("-fx-border-style: hidden;" + 
    				"-fx-border-width: 1;" +
    				"-fx-border-radius: 0;" + 
    				"-fx-border-color: black;"); 
    		if (i==stackItems.size()-1) temp2.setGraphic(tempImageView);
    		stringInputCurrentLeft.getChildren().add(temp2);
    	}
    }
    
    public void scrollToBottom() {
    	stringStepsSP.setVvalue(stringStepsSP.getVmax());
    }
    
    public static void toConsoleLog (int type, String text) {
    	// 0 = Info, 1 = Alert, 2 = Error
    	HBox insertedValue = new HBox();
    	insertedValue.setStyle("-fx-border-style: solid hidden hidden hidden;" +
		  					   "-fx-border-width: 1;" +
		  					   "-fx-border-radius: 0;" +
			  				   "-fx-border-color: lightgrey;");
    	
    	Label typeLabel = new Label();
    	typeLabel.prefWidthProperty().bind(insertedValue.widthProperty().divide(4));
    	typeLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 17)); 
    	typeLabel.setStyle("-fx-border-style: hidden solid hidden hidden;" +
				   		   "-fx-border-width: 1;" +
				   		   "-fx-border-radius: 0;" +
				   		   "-fx-border-color: lightgrey;");
    	typeLabel.setAlignment(Pos.CENTER);
    	typeLabel.setPadding(new Insets(3, 5, 3, 5));
    	
    	if (type==0) {
    		typeLabel.setText(getLanguageString("tp_info"));
    		typeLabel.setTextFill(Color.BLUE);
    	}
    	else if (type==1) {
    		typeLabel.setText(getLanguageString("tp_alert"));
    		typeLabel.setTextFill(Color.ORANGE);
    	}
    	else {
    		typeLabel.setText(getLanguageString("tp_error"));
    		typeLabel.setTextFill(Color.RED);
    	}
    	Label stringLabel = new Label(text);
    	stringLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 17)); 
    	HBox.setHgrow(stringLabel, Priority.ALWAYS);
    	stringLabel.setMaxWidth(Double.MAX_VALUE);
    	stringLabel.setAlignment(Pos.CENTER_LEFT);
    	stringLabel.setPadding(new Insets(3, 10, 3, 10));
    	insertedValue.getChildren().addAll(typeLabel, stringLabel);
    	consoleLog.getChildren().add(insertedValue);
    }
    
    public static void startGraphics(String[] args) {
      	launch(args);
    }
}
