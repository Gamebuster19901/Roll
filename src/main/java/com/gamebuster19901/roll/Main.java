package com.gamebuster19901.roll;

import java.io.File;
import java.io.IOException;

import java.sql.SQLException;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.login.LoginException;

import com.gamebuster19901.roll.bot.DiscordBot;
import com.gamebuster19901.roll.bot.database.sql.Database;
import com.google.gson.Gson;

import net.dv8tion.jda.api.exceptions.ErrorResponseException;

public class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
	public static long botOwner = -1;

	public static DiscordBot discordBot;
	public static Gson gson = GsonInitalizer.initialize();
	
	private static ConcurrentLinkedDeque<String> consoleCommandsAwaitingProcessing = new ConcurrentLinkedDeque<String>();

	public static boolean stopping = false;
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws InterruptedException, ClassNotFoundException, IOException, SQLException {
		if(args.length % 2 != 0) {
			throw new IllegalArgumentException("Must be started with an even number of arguments!");
		}
		
		LOGGER.setLevel(Level.FINEST);
		
		for(int i = 0; i < args.length; i++) {
			if(args[i].equals("-owner")) {
				botOwner = Long.parseLong(args[++i]);
			}
		}
		
		discordBot = null;
		
		int bootAttempts = 0;
		while(discordBot == null) {
			try {
				bootAttempts++;
				discordBot = startDiscordBot(args);
				discordBot.setLoading();
			} catch (LoginException | IOException | ErrorResponseException e) {
				LOGGER.log(Level.SEVERE, e, () -> e.getMessage());
				if(bootAttempts >= 3) {
					System.exit(-2);
				}
				Thread.sleep(5000);
			}
		}
		
		do {
			try {
				Database.INSTANCE = new Database();
			}
			catch(Throwable t) {
				System.out.println(t);
				discordBot.setNoDB();
				Thread.sleep(5000);
			}
		}
		while(Database.INSTANCE == null);
		
		Thread.sleep(5000);
		
		discordBot.setOnline();
		while(true) {
			//stuff
		}
		
		//System.exit(-1);
	}
	
	private static DiscordBot startDiscordBot(String[] args) throws LoginException, IOException {
			String botOwner = null;
			File keyFile = new File("./discord.secret");
		for(int i = 0; i < args.length; i++) {
			if(args[i].equalsIgnoreCase("-owner")) {
				botOwner = args[++i];
			}
			if(args[i].equalsIgnoreCase("-keyFile")) {
				keyFile = new File(args[++i]);
			}
		}
		return new DiscordBot(botOwner, keyFile);
	}
	
}
