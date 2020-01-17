% encapsulated module
% interface functions that access to the actor of this MODULE
-module(negotiations_manager).
-export ([start/0, findNegotiator/1]).

% register module as a process and start it
start() ->
    register(?MODULE, spawn(fun() -> negotiationsLoop(#{}, #{}) end)).

% function that asks the process that mantains de negotiations state for a Negotiator
findNegotiator(Manufacturer) ->
    ?MODULE ! {find, Manufacturer, self()},
    receive
        Pid -> Pid
    end.

% process that runs indefinitely and keeps the negotiators state
negotiationsLoop(NegotiatorsMap, ManufaturersMap) ->
    receive
        {find, Manufacturer, From} ->
            case maps:get(Manufacturer, ManufaturersMap) of
                {ok, NegotiatorPid} ->
                    From ! NegotiatorPid,
                    negotiationsLoop(NegotiatorsMap, ManufaturersMap);
                error ->
                    Res = findManufacturerInCatalog(Manufacturer),
                    Name = maps:get(name, Res),
                    case maps:get(Name, NegotiatorsMap) of
                        {ok, NegotiatorPid} ->
                            maps:put(Manufacturer, NegotiatorPid, ManufaturersMap),
                            From ! {ok, NegotiatorPid},
                            negotiationsLoop(NegotiatorsMap, ManufaturersMap);
                        error ->
                            Host = maps:get(host, Res),
                            Port = maps:get(port, Res),
                            %NegotiatorPid = exchangeProducer:run(Host,Port),
                            maps:put(Name, NegotiatorPid, NegotiatorsMap),
                            maps:put(Manufacturer, NegotiatorPid, ManufaturersMap),
                            From ! {ok, NegotiatorPid},
                            negotiationsLoop(NegotiatorsMap, ManufaturersMap)
                    end
            end
    end.

% function that finds a manufaturer in the catalog
findManufacturerInCatalog(Manufacturer) ->
    inets:start(),
    case httpc:request("http://localhost:8080/manufacturers/" ++ Manufacturer) of
        {ok, {_, _, Result}} ->
            inets:stop(),
            {struct, Json} = mochijson2:decode(Result),
            {_, Data} = proplists:get_value("data", Json),
            {_, Negotiator} = proplists:get_value("negotiator", Data),
            Name = proplists:get_value("name", Negotiator),
        	Host = proplists:get_value("host", Negotiator),
        	Port = proplists:get_value("port", Negotiator),
        	#{name => Name, host => Host, port => Port};
        _ ->
            inets:stop()
    end.
