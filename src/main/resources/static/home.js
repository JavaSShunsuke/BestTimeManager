    function copyToDialog(row){
        document.getElementById('update_id').value = row.cells[0].firstChild.data;
        document.getElementById('update_name').value = row.cells[1].firstChild.data;
        }
    function outputRecordTime(){
        var formObj = document.record_form;
        var minute = formObj.minute.value;
        var second = formObj.second.value;
        var millisecond = formObj.millisecond.value;

        while(minute.length<2){
        minute = "0"+minute
        }
        while(second.length<2){
        second = "0"+second
        }
        while(millisecond.length<2){
        millisecond = "0"+ millisecond
        }

        var recordTime = minute + ":" + second + ":" + millisecond;
        formObj.recordTime.value = recordTime;
        formObj.minute.disabled=true;
        formObj.second.disabled=true;
        formObj.millisecond.disabled=true;
        }

    function copyToUpdateRecordDialog(row){
        document.getElementById('update_recordId').value = row.cells[0].firstChild.data;
        document.getElementById('update_playerId').value = row.cells[1].firstChild.data;
        document.getElementById('update_eventId').value = row.cells[2].firstChild.data;
        document.getElementById('update_minute').value = row.cells[4].firstChild.data.substring(0,2);
        document.getElementById('update_second').value = row.cells[4].firstChild.data.substring(3,5);
        document.getElementById('update_millisecond').value = row.cells[4].firstChild.data.substring(6,8);
        document.getElementById('title_eventId').innerText = row.cells[3].firstChild.data +"の記録の更新";
        }
    function updateRecordTime(){
        var formObj = document.updateRecord_form;
        var minute = formObj.minute.value;
        var second = formObj.second.value;
        var millisecond = formObj.millisecond.value;

        while(minute.length<2){
        minute = "0"+minute;
        }
        while(second.length<2){
        second = "0"+second;
        }
        while(millisecond.length<2){
        millisecond =millisecond+"0";
        }

        var recordTime = minute + ":" + second + ":" + millisecond;
        formObj.recordTime.value = recordTime;
        formObj.minute.disabled=true;
        formObj.second.disabled=true;
        formObj.millisecond.disabled=true;
        }
//ラップタイム
    function copyToAddLapTimeDialog(row){
        document.getElementById('addLapTime_recordId').value = row.cells[0].firstChild.data;
        document.getElementById('addLapTime_playerId').value = row.cells[1].firstChild.data;
         }
    function addLapTime(){
        var formObj = document.addLapTime_form;
        var minute = formObj.minute.value;
        var second = formObj.second.value;
        var millisecond = formObj.millisecond.value;

        while(minute.length<2){
        minute = "0"+minute;
        }
        while(second.length<2){
        second = "0"+second;
        }
        while(millisecond.length<2){
        millisecond =millisecond+"0";
        }

        var lapTimeRecord = minute + ":" + second + ":" + millisecond;
        formObj.lapTimeRecord.value = lapTimeRecord;
        formObj.minute.disabled=true;
        formObj.second.disabled=true;
        formObj.millisecond.disabled=true;
        }
    function ListLapTimeDialog(row){

                var recordId = row.cells[0].firstChild.data
                $.ajax({
                  type: "POST",
                  url: "/listlaptime",
                  data: { recordId:recordId }
                }).done(function( msg ) {
                $("#listLapTime_body").empty();
                  var str = JSON.stringify(msg);
                  for(var i=0; i<msg.length; i++){
                    var lapTimeId = msg[i].lapTimeId;
                    var recordId = msg[i].recordId;
                    var lapTimeNum = msg[i].lapTimeNum;
                    var lapTimeRecord = msg[i].lapTimeRecord;
                    var lapTimeMemo = msg[i].lapTimeMemo

                    $obj = $("#table_lapTime_row").clone();
                    $obj.find("[name=lapTimeId]").text(lapTimeId);
                    $obj.find("[name=recordId]").text(recordId);
                    $obj.find("[name=lapTimeNum]").text(lapTimeNum);
                    $obj.find("[name=lapTimeRecord]").text(lapTimeRecord);
                    $obj.find("[name=lapTimeMemo]").text(lapTimeMemo);
                    $obj.find("[name=deleteLapTime_button]").removeAttr('hidden');
                    $("#listLapTime_body").append($obj);
                  }

                });
            }

    function deleteLapTime(row){
            var lapTimeId = row.cells[0].firstChild.data
            var recordId = row.cells[1].firstChild.data
            row.hidden=true;
            $.ajax({
              type: "POST",
              url: "/deletelaptime",
              data: { lapTimeId:lapTimeId,recordId:recordId}
              })
                }