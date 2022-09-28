package com.gamebuster19901.excite.exception;

import java.io.IOException;

public class DieRenderException extends IOException {

	public DieRenderException(String reason) {
		super(reason);
	}
	
	public DieRenderException(String reason, Throwable cause) {
		super(reason, cause);
	}
	
	public DieRenderException(Throwable cause) {
		super(cause);
	}
	
}
