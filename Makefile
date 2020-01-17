build: frontend
current_dir = $(shell pwd)

frontend:
	mkdir Frontend/out
	dependencies/gpb/bin/protoc-erl -I. -maps -o Frontend/ Protos/protocol.proto
	erlc -I dependencies/gpb/include -o Frontend/out Frontend/protocol.erl
	erlc -o Frontend/out Frontend/*.erl dependencies/mochijson2.erl

run:
	erl < MakefileCommands

.PHONY: frontend

clean:
	-@rm -rf Frontend/out/
	-@rm Frontend/protocol.erl
