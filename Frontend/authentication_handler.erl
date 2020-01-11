
% function that hendles the authentication process
authenticationHandler(Sock) ->
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
                    UserType = maps:get(userType, Msg),
                    registerHandler(Sock, User, Pass, UserType)
            end;
        {tcp_closed, _} ->
            io:format("Connection ~p teardown~n", [Sock]);
        {tcp_error, _, _} ->
            gen_tcp:close(Sock)
    end.

% function that verifies the credentials for authentication
loginHandler(Sock, Username, Password) ->
    case login_manager:login(Username, Password) of
        {ok, UserType} ->
            sender:sendAuthResponse(Sock, UserType, 'RESPONSE', true, "LOGGED IN");
            % here I'll invoke an UserHandler
        {error, UserType} ->
            sender:sendAuthResponse(Sock, UserType, 'RESPONSE', false, "INVALID LOGIN"),
            authenticationHandler(Sock)
    end.

% function that tries to register a client
registerHandler(Sock, Username, Password, UserType) ->
    case login_manager:register(Username, Password, UserType) of
        {ok, UT} ->
            sender:sendAuthResponse(Sock, UT, 'RESPONSE', true, "USER CREATED"),
            authenticationHandler(Sock);
        {user_exists, UT} ->
            sender:sendAuthResponse(Sock, UT, 'RESPONSE', false, "USER EXISTS"),
            authenticationHandler(Sock)
    end.
