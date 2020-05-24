$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + '/sys/user/list',
        datatype: "json",
        colModel: [			
			{ label: '用户id', name: 'id', index: 'id', key: true, hidden: true },
			{ label: '用户名', name: 'username', width: 75 ,sortable:false},
            { label: '别名', name: 'nickname', width: 75,sortable:false },
			{ label: '邮箱', name: 'email', width: 90 ,sortable:false},
			{ label: '手机号码', name: 'mobile', width: 100 ,sortable:false},
			{ label: '状态', name: 'status',sortable:false, width: 80, formatter: function(value, options, row){
				return value === 0 ? 
					'<span class="label label-danger">禁用</span>' : 
					'<span class="label label-success">正常</span>';
			}},
			{name:'createTime',index:'create_time',label:"创建时间",  width:80},
//			{ label: '操作', name: 'opt',  width: 80,sortable:false}
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
        	////$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
//        	var ids = $("#jqGrid").jqGrid('getDataIDs');
//	        for (var i = 0; i < ids.length; i++) {
//	          var id = ids[i];
//	          var rowData = "";
//	          var editBtn = "<a onclick='vm.getUser("+ i +")'>修改</a>  ";
//	          var delBtn = "<a  onclick='vm.del("+ i +")'>刪除<a>";
//	          if(hasPermission('sys:user:update')){
//	        	  rowData += editBtn;
//	          }
//	          if(hasPermission('sys:user:info')){
//	        	  rowData += delBtn;
//	          }
//	          $("#jqGrid").jqGrid('setRowData', ids[i], { opt: rowData});
//	        }
        }
    });
});

var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url:"nourl"
        }
    }
};
var ztree;

var vm = new Vue({
	el:'#rapp',
	i18n,
	data:{
		q:{
            keyword: null
		},
		showList: true,
		title:null,
		roleList:{},
		user:{
			status:1,
            deptId:null,
            deptName:null,
			roleIdList:[],
            teamPermissionIds:[]
		},
		teamPermissionIds:{"CN":"中国","JP":"日本","KR":"韩国","EN":"英国"}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.roleList = {};
			vm.user = {deptName:null, deptId:null, status:1, roleIdList:[],teamPermissionIds:[]};
			
			//获取角色信息
			this.getRoleList();

            vm.getDept();
		},
        getDept: function(){
            //加载部门树
            $.get(baseURL + "/sys/dept/list", function(r){
                ztree = $.fn.zTree.init($("#deptTree"), setting, r);
                var node = ztree.getNodeByParam("id", vm.user.deptId);
                if(node != null){
                    ztree.selectNode(node);

                    vm.user.deptName = node.name;
                }
            });
        },
		update: function () {
			var userId = getSelectedRow();
			if(userId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
			
			vm.getUser(userId);
			//获取角色信息
			this.getRoleList();
		},
		del: function (i) {
            var ids = $("#jqGrid").jqGrid('getDataIDs');
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "/sys/user/delete",
                    contentType: "application/json",
				    data: JSON.stringify([ids[i]]),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(){
                                vm.reload();
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		saveOrUpdate: function () {
			let url = vm.user.id == null ? "/sys/user/save" : "/sys/user/update";
			try{
				vm.user.teamPermissionIds = vm.user.teamPermissionIds.join()
				$.ajax({
					type: "POST",
					url: baseURL + url,
					contentType: "application/json",
					data: JSON.stringify(vm.user),
					success: function(r){
						if(r.code === 0){
							alert('操作成功', function(){
								vm.reload();
							});
						}else{
							alert(r.msg);
						}
					}
				});
			}
			catch (e) {
				console.error("保存或者更新用户失败",e)
			}
		},
		getUser: function(i){
			vm.showList = false;
            vm.title = "修改";
			$.get(baseURL + "/sys/user/info/"+i, function(r){
				vm.user = r.user;
				vm.user.password = null;
				if(typeof(r.user.teamPermissionIds) != "undefined"){
					vm.user.teamPermissionIds = r.user.teamPermissionIds.split(",");
				}
				else {
					vm.user.teamPermissionIds = []
				}
			});
            this.getRoleList();
		},
		getRoleList: function(){
			$.get(baseURL + "/sys/role/select", function(r){
				vm.roleList = r.list;
			});
		},
		reload: function () {
			vm.showList = true;
			$("#jqGrid").jqGrid('setGridParam',{ 
                postData:{'keyword': vm.q.keyword},
                page:1
            }).trigger("reloadGrid");
		}
	}
});
