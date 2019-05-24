import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * This main class placeholder is for more succinct classes and also creates a separate
 * interface for the Solar Entertainment Console to be dynamic and allow users to remove
 * and add separate apps
 * 
 * @author Vincent Trolia
 *
 */
public class main {
	public static void main(String[] args) {
		// load the needed variables
		BufferedReader appList = null;
		ArrayList<String> apps = null;
		
		// exit the app if any operation cannot be done
		try {
			appList = new BufferedReader(new FileReader("apps.applist"));
			apps = new ArrayList<String>();
			
			/* appList is a file where the user adds in the names of the apps they want
			 * displayed. Read each of them and then add them to the gui
			 */
			String str;
			while((str = appList.readLine()) != null)
				apps.add(str);
		
			String[][] names = new String[2][apps.size()];
			for(int i = 0; i < apps.size(); i++) {
				names[0][i] = apps.get(i);
				names[1][i] = apps.get(i) + ".png";
			}
			
			gui GUI = new gui(2, names[0], names[1]);
		}
		catch(Exception e) {};
	}
}
