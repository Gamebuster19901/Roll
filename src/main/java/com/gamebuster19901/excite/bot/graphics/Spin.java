package com.gamebuster19901.excite.bot.graphics;

import java.util.concurrent.ThreadLocalRandom;

public enum Spin {
	horizontal,
	vertical,
	diagonal;
	
	public static Spin getSpin(int i) {
		return values()[i % values().length];
	}
	
	public static Spin randSpin() {
		return getSpin(ThreadLocalRandom.current().nextInt(values().length));
	}
}
