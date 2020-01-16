-module(sender_handler).
-export ([sendAuthResponse/3, sendInvalidAuthResponse/2, sendEncoded/2, sendInvalidOperation/2]).

% function that sends an authentication response to the correspondent client
sendAuthResponse(Sock, UserType, Description) ->
    Resp = protocol:encode_msg(#{user_type => UserType, type => 'RESPONSE', state => #{result => true, description => Description}}, 'Message'),
    gen_tcp:send(Sock, Resp).

% function that sends an authentication response to the correspondent client
sendInvalidAuthResponse(Sock, Description) ->
    Resp = protocol:encode_msg(#{type => 'RESPONSE', state => #{result => false, description => Description}}, 'Message'),
    gen_tcp:send(Sock, Resp).

% function that sends a message already encoded
sendEncoded(Sock, Msg) ->
    gen_tcp:send(Sock, Msg).

% function that sends an invalid operation response
sendInvalidOperation(Sock, UserType) ->
    Op = protocol:encode_msg(#{user_type => UserType, type => 'RESPONSE', state => #{result => false, description => "YOU HAVE NO PERMISSION"}}, 'Message'),
    gen_tcp:send(Sock, Op).
