package me.REDSTONER85;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;

import me.REDSTONER85.screens.GameFrame;
import me.REDSTONER85.screens.GuiScreen;
import me.REDSTONER85.screens.MainMenuGui;
import me.REDSTONER85.utils.AudioManager;
import me.REDSTONER85.utils.TextureBin;
import me.REDSTONER85.utils.settings.Settings;

public class Shark {

	public static GameFrame frame;
	public static GuiScreen currentScreen;
	
	public static void main(String[] args) {
		
		TextureBin.init();
		Settings.updateAll();
		AudioManager.startSongLoop();
		
		frame = new GameFrame();
		currentScreen = new MainMenuGui();
		
		gameLoop();
		
	}
	
    private static void gameLoop() {
    	
        final int fps = Math.max(1, Settings.maxFrameRate);
        final long frameTimeNanos = 1000000000L / fps;

        BufferStrategy strategy = null;

        while (true) {
        	
            long frameStart = System.nanoTime();

            if (strategy == null) {
                strategy = frame.getBufferStrategy();
                if (strategy == null) {
                    frame.createBufferStrategy(3);
                    continue;
                }
            }

            Graphics2D g = null;
            
            try {
                g = (Graphics2D)strategy.getDrawGraphics();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                frame.frameLoop(g);
            } catch (Exception e) {
                System.out.println("it farded");
                strategy = null;
            } finally {
                if (g != null) g.dispose();
            }

            if (strategy != null) strategy.show();

            long frameEnd = System.nanoTime();
            long elapsed = frameEnd - frameStart;
            long sleepNanos = frameTimeNanos - elapsed;

            if (sleepNanos <= 0) continue;
            
            try {
                long sleepMillis = sleepNanos / 1000000L;
                int sleepExtraNanos = (int)(sleepNanos % 1000000L);
                Thread.sleep(sleepMillis, sleepExtraNanos);
            } catch (Exception E) {}
            
        }
    }
	
}
