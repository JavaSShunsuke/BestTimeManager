//分、秒、ミリ秒の入力を一つにまとめ整形してinputタグのvalueに設定
function outputRecordTime(){
    var formObj = document.record_form;
    var minute = formObj.minute.value;
    var second = formObj.second.value;
    var millisecond = formObj.millisecond.value;
    minute = ("00" + minute).slice(-2);
    second = ("00" + second).slice(-2);
    millisecond = (millisecond+"00").slice(0,2);
    var recordTime = minute + ":" + second + ":" + millisecond;
    console.log();
    formObj.recordTime.value = recordTime;
    formObj.minute.disabled=true;
    formObj.second.disabled=true;
    formObj.millisecond.disabled=true;
}

//ダイアログに必要データを渡す
function copyToUpdateRecordDialog(row){
    document.getElementById('update_recordId').value = row.cells[0].firstChild.data;
    document.getElementById('update_playerId').value = row.cells[1].firstChild.data;
    document.getElementById('update_eventId').value = row.cells[2].firstChild.data;
    document.getElementById('update_addNowDate').value = row.cells[5].firstChild.data;
    document.getElementById('update_minute').value = row.cells[6].firstChild.data.substring(0,2);
    document.getElementById('update_second').value = row.cells[6].firstChild.data.substring(3,5);
    document.getElementById('update_millisecond').value = row.cells[6].firstChild.data.substring(6,8);
    document.getElementById('title_eventId').innerText = row.cells[3].firstChild.data +"の記録の更新";
}

//分、秒、ミリ秒の入力を一つにまとめ整形してinputタグのvalueに設定
function updateRecordTime(){
    var formObj = document.updateRecord_form;
    var minute = formObj.minute.value;
    var second = formObj.second.value;
    var millisecond = formObj.millisecond.value;
    minute = ("00"+minute).slice(-2);
    second = ("00"+second).slice(-2);
    millisecond = (millisecond+"00").slice(0,2);
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
    document.getElementById('addLapTimeLabel').innerText = row.cells[3].firstChild.data + "の記録";
}

//分、秒、ミリ秒の入力を一つにまとめ整形してinputタグのvalueに設定
function addLapTime(){
    var formObj = document.addLapTime_form;
    var minute = formObj.minute.value;
    var second = formObj.second.value;
    var millisecond = formObj.millisecond.value;
    minute = ("00"+minute).slice(-2);
    second = ("00"+second).slice(-2);
    millisecond = (millisecond+"00").slice(0,2);
    var lapTimeRecord = minute + ":" + second + ":" + millisecond;
    formObj.lapTimeRecord.value = lapTimeRecord;
    formObj.minute.disabled=true;
    formObj.second.disabled=true;
    formObj.millisecond.disabled=true;
}

//ラップタイムダイアログの制御
function ListLapTimeDialog(row){
    document.getElementById("title_listLapTime").innerText = row.cells[3].firstChild.data + "のラップタイム"
    var recordId = row.cells[0].firstChild.data
    $.ajax({
      type: "POST",
      url: "/list_lap_time",
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

//ラップタイムダイアログ内でのラップタイム削除機能
function deleteLapTime(row){
    var lapTimeId = row.cells[0].firstChild.data
    var recordId = row.cells[1].firstChild.data
    row.hidden=true;
    $.ajax({
      type: "POST",
      url: "/delete_lap_time",
      data: { lapTimeId:lapTimeId,recordId:recordId}
    })
}

//読み込み時必要データを整形
window.onload = function(){
    var now = new Date();
    var nowYear = now.getFullYear();
    var nowMonth = ('00'+(now.getMonth()+1)).slice(-2);
    var nowDay = ('00'+(now.getDate())).slice(-2);
    var text = nowYear + "-" + nowMonth + "-" + nowDay;
    document.getElementById("nowDate").value=text;
    let playerId=location.pathname.substring(12,20);
}
