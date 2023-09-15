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
        }
        function updateRecordTime(){
            var formObj = document.updateRecord_form;
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