import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class MainSceneController {

    @FXML
    private Button start;

    @FXML
    private Button Back;

    @FXML
    private Button Enter;

    @FXML
    private GridPane GridPaneBottomKeys;

    @FXML
    public GridPane GridPaneLabel;

    @FXML
    private GridPane GridPaneMiddleKeys;

    @FXML
    private GridPane GridPaneTopKeys;

    @FXML
    private Label L0;

    @FXML
    private Label L29;

    @FXML
    private TitledPane statsPane;

    @FXML
    private AnchorPane statsAnchorPane;

    @FXML
    private Label firstG;

    @FXML
    private Label secondG;

    @FXML
    private Label thirdG;

    @FXML
    private Label fourthG;

    @FXML
    private Label fifthG;

    @FXML
    private Label sixthG;

    @FXML
    private Label currentStreak;

    @FXML
    private Label maxStreak;

    @FXML
    private Label played;

    @FXML
    private Label winPercent;

    @FXML
    private Button playAgainBtn;

    @FXML
    private Label hiddenWord;

    @FXML
    private Label notValidWordLabel;

    @FXML
    private Label CongratsGameOver;

    @FXML
    private Button saveBtn;

    @FXML
    private Button resetBtn;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label Title;

    @FXML
    private GridPane backgroundGrid;

    int plays = 0;
    int wins = 0;
    double winsPercent = 0;
    int currentStreakNum = 0;
    int maxStreakNum = 0; //keep highest streak

    String targetWord = "";
    List<String> listOfWords = new ArrayList<>();
    List<String> wordTries = new ArrayList<String>(); //max 6 words/elements
    String[] guessDistribution = null;

    void ListOfWordsInitializer(){
        Scanner scn = null;
        try {
            scn = new Scanner(new File("src/listOfWords.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();  
        }
        while (scn.hasNextLine()) {
            String line = scn.nextLine();
            for(String word : line.split("\\s")){
                if(word.equals("")){
                    continue;
                }
                listOfWords.add(word);
            }
        }
        scn.close();
        targetWordSelector();
    }

    void targetWordSelector(){
        Random rand = new Random();
        targetWord = listOfWords.get(rand.nextInt(listOfWords.size()));
        System.out.println("TARGET WORD: " + targetWord);
    }

    void showStats(){
        loadData(); //updates the data to display in scoreBoard

        firstG.setText(guessDistribution[0]);
        secondG.setText(guessDistribution[1]);
        thirdG.setText(guessDistribution[2]);
        fourthG.setText(guessDistribution[3]);
        fifthG.setText(guessDistribution[4]);
        sixthG.setText(guessDistribution[5]);

        played.setText(Integer.toString(plays));
        DecimalFormat df = new DecimalFormat("#.#");        
        winsPercent = (wins/(Double.valueOf(plays))) * 100.0;
        winPercent.setText(df.format(winsPercent) + "%");
        currentStreak.setText(Integer.toString(currentStreakNum));
        maxStreak.setText(Integer.toString(maxStreakNum));
        statsAnchorPane.setStyle("-fx-background-color: #AED6DC");
        statsPane.setVisible(true);
        modifyKeys(true);
        Back.setDisable(true);
        Enter.setDisable(true);
        playAgainBtn.setVisible(true);
        
    }

    @FXML
    void startBtnClicked(ActionEvent event){
        //everytime you start a new game -> you load the file, at the end of every game or if saveBtn clicked -> you save data to file (update file)
        wordTries.clear();
        modifyKeys(false);
        buttonsToBeDisabled.clear();
        GridPaneLabel.setVisible(true);
        GridPaneTopKeys.setVisible(true);
        GridPaneMiddleKeys.setVisible(true);
        GridPaneBottomKeys.setVisible(true);
        saveBtn.setVisible(true);
        resetBtn.setVisible(true);
        start.setVisible(false);
        ListOfWordsInitializer();
        statsPane.setVisible(false);
        playAgainBtn.setVisible(false);
        notValidWordLabel.setVisible(false);
        Title.setVisible(false);
        //clear gridPaneLabel:
        for(Node node: GridPaneLabel.getChildren()){
            if(GridPane.getColumnIndex(node) == null){
                break;
            }            
            ((Label)node).setText("");
            ((Label)node).setTextFill(Color.BLACK);
            ((Label)node).setDisable(false);
            ((Label)node).setStyle("-fx-background-color: #E9D2C9");
        }
        anchorPane.setStyle("-fx-background-color: #4A536B");

        for( Node node : backgroundGrid.getChildren()){
            if(GridPane.getColumnIndex(node) == null){
                break;
            }
            ((Label)node).setVisible(false);
        }

        loadData(); //reads from file and puts into variables
        saveData(); //writes to files with updated values
    }

    @FXML
    void resetFunc(ActionEvent event){
        buttonsToBeDisabled.clear();
        modifyKeys(false);
        loadData();
        targetWordSelector();
        wordTries.clear();
        saveData();
        for(Node node: GridPaneLabel.getChildren()){
            if(GridPane.getColumnIndex(node) == null){
                break;
            }            
            ((Label)node).setText("");
            ((Label)node).setTextFill(Color.BLACK);
            ((Label)node).setDisable(false);
        }
    }
    
    void loadData(){ //read from file using Scanner class
        Scanner scn = null;
        try {
            scn = new Scanner(new File("src/loadData.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();  
        }

        plays = Integer.valueOf(scn.nextLine());
        System.out.println("file plays: " + plays);

        wins = Integer.valueOf(scn.nextLine());
        System.out.println("file wins: " + wins);

        currentStreakNum = Integer.valueOf(scn.nextLine());
        System.out.println("file currentStreakNum: " + currentStreakNum);

        maxStreakNum = Integer.valueOf(scn.nextLine());
        System.out.println("file maxStreakNum: " + maxStreakNum);

        String line = "";
        line = scn.nextLine();
        guessDistribution = line.split(" ");
        System.out.println("file guessDistributionLine: " + line);            

        line = "";
        if(scn.hasNextLine()){
            line = scn.nextLine();
            targetWord = line;
        }

        line = "";
        if(scn.hasNextLine()){
            line = scn.nextLine();
            String[] wordTriesArray = line.split(" ");
            for( String wordTry : wordTriesArray ){
                wordTries.add(wordTry);
            }          
            //implement gridPane initilization with words from loadFile:  
            for(int i = 0; i < wordTries.size(); ++i){
                String temp = wordTries.get(i);
                for(int j = 0; j < 5; ++j){
                    for( Node node: GridPaneLabel.getChildren()){
                        if(GridPane.getColumnIndex(node) == null){
                            break;
                        }
                        if(GridPane.getColumnIndex(node) == j && GridPane.getRowIndex(node) == i){
                            ((Label)node).setText(Character.toString(temp.charAt(j)));
                            ((Label)node).setDisable(true);
                            break;
                        }
                    }  
                }
                colorLetters(temp.toLowerCase(), i);
            }
        }
        else{
            System.out.println("No Word Tries to load");
        }
        scn.close();
    }

    @FXML
    void saveDataFunc(ActionEvent event){
        saveData();
    }

    void saveData(){ //write to file using FileWriter
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("src/loadData.txt");
            fileWriter.write(plays + "\n");
            fileWriter.write(wins + "\n");
            fileWriter.write(currentStreakNum + "\n");
            fileWriter.write(maxStreakNum + "\n");
            String line = "";
            for(String guess : guessDistribution){
                line = line + guess + " ";
            }
            fileWriter.write(line + "\n"); //guessDistribution line

            if(!targetWord.equals("")){ //!
                fileWriter.write(targetWord + "\n"); //targetWord line
            }

            line = "";
            if(wordTries.size() > 0){
                for(String wordTry : wordTries){
                    line = line + wordTry + " ";
                }
                fileWriter.write(line + "\n"); //use write because you want data to be overwritten                
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    List<String> buttonsToBeDisabled = new ArrayList<>();

    void colorLetters(String word, int wordSize){
        for(int i = 0; i < targetWord.length(); ++i){
            if(!targetWord.contains(Character.toString(word.charAt(i)))){
                buttonsToBeDisabled.add(Character.toString(word.charAt(i)).toUpperCase());
            }
            if(word.indexOf(targetWord.charAt(i)) != -1 && word.charAt(i) == targetWord.charAt(i)){ //GREEN
                for(Node node : GridPaneLabel.getChildren()){ //to iterate through labels
                    if(GridPane.getColumnIndex(node) == null){
                        break;
                    }
                    if(GridPane.getColumnIndex(node) == i && GridPane.getRowIndex(node) == wordSize){
                        ((Label)node).setTextFill(Color.color(0.03, 0.87, 0.03));
                    }
                }
            }
            else if( (word.indexOf(targetWord.charAt(i)) != -1) && (word.charAt(i) != targetWord.charAt(i))){ //YELLOW
                for(Node node : GridPaneLabel.getChildren()){
                    if(GridPane.getColumnIndex(node) == null){
                        break;
                    }
                    if(GridPane.getColumnIndex(node) == word.indexOf(targetWord.charAt(i)) && GridPane.getRowIndex(node) == wordSize){
                        ((Label)node).setTextFill(Color.color(1, 1, 0.2)); //color based on letter found not location
                    }
                }
            }
        }
        disableKeys(buttonsToBeDisabled, true);
    }

    void disableKeys(List<String> disabledKeys, boolean state){
        for(int j = 0; j < disabledKeys.size(); ++j){
            for(Node node: GridPaneTopKeys.getChildren()){
                if(((Button)node).getText().equals(disabledKeys.get(j))){
                    ((Button)node).setDisable(state); //true
                }
            }
            for(Node node: GridPaneMiddleKeys.getChildren()){
                if(((Button)node).getText().equals(disabledKeys.get(j))){
                    ((Button)node).setDisable(state); //true
                }
            }
            for(Node node: GridPaneBottomKeys.getChildren()){
                if(((Button)node).getText().equals(disabledKeys.get(j))){
                    ((Button)node).setDisable(state); //true
                }
            }
        }
    }

    void checkLetters(String word){
        if(word.equals(targetWord)){
            colorLetters(word, wordTries.size());
            wins++;
            plays++;
            currentStreakNum++;

            String temp = guessDistribution[wordTries.size()];//-1 //POSSIBLE PROBLEM AREA - CHECK ME
            int tempNum = Integer.valueOf(temp);
            tempNum++;
            guessDistribution[wordTries.size()] = Integer.toString(tempNum);
            hiddenWord.setText(targetWord);
            targetWord = "";
            wordTries.clear();
            saveData();

            CongratsGameOver.setText("You Won!");
            showStats();
            return;
        }
        else{ //check letters and update graphics yellow/green
            colorLetters(word, wordTries.size());
        }

        if(wordTries.size() == 5 && !word.equals(targetWord)){
            CongratsGameOver.setText("Game Over!");
            plays++;
            if(currentStreakNum > maxStreakNum){
                maxStreakNum = currentStreakNum;
            }
            currentStreakNum = 0;
            wordTries.clear();
            hiddenWord.setText(targetWord);
            targetWord = "";
            saveData();
            showStats();
        }
    }

    List<Integer> positionXY = new ArrayList<>();
    List<Integer> findCurrIndex() {
        positionXY.clear();
        for(Node node: GridPaneLabel.getChildren()){
            if(((Label)node).getText().isEmpty()){
                positionXY.add(GridPane.getColumnIndex(node));
                positionXY.add(GridPane.getRowIndex(node));
                break;
            }
        }
        return positionXY;
    }

    void modifyKeys(boolean state){
        for(Node TopButton: GridPaneTopKeys.getChildren()){
            ((Button)TopButton).setDisable(state);
        }

        for(Node MiddleButton: GridPaneMiddleKeys.getChildren()){
            ((Button)MiddleButton).setDisable(state);
        }

        for(Node BottomButton: GridPaneBottomKeys.getChildren()){
            //except for Back key and Enter key
            if(!(GridPane.getColumnIndex(BottomButton) == GridPane.getColumnIndex(Back) && GridPane.getRowIndex(BottomButton) == GridPane.getRowIndex(Back))){
                ((Button)BottomButton).setDisable(state);
                if(GridPane.getColumnIndex(BottomButton) == GridPane.getColumnIndex(Enter) && GridPane.getRowIndex(BottomButton) == GridPane.getRowIndex(Enter)){
                    ((Button)BottomButton).setDisable(false);
                }
            }
        }
    }

    void colorKeys(String hexCode){
        for(Node TopButton: GridPaneTopKeys.getChildren()){
            ((Button)TopButton).setStyle(hexCode);
        }

        for(Node MiddleButton: GridPaneMiddleKeys.getChildren()){
            ((Button)MiddleButton).setStyle(hexCode);
        }

        for(Node BottomButton: GridPaneBottomKeys.getChildren()){
            ((Button)BottomButton).setStyle(hexCode);
        }
    }

    @FXML
    void BtnOnClicked(ActionEvent event) {
            List<Integer> positionCurr = findCurrIndex();
            String text = ((Button)event.getSource()).getText();

            for (Node node : GridPaneLabel.getChildren()) {
                if(GridPane.getColumnIndex(node) == positionCurr.get(0) && GridPane.getRowIndex(node) == positionCurr.get(1)){
                    ((Label)node).setText(text);
                    ((Label)node).setTextFill(Color.BLACK);
                        if(GridPane.getColumnIndex(node) == 4 && GridPane.getRowIndex(node) == wordTries.size()){    
                            modifyKeys(true);
                            Enter.setDisable(false);
                        }
                    break;
                }
            }
            if(!(L0.getText().isEmpty())){
                Back.setDisable(false);
            }
    }
    
    @FXML
    void BackBtnClicked(ActionEvent event){
        //if(buttonsToBeDisabled.size() == 0){
            modifyKeys(false);
            if(buttonsToBeDisabled.size() > 0){
                disableKeys(buttonsToBeDisabled, true);
            }
        //}


        notValidWordLabel.setVisible(false);
        List<Integer> positionCurr = findCurrIndex();
        if(((wordTries.size() >= 1) && (positionCurr.get(0) == 0) && (positionCurr.get(1)) == wordTries.size())){
            Back.setDisable(true);
            return;
        }
        else{
            Back.setDisable(false);
        }        
        
        if(!(L29.getText().isEmpty())){ //All full -> can't find "" node so all full, no elements pushed to positionCurr. 
            L29.setText("");
            return;
        }

        if(positionCurr.get(0) == 0){ //column 0 / check test case 0,0
            for(Node node : GridPaneLabel.getChildren()){
                if(GridPane.getColumnIndex(node) == 4 && GridPane.getRowIndex(node) == (positionCurr.get(1) - 1)){
                    ((Label)node).setText("");
                    break;
                }
            }
        }
        else{
            for(Node node : GridPaneLabel.getChildren()){
                if(GridPane.getColumnIndex(node) == (positionCurr.get(0)-1) && GridPane.getRowIndex(node) == positionCurr.get(1)){
                    ((Label)node).setText("");
                    break;
                }
            }
        }
        
        if(L0.getText().isEmpty()){// => grid empty
            Back.setDisable(true);
        }
        Enter.setDisable(true);
    }
        
    @FXML
    void EnterBtnClicked(ActionEvent event){
        modifyKeys(false);
        String tempWord = "";
        for(Node node : GridPaneLabel.getChildren()){
            if(GridPane.getColumnIndex(node) == null){
                break;
            }
            if(GridPane.getRowIndex(node) == wordTries.size()){
                tempWord = tempWord + ((Label)node).getText();
            }
        }

        if(listOfWords.contains(tempWord.toLowerCase())){
            checkLetters(tempWord.toLowerCase());  
            for(Node node : GridPaneLabel.getChildren()){
                if(GridPane.getColumnIndex(node) == null){
                    break;
                }
                if(GridPane.getRowIndex(node) == wordTries.size()){
                    ((Label)node).setDisable(true);
                }
            } 
        }
        else{
            notValidWordLabel.setVisible(true);
            modifyKeys(true);
            return;
        }
        wordTries.add(tempWord);
        Enter.setDisable(true);
    }
}
