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
                            %IPO = maps:get(item_production_offer, Msg),
                            U = maps:get(user, Msg),
                            Username = maps:get(username, U),
                            NPid = negotiations_manager:findNegotiator(Username),
                            % Send to NPid the order
                            % send response to user
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
                            % send to NPid the order
                            % send response to user
                            loop(Sock,User);
                        _ ->
                            sender_handler:sendInvalidOperation(Sock, UT),
                            loop(Sock, User)
                    end
            end;
        {send_msg, Data} ->
            sender_handler:sendEncoded(Sock, Data),
            loop(Sock, User);
        {tcp_closed, Sock} ->
            supervisor_manager:exit(?MODULE, {User, Sock});
        {tcp_error, _, _} ->
            supervisor_manager:exit(?MODULE, {User, Sock})
  end.
