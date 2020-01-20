-module(negotiations_consumer).
-export([start/0]).

% functions that starts a pull socket for negotiations
start() ->
    {ok, Context} = erlzmq:context(),
    {ok, Sock} = erlzmq:socket(Context, [pull, {active, false}]),
    ok = erlzmq:connect(Sock, "tcp://localhost:3000"),
    io:put_chars("NEGOTIATIONS CONSUMER STARTED...\n"),
    spawn( fun() -> negotiationsConsumer(Sock) end).

% function that receives orders from the negotiator
negotiationsConsumer(Sock) ->
	case erlzmq:recv(Sock) of
		{ok, M} ->
            io:put_chars("Receive Msg from Negotiator\n"),
			Msg = protocol:decode_msg(M, 'Message'),
			User = maps:get(user, Msg),
			Username = maps:get(username, User),
			case clients_state_manager:isOnline(Username) of
				error ->
					negotiationsConsumer(Sock);
				Pid ->
					user_manager:sendMsg(Pid, M),
					negotiationsConsumer(Sock)
			end;
		{error, _} ->
			negotiationsConsumer(Sock)
	end.
