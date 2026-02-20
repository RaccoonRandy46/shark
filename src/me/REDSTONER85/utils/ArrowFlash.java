package me.REDSTONER85.utils;

public class ArrowFlash {

	public static final long FLASH_SPEED = 200;
	
	public long time;
	public int x;
	public int y;
	
	public ArrowFlash(int xIn, int yIn) {
		this.x = xIn;
		this.y = yIn;
		this.time = System.currentTimeMillis();
	}
	
	public int flashAlpha() {
		if (this.isFinished()) return 0;
		float mult = (float)(System.currentTimeMillis() - this.time) / FLASH_SPEED;
		mult *= -1;
		mult++;
		return (int)(mult * 255f);
	}
	
	public boolean isFinished() {
		return System.currentTimeMillis() - this.time > FLASH_SPEED;
	}
	
}
