$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + '/sys/log/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', key: true, hidden: true },
			{ label: '用户名', name: 'username',index:'username', width: 50 }, 			
			{ label: '用户操作', name: 'operation',index:'operation', width: 70 }, 			
			{ label: '请求方法', name: 'method',index:'method', width: 150 }, 			
			{ label: '请求参数', name: 'params',index:'params', width: 80 },
            { label: '执行时长(毫秒)', name: 'time', index:'time',width: 80 },
			{ label: 'IP地址', name: 'ip', width: 70  ,index:'ip',}, 			
			{ label: '创建时间', name: 'createTime',index: 'create_time', width: 90 }
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList: [10, 30, 50,100],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	//$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
    
    
//    $('.summernote').summernote({
//    	lang : 'zh-CN',
//    	callbacks: { // 覆写掉自带的上传文件函数
//	        onImageUpload: function(files, editor, $editable) {
//	        	var formData = new FormData();
//	            formData.append("file", files[0]);
//	            $.ajax({
//	                data: formData,  
//	                type: "POST",  
//	                url: baseURL+ "/sys/attachment/upload",
//	                cache: false,  
//	                contentType: false,  
//	                processData: false,  
//	                success: function(image) {  
//	                    $('.summernote').summernote('editor.insertImage',image.src);  
//	                },  
//	                error: function() {  
//	                	 alert("插入失败");
//	                }  
//	            })
//	        }
//    	}
//    });
});

var vm = new Vue({
	el:'#rapp',
	i18n,
	data:{
		q:{
			key: null
		},
		log:{}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		reload: function (event) {
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData:{'username': vm.q.key},
                page:1
            }).trigger("reloadGrid");
		},
		getLog: function (logId){
            
        },
		info: function (){
            var id = getSelectedRow();
            if(id == null){
                return ;
            }
            $.get(baseURL + '/sys/log/info/'+id, function(r){
                vm.log = r.log;
                openLayer('900px', '600px', '查看附件', 'logInfoLayer');
                $('.summernote').summernote('disable');
                $('.summernote').summernote('code',vm.log.params);
            });
           
        }
	}
});