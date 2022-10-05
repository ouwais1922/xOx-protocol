# xOx design document:

The xOx design document will provide a well description of what the Server and Client files do. In addition, this document provides steps to run the server and client application. Eventually, as the protocol very matter to get a structured communication between the client and the server, the xOx design document will provide the protocol used between the client and server. At the end, some resources used in coding.

## Description:

Our RAT (Remote access tool) that takes control of the remote system would be able to:

1. Get the list of processes running in the remote system.
2. Take screenshots of the remote system.
3. Reboot the remote system.

## Running the app:

### Steps:

In order to visualize the two different boxes one of the client and the other of the server, is preferably to run the application using the commands line,so we get the real understanding of the communication between the client and server. (check the running picture in the resources folder)

Server side:

1. Javac OxServer.java
2. java oXServer

Client side:

1. javac OxClient.java
2. java OxClient

## Application files structure:

the file structure consists mainly of a file of:

- the client "OxClient.java" with a folder "ClientShare" where the screenshot is downloaded
- the server "OxServer.java" with a folder "ServerShare" where the first screenshot is generetaed before send it to the client
- Resources folder has some screenshot that shows the success running for our remote access tool

(check the FileStructure picture under Resources folder)

## xOx Protocol:

The client opens a connection with the server and _informs_ the server whether it wants to _getListOfRunningProcesses_, _getScreenShot_, or _rebootRemote_ using a _header_.

### List of running processes in the remote system:

If the client wants to get the list of runnig processes in the remote machine (server), the header will be as following:

- **process[one space][line_feed]**

The server will get the previous header and it will send the list of running process back to the client, so the client can read it(check picture processList.png under the Respurces folder).

### Screenshots of the remote sytem:

If the client wants to take the screenshots of the remotes system, the header will be as following:

- **screen[one space][line_feed]**

for example:

     - C:\Users\ouwais> java OxClient s \n

Upon receiving this header, the server will take screenshot, and it will be directly downloaded in the ClientShare folder.

        If the server does not make the screenshot properly, then the server will reply with a header as flollowing:

        SCREENSHOT[one space]ERROR[Line Feed]

        If the screenshot is done, so it will be generated in the server, in the ServerShare/ folder,  the server will reply with a header as following:

        BINGO[one space][file size][Line Feed]

        followed by the bytes of the filE

        Eventually, the screenshot will be downloaded directly in the client side espically in the ClientShare/ folder under the name of screen.PNG.

        NB: check Recourses/OxScreenShot.png

### Reboot the remote system:

If the client wants to reboot the remote system, the header will be as following:

- **reboot[one space][line_feed]**

for example:

    - C:\Users\ouwais> java OxClient reboot \n

Upon receiving this header, the server will be rebooted.

Note: the remote system will be rebooted after 30s.

## Resources:

- https://github.com/oiraqi/paradigms/tree/main/P1-Communication/case-studies/Fx
- https://stackoverflow.com/questions/54686/how-to-get-a-list-of-current-open-windows-process-with-java
- https://stackoverflow.com/questions/25637/shutting-down-a-computer
- https://stackoverflow.com/questions/4490454/how-to-take-a-screenshot-in-java
- https://www.baeldung.com/java-inputstream-to-outputstream
- https://docs.oracle.com/javase/7/docs/api/java/net/Socket.html
