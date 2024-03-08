# PDF Generator Cheatsheet

PDF Generator is build in JAVA (Spring boot), and version above 19 will compile

This application is ment to be hosted on a single Server
The SSL certificates are generated using Let's encrypt and should be refreshed every 90 days(Or an autrefresh should be set up in the future)

The root of this proct should contain a file called key.json, this json file holds the credentials to be able to conenct with firebase
this json can be requested/downloaded at firebase itself

## Commands / Cheatsheets

Kill server
> sudo kill -9 $(sudo lsof -t -i :5612)
 
Start server (on server itself)
> java -jar pdf-0.0.1-SNAPSHOT.jar &

Copy over pdfs from local machine to server
> scp -r ~/pdfs danny@136.144.188.213:/home/danny/pdfs
> 
Copy over all pdfs from server to local machine
> scp -r danny@136.144.188.213:/home/danny/pdfs ~/pdfs

Build the jar (server executable)
> mvn clean install -Dskiptests

Copy from local machine to server<br>
> scp -r "/home/danny/projects/robbin/pdf/target/pdf-0.0.1-SNAPSHOT.jar" "136.144.188.213:/home/danny"
