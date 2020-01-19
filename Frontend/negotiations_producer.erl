-module(negotiations_producer).
-export([run/2, sendOrder/2]).

% function that starts communication with negotiator server
run(Host, Port) ->
	{ok, Context} = erlzmq:context(1),
	{ok, Sock} = erlzmq:socket(Context, [push, {active, false}]),
	ok = erlzmq:connect(Sock, "tcp://" ++ Host ++ ":" ++ Port),
	io:format("ZMQ connect " ++ Host ++ ":" ++ Port ++ "~n"),
	spawn( fun() -> negotiationsProducer(Sock) end).

% function that sends an order to a negotiator
sendOrder(Msg, Pid) ->
	Pid ! {order, Msg, self()},
	receive
		Rep -> Rep
	end.

% function that sends orders to the negotiator
negotiationsProducer(Sock) ->
	receive
		{order, Msg, From} ->
			case erlzmq:send(Sock, Msg) of
				ok ->
					io:put_chars("Send Msg to Negotiator\n"),
					From ! ok;
				_ -> From ! error
			end,
			negotiationsProducer(Sock)
	end.
