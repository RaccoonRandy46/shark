package me.REDSTONER85.screens;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

import me.REDSTONER85.Shark;
import me.REDSTONER85.utils.AudioManager;
import me.REDSTONER85.utils.TextureBin;
import me.REDSTONER85.utils.settings.Keybinds;

public class GameFrame extends JFrame implements MouseListener, KeyListener {

	private static final long serialVersionUID = 1L;
	
	private boolean isMdown = false;
	public boolean isMouseDown = false;
	public boolean isMouseInFrame = false;

	public GameFrame() {
		this.setBounds(100, 100, 1326, 842);
		//this.setResizable(false);
		this.getContentPane().setBackground(new Color(0, 0, 0));
		this.setTitle("shark");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addMouseListener(this);
		this.addKeyListener(this);
		this.setIconImage(TextureBin.SHARK);
		this.setIgnoreRepaint(true);
		this.setVisible(true);
	}

	public void frameLoop(Graphics2D g) {
		
		Shark.currentScreen.drawFrame(g, GuiScreen.scaler());
		
	}
	
	public int mouseX() {
		Point pos = this.getMousePosition();
		if (pos == null) return -1;
		return pos.x;
	}
	
	public int mouseY() {
		Point pos = this.getMousePosition();
		if (pos == null) return -1;
		return pos.y;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		Shark.currentScreen.keyTyped(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == Keybinds.getKeybind("mute")) {
			if (!this.isMdown) AudioManager.toggleMusic();
			this.isMdown = true;
		}
		Shark.currentScreen.keyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == Keybinds.getKeybind("mute")) this.isMdown = false;
		Shark.currentScreen.keyReleased(e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Shark.currentScreen.mouseClicked(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.isMouseDown = true;
		Shark.currentScreen.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.isMouseDown = false;
		Shark.currentScreen.mouseReleased(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		this.isMouseInFrame = true;
		Shark.currentScreen.mouseEntered(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		this.isMouseInFrame = false;
		Shark.currentScreen.mouseExited(e);
	}
	
}
