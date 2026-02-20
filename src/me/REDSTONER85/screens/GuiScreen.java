package me.REDSTONER85.screens;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import me.REDSTONER85.Shark;

public abstract class GuiScreen {
	
	public static final double RATIO = 622d / 395d;
	
	public static final int _ORIGIN_X = 3;
	public static final int _ORIGIN_Y = 26;
	public static final int _WIDTH = 616 + _ORIGIN_X;
	public static final int _HEIGHT = 392 + _ORIGIN_Y;
	
	public abstract void drawFrame(Graphics2D g, double scaler);
	
	public void keyTyped(KeyEvent e) {}

	public void keyPressed(KeyEvent e) {
		System.out.println(e.getKeyCode());
	}

	public void keyReleased(KeyEvent e) {}

	public void mouseClicked(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}
	
	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}
	
	private static double currentRatio() {
		
		return (double)(Shark.frame.getWidth()) / (Shark.frame.getHeight() - _ORIGIN_Y);
		
	}
	
	public static double scaler() {
		
		return width() / ((double)_WIDTH - _ORIGIN_X);
		
	}
	
	public static int width() {
		
		if (currentRatio() <= RATIO) {
			return Shark.frame.getWidth() - 3;
		}
		
		return (int)(Shark.frame.getHeight() * RATIO) - 3;
		
	}
	
	public static int height() {
		
		if (currentRatio() >= RATIO) {
			return Shark.frame.getHeight() - 26;
		}
		
		return (int)(Shark.frame.getWidth() / RATIO) - 26;
		
	}
	
	public static int originX() {
		
		if (currentRatio() <= RATIO) {
			return _ORIGIN_X;
		}
		
		return (int)(Shark.frame.getWidth() / 2d - width() / 2d) + 3;
		
	}
	
	public static int originY() {
		
		if (currentRatio() >= RATIO) {
			return _ORIGIN_Y;
		}
		
		return (int)(Shark.frame.getHeight() / 2d - height() / 2d) + 26;
		
	}
	
	public static int windowWidth() {
		return Shark.frame.getWidth();
	}
	
	public static int windowHeight() {
		return Shark.frame.getHeight();
	}
	
}
