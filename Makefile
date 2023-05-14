docker:
	docker build  . -t konkurs-local:1.0
	
stest:
	mvn -f pomSTest.xml -Dexec.mainClass="ski.obuchow.konkurs.SpeedTest" exec:java -Dexec.args="$$(realpath)/tests"
	
save:
	cp tests/lastTimes.json tests/baseline.json

test:
	mvn test

mycontainer:
	docker run --name mycontainer -d -i -t debian:bullseye-slim /bin/sh
	docker run --name mycontainer -d -i -t konkurs-local:1.0 /bin/sh
	docker exec -it mycontainer sh 

profiler:
	/Users/tobuchowski/Downloads/visualvm_216/bin/visualvm --jdkhome /opt/homebrew/Cellar/openjdk/20/libexec/openjdk.jdk/Contents/Home
	
codeql-create:
	codeql database create ~/personal/codeql --language=java --command='mvn clean install -Dgpg.skip' --overwrite
	
codeql-run:
	codeql database analyze /Users/tobuchowski/personal/codeql --format=CSV --output=/Users/tobuchowski/personal/cresult/result.csv
