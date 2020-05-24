//jqGrid的配置信息
$.jgrid.defaults.width = 1000;
$.jgrid.defaults.responsive = true;
$.jgrid.defaults.styleUI = 'Bootstrap';

//工具集合Tools
window.T = {};

// 获取请求参数
// 使用示例
// location.href = http://localhost/index.html?id=123
// T.p('id') --> 123;
var url = function (name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
};
T.p = url;

//请求前缀
var baseURL = getRootPath();
// var baseURL = "http://192.168.162.1:8889/bb/admin";

function getRootPath() {
    var pathName = parent.location.pathname;
    var projectName = pathName.substring(0, pathName.substr(1).lastIndexOf('/') + 1);
    return projectName;
}

//上传文件的映射地址
var uploadFileResource = baseURL + '/uploadFile';

//登录token
var token = localStorage.getItem("X-Token");
if (token == 'null') {
    parent.location.href = baseURL + '/login.html';
}

//jquery全局配置
$.ajaxSetup({
    dataType: "json",
    cache: false,
    headers: {
        "X-Token": token
    },
    xhrFields: {
        withCredentials: true
    },
    complete: function (xhr) {
        //无效的token，则跳转到登录页面
    	if(xhr.responseJSON){
    		if (xhr.responseJSON.code == 401) {
                parent.location.href = baseURL + '/login.html';
            }
    	}
        
    }
});

//jqgrid全局配置
$.extend($.jgrid.defaults, {
    ajaxGridOptions: {
        headers: {
            "X-Token": token
        }
    }
});
//权限判断
function hasPermission(permission) {
    if (window.parent.permissions.indexOf(permission) > -1) {
        return true;
    } else {
        return false;
    }
}
function reloand (){
    window.location.reload();
}
//重写alert
window.alert = function (msg, callback) {
    parent.layer.alert(msg, function (index) {
        parent.layer.close(index);
        if (typeof(callback) === "function") {
            callback("ok");
        }
    });
}

//重写confirm式样框
window.confirm = function (msg, callback) {
    parent.layer.confirm(msg, {btn: ['确定', '取消']},
        function () {//确定事件
            if (typeof(callback) === "function") {
                callback("ok");
            }
        });
}

//选择一条记录
function getSelectedRow() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if (!rowKey) {
        alert("请选择一条记录");
        return;
    }

    var selectedIDs = grid.getGridParam("selarrrow");
    if (selectedIDs.length > 1) {
        alert("只能选择一条记录");
        return;
    }

    return selectedIDs[0];
}

//选择多条记录
function getSelectedRows() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if (!rowKey) {
        alert("请选择一条记录");
        return;
    }

    return grid.getGridParam("selarrrow");
}

//判断是否为空
function isBlank(value) {
    return !value || !/\S/.test(value);
}

function openLayer(width, height, title, div) {
    layer.open({
        type: 1,
        skin: 'layui-layer-molv',
        title: title,
        shadeClose: false,
        area: [width, height],
        content: jQuery("#" + div)
    });
}


//选择附件layer
function openAttachmentLayer(callback, mime_type) {
    var attachment_jqGrid = $("#attachment_jqGrid");

    layer.open({
        type: 1,
        skin: 'layui-layer-molv',
        title: '选择附件',
        shadeClose: false,
        area: ['80%', '80%'],
        content: jQuery("#attachmentLayer"),
        btn: ['确定', '取消'],
        btn1: function (index) {
            var rowKey = attachment_jqGrid.getGridParam("selrow");
            if (!rowKey) {
                alert("请选择一条记录");
                return;
            }

            var rowData = attachment_jqGrid.jqGrid("getRowData", rowKey);
            var path = rowData.path;
            callback(uploadFileResource + path);

            layer.close(index);
        }
    });

    attachment_jqGrid.jqGrid({
        url: baseURL + '/sys/attachment/list',
        postData: {mime_type: mime_type},
        datatype: "json",
        colModel: [
            {label: 'id', name: 'id', index: 'id', key: true, hidden: true},
            {label: '标题', name: 'title', width: 100},
            {label: '地址', name: 'path', width: 100, hidden: true},
            {
                label: '缩略图', name: 'img', index: 'path', width: 100, formatter: function (value, options, row) {
                var mime = row.mimeType;
                var path = row.path;
                if (mime.indexOf('image') >= 0) {
                    return '<img style="width: 100px;height: 100px;" src="' + uploadFileResource + path + '" >';
                } else if (mime.indexOf('audio') >= 0) {
                    return '<img style="width: 100px;height: 100px;" src="' + baseURL + '/image/audio.jpg" >';
                } else if (mime.indexOf('video') >= 0) {
                    return '<img style="width: 100px;height: 100px;" src="' + baseURL + '/image/video.jpg" >';
                } else if (mime.indexOf('application') >= 0) {
                    return '<img style="width: 100px;height: 100px;" src="' + baseURL + '/image/file.jpg" >';
                }
            }
            },
            {label: '创建时间', name: 'createTime', index: 'create_time', width: 90}
        ],
        viewrecords: true,
        height: 450,
        rowNum: 10,
        rowList: [10, 30, 50,100],
        rownumbers: true,
        rownumWidth: 25,
        autowidth: true,
        multiselect: false,
        pager: "#attachment_jqGridPager",
        jsonReader: {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order"
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            attachment_jqGrid.closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });
}

//创建attachment-layer组件
var attachmentLayerTemplate = Vue.extend({
    template: [
        '<div id="attachmentLayer" style="display: none;">',
        '<div class="grid-btn" style="margin-top: 12px;">\n' +
        '    <div class="form-group col-sm-2">\n' +
        '    <input type="text" class="form-control" v-model="title" @keyup.enter="searchAttachment" placeholder="标题">\n' +
        '    </div>\n' +
        '    <a class="btn btn-default" @click="searchAttachment"><i class="fa fa-search">&nbsp;查询</i></a>\n' +
        '</div>',
        '<table id="attachment_jqGrid"></table>',
        '<div id="attachment_jqGridPager"></div>',
        '</div>'
    ].join(''),
    data(){
        return {
            title: ''
        }
    },
    methods: {
        searchAttachment: function () {
            $("#attachment_jqGrid").jqGrid('setGridParam', {
                postData: {'title': this.title},
                page: 1
            }).trigger("reloadGrid");
        }
    }
});

Vue.component('attachment-layer', attachmentLayerTemplate);

var getJsonFromLocal = function(urlForJson){
	var temp ;
	 $.ajax({url:urlForJson,dataType:'json',error:function(e){console.log(e)},async:false,success:function(result){
		 temp =  result;
	 }});
	 return temp;
}
var messages = {
		zh: getJsonFromLocal('../../i18n/zh.json'),
		ko: getJsonFromLocal('../../i18n/ko.json'),
}
const i18n = new VueI18n({
    locale: $.cookie('locale_language') == undefined ? 'zh':$.cookie('locale_language'),//$.cookie('bitzon_local') === "" ? 'ko':$.cookie('bitzon_local'),//'ko',    // 语言标识
    //this.$i18n.locale // 通过切换locale的值来实现语言切换
    messages: messages
})

var locale = $.cookie('locale_language') == undefined ? 'zh':$.cookie('locale_language')

function setCountryList(value,lang){
        let countryList = [];
        for (let i = 0; i < value.length; i++) {
            var obj = {};
            obj.countryCode = value[i].countryCode;
            if(locale == 'zh'){
                obj.countryNameEn = value[i].countryNameCn
            }
            else if( locale == 'ko'){
                obj.countryNameEn= value[i].countryNameKo
            }
            else{
                obj.countryNameEn = value[i].countryNameEn
            }
            obj.lang = lang
            countryList.push(obj)
        }
        return countryList;
    }
