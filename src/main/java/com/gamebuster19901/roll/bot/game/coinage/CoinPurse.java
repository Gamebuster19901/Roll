package com.gamebuster19901.roll.bot.game.coinage;

import com.gamebuster19901.roll.bot.game.Statted;
import com.gamebuster19901.roll.bot.game.character.Stat;

public interface CoinPurse {

	public int getCopper();
	public int getSilver();
	public int getElectrum();
	public int getGold();
	public int getPlatinum();
	
	public default void addCopper(int amount) {
		addCoinage(new Coinage(Coin.COPPER, amount));
	}
	
	public default void addSilver(int amount) {
		addCoinage(new Coinage(Coin.SILVER, amount));
	}
	
	public default void addElectrum(int amount) {
		addCoinage(new Coinage(Coin.ELECTRUM, amount));
	}
	
	public default void addGold(int amount) {
		addCoinage(new Coinage(Coin.GOLD, amount));
	}
	
	public default void addPlatinum(int amount) {
		addCoinage(new Coinage(Coin.PLATINUM, amount));
	}
	
	public void addCoinage(Coinage... coinages);
	
	public void removeCoinage(Coinage... coinages);
	
	public void setCoinage(Coinage... coinages);
	
	public default boolean canRemoveCoinageLiberally(Coinage... coinages) {
		return asCopper().getAmount() >= of(coinages).asCopper().getAmount();
	}
	
	public default boolean canRemoveCoinageStrictly(Coinage... coinages) {
		CoinPurse remove = of(coinages);
		return 
			getCopper() >= remove.getCopper() 
			&& getSilver() >= remove.getSilver() 
			&& getElectrum() >= remove.getElectrum() 
			&& getGold() > remove.getGold() 
			&& getPlatinum() >= remove.getPlatinum();
	}
	
	public default Coinage[] asCoinage() {
		Coinage[] coinage = new Coinage[5];
		coinage[0] = new Coinage(Coin.COPPER, getCopper());
		coinage[1] = new Coinage(Coin.SILVER, getSilver());
		coinage[2] = new Coinage(Coin.ELECTRUM, getElectrum());
		coinage[3] = new Coinage(Coin.GOLD, getGold());
		coinage[4] = new Coinage(Coin.PLATINUM, getPlatinum());
		return coinage;
	}
	
	public static CoinPurse of(CoinPurse purse) {
		return new StandardCoinPurse(purse);
	}
	
	public static CoinPurse of(Coinage... coinage) {
		return new StandardCoinPurse(coinage);
	}
	
	@Deprecated
	public static CoinPurse of(Statted statted) {
		Coinage[] coinages = new Coinage[] {
			new Coinage(Coin.COPPER, statted.getStat(Stat.Copper, int.class)),
			new Coinage(Coin.SILVER, statted.getStat(Stat.Silver, int.class)),
			new Coinage(Coin.ELECTRUM, statted.getStat(Stat.Electrum, int.class)),
			new Coinage(Coin.GOLD, statted.getStat(Stat.Gold, int.class)),
			new Coinage(Coin.PLATINUM, statted.getStat(Stat.Platinum, int.class))
		};
		return new StandardCoinPurse(coinages);
	}
	
	public static boolean hasCoinPurse(Statted statted) {
		return statted instanceof CoinPurse;
	}
	
	public default Coinage asCopper() {
		Coinage coinage = new Coinage(Coin.COPPER, getCopper());
		coinage.add(getSilver() * Coin.SILVER.getMultiplier());
		coinage.add(getElectrum() * Coin.ELECTRUM.getMultiplier());
		coinage.add(getGold() * Coin.GOLD.getMultiplier());
		coinage.add(getPlatinum() * Coin.PLATINUM.getMultiplier());
		return coinage;
	}
	
}
