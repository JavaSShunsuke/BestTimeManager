    function copyToDialog(row){
        document.getElementById('update_id').value = row.cells[0].firstChild.data;
        document.getElementById('update_name').value = row.cells[1].firstChild.data;
        }

//    function searchEventInEvent(){
//    searchEventName = document.getElementById('searchEventName').value;
//        $.ajax({
//        type: "POST",
//        url: "/search_event_inEvent",
//        data: { searchEventName:searchEventName }
//        }).done(function( msg ) {
//        console.log(msg)
//            $("#event_name_body").empty();
//            var str = JSON.stringify(msg);
//            for(var i=0; i<msg.length; i++){
//            var eventId = msg[i].eventId;
//            var eventName = msg[i].eventName;
//
//            $obj = $("#event_name_row").clone();
//             $obj.find("[name=eventId]").text(eventId);
//             $obj.find("[name=eventName]").text(eventName);
//             $obj.find("[name=clone_button]").removeAttr('hidden');
//
//             $("#event_name_body").append($obj);
//             }
//             });
//             }
//    function searchPlayerInPlayer(){
//    searchPlayerName = document.getElementById('searchPlayerName').value;
//        $.ajax({
//        type: "POST",
//        url: "/search_player_inPlayer",
//        data: { searchPlayerName:searchPlayerName }
//        }).done(function( msg ) {
//            $("#player_name_body").empty();
//            var str = JSON.stringify(msg);
//            for(var i=0; i<msg.length; i++){
//            var playerId = msg[i].playerId;
//            var playerName = msg[i].playerName;
//
//
//            $obj = $("#player_name_row").clone();
//            $obj.find("[name=playerId]").text(playerId);
//            $obj.find("[name=playerName]").text(playerName);
//            $obj.find("[name=clone_button]").removeAttr('hidden');
//            $obj.find("[id=clonePlayerId]").text(playerId);
//            let href = $obj.find("[name=clone_href]").attr('href');
//            $obj.find("[name=clone_href]").attr('href', "/recordlist/"+playerId);
//            $obj.find("[name=clone_href]").text(playerName);
//            $obj.find("[name=clone_href]").removeAttr('hidden');
//            console.log($obj.find("[name=playerId]"));
//            $("#player_name_body").append($obj);
//            }
//            });
//            }