package serverMain;

import java.io.Serializable;

public class Message implements Serializable 
{
	private static final long serialVersionUID = -5489078160352713381L;
	private Object source;
    private Object content;

	public Message(Object content)
	{
		setContent(content);
	}
	public Message(Object content,Object source)
	{
		setContent(content);
		setSource(source);
	}
    public Object getSource()
    {
        return source;
    }
    public Object getContent()
    {
        return content;
    }
    public void setSource(Object obj)
    {
        source = obj;
    }
    public void setContent(Object obj)
    {
        content = obj;
    }
}
