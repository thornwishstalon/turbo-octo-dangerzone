package main.input.command;

public class CommandException extends Exception {
	/**
	 * 
	 * @author f
	 *
	 */
	private static final long serialVersionUID = -6670526090128484851L;
	private String message;
	
	public CommandException(String message){
		this.message= message;
	}
	
	@Override
	public String getMessage() {
		return message;//super.getMessage();
	}
}
