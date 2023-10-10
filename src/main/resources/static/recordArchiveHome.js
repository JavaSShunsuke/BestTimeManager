window.onload=function(){
    var table=document.getElementById('record_archive_body');

    for(var i=0; i<table.rows.length; i++){
        if(table.rows[i].cells[6].innerHTML == 'true'){
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
            layout:{
              autoPadding:true,
            },
            interaction: {
              mode: 'nearest',
              intersect:false,
            },
            plugins:{
              tooltip: {
                callbacks: {
                  label: function(context){	// 引数は1つ
                    let label = context.label || '';
                    if (context.formattedValue !== null) {
                        label = context.formattedValue;	// 単位
                    }
                    return label;
                  },
                },
              },
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
                  stepSize: 1000,
                  tooltipFormat:'mm:ss:SS',
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
