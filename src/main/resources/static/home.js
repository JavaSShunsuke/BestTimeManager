    //ダイアログニにデータを渡す
    function copyToDialog(row){
        document.getElementById('update_id').value = row.cells[0].firstChild.data;
        document.getElementById('update_name').value = row.cells[1].firstChild.data;
        }
