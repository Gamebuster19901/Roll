package com.gamebuster19901.excite.bot.user;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.Set;

import com.gamebuster19901.excite.Player;
import com.gamebuster19901.excite.Wiimmfi;
import com.gamebuster19901.excite.bot.command.MessageContext;
import com.gamebuster19901.excite.bot.common.preferences.IntegerPreference;
import com.gamebuster19901.excite.bot.common.preferences.LongPreference;
import com.gamebuster19901.excite.bot.common.preferences.StringPreference;
import com.gamebuster19901.excite.output.OutputCSV;
import com.gamebuster19901.excite.util.TimeUtils;

public class UserPreferences implements OutputCSV{
	private static final String validPasswordChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890,.!?";
	private Random random = new Random();
	
	private StringPreference discord;
	private LongPreference discordId;
	private IntegerPreference notifyThreshold = new IntegerPreference(-1); //-1 means don't notify
	private DurationPreference notifyFrequency = new DurationPreference(Duration.ofMinutes(30), Duration.ofMinutes(5), ChronoUnit.FOREVER.getDuration());
	private ProfilePreference profiles = new ProfilePreference();
	private InstantPreference banTime = new InstantPreference(Instant.MIN);
	private DurationPreference banDuration = new DurationPreference();
	private InstantPreference banExpire = new InstantPreference(Instant.MIN);
	private StringPreference banReason = new StringPreference("");
	private IntegerPreference banCount = new IntegerPreference(0);
	
	private transient IntegerPreference desiredProfile = new IntegerPreference(-1);
	private transient StringPreference registrationCode = new StringPreference("");
	private transient InstantPreference registrationTimer = new InstantPreference(Instant.MIN);
	private transient IntegerPreference messageCountPastFifteenSeconds = new IntegerPreference(0);

	public UserPreferences(DiscordUser discordUser) {
		discord = new StringPreference(discordUser.getJDAUser().getAsTag());
		discordId = new LongPreference(discordUser.getId());
	}
	
	public UserPreferences(String discord, long discordId) {
		this.discord = new StringPreference(discord);
		this.discordId = new LongPreference(discordId);
	}
	
	public UserPreferences() {
		
	}
	
	public void parsePreferences(String discord, long discordId, int notifyThreshold, Duration notifyFrequency, Player[] profiles, Instant banTime, Duration banDuration, Instant banExpire, String banReason, int banCount) {
		this.discord = new StringPreference(discord);
		this.discordId = new LongPreference(discordId);
		this.notifyThreshold = new IntegerPreference(notifyThreshold);
		this.notifyFrequency = new DurationPreference(notifyFrequency);
		this.profiles = new ProfilePreference(profiles);
		this.banTime = new InstantPreference(banTime);
		this.banDuration = new DurationPreference(banDuration);
		this.banExpire = new InstantPreference(banExpire);
		this.banReason = new StringPreference(banReason);
		this.banCount = new IntegerPreference(banCount);
	}
	
	@Override
	public String toCSV() {
		return discord + "," + discordId + "," + notifyThreshold + "," + notifyFrequency + "," + profiles + "," + banTime + "," + banDuration + "," + banExpire + "," + banReason + "," + banCount;
	}
	
	public int getNotifyThreshold() {
		return notifyThreshold.getValue();
	}
	
	public Duration getNotifyFrequency() {
		return notifyFrequency.getValue();
	}
	
	public Set<Player> getProfiles() {
		return profiles.getValue();
	}
	
	public Instant getTimeBanned() {
		return banTime.getValue();
	}
	
	public boolean isBanned() {
		return Instant.now().isBefore(banExpire.getValue());
	}
	
	public Instant getBanExpireTime() {
		return banExpire.getValue();
	}
	
	public Duration getBanDuration() {
		return banDuration.getValue();
	}
	
	public String getBanReason() {
		return (String) banReason.getValue();
	}
	
	public void setNotifyThreshold(int threshold) {
		notifyThreshold.setValue(threshold);
	}
	
	public void setNotifyThreshold(String threshold) {
		notifyThreshold.setValue(threshold);
	}
	
	public void setNotifyFrequency(Duration duration) {
		notifyFrequency.setValue(duration);
	}
	
	public void setNotifyFrequency(String duration) {
		notifyFrequency.setValue(duration);
	}
	
	public void addProfile(int profileID) {
		profiles.addProfile(profileID);
	}
	
	public void addProfile(Player profile) {
		profiles.addProfile(profile);
	}
	
	@SuppressWarnings("rawtypes")
	public void ban(MessageContext context, Duration duration, String reason) {
		this.banTime.setValue(Instant.now());
		this.banDuration.setValue(duration);
		try {
			this.banExpire.setValue(Instant.now().plus(duration));
		}
		catch(ArithmeticException e) {
			this.banExpire.setValue(Instant.MAX);
		}
		this.banReason.setValue(reason);
		banCount.setValue(banCount.getValue() + 1);
		DiscordUser discordUser = DiscordUser.getDiscordUser(this.discordId.getValue());
		discordUser.sendMessage(context, discordUser.getJDAUser().getAsMention() + " " + (String)banReason.getValue());
	}
	
	public void pardon(int amount) {
		this.banTime.setValue(Instant.MIN);
		this.banDuration.setValue(Duration.ZERO);
		this.banExpire.setValue(Instant.MIN);
		this.banReason.setValue("");
		if(this.banCount.getValue() > 0) {
			this.banCount.setValue(this.banCount.getValue() - amount);
		}
		if(this.banCount.getValue() < 0) {
			this.banCount.setValue(0);
		}
	}
	
	public void clearRegistration() {
		this.desiredProfile.setValue(-1);
		this.registrationCode.setValue("");
		this.registrationTimer.setValue(Instant.MIN);
	}
	
	public boolean requestingRegistration() {
		return desiredProfile.getValue() != -1;
	}
	
	public String requestRegistration(Player desiredProfile) {
		this.desiredProfile.setValue(desiredProfile.getPlayerID());
		this.registrationCode.setValue(generatePassword());
		this.registrationTimer.setValue(Instant.now().plus(Duration.ofMinutes(5)));
		return (String) registrationCode.getValue();
	}
	
	@SuppressWarnings("rawtypes")
	void sentCommand(MessageContext context) {
		int messageCount = messageCountPastFifteenSeconds.setValue(messageCountPastFifteenSeconds.getValue() + 1);
		if(messageCount == 3) {
			DiscordUser user = DiscordUser.getDiscordUser(this.discordId.getValue());
			user.sendMessage(context, user.getJDAUser().getAsMention() + " Slow down! Spamming the bot will result in loss of privilages.");
		}
		if(messageCount > 5) {
			Duration banTime;
			switch(banCount.getValue()) {
				case 0:
					banTime = Duration.ofSeconds(30);
					break;
				case 1:
					banTime = Duration.ofMinutes(5);
					break;
				case 2:
					banTime = Duration.ofMinutes(30);
					break;
				case 3:
					banTime = Duration.ofDays(1);
					break;
				case 4:
					banTime = Duration.ofDays(7);
					break;
				case 5:
					banTime = Duration.ofDays(30);
				default:
					banTime = ChronoUnit.FOREVER.getDuration();
					break;
			}
			ban(context, banTime, "Do not spam the bot. You have been banned from using the bot for " + TimeUtils.readableDuration(banTime));
		}
	}
	
	void updateCooldowns() {
		if(messageCountPastFifteenSeconds.getValue() > 0) {
			messageCountPastFifteenSeconds.setValue(messageCountPastFifteenSeconds.getValue() - 1);
		}
	}
	
	private void register() {
		this.profiles.addProfile(desiredProfile.getValue());
		clearRegistration();
	}
	
	private final String generatePassword() {
		char[] sequence = new char[7];
		for(int i = 0; i < sequence.length; i++) {
			sequence[i] = validPasswordChars.charAt(random.nextInt(validPasswordChars.length()));
		}
		return new String(sequence);
	}
	
	public static void attemptRegister() {
		for(DiscordUser user : DiscordUser.getKnownUsers()) {
			UserPreferences preferences = user.preferences;
			if(preferences.requestingRegistration()) {
				int desiredProfile = preferences.desiredProfile.getValue();
				if(desiredProfile > -1) {
					if(preferences.registrationTimer.getValue().isAfter(Instant.now())) {
						for(Player player : Wiimmfi.getOnlinePlayers()) {
							if(player.getName().equals(preferences.registrationCode.getValue())) {
								if(player.getPlayerID() == desiredProfile) {
									preferences.register();
								}
								else {
									user.sendMessage("Registration aborted:\n\nYou selected the following account ID for registration:\n`" + desiredProfile + "`\nbut logged in with\n`" + player.getPlayerID() + "`");
									preferences.clearRegistration();
								}
							}
						}
					}
					else {
						user.sendMessage("Registration for \n" + Player.getPlayerByID(desiredProfile).toString() + "\n has expired!");
						preferences.clearRegistration();
					}
				}
				else {
					throw new IllegalStateException("No profile to register!");
				}
			}
		}
	}
	
}
