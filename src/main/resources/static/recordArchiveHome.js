window.onload=function(){
var table=document.getElementById('record_archive_body');
for(var i=0; i<table.rows.length; i++){
if(table.rows[i].cells[6].innerHTML == 'true' && table.rows[i].cells[7].innerHTML=='true'){
table.rows[i].className="table-success"
}
}
}
