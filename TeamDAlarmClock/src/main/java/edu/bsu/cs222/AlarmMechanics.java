package edu.bsu.cs222;

import java.text.SimpleDateFormat;
import java.util.Date;

import static edu.bsu.cs222.AlarmUI.snoozeButtonHit;
import static edu.bsu.cs222.AlarmUI.dismissButtonHit;
import static edu.bsu.cs222.MainUI.playSound;

class AlarmMechanics {

    private static int playSoundTrueSetOnce=0;
    private SoundPlayer soundPlayer = new SoundPlayer();
    private String userAlarmTime;

    void comparisonLoopInitializer(String timeString){
        userAlarmTime = timeString;
        comparisonLoop.start();
    }

    private boolean enough;
    private Thread comparisonLoop = new Thread(() -> {
        SimpleDateFormat dt = new SimpleDateFormat("h:mm a MMM dd");
        while(!this.enough) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException var3) {
                var3.printStackTrace();
            }
            String time = dt.format(new Date());

            while (time.equals(userAlarmTime)) {
                playSoundTrueSetOnce += 1;
                if (playSoundTrueSetOnce == 1) {
                    boolean playSound = true;
                    while (playSound){
                        soundPlayer.WAVPlayer();
                        if(snoozeButtonHit==true)
                        {
                            break;
                        }
                        if(dismissButtonHit== true){
                            break;
                        }
                    }
                }
                break;
            }
        }
    });
}