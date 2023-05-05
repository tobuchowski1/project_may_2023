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