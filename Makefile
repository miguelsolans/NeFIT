build: frontend client negotiator catalog
current_dir = $(shell pwd)

frontend:
	mkdir Frontend/bin
	dependencies/gpb/bin/protoc-erl -I. -maps -o Frontend/ Protos/protocol.proto
	erlc -I dependencies/gpb/include -o Frontend/bin Frontend/protocol.erl
	erlc -I dependencies/erlzmq2/include -o Frontend/bin dependencies/erlzmq2/src/erlzmq.erl
	erlc -I dependencies/erlzmq2/include -o Frontend/bin dependencies/erlzmq2/src/erlzmq_nif.erl
	erlc -o Frontend/bin Frontend/*.erl dependencies/mochijson.erl

client:
	mkdir exec/
	cd Client && mvn clean compile package && cd ../
	mv Client/target/Client-1.0.jar exec/

negotiator:
	cd Negotiator && mvn clean compile package && cd ../
	mv Negotiator/target/Negotiator-1.0.jar exec/

catalog:
	cd catalog && mvn clean compile package && cd ../
	mv catalog/target/nefit-1.0.jar exec/
	cp catalog/config.yml exec/

run-frontend:
	cd dependencies/erlzmq2/ebin && erl < ErlangCommands

.PHONY: frontend client negotiator catalog

clean:
	-@rm -rf Frontend/bin/
	-@rm Frontend/protocol.erl
	-@rm -rf exec/
