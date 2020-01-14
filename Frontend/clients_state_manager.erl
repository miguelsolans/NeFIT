% encapsulated module
% interface functions that access to the actor of this MODULE

-module(clients_state_manager).
-export([start/0, login/2, logout/1, register/3]).

% register module as a process and start it
start() ->
    register(?MODULE, spawn(fun() -> clientsLoop(#{}) end)).

% function that contacts process to verify the login
login(Username, Passwd) ->
    ?MODULE ! {login, Username, Passwd, self()},
    receive
        {?MODULE, Res, UT} -> {Res, UT}
    end.

% function that contacts process to logout a client
logout(Username) ->
    ?MODULE ! {logout, Username, self()},
    receive
        {?MODULE, Res} -> Res
    end.

% function that contacts process to register an account
register(Username, Passwd, UserType) ->
    ?MODULE ! {register, Username, Passwd, UserType, self()},
    receive
        {?MODULE, Res, UT} -> {Res, UT}
    end.

% process that runs indefinitely and keeps the users state
clientsLoop(StateMap) ->
    receive
        {register, U, P, UT, From} ->
            case maps:find(U, StateMap) of
                error ->
                    From ! {?MODULE, ok, UT},
                    clientsLoop(maps:put(U, {P, UT, false}, StateMap));
                _ ->
                    From ! {?MODULE, user_exists, UT},
                    clientsLoop(StateMap)
            end;
        {login, U, P, From} ->
            case maps:find(U, StateMap) of
                error ->
                    From ! {?MODULE, error, user_not_found},
                    clientsLoop(StateMap);
                {ok, {P, UT, _}} ->
                    From ! {?MODULE, ok, UT},
                    clientsLoop(maps:put(U, {P, UT, true}, StateMap))
            end;
        {logout, U, From} ->
            case maps:find(U, StateMap) of
                error ->
                    From ! {?MODULE, error},
                    clientsLoop(StateMap);
                {ok, {P, UT, _}} ->
                    From ! {?MODULE, ok},
                    clientsLoop(maps:put(U, { P, UT, false}, StateMap))
            end
    end.
