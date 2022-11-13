package com.gamebuster19901.roll.bot.command.embed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gamebuster19901.roll.bot.game.Statted;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu.Builder;

public class StatInteractionBuilder {
	
	private static final String MENU = "" + '\u2630';

	private final Statted statted;
	private StatEmbedPage page;
	private boolean showMenu;
	
	public StatInteractionBuilder(Statted statted) {
		this(statted, StatEmbedPage.Stats);
	}

	public StatInteractionBuilder(Statted statted, StatEmbedPage page) {
		this(statted, page, false);
	}
	
	public StatInteractionBuilder(Statted statted, Message message) {
		this(statted, StatEmbedPage.valueOf(message.getEmbeds().get(0).getTitle()), isShowingMenu(message));
	}
	
	public StatInteractionBuilder(Statted statted, Message message, boolean showMenu) {
		this(statted, StatEmbedPage.valueOf(message.getEmbeds().get(0).getTitle()), showMenu);
	}
	
	public StatInteractionBuilder(Statted statted, StatEmbedPage page, boolean showMenu) {
		this.statted = statted;
		this.page = page;
		this.showMenu = showMenu;
	}
	
	public ActionRow getComponents() {
		ArrayList<ItemComponent> components = new ArrayList<ItemComponent>();

		List<StatEmbedPage> validPages = getValidPages();
		
		StatEmbedPage prevPage = getPrevPage();
		StringSelectMenu menu = getSelectMenu(validPages);
		StatEmbedPage nextPage = getNextPage();

		if(!showMenu) {
			if(prevPage != null) {
				components.add(Button.secondary("charsheet page " + statted.getID() + " " + prevPage.name(), prevPage.name()));
			}
			if(menu != null) {
				components.add(Button.secondary("charsheet menu show " + statted.getID() + " " + page.name(), MENU));
			}
			if(nextPage != null) {
				components.add(Button.secondary("charsheet page " + statted.getID() + " " + nextPage.name(), nextPage.name()));
			}
		}
		else {
			components.add(menu);
		}
		
		return ActionRow.of(components);
	}
	
	private static boolean isValidPage(Statted statted, StatEmbedPage page) {
		return true;
	}
	
	private StatEmbedPage getPrevPage() {
		List<StatEmbedPage> pages = getValidPages();
		int size = pages.size();
		int index = pages.indexOf(page);
		if(size > 1 && index > 0) {
			return pages.get(index - 1);
		}
		return null;
	}
	
	private StringSelectMenu getSelectMenu(List<StatEmbedPage> validPages) {
		Builder menuBuilder = StringSelectMenu.create("statsSelect");

		for(StatEmbedPage page : validPages) {
			if(page != this.page) {
				menuBuilder.addOption(page.name(), page.name());
			}
		}
		
		if(menuBuilder.getOptions().size() > 1) {
			return menuBuilder.build();
		}
		return null;
	}
	
	private StatEmbedPage getNextPage() {
		List<StatEmbedPage> pages = getValidPages();
		int size = pages.size();
		int index = pages.indexOf(page);
		if(size > 1 && index < size - 1) {
			return pages.get(index + 1);
		}
		return null;
	}
	
	private List<StatEmbedPage> getValidPages() {
		return getValidPages(statted);
	}
	
	public static boolean canShowMenu(Statted statted) {
		return getValidPages(statted).size() > 2;
	}
	
	public StatInteractionBuilder setShowMenu(boolean showMenu) {
		this.showMenu = showMenu;
		return this;
	}
	
	public static List<StatEmbedPage> getValidPages(Statted statted) {
		List<StatEmbedPage> pages = Arrays.asList(StatEmbedPage.values());
		List<StatEmbedPage> validPages = new ArrayList<>();
		for(StatEmbedPage page : pages) {
			if(isValidPage(statted, page)) {
				validPages.add(page);
			}
		}
		return validPages;
	}
	
	public boolean isShowingMenu() {
		return showMenu;
	}
	
	private static boolean isShowingMenu(Message message) {
		if (message.getButtons().size() > 2) {
			for(Button b : message.getButtons()) {
				if(b.getEmoji() != null) {
					if(message.getButtons().get(1).getEmoji().getFormatted().equals(MENU)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
}
