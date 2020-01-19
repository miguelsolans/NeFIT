-module(user_manager).
-export ([loop/2, sendMsg/2]).

% function for send a message to the client
sendMsg(Pid, Data) ->
    Pid ! {send_msg, Data}.

% user manager that runs a loop for mailbox message treatment
loop(Sock, User) ->
    receive
        {tcp, Sock, Data} ->
            Msg = protocol:decode_msg(Data, 'Message'),
            UT = maps:get(user_type, Msg),
            T = maps:get(type, Msg),
            case T of
                'ITEMPRODUCTIONOFFER' ->
                    case UT of
                        "MANUFACTURER" ->
                            U = maps:get(user, Msg),
                            Username = maps:get(username, U),
                            NPid = negotiations_manager:findNegotiator(Username),
                            case negotiations_producer:sendOrder(Data,NPid) of
                                ok ->
                                    sender_handler:sendOrderResponse(Sock,UT,true,"ITEM PRODUCTION OFFER SUCCEDED");
                                error ->
                                    sender_handler:sendOrderResponse(Sock,UT,false,"ITEM PRODUCTION OFFER DOES NOT SUCCEDED")
                            end,
                            loop(Sock, User);
                        _ ->
                            sender_handler:sendInvalidOperation(Sock, UT),
                            loop(Sock, User)
                    end;
                'ITEMORDEROFFER' ->
                    case UT of
                        "IMPORTER" ->
                            IOO = maps:get(item_order_offer, Msg),
                            Name = maps:get(manufacturer_name, IOO),
                            NPid = negotiations_manager:findNegotiator(Name),
                            case negotiations_producer:sendOrder(Data,NPid) of
                                ok ->
                                    sender_handler:sendOrderResponse(Sock,UT,true,"ITEM ORDER OFFER SUCCEDED");
                                error ->
                                    sender_handler:sendOrderResponse(Sock,UT,false,"ITEM ORDER OFFER DOES NOT SUCCEDED")
                            end,
                            loop(Sock,User);
                        _ ->
                            sender_handler:sendInvalidOperation(Sock, UT),
                            loop(Sock, User)
                    end;
                'LOGOUT' ->
                    clients_state_manager:logout(User),
                    sender_handler:sendAuthResponse(Sock, UT, "SIGN OUT"),
                    authentication_handler:authentication(Sock)
            end;
        {send_msg, Data} ->
            sender_handler:sendEncoded(Sock, Data),
            loop(Sock, User);
        {tcp_closed, Sock} ->
            supervisor_manager:exit(?MODULE, {User, Sock});
        {tcp_error, _, _} ->
            supervisor_manager:exit(?MODULE, {User, Sock})
  end.
