import java.net.*;
import java.io.*;
import java.util.StringTokenizer;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class OxServer{@Deprecated

    public static void getScreenShot()  throws Exception
{
    BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
    ImageIO.write(image, "png", new File("ServerShare/"+"screen.PNG"));
}

public static void rebootSystem() throws IOException {
	Runtime r = Runtime.getRuntime();
	String shutDown = "shutdown -r ";
	try
	{
	   r.exec(shutDown);
	}
	catch(IOException e)
	{
	   System.out.println(e.getMessage());
	}
	}
// 	public static void processList() throws IOException {

// 		try {
// 		Process pr = Runtime.getRuntime().exec("tasklist");
// 		BufferedReader br =new BufferedReader(new InputStreamReader(pr.getInputStream()));
// 		String temp = br.readLine();
// 		while (temp != null) {
// 		System.out.println(""+temp);
// 		temp = br.readLine();
// 		}
// 		} catch (Exception e) {
// 		}

//   }

	public static void main(String[] args) throws Exception {

        int SERVER_PORT = 80;

		try (ServerSocket ss = new ServerSocket(SERVER_PORT)) {
			while (true) {
				System.out.println("Server waiting...");
				Socket connectionFromClient = ss.accept();
				System.out.println(
						"Server got a connection from a client whose port is: " + connectionFromClient.getPort());
				
				try {
					InputStream in = connectionFromClient.getInputStream();
					OutputStream out = connectionFromClient.getOutputStream();

					String errorMessage = "SCREENSHOT ERROR\n";

					BufferedReader headerReader = new BufferedReader(new InputStreamReader(in));
					BufferedWriter headerWriter = new BufferedWriter(new OutputStreamWriter(out));

					// DataInputStream dataIn = new DataInputStream(in);
					DataOutputStream dataOut = new DataOutputStream(out);

					String header = headerReader.readLine();
					StringTokenizer strk = new StringTokenizer(header, " ");

					String command = strk.nextToken();


					if (command.equals("screen")) {
						try {
                            getScreenShot();
							FileInputStream fileIn = new FileInputStream("ServerShare/" + "screen.PNG");
							int fileSize = fileIn.available();
							header = "BINGO " + fileSize + "\n";

							headerWriter.write(header, 0, header.length());
							headerWriter.flush();

							byte[] bytes = new byte[fileSize];
							fileIn.read(bytes);

							fileIn.close();

							dataOut.write(bytes, 0, fileSize);

						} catch (Exception ex) {
							headerWriter.write(errorMessage, 0, errorMessage.length());
							headerWriter.flush();

						} finally {
							connectionFromClient.close();
						}
					} else if (command.equals("reboot")) {
						try {
                            rebootSystem();
						} catch (Exception e) {
							System.out.println(e.getMessage());

						} finally {
							connectionFromClient.close();
						}

					}else if (command.equals("process")){

						// I could not invoke the processList function for a "Deplecated version" problem
						try {
									Process pr = Runtime.getRuntime().exec("tasklist");
									BufferedReader br =new BufferedReader(new InputStreamReader(pr.getInputStream()));
									String temp = br.readLine();
									while (temp != null) {
									System.out.println(""+temp);
									temp = br.readLine();
									}
									} catch (Exception e) {
										System.out.println(e.getMessage());
									}finally {
										connectionFromClient.close();
									}
					
					} else {

						System.out.println("Connection got from an incompatible client");

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}