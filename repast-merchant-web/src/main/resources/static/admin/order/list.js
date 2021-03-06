$(function () {
	 $('#startTime,#endTime').datetimepicker({
        language: 'zh-CN',
        startDate: "2017-09-09",
        endDate:new Date(),
        autoclose:true,
        todayBtn:true,
        todayHighlight:true,
        startView: 2,
        minView: 2,
        maxView: 3,
        format: 'yyyy-mm-dd'
     })
	 var dataTable=null;
	 $("#addBtn").click(function(){
		location.href=ctx+'/order/edit'; 
	 });
	 window.operateEvents={
	    "click #edit":function(e,value, row, index){
	    	location.href=ctx+'/order/view/'+row.id; 
		    return false;
	    },"click #toggle":function(e,value, row, index){
	    	var toggleTips=$(this).text();
	    	var status=toggleTips=="取消"?"cancel":"settle";
	    	toastrC.confirm({ message: "确认要"+toggleTips+"该订单嘛？" }).on(function (e) {
	    		if (!e) { return; }
	    		$.ajax({
		    		url:ctx+'/order/toggle.do',
		    		type:'POST',
		    		data:{id:row.id,status:status},
		    		success:function(rsp){
		    			if(rsp.status==200){
		    				toastr.success(toggleTips+"成功");
		    				doQuery();
		    			}else{
		    				toastr.error("服务器繁忙，请稍后再试");
		    			}
		    		}
		    	});
	    	});
		    return false;
	    }
     } 
	 var url = ctx+"/order/listData.do?random="+Math.random();
	 var columns= [
	        { field: '',title: '序号', formatter: function (value, row, index) { return index+1; }  },
	        { field : 'id',title : '编号',visible:false}, 
	        { field : 'orderId',title : '订单号',}, 
	        { field : 'deskNum', title : '桌号',},
	        { field : 'peopleCount', title : '人数', }, 
	        { field : 'amount', title : '消费金额',},
	        { field : 'statusName',title : '状态',}, 
	        { field : 'createTime',title : '下单时间', formatter : function (value, row, index){ return new Date(value).Format('yyyy-MM-dd hh:mm:ss');}}, 
	        { field : 'opers',title: '操作',events:operateEvents, formatter : operateFormatter }]
	  function operateFormatter(value, row, index) {
		  var toggle="";
		  if(row.status=="await"||row.status=="ing"||row.status=="already"){
			  toggle="取消";
		  }else if(row.status=="awaitPay"){
			  toggle="结算";
		  }
	      return ['<button type="button" class="btn btn-primary btn-circle" id="edit">查看</button>&nbsp;&nbsp;'+
	    	  (toggle==""?'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;':'<button type="button" class="btn btn-warning btn-circle"  id="toggle">'+toggle+'</button>')].join("");
	  }
	  loadTable(url,columns,function(options){
		 options.rowStyle= function (row, index) {
             var strclass = "";
             if (row.statusName == "取消") {
                 strclass = 'danger';
             }else{
                 strclass = 'success';
             }
             return { classes: strclass }
          }
          dataTable=$('#dataTable').bootstrapTable(options);
	  });
});

