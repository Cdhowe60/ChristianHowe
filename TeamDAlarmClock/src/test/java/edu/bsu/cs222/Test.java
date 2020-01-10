package edu.bsu.cs222;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.Assert;

import javax.speech.AudioException;
import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.swing.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static edu.bsu.cs222.MainUI.soundPlayer;

public class Test {
    public static boolean testStringRead = false;
    private boolean enough;
    private JLabel txtTime;
    private static String precipitationIntensity = "none";

    @org.junit.Test
    public void soundTester() {
        SoundPlayer soundPlayer = new SoundPlayer();
        soundPlayer.WAVPlayer();
    }

    // TODO: 12/5/2019
    @org.junit.Test
    public void alarmTester() {
        SimpleDateFormat dt = new SimpleDateFormat("h:mm a MMM dd");
        String currentTime = dt.format(new Date());
        AlarmMechanics alarmMechanics = new AlarmMechanics();
        alarmMechanics.comparisonLoopInitializer(currentTime);
    }

    @org.junit.Test
    public void DateFormatTest() {
        SimpleDateFormat dt = new SimpleDateFormat();
        Thread liveClock = new Thread(() -> {
            while (!this.enough) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException ignored) {
                }
                String time = dt.format(new Date());
                Platform.runLater(() -> {
                    this.txtTime.setText(time);
                    Assert.assertEquals(dt, time);
                });
            }
        });
    }

    @org.junit.Test
    public void timerAndSnoozeTest() {
        int secs = 0;
        int mins = 0;
        Timeline timeline = new Timeline();
        if (secs == -1) {
            if (mins > 0) {
                mins--;
                secs = 59;
            }
        }
        if (mins == -1) {
            mins = 0;
        }
        if (mins == 0 && secs == -1) {
            timeline.pause();
            boolean playSound = true;
            while (playSound) {
                for (int i = 0; i < 1; i++) soundPlayer.WAVPlayer();
                break;
            }
        }
    }

    @org.junit.Test
    private void MainUI(Stage stage){
        MainUI mainUI = new MainUI();
        mainUI.start(stage);
    }

    @org.junit.Test
    public void checkConnection() {
        try {
            URL url = new URL("http://dataservice.accuweather.com/forecasts/v1/daily/1day/348735?apikey=j2VWivDN4bmnna3ardSnsNNHW2ACzf7j");
            URLConnection connection = url.openConnection();
            connection.connect();
            System.out.println("Connection Successful");
        } catch (Exception e) {
            System.out.println("Connection Failed");
        }
    }

    @org.junit.Test
    public void TTSTest() {
        TTS tts = new TTS();
        String testString = "This is a test.";
        try {
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us" + ".cmu_us_kal.KevinVoiceDirectory");
            Central.registerEngineCentral("com.sun.speech.freetts" + ".jsapi.FreeTTSEngineCentral");
            Synthesizer synthesizer = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));
            synthesizer.allocate();
            synthesizer.resume();
            synthesizer.speakPlainText(testString, null);
            synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
            synthesizer.deallocate();
        } catch (AudioException | EngineException | InterruptedException e) {
            e.printStackTrace();

        }
    }

    @org.junit.Test
    public void WeatherAPIGetDataAsJsonArrayTest() {
        WeatherAPI weatherAPI = new WeatherAPI();
        weatherAPI.APIAccess();
    }

    @org.junit.Test
    public void findTemp() {
        WeatherAPI weatherAPI = new WeatherAPI();
        Assert.assertNotNull(weatherAPI.printTemp());
    }

    @org.junit.Test
    public void findChanceOfRain() {
        WeatherAPI weatherAPI = new WeatherAPI();
        Assert.assertNotNull(weatherAPI.printTemp());
    }

    @org.junit.Test
    public void findPrecipitationType() {
        WeatherAPI weatherAPI = new WeatherAPI();
        Assert.assertNotNull(weatherAPI.printPrecipitationType());
    }

    @org.junit.Test
    public void findPrecipitationIntensity() {
        WeatherAPI weatherAPI = new WeatherAPI();
        Assert.assertNotNull(weatherAPI.printPrecipitationIntensity());
    }

    @org.junit.Test
    public void tempParser() {
        InputStream inputstream = getClass().getClassLoader().getResourceAsStream("1hour.json");
        assert inputstream != null;
        Reader reader = new InputStreamReader(inputstream);
        JsonParser jsonParser = new JsonParser();
        JsonElement rootElement = jsonParser.parse(reader);
        JsonArray jsonArray = (JsonArray) jsonParser.parse(String.valueOf(rootElement));
        Assert.assertNotNull(jsonArray);
        String jsonString = jsonArray.toString();
        int tempIndex = jsonString.indexOf("Value");
        String temperature = jsonString.substring(tempIndex + 7, tempIndex + 11);
        System.out.println(temperature);
    }

    @org.junit.Test
    public void precipitationProbabilityParser() {
        InputStream inputstream = getClass().getClassLoader().getResourceAsStream("1hour.json");
        assert inputstream != null;
        Reader reader = new InputStreamReader(inputstream);
        JsonParser jsonParser = new JsonParser();
        JsonElement rootElement = jsonParser.parse(reader);
        JsonArray jsonArray = (JsonArray) jsonParser.parse(String.valueOf(rootElement));
        Assert.assertNotNull(jsonArray);
        String jsonString = jsonArray.toString();
        int precProbability = jsonString.indexOf("Probability");
        String chanceOfRain = jsonString.substring(precProbability + 13, precProbability + 15) + " percent";
        System.out.println(chanceOfRain);
    }

    @org.junit.Test
    public void precipitationTypeParser() {
        InputStream inputstream = getClass().getClassLoader().getResourceAsStream("1hour.json");
        assert inputstream != null;
        Reader reader = new InputStreamReader(inputstream);
        JsonParser jsonParser = new JsonParser();
        JsonElement rootElement = jsonParser.parse(reader);
        JsonArray jsonArray = (JsonArray) jsonParser.parse(String.valueOf(rootElement));
        Assert.assertNotNull(jsonArray);
        String jsonString = jsonArray.toString();
        int precType = jsonString.indexOf("tationType");
        String precipitationTypeStarter = jsonString.substring(precType + 13, precType + 15);
        String precipitationType;
        switch (precipitationTypeStarter) {
            case "Ra":
                precipitationType = "Rain";
                break;
            case "Sl":
                precipitationType = "Sleet";
                break;
            case "Ha":
                precipitationType = "Hail";
                break;
            case "Sn":
                precipitationType = "Snow";
                break;
            default:
                precipitationType = "None";
                break;
        }
        System.out.println(precipitationType);
    }

    @org.junit.Test
    public void intensityParser() {
        InputStream inputstream = getClass().getClassLoader().getResourceAsStream("1hour.json");
        assert inputstream != null;
        Reader reader = new InputStreamReader(inputstream);
        JsonParser jsonParser = new JsonParser();
        JsonElement rootElement = jsonParser.parse(reader);
        JsonArray jsonArray = (JsonArray) jsonParser.parse(String.valueOf(rootElement));
        Assert.assertNotNull(jsonArray);
        String jsonString = jsonArray.toString();
        int precIntensity = jsonString.indexOf("Intensity");
        String precipitationIntensityStarter = jsonString.substring(precIntensity + 12, precIntensity + 14);
        if (precipitationIntensityStarter.equals("Li")) {
            precipitationIntensity = "Light";
        }
        if (precipitationIntensityStarter.equals("He")) {
            precipitationIntensity = "Heavy";
        }
        if (precipitationIntensityStarter.equals("Me")) {
            precipitationIntensity = "Medium";
        }
        System.out.println(precipitationIntensity);
    }

    @org.junit.Test
    public void testInputStream() {
        InputStream inputstream = getClass().getClassLoader().getResourceAsStream("1hour.json");
        Assert.assertNotNull(inputstream);
    }
}