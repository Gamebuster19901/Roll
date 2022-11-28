package com.gamebuster19901.roll.bot.game.session;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.gamebuster19901.roll.util.ThreadService;

import net.dv8tion.jda.api.entities.User;

public final class Sessions {

	static final Set<SelfSession> SELF_SESSIONS = ConcurrentHashMap.newKeySet();
	static final Set<Session> ACTIVE_SESSIONS = ConcurrentHashMap.newKeySet();
	
	static {
		ThreadService.run("Session Handler", () -> {
			try {
				while(true) {
					Thread.sleep(15000);
					for(Session session : ACTIVE_SESSIONS) {
						if(session.expired()) {
							session.end("More than an hour has passed since the last interaction with the session.");
						}
					}
					for(Session session : SELF_SESSIONS) {
						if(session.expired()) {
							session.end();
						}
					}
				}
			}
			catch(InterruptedException e) {
				for(Session session : ACTIVE_SESSIONS) {
					session.end("The bot is shutting down!");
				}
			}
		});
	}
	
	public static Session getCampaignSession(User user) {
		for(Session session : ACTIVE_SESSIONS) {
			if(session.getActivePlayers().contains(user.getIdLong())) {
				return session;
			}
		}
		return null;
	}
	
	public static SelfSession getSelfSession(User user) {
		for(SelfSession selfSession : SELF_SESSIONS) {
			if(selfSession.hasPlayer(user)) {
				return selfSession;
			}
		}
		SelfSession session = new SelfSession(user);
		SELF_SESSIONS.add(session);
		return session;
	}
	
	public static Session getSession(User user) {
		Session session = getCampaignSession(user);
		if(session == null) {
			return getSelfSession(user);
		}
		return session;
	}
	
	public static enum SessionType {
		CAMPAIGN,
		SELF,
		;
	}
	
}
