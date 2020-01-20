% encapsulated module
% interface functions that access to the actor of this MODULE
-module(negotiations_manager).
-export ([start/0, findNegotiator/1]).

% register module as a process and start it
start() ->
    negotiations_consumer:start(),
    register(?MODULE, spawn(fun() -> negotiationsLoop(#{}, #{}) end)).

% function that asks the process that mantains de negotiations state for a Negotiator
findNegotiator(Manufacturer) ->
    ?MODULE ! {find, Manufacturer, self()},
    receive
        {ok, Pid} -> Pid
    end.

% process that runs indefinitely and keeps the negotiators state
negotiationsLoop(NegotiatorsMap, ManufaturersMap) ->
    receive
        {find, Manufacturer, From} ->
            case maps:find(Manufacturer, ManufaturersMap) of
                {ok, NegotiatorPid} ->
                    From ! {ok, NegotiatorPid},
                    negotiationsLoop(NegotiatorsMap, ManufaturersMap);
                error ->
                    Res = findManufacturerInCatalog(Manufacturer),
                    Name = maps:get(name, Res),
                    case maps:find(Name, NegotiatorsMap) of
                        {ok, NegotiatorPid} ->
                            maps:put(Manufacturer, NegotiatorPid, ManufaturersMap),
                            From ! {ok, NegotiatorPid},
                            negotiationsLoop(NegotiatorsMap, ManufaturersMap);
                        error ->
                            Host = maps:get(host, Res),
                            Port = maps:get(port, Res),
                            NegotiatorPid = negotiations_producer:run(Host,Port),
                            From ! {ok, NegotiatorPid},
                            negotiationsLoop(maps:put(Name, NegotiatorPid, NegotiatorsMap), maps:put(Manufacturer, NegotiatorPid, ManufaturersMap))
                    end
            end
    end.

% function that finds a manufaturer in the catalog
findManufacturerInCatalog(Manufacturer) ->
    inets:start(),
    case httpc:request("http://localhost:8080/manufacturer/" ++ Manufacturer) of
        {ok, {_, _, Result}} ->
            inets:stop(),
            {struct, Json} = mochijson:decode(Result),
            {_, Negotiator} = proplists:get_value("negotiator", Json),
            Name = proplists:get_value("name", Negotiator),
        	Host = proplists:get_value("host", Negotiator),
        	Port = proplists:get_value("port", Negotiator),
        	#{name => Name, host => Host, port => Port};
        _ ->
            inets:stop()
    end.
