% encapsulated module
% interface functions that access to the actor of this MODULE

-module(clients_state_manager).
-export([start/0, start/1, login/2, logout/1, register/3, isOnline/1]).

% register module as a process and start it, as well as start a consumer process
start() ->
    negotiations_consumer:start(),
    register(?MODULE, spawn(fun() -> clientsLoop(#{}) end)).

% register module as a process and start it with received Data
start(Data) ->
    register(?MODULE, spawn(fun() -> clientsLoop(Data) end)).

% function that contacts process to register an account
register(Username, Passwd, UserType) ->
    ?MODULE ! {register, Username, Passwd, UserType, self()},
    receive
        {?MODULE, Res, UT} -> {Res, UT}
    end.

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

% function that checks if a user is authenticated
isOnline(Username) ->
    ?MODULE ! {online, Username, self()},
    receive
        {?MODULE, Res} -> Res
    end.

% process that runs indefinitely and keeps the users state
clientsLoop(StateMap) ->
    receive
        {register, U, P, UT, From} ->
            case maps:find(U, StateMap) of
                error ->
                    From ! {?MODULE, ok, UT},
                    io:format("Sign Up: ~s -> ~s~n",[U,UT]),
                    clientsLoop(maps:put(U, {P, UT, false, 0}, StateMap));
                _ ->
                    From ! {?MODULE, user_exists, UT},
                    clientsLoop(StateMap)
            end;
        {login, U, P, From} ->
            case maps:find(U, StateMap) of
                error ->
                    From ! {?MODULE, error, user_not_found},
                    clientsLoop(StateMap);
                {ok, {P, UT, _, _}} ->
                    From ! {?MODULE, ok, UT},
                    io:format("Sign In: ~s~n",[U]),
                    clientsLoop(maps:put(U, {P, UT, true, From}, StateMap));
                {ok, _} ->
                    From ! {?MODULE, error, wrong_password},
                    clientsLoop(StateMap)
            end;
        {logout, U, From} ->
            case maps:find(U, StateMap) of
                error ->
                    From ! {?MODULE, error},
                    clientsLoop(StateMap);
                {ok, {P, UT, _, _}} ->
                    From ! {?MODULE, ok},
                    io:format("Logout: ~s~n",[U]),
                    clientsLoop(maps:put(U, { P, UT, false, 0}, StateMap))
            end;
        {online, U, From} ->
            case maps:find(U, StateMap) of
                error ->
                    From ! {?MODULE, error},
                    clientsLoop(StateMap);
                {ok, {_, _, _, Pid}} ->
                    From ! {?MODULE, Pid},
                    clientsLoop(StateMap)
            end
    end.
