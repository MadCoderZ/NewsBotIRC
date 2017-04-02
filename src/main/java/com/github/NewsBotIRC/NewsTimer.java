package com.github.NewsBotIRC;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Gerardo Canosa on 9/6/16
 */
public class NewsTimer
{
        private final Timer timer;
        private final int seconds;

        public NewsTimer(int seconds)
        {
            this.seconds = seconds;
            timer = new Timer();
        }

        public void addTask(TimerTask task)
        {
            this.timer.schedule(task, this.seconds * 1000, this.seconds * 1000);
        }
}