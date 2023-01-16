package com.gamebuster19901.roll.bot.game.coinage;

public class StandardCoinPurse implements CoinPurse {

	int copper;
	int silver;
	int electrum;
	int gold;
	int platinum;
	
	public StandardCoinPurse() {
		
	}
	
	public StandardCoinPurse(Coinage... coinages) {
		addCoinage(coinages);
	}
	
	public StandardCoinPurse(CoinPurse purse) {
		this(purse.asCoinage());
	}
	
	@Override
	public int getCopper() {
		return copper;
	}

	@Override
	public int getSilver() {
		return silver;
	}

	@Override
	public int getElectrum() {
		return electrum;
	}

	@Override
	public int getGold() {
		return gold;
	}

	@Override
	public int getPlatinum() {
		return platinum;
	}

	@Override
	public void addCoinage(Coinage... coinages) {
		for(Coinage coinage : coinages) {
			switch(coinage.getType()) {
				case COPPER:
					copper += coinage.getAmount();
					break;
				case ELECTRUM:
					electrum += coinage.getAmount();
					break;
				case GOLD:
					gold += coinage.getAmount();
					break;
				case PLATINUM:
					platinum += coinage.getAmount();
					break;
				case SILVER:
					silver += coinage.getAmount();
					break;
				default:
					break;
			}
		}
	}

	@Override
	public void removeCoinage(Coinage... coinages) {
		for(Coinage coinage : coinages) {
			switch(coinage.getType()) {
				case COPPER:
					copper -= coinage.getAmount();
					break;
				case ELECTRUM:
					electrum -= coinage.getAmount();
					break;
				case GOLD:
					gold -= coinage.getAmount();
					break;
				case PLATINUM:
					platinum -= coinage.getAmount();
					break;
				case SILVER:
					silver -= coinage.getAmount();
					break;
				default:
					break;
			}
		}
	}

	@Override
	public void setCoinage(Coinage... coinages) {
		for(Coinage coinage : coinages) {
			switch(coinage.getType()) {
				case COPPER:
					copper = coinage.getAmount();
					break;
				case ELECTRUM:
					electrum = coinage.getAmount();
					break;
				case GOLD:
					gold = coinage.getAmount();
					break;
				case PLATINUM:
					platinum = coinage.getAmount();
					break;
				case SILVER:
					silver = coinage.getAmount();
					break;
				default:
					break;
			}
		}
	}

}
