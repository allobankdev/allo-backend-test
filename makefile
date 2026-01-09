ARTIFACT_ID=idr-rate-aggregator
ZIP_FILE=$(ARTIFACT_ID).zip

first_setup:
	curl -L -X POST "https://start.spring.io/starter.zip" \
		-H "Accept: application/zip" \
		-d type=maven-project \
		-d language=java \
		-d bootVersion=3.5.9 \
		-d groupId=com.example \
		-d artifactId=$(ARTIFACT_ID) \
		-d name=$(ARTIFACT_ID) \
		-d packageName=com.example.idr \
		-d packaging=jar \
		-d javaVersion=17 \
		-d dependencies=web,actuator,validation,test \
		-o $(ZIP_FILE)
	
	unzip $(ZIP_FILE) -d $(ARTIFACT_ID)
	
	chmod +x $(ARTIFACT_ID)/mvnw
	
	rm $(ZIP_FILE)
	

run:
	cd $(ARTIFACT_ID) && ./mvnw spring-boot:run

run_test:
	cd $(ARTIFACT_ID) && ./mvnw clean test

make compile:
	cd $(ARTIFACT_ID) && ./mvnw clean compile

clean:
	rm -rf $(ARTIFACT_ID)
	rm -f $(ZIP_FILE)