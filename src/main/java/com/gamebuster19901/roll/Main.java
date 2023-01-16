package com.gamebuster19901.roll;

import java.io.File;
import java.io.IOException;

import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.login.LoginException;

import com.gamebuster19901.roll.bot.DiscordBot;
import com.gamebuster19901.roll.bot.command.Commands;
import com.gamebuster19901.roll.bot.command.ConsoleContext;
import com.gamebuster19901.roll.bot.database.sql.Database;
import com.gamebuster19901.roll.bot.user.ConsoleUser;
import com.gamebuster19901.roll.util.ThreadService;
import com.google.gson.Gson;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.dv8tion.jda.api.exceptions.ErrorResponseException;

public class Main {
	public static final File RUN_DIR = new File("./run");
	static {
		RUN_DIR.mkdirs();
	}
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
	public static long botOwner = -1;

	public static DiscordBot discordBot;
	public static Gson gson = GsonInitalizer.initialize();
	
	private static ConcurrentLinkedDeque<String> consoleCommandsAwaitingProcessing = new ConcurrentLinkedDeque<String>();

	public static ConsoleUser CONSOLE = ConsoleUser.INSTANCE;
	public static boolean stopping = false;
	public static Instant lastDBInitialization;
	
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws InterruptedException, ClassNotFoundException, IOException, SQLException {
		System.out.println(System.getProperty("java.version"));
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
		startDatabaseHeartbeatThread();
		startConsole();
		while(true) {
			if(consoleCommandsAwaitingProcessing.size() > 0) {
				try {
					Commands.DISPATCHER.getDispatcher().execute(consoleCommandsAwaitingProcessing.poll(), ConsoleContext.INSTANCE);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
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
	
	private static void startConsole() {
		Thread consoleThread = new Thread() {
			@Override
			public void run() {
				Scanner scanner = new Scanner(System.in);
				while(scanner.hasNextLine()) {
					consoleCommandsAwaitingProcessing.addFirst(scanner.nextLine());
				}
			}
		};
		consoleThread.setName("consoleReader");
		consoleThread.setDaemon(true);
		consoleThread.start();
	}
	
	private static void startDatabaseHeartbeatThread() {
		ThreadService.run("Database Heartbeat", () -> {
			while(true) {
				try {
					int response = Database.ping();
					//System.out.println(response));
					Thread.sleep(10000);
				}
				catch(InterruptedException e) {
					//swallow, kill thread
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static synchronized void recoverDB() {
		Thread old = ThreadService.getThread("Database Reviver");
		if(old != null) {
			if(old.getState() != Thread.State.TERMINATED) {
				return; //already being recovered
			}
		}
		if(Database.lastDBInitialization.plus(Duration.ofSeconds(5)).isAfter(Instant.now())) { //5 second grace period after recovering db before trying again
			ThreadService.run("Database Reviver", () -> {
				if(Database.INSTANCE != null) {
					try {
						Database.INSTANCE.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				Database.INSTANCE = null;
				while(Main.stopping == false && (Database.INSTANCE == null)) {
					try {
						Database.INSTANCE.close();
						Database.INSTANCE = null;
						while(Database.INSTANCE == null) {
							discordBot.setNoDB();
							try {
								Database.INSTANCE = new Database();
								discordBot.setOnline();
							}
							catch(Throwable t) {
								System.err.println("Attempting to recover from database connection failure...");
								t.printStackTrace(System.err);
								if(Database.INSTANCE != null) {
									try {Database.INSTANCE.close();}catch(Throwable t2) {}
								}
								Database.INSTANCE = null;
								Thread.sleep(1000);
							}
						}
					}
					catch(Throwable t) {
						t.printStackTrace();
					}
				}
			});
		}
	}
	
}
