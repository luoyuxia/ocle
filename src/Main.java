import java.io.IOException;
import java.io.InputStream;

public class Main {

    public static void main(String[] args) {
        try {
            String locationCmd = "run_windows.bat C:\\Users\\yuxia\\Desktop\\files\\GPS_UM.xml C:\\Users\\yuxia\\Desktop\\files\\GPS_UM.ocl";
            Process child = Runtime.getRuntime().exec(locationCmd);
            InputStream in = child.getInputStream();
            int c;
            while ((c = in.read()) != -1) {
                System.out.println((char)c);
            }
            in.close();

            try {
                child.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
