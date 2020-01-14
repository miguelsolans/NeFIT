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
            io:format("Connection ~p teardown~n", [Sock]);
        {tcp_error, _, _} ->
            gen_tcp:close(Sock)
    end.

% function that verifies the credentials for authentication
loginHandler(Sock, Username, Password) ->
    case clients_state:login(Username, Password) of
        {ok, UserType} ->
            sender:sendAuthResponse(Sock, UserType, 'RESPONSE', true, "LOGGED IN");
            % here I'll invoke an UserHandler
        {error, UserType} ->
            sender:sendAuthResponse(Sock, UserType, 'RESPONSE', false, "INVALID LOGIN"),
            authentication(Sock)
    end.

% function that tries to register a client
registerHandler(Sock, Username, Password, UserType) ->
    case clients_state:register(Username, Password, UserType) of
        {ok, UT} ->
            sender:sendAuthResponse(Sock, UT, 'RESPONSE', true, "USER CREATED"),
            authentication(Sock);
        {user_exists, UT} ->
            sender:sendAuthResponse(Sock, UT, 'RESPONSE', false, "USER EXISTS"),
            authentication(Sock)
    end.
