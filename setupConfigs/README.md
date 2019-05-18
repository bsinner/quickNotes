### Setting up git-ignored config files

1. Add file named translateKey.txt to src/main/resources with the Microsoft translate api key.

2. Add file named accessTokenPw.txt to src/main/resources with a random 32 character long string.

3. Copy hibernate.cfg.xml and log4j.properties templates to src/main/resources and fill in.

4. Copy databaseUtility.properties file to src/test/resources and fill in.

5. Optionaly copy the HelloWorld.java template to src/main/java/.../api for testing new features. Any time /api/hello is opened in browser the code in HelloWorld will run.
