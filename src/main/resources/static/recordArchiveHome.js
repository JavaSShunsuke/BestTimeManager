window.onload=function(){
var table=document.getElementById('record_archive_body');
for(var i=0; i<table.rows.length; i++){
if(table.rows[i].cells[6].innerHTML == 'true' && table.rows[i].cells[7].innerHTML=='true'){
table.rows[i].className="table-success"
}
}
        let lineCtx = document.getElementById("lineChart");
        var table = document.getElementById('record_archive_body');
        const aryDate=[];
        const aryRecord=[];
        var MAX ='00:00:00';
        for(var i = 0; i < table.rows.length; i++){
        aryDate.push(table.rows[i].cells[4].firstChild.data);
        var record = table.rows[i].cells[5].firstChild.data;
        console.log(MAX);
        var max = Number(MAX.substring(0,2)+MAX.substring(3,5)+MAX.substring(6,8))
        var x = Number(record.substring(0,2)+record.substring(3,5)+record.substring(6,8))
        if(x>max){
        MAX=record;
        }
        aryRecord.push(table.rows[i].cells[5].firstChild.data);
        }

        // 線グラフの設定
        let lineConfig = {
          type: 'line',
          data: {
            // ※labelとデータの関係は得にありません
            labels: aryDate,
            datasets: [{
              label: 'Red',
              data: aryRecord,
              borderColor: '#f88',
            }],
          },
          options: {
          plugins:{
          legend:{
                      display:false,
                      },
                      },
            scales: {
            y:{
            max:MAX,

                    type: 'time',
                    time: {
                      parser: "mm:ss:SS", //<- use 'parser'
                      unit: 'millisecond',
                      stepSize: 10000,
                      displayFormats: {
                        millisecond: 'mm:ss:SS'
                      }
                      }
              }
            },
        }
        };
        let lineChart = new Chart(lineCtx, lineConfig);
}
