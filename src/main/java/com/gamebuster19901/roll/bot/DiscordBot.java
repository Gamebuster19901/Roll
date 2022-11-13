package com.gamebuster19901.roll.bot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.login.LoginException;

import com.gamebuster19901.roll.bot.command.Commands;
import com.gamebuster19901.roll.bot.command.argument.GlobalLiteralArgumentBuilder.GlobalLiteralCommandNode;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Activity.ActivityType;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.managers.Presence;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class DiscordBot {

	private static final Logger LOGGER = Logger.getLogger(DiscordBot.class.getName());
	
	private static final List<GatewayIntent> GATEWAYS = Arrays.asList(new GatewayIntent[] {GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_EMOJIS_AND_STICKERS});
	private String botOwner;
	public final JDA jda;
	
	public DiscordBot(String botOwner, File secretFile) throws LoginException, IOException {
		BufferedReader reader = null;
		String secret = null;
		
		try {
			reader = new BufferedReader(new FileReader(secretFile));
			secret = reader.readLine();
			JDABuilder builder = JDABuilder.createDefault(secret, GATEWAYS).setMemberCachePolicy(MemberCachePolicy.ALL).setChunkingFilter(ChunkingFilter.ALL);
			builder.addEventListeners(new EventReceiver());
			this.jda = builder.build();
			setupGlobalCommands();
		} 
		catch (IOException e) {
			LOGGER.log(Level.SEVERE, e, () -> e.getMessage());
			throw e;
		}
		finally {
			secret = null;
			secretFile = null;
			if(reader != null) {
				reader.close();
			}
		}
		
		secret = null;
		secretFile = null;
	}
	
	public String getOwner() {
		return botOwner;
	}
	
	public void setLoading() {
		Presence presence = jda.getPresence();
		presence.setPresence(OnlineStatus.IDLE, Activity.of(ActivityType.WATCHING, "Loading..."));
	}
	
	public void setOnline() {
		Presence presence = jda.getPresence();
		presence.setPresence(OnlineStatus.ONLINE, Activity.of(ActivityType.LISTENING, "commands"));
	}
	
	public void setNoDB() {
		Presence presence = jda.getPresence();
		presence.setPresence(OnlineStatus.DO_NOT_DISTURB, Activity.of(ActivityType.STREAMING, "No Database!"));
	}
	
	public SelfUser getSelfUser() {
		return jda.getSelfUser();
	}
	
	private void setupGlobalCommands() {
		List<CommandData> commands = new ArrayList<>();
		Commands.DISPATCHER.getDispatcher().getRoot().getChildren().forEach((command) -> {
			if(command instanceof GlobalLiteralCommandNode) {
				SlashCommandData data = net.dv8tion.jda.api.interactions.commands.build.Commands.slash(command.getName(), command.getUsageText());
				if(command.getChildren().size() > 0) {
					data.addOption(OptionType.STRING, "argument", "argument");
				}
				System.out.println("Global: " + command.getUsageText());
				commands.add(data);
			}
		});
		this.jda.updateCommands().addCommands(commands).queue();
	}
	
}
