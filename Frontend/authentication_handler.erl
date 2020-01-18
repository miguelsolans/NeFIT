-module(authentication_handler).
-export ([authentication/1]).

% function that hendles the authentication process
authentication(Sock) ->
    receive
        {tcp, Sock, Data} ->
            Msg = protocol:decode_msg(Data, 'Message'),
            Cli = maps:get(user, Msg),
            User = maps:get(username, Cli),
            Pass = maps:get(password, Cli),
            case maps:get(type, Msg) of
                'LOGIN' ->
                    loginHandler(Sock, User, Pass);
                'REGISTER' ->
                    UserType = maps:get(user_type, Msg),
                    registerHandler(Sock, User, Pass, UserType)
            end;
        {tcp_closed, _} ->
            io:format("Connection ~p teardown~n", [Sock]),
            exit(normal);
        {tcp_error, _, _} ->
            io:puts("An error occured in the connection~n"),
            exit(normal)
    end.

% function that verifies the credentials for authentication
loginHandler(Sock, Username, Password) ->
    case clients_state_manager:login(Username, Password) of
        {ok, UserType} ->
            sender_handler:sendAuthResponse(Sock, UserType, "LOGGED IN"),
            user_manager:loop(Sock, Username);
        {error, user_not_found} ->
            sender_handler:sendInvalidAuthResponse(Sock, "INVALID LOGIN; USER NOT FOUND"),
            authentication(Sock);
        {error, wrong_password} ->
            sender_handler:sendInvalidAuthResponse(Sock, "INVALID LOGIN; WRONG PASSWORD"),
            authentication(Sock)
    end.

% function that tries to register a client
registerHandler(Sock, Username, Password, UserType) ->
    case clients_state_manager:register(Username, Password, UserType) of
        {ok, UT} ->
            sender_handler:sendAuthResponse(Sock, UT, "USER CREATED"),
            authentication(Sock);
        {user_exists, UT} ->
            sender_handler:sendInvalidAuthResponse(Sock, "USER EXISTS"),
            authentication(Sock)
    end.
