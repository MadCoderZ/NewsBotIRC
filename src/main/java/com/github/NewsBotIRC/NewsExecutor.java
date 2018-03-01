package com.github.NewsBotIRC;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by Gerardo Canosa on 9/6/16
 */
public class NewsExecutor
{
        private final ScheduledExecutorService executor;
        private final int seconds;

        public NewsExecutor(int seconds)
        {
            this.seconds = seconds;
            this.executor = Executors.newSingleThreadScheduledExecutor();
        }

        public void addTask(Runnable task)
        {
            this.executor.scheduleAtFixedRate(task, this.seconds, this.seconds, SECONDS);
        }
}