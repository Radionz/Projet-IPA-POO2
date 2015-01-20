package zuul.rooms;

public class SecretPassage extends Room{

	public SecretPassage(String description) {
		super(description);
	}
	
	@Override
	public boolean isHidden()
	{
		return true;
	}
	
}
