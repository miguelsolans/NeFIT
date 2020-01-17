build: frontend
current_dir = $(shell pwd)

frontend:
	mkdir Frontend/bin
	dependencies/gpb/bin/protoc-erl -I. -maps -o Frontend/ Protos/protocol.proto
	erlc -I dependencies/gpb/include -o Frontend/bin Frontend/protocol.erl
	erlc -I dependencies/erlzmq2/include -o dependencies/erlzmq2/ebin dependencies/erlzmq2/src/erlzmq.erl
	erlc -I dependencies/erlzmq2/include -o dependencies/erlzmq2/ebin dependencies/erlzmq2/src/erlzmq_nif.erl
	erlc -o Frontend/bin Frontend/*.erl dependencies/mochijson2.erl

run:
	erl < MakefileCommands

.PHONY: frontend

clean:
	-@rm -rf Frontend/bin/
	-@rm Frontend/protocol.erl
