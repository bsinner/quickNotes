### Setting up after cloning

1. Add a file named translateKey.txt to src/main/resources with the Microsoft translate api key.

2. Add a file named accessTokenPw.txt to src/main/resources with a random 32 character password.

3. Copy hibernate.cfg.xml and log4j.properties templates to src/main/resources and fill in.

4. Copy databaseUtility.properties file to src/test/resources and fill in.

5. Copy mailLogin.properties to src/main/resources and fill in the email to send mail from,
   quicknotesmail@gmail.com at the time of writing this. 

6. Create a database named "notes", run DesignDocuments/createTables.sql in the database.

7. Optionally copy the HelloWorld.java template to src/main/java/.../api for testing new features;
   any time /api/hello is opened in browser the code in HelloWorld will run.

