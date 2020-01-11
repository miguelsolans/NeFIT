-module(sender).
-export ([sendAuthResponse/5]).

% function that sends an authentication response to the correspondent client
sendAuthResponse(Sock, Type, UserType, Result, Description) ->
    Resp = protocol:encode_msg(#{userType => UserType, type => Type, state => #{result => Result, description => Description}}, 'Message'),
    io:format("data ~p", [Resp]),
    gen_tcp:send(Sock, Resp).
