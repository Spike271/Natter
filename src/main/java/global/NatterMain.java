package global;

import com.client.Application;

import java.lang.reflect.InvocationTargetException;

public class NatterMain
{
	public static void main(String[] args)
	{
        try
		{
            Application.Run();
        }
		catch (InvocationTargetException | InterruptedException e)
		{
            System.err.println(e.getMessage());
        }
    }
}