package com.gamebuster19901.excite.bot.challenge.mail;

import java.util.logging.Level;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.gamebuster19901.excite.game.challenge.Rewardable;

public class RefundResponse extends MailReplyResponse implements Rewardable {

	private final Rewardable rewardable;
	
	public RefundResponse(MimeMessage message, Rewardable rewardable) {
		super(message);
		this.rewardable = rewardable;
	}

	@Override
	public int getReward() {
		return rewardable.getReward();
	}
	
	@Override
	public void respond() throws MessagingException {
		super.respond();
		MailHandler.LOGGER.log(Level.INFO, "Refunded " + getReward() + " to " + message.getSender());
	}

}
