package processor;

public class TextDocument {
	
	public TextDocument (String id, String text, String parentName) {
		this.id = id;
		this.text = text;
		this.parentName = parentName;
	}
	
	private String id;
	private String text;
	private String parentName;
	
	@Override
	public String toString() {
		return "Doc id: " + id + "Doc parent folder name: " + parentName + "\n Doc text: " + text;
	}
}
