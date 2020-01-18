-module(supervisor_manager).
-export ([start/0, exit/1, exit/2]).

% funtion that initializes supervisor
start() ->
    register(supervisor, self()),
    io:put_chars("\nSUPERVISOR STARTED...\n"),
    supervision().

% function that tells supervisor thar a process terminates with Data
exit(Module, Data) ->
    supervisor ! {Module, Data}.

% function that tells supervisor thar a process terminates
exit(Module) ->
    supervisor ! Module.

% function that handles manually a supervision role
supervision() ->
    receive
        {clients_state_manager, Data} ->
            clients_state_manager:start(Data),
            supervision();
        {authentication_handler, Sock} ->
            io:format("Connection ~p teardown~n", [Sock]),
            supervision();
        {user_manager, {User, Sock}} ->
            clients_state_manager:logout(User),
            io:format("Connection ~p teardown~n", [Sock]),
            supervision()
    end.
