-module(user_manager).
-export ([loop/2]).

% user manager that runs a loop for mailbox message treatment
loop(Sock, User) ->
  receive
    {tcp, Sock, Data} ->
      Msg = protocol:decode_msg(Data, 'Message');
    {tcp_closed, Sock} ->
      clients_state_manager:logout(User),
      exit(normal);
    {tcp_error, _, _} ->
      clients_state_manager:logout(User),
      exit(normal)
  end.
