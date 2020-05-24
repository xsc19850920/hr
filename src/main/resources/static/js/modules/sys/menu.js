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
        showList: true,
        title: null,
        menu:{
            parentName:null,
            parentId:0,
            type:1,
            orderNum:0
        }
    },
    methods: {
        getMenu: function(){
            //加载菜单树
            $.get(baseURL + "/sys/menu/select", function(r){
            	r.menuList.forEach(v=>{  
            		if(v.id === '0'){
            			 v.name =  vm.$t('sys.menu.firstmenu');
            		}else{
            			v.name =  vm.$t(v.name);
            		}
            	});
                ztree = $.fn.zTree.init($("#menuTree"), setting, r.menuList);
                var node = ztree.getNodeByParam("id", vm.menu.parentId);
                ztree.selectNode(node);
                vm.menu.parentName = node.name;
            })
        },
        add: function(){
            vm.showList = false;
            vm.title = this.$t('sys.menu.save');
            vm.menu = {parentName:null,parentId:0,type:1,orderNum:0};
            vm.getMenu();
        },
        update: function () {
            var menuId = getMenuId();
            if(menuId == null){
                return ;
            }

            $.get(baseURL + "/sys/menu/info/"+menuId, function(r){
                vm.showList = false;
                vm.title =vm.$t('sys.menu.update');
                
                if(r.menu.parentId === '0'){
                	r.menu.parentName =  vm.$t('sys.menu.firstmenu');
	       		}
//	       		r.menu.name =  vm.$t(r.menu.name);
                vm.menu = r.menu;
//                vm.menu.parentName = r.menu.parentName;
                vm.getMenu();
            });
        },
        del: function () {
            var menuId = getMenuId();
            if(menuId == null){
                return ;
            }

            confirm(vm.$t('sys.menu.deletealert'), function(){
                $.ajax({
                    type: "POST",
                    url: baseURL + "/sys/menu/delete",
                    data: "menuId=" + menuId,
                    success: function(r){
                        if(r.code === 0){
                            alert(vm.$t('sys.menu.success'), function(){
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
            var url = vm.menu.id == null ? "/sys/menu/save" : "/sys/menu/update";
            $.ajax({
                type: "POST",
                url: baseURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.menu),
                success: function(r){
                    if(r.code === 0){
                        alert(vm.$t('sys.menu.success'), function(){
                            vm.reload();
                        });
                    }else{
                        alert(r.msg);
                    }
                }
            });
        },
        menuTree: function(){
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: vm.$t('sys.menu.choosemenu'),
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#menuLayer"),
                btn: [vm.$t('sys.menu.confirm'), vm.$t('sys.menu.cancel')],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();
                    //选择上级菜单
                    vm.menu.parentId = node[0].id;
                    vm.menu.parentName = node[0].name;

                    layer.close(index);
                }
            });
        },
        reload: function () {
            vm.showList = true;
            Menu.table.refresh();
        }
    }
});

var Menu = {
    id: "menuTable",
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Menu.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        /*{title: '菜单id', field: 'id', visible: false, align: 'center', valign: 'middle', width: '80px'},*/
        {title: vm.$t('sys.menu.menuname'), field: 'name', align: 'center', valign: 'middle', sortable: true, width: '160px', formatter: function(item, index){
        	return vm.$t(item.name) ;
        }},
        {title: vm.$t('sys.menu.parentmenu'), field: 'parentName', align: 'center', valign: 'middle', sortable: true, width: '160px', formatter: function(item, index){
        	return vm.$t(item.parentName) ;
        }},
        {title: vm.$t('sys.menu.icon'), field: 'icon', align: 'center', valign: 'middle', sortable: true, width: '60px', formatter: function(item, index){
            return item.icon == null ? '' : '<i class="'+item.icon+' fa-lg"></i>';
        }},
        {title: vm.$t('sys.menu.type'), field: 'type', align: 'center', valign: 'middle', sortable: true, width: '60px', formatter: function(item, index){
            if(item.type === 0){
                return '<span class="label label-primary">'+vm.$t('sys.menu.dirc')+'</span>';
            }
            if(item.type === 1){
                return '<span class="label label-success">'+vm.$t('sys.menu.menu')+'</span>';
            }
            if(item.type === 2){
                return '<span class="label label-warning">'+vm.$t('sys.menu.button')+'</span>';
            }
        }},
        {title: vm.$t('sys.menu.order'), field: 'orderNum', align: 'center', valign: 'middle', sortable: true, width: '60px'},
        {title: vm.$t('sys.menu.menuurl'), field: 'url', align: 'center', valign: 'middle', sortable: true, width: '160px'},
        {title: vm.$t('sys.menu.id'), field: 'perms', align: 'center', valign: 'middle', sortable: true, width: '160px'}]
    return columns;
};

function getMenuId () {
    var selected = $('#menuTable').bootstrapTreeTable('getSelections');
    if (selected.length == 0) {
        alert(vm.$t('sys.menu.alert'));
        return;
    } else {
        return selected[0].id;
    }
}

$(function () {
    var colunms = Menu.initColumn();
    var table = new TreeTable(Menu.id, baseURL + "/sys/menu/list", colunms);
    table.setExpandColumn(2);
    table.setIdField("id");
    table.setCodeField("id");
    table.setParentCodeField("parentId");
    table.setExpandAll(false);
    table.init();
    Menu.table = table;
});
