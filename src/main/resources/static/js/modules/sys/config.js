$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + '/sys/config/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true ,hidden: true},
			{ label: '键', name: 'configKey',  width: 80 },
			{ label: '值', name: 'configValue',  width: 80 },
			{ label: '状态', name: 'status', width: 80, sortable:false, formatter: function(value, options, row){
				return value === 0 ? 
					'<span class="label label-danger">隐藏</span>' : 
					'<span class="label label-success">显示</span>';
			}},
			{ label: '备注', name: 'remark', index: 'remark', width: 80 }
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

	new AjaxUpload('#upload', {
		action: baseURL + '/sys/config/importExcel?X-Token=' + token,
		name: 'file',
		autoSubmit:true,
		responseType:"json",
		onSubmit:function(file, extension){
			layer.load(2);
			if (!(extension && /^(xls)$/.test(extension.toLowerCase()))){
                alert('只支持xls格式的文件！');
                return false;
            }
		},
		onComplete : function(file, r){
			layer.closeAll('loading');
			if(r.code == 0){
				alert(r.msg);
				vm.reload();
			}else{
				alert(r.msg);
			}
		}
	});
});
var vm = new Vue({
	el:'#rapp',
	i18n,
	data:{
		showList: true,
		title: null,
		config: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.config = {};
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
			var url = vm.config.id == null ? "/sys/config/save" : "/sys/config/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.config),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "/sys/config/delete",
                    contentType: "application/json",
				    data: JSON.stringify(ids),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		getInfo: function(id){
			$.get(baseURL + "/sys/config/info/"+id, function(r){
                vm.config = r.profiles;
            });
		},
		reload: function (event) {
			vm.showList = true;
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:1
            }).trigger("reloadGrid");
		},
		download: function (){
			var url=baseURL + "/sys/config/download?X-Token="+token;
			window.location.href=url;
		},
	}
});