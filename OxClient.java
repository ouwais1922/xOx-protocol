import java.net.*;
import java.io.*;
import java.util.StringTokenizer;

public class OxClient {
	public static void main(String[] args) throws Exception {
		String command = args[0];

		try (Socket connectionToServer = new Socket("localhost", 80)) {

			// I/O operations

			InputStream in = connectionToServer.getInputStream();
			OutputStream out = connectionToServer.getOutputStream();

			BufferedReader headerReader = new BufferedReader(new InputStreamReader(in));
			BufferedWriter headerWriter = new BufferedWriter(new OutputStreamWriter(out));
			DataInputStream dataIn = new DataInputStream(in);

			if (command.equals("s")) {
				String header = "screen "+"\n";
				headerWriter.write(header, 0, header.length());
				headerWriter.flush();

				header = headerReader.readLine();

				if (header.equals("SCREENSHOT ERROR")) {
					System.out.println("Please try again!! Make sure to enter the command line as it is specified in the protocol");
				} else {
					StringTokenizer strk = new StringTokenizer(header, " ");

					String status = strk.nextToken();

					if (status.equals("BINGO")) {

						String temp = strk.nextToken();

						int size = Integer.parseInt(temp);

						byte[] space = new byte[size];

						dataIn.readFully(space);

						try (FileOutputStream fileOut = new FileOutputStream("ClientShare/" + "screen.PNG")) {
							fileOut.write(space, 0, size);
						}

					} else {
						System.out.println("You're not connected to the right Server!");
					}

				}

			} else if (command.equals("r")) {
				String header = "reboot "+"\n";
				headerWriter.write(header, 0, header.length());
				headerWriter.flush();
			} else if(command.equals("p"))
			{
				String header = "process "+"\n";
				headerWriter.write(header, 0, header.length());
				headerWriter.flush();

				System.out.println(headerReader.readLine()+"\n");
				
			}
		}
	}
}