package com.gamebuster19901.roll.util.pipe;

import java.io.ByteArrayOutputStream;
import java.io.PipedOutputStream;

import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

public class PipeHelper {

	public static void pipe(IReplyCallback callback, ByteArrayOutputStream out, PipedOutputStream pipe) {
		new Thread(
				() -> {
					try (pipe) {
						out.writeTo(pipe);
					}
					catch(Throwable t) {
						if(callback != null) {
							callback.reply(t.getMessage() + "\n\n Printing stacktrace to console.").queue();
						}
						t.printStackTrace();
						throw new RuntimeException(t);
					}
				}
		).start();
	}
	
}
