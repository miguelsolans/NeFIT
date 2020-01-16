-module(user_manager).
-export ([loop/2, sendMsg/2]).

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
                            IPO = maps:get(item_production_offer, Msg),
                            %negotiations_manager:add_production_offer(User, IPO),
                            % send response
                            loop(Sock, User);
                        _ ->
                            sender_handler:sendInvalidOperation(Sock, UT),
                            loop(Sock, User)
                    end;

                'ITEMORDEROFFER' ->
                    case UT of
                        "IMPORTER" ->
                            IOO = maps:get(item_order_offer, Msg),
                            % send response
                            loop(Sock,User);
                        _ ->
                            sender_handler:sendInvalidOperation(Sock, UT),
                            loop(Sock, User)
                    end;

                _ ->
                    clients_state_manager:logout(User),
                    exit(normal)
            end;

        {send_msg, Data} ->
            sender_handler:sendEncoded(Sock, Data),
            loop(Sock, User);

        {tcp_closed, Sock} ->
            clients_state_manager:logout(User),
            exit(normal);

        {tcp_error, _, _} ->
            clients_state_manager:logout(User),
            exit(normal)
  end.
