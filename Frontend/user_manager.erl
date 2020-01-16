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
            case UT of
                "MANUFACTURER" ->
                    IPO = maps:get(item_production_offer, Msg),
                    %negotiations_manager:add_production_offer(User, IPO),
                    % send response
                    loop(Sock,User);
                "IMPORTER" ->
                    IOO = maps:get(item_order_offer, Msg);
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
