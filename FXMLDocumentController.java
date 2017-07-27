/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myplayer;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author rahulsehrawat
 */
public class FXMLDocumentController implements Initializable {
   @FXML
    private MediaView mediaView;
    
    @FXML private Button play;
     @FXML private Label Time;
     @FXML private Slider volume;
     @FXML private Slider timeSlider; 
     @FXML private Button openFile;
     private boolean endOfMEdia = false;
     
    private javafx.util.Duration duration;
    private javafx.util.Duration currentTime;
    
    private  MediaPlayer mediaPlayer;
    private boolean fullScreen = false;
    @FXML Pane pane;
    
    String filePath = "1.mp4";
    private boolean start = true;
    
    @FXML
    private void chooseFile(ActionEvent event)
    {
       
       
        FileChooser chooser = new FileChooser();
    chooser.setTitle("Open File");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP4", "*.mp4"),
                new FileChooser.ExtensionFilter("MP3", "*.mp3"),
                 new FileChooser.ExtensionFilter("flv", "*.flv")
        );
         File file = chooser.showOpenDialog(new Stage());
         
            
         if(file!=null)
         {
             mediaPlayer.dispose();
             filePath = file.getAbsolutePath().toString();
            startApp();
         }
         
            
    }
    
    
    @FXML
    private void play(ActionEvent event) {
        
        if(filePath=="")
        {
            Effect effect = openFile.getEffect();
        }
        
        if(play.getText().equals("Play"))
        {
            
            if(endOfMEdia)
            {   
                mediaPlayer.seek(new javafx.util.Duration(0));
                updateValues();
                
            }
            else{
                mediaPlayer.play();
              play.setText("||");
            }
        }
        else{
           mediaPlayer.pause();
 
           play.setText("Play");
        }
       
    }
    
    
    
    
    @FXML
    private void fullScreen(ActionEvent event){
        Myplayer.getStage().setFullScreen(!Myplayer.getStage().isFullScreen());
    } 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
         
        startApp();
        
//        timeSlider.setDisable(duration.isUnknown());
        
    }    
    
    private void startApp()
    {
        
            
       
        
        Media media = new Media(new File(filePath).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setAutoPlay(!start);
            start = false;
            volume.setValue(100);
            mediaPlayer.setVolume(volume.getValue()/100);
                 mediaView.setMediaPlayer(mediaPlayer);
                 
        volume.valueProperty().addListener((Observable observable) -> {
            mediaPlayer.setVolume(volume.getValue() / 100);
        });
        
        timeSlider.valueProperty().addListener(new ChangeListener<Number>(){
             @Override
             public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                 if(timeSlider.isValueChanging())
                 {
                     if(duration!=null)
                     {
                         mediaPlayer.seek(duration.multiply(timeSlider.getValue()/100));
                         updateValues();
                     }
                 }
             }
            
        });
         
       
        
        
        pane.setStyle("-fx-background-color: #000; ");
//        mediaPlayer.setRate();
        
        mediaPlayer.setOnReady(new Runnable() {
                public void run() {
                    duration = mediaPlayer.getMedia().getDuration();
                }
            });
        
        
        mediaPlayer.setOnPlaying(new Runnable() {
             @Override
             public void run() {
                 endOfMEdia = false;
                 play.setText("||");
                 timeSlider.setDisable(duration.isUnknown());
                 updateValues();
             }
         });
        
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<javafx.util.Duration>(){
             @Override
             public void changed(ObservableValue<? extends javafx.util.Duration> observable, javafx.util.Duration oldValue, javafx.util.Duration newValue) {
                 play.setText("||");
                updateValues();
                 
             }
            
        });
        
        mediaPlayer.setOnEndOfMedia(new Runnable() {
             @Override
             public void run() {
                 endOfMEdia = true;
                 play.setText("Play");
             }
         });
        
             DoubleProperty mvw = mediaView.fitWidthProperty();
                DoubleProperty mvh = mediaView.fitHeightProperty();
                mvw.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
                mvh.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
       
    }
    
    private void updateValues()
    {
                currentTime = mediaPlayer.getCurrentTime();
                 Time.setText(formatTime(currentTime, duration));
                 if(!timeSlider.isDisabled())
                     timeSlider.setValue(currentTime.divide(duration).toMillis() * 100.0);
    }
    
    private String formatTime(javafx.util.Duration elapsed, javafx.util.Duration duration) {
            int intElapsed = (int)Math.floor(elapsed.toSeconds());
            int elapsedHours = intElapsed / (60 * 60);
            if (elapsedHours > 0) {
                intElapsed -= elapsedHours * 60 * 60;
            }
            int elapsedMinutes = intElapsed / 60;
            int elapsedSeconds = intElapsed - elapsedHours * 60 * 60 - elapsedMinutes * 60;

            if (duration.greaterThan(javafx.util.Duration.ZERO)) {
                int intDuration = (int)Math.floor(duration.toSeconds());
                int durationHours = intDuration / (60 * 60);
                if (durationHours > 0) {
                    intDuration -= durationHours * 60 * 60;
                }
                int durationMinutes = intDuration / 60;
                int durationSeconds = intDuration - durationHours * 60 * 60 - durationMinutes * 60;

                if (durationHours > 0) {
                    return String.format("%d:%02d:%02d/%d:%02d:%02d",
                                         elapsedHours, elapsedMinutes, elapsedSeconds,
                                         durationHours, durationMinutes, durationSeconds);
                } else {
                    return String.format("%02d:%02d/%02d:%02d",
                                         elapsedMinutes, elapsedSeconds,
                                         durationMinutes, durationSeconds);
                }
            } else {
                if (elapsedHours > 0) {
                    return String.format("%d:%02d:%02d",
                                         elapsedHours, elapsedMinutes, elapsedSeconds);
                } else {
                    return String.format("%02d:%02d",
                                         elapsedMinutes, elapsedSeconds);
                }
            }
        }
    
    
}
