-module(sender_handler).
-export ([sendAuthResponse/5, sendInvalidAuthResponse/4, sendEncoded/2]).

% function that sends an authentication response to the correspondent client
sendAuthResponse(Sock, UserType, Type, Result, Description) ->
    Resp = protocol:encode_msg(#{user_type => UserType, type => Type, state => #{result => Result, description => Description}}, 'Message'),
    gen_tcp:send(Sock, Resp).

% function that sends an authentication response to the correspondent client
sendInvalidAuthResponse(Sock, Type, Result, Description) ->
    Resp = protocol:encode_msg(#{type => Type, state => #{result => Result, description => Description}}, 'Message'),
    gen_tcp:send(Sock, Resp).

% function that sends a message already encoded
sendEncoded(Sock, Msg) ->
    gen_tcp:send(Sock, Msg).
